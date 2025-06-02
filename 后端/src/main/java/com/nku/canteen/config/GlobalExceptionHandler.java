package com.nku.canteen.config;

import com.nku.canteen.util.R;
import com.nku.canteen.util.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.validation.ConstraintViolationException;
import java.sql.SQLException;
import java.util.List;

/**
 * 全局异常处理器
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 处理参数校验异常
     */
    @ExceptionHandler({
            MethodArgumentNotValidException.class,
            BindException.class,
            ConstraintViolationException.class
    })
    public R<Void> handleValidationException(Exception e) {
        String message = "参数错误";

        if (e instanceof MethodArgumentNotValidException) {
            BindingResult bindingResult = ((MethodArgumentNotValidException) e).getBindingResult();
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            if (!fieldErrors.isEmpty()) {
                message = fieldErrors.get(0).getDefaultMessage();
            }
        } else if (e instanceof BindException) {
            BindingResult bindingResult = ((BindException) e).getBindingResult();
            List<FieldError> fieldErrors = bindingResult.getFieldErrors();
            if (!fieldErrors.isEmpty()) {
                message = fieldErrors.get(0).getDefaultMessage();
            }
        } else if (e instanceof ConstraintViolationException) {
            message = e.getMessage();
        }

        return R.paramError(message);
    }
    
    /**
     * 处理业务异常
     */
    @ExceptionHandler(ServiceException.class)
    public R<Void> handleServiceException(ServiceException e) {
        log.error("业务异常: {}", e.getMessage());
        return R.error(e.getCode(), e.getMessage());
    }
    
    /**
     * 处理数据库异常
     */
    @ExceptionHandler({SQLException.class, DataAccessException.class})
    public R<Void> handleDatabaseException(Exception e) {
        log.error("数据库异常: {} ({})", e.getMessage(), e.getClass().getName(), e);
        
        // 检查异常消息是否包含触发器的错误信息
        String message = e.getMessage() != null ? e.getMessage().toLowerCase() : "";
        
        if (message.contains("菜品价格不能为负数") || 
            message.contains("sqlstate=45000") || 
            message.contains("1644")) {
            return R.error(400, "菜品价格不能为负数");
        }
        
        // 处理特定类型的数据库异常
        if (e instanceof DataIntegrityViolationException) {
            return R.error(400, "数据完整性约束异常，请检查输入数据");
        } else if (e instanceof BadSqlGrammarException) {
            return R.error(500, "SQL语法错误");
        }
        
        return R.systemError();
    }

    /**
     * 处理所有未捕获的异常
     */
    @ExceptionHandler(Exception.class)
    public R<Void> handleException(Exception e) {
        log.error("系统异常: {}", e.getMessage(), e);
        return R.systemError();
    }
} 