package com.nku.canteen.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.nku.canteen.dto.DishDTO;
import com.nku.canteen.entity.Dish;
import com.nku.canteen.service.DishService;
import com.nku.canteen.util.R;
import com.nku.canteen.util.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.List;

/**
 * 菜品控制器
 */
@Slf4j
@RestController
@RequestMapping("/dish")
public class DishController {

    @Autowired
    private DishService dishService;

    /**
     * 获取指定档口下的菜品列表
     */
    @GetMapping("/list")
    public R<?> getDishList(
            @RequestParam("stall_id") Integer stallId,
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false, name = "page_size") Integer pageSize) {
        log.info("获取档口菜品列表，stallId: {}, page: {}, pageSize: {}", stallId, page, pageSize);
        return R.success(dishService.getDishListByStallFromView(stallId, page, pageSize));
    }

    /**
     * 获取菜品详情
     */
    @GetMapping("/detail")
    public R<?> getDishDetail(@RequestParam("dish_id") Integer dishId) {
        DishDTO dishDTO = dishService.getDishDetail(dishId);
        if (dishDTO == null) {
            return R.notFound("菜品不存在");
        }
        return R.success(dishDTO);
    }

    /**
     * 新增菜品（管理员）
     */
    @PostMapping("/add")
    public R<?> addDish(@RequestBody Dish dish, HttpServletRequest request) {
        // 只有管理员才能添加菜品
        if (!"admin".equals(request.getAttribute("userType"))) {
            return R.unauthorized("非管理员无权添加菜品");
        }
        
        log.info("添加菜品请求，name: {}, price: {}, stallId: {}", dish.getName(), dish.getPrice(), dish.getStallId());
        try {
            // 参数校验
            if (dish.getName() == null) {
                return R.paramError("菜品名称不能为空");
            }
            if (dish.getStallId() == null) {
                return R.paramError("所属档口ID不能为空");
            }
            if (dish.getPrice() == null) {
                return R.paramError("菜品价格不能为空");
            }
            
            int result = dishService.addDish(dish);
            log.info("添加菜品成功，结果: {}, dishId: {}", result, dish.getDishId());
            return R.success(dish.getDishId());
        } catch (ServiceException e) {
            log.error("添加菜品失败，业务异常: {}", e.getMessage());
            return R.paramError(e.getMessage());
        } catch (Exception e) {
            log.error("添加菜品失败，系统异常: {}", e.getMessage(), e);
            return R.systemError();
        }
    }

    /**
     * 更新菜品（管理员）
     */
    @PostMapping("/update")
    public R<?> updateDish(@RequestBody Dish dish, HttpServletRequest request) {
        // 只有管理员才能更新菜品
        if (!"admin".equals(request.getAttribute("userType"))) {
            return R.unauthorized("非管理员无权更新菜品");
        }
        
        log.info("更新菜品请求，dishId: {}, price: {}", dish.getDishId(), dish.getPrice());
        try {
            // 参数校验
            if (dish.getDishId() == null) {
                return R.paramError("菜品ID不能为空");
            }
            
            int result = dishService.updateDish(dish);
            log.info("更新菜品成功，结果: {}", result);
            return R.success();
        } catch (ServiceException e) {
            log.error("更新菜品失败，业务异常: {}", e.getMessage());
            return R.paramError(e.getMessage());
        } catch (Exception e) {
            log.error("更新菜品失败，系统异常: {}", e.getMessage(), e);
            return R.systemError();
        }
    }

    /**
     * 删除菜品（管理员）
     */
    @PostMapping("/delete")
    public R<?> deleteDish(@RequestParam("dish_id") Integer dishId, HttpServletRequest request) {
        // 只有管理员才能删除菜品
        if (!"admin".equals(request.getAttribute("userType"))) {
            return R.unauthorized("非管理员无权删除菜品");
        }
        
        int result = dishService.deleteDish(dishId);
        if (result <= 0) {
            return R.error(999, "删除失败");
        }
        
        return R.success(null, "删除成功");
    }
} 