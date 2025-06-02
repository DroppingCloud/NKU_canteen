package com.nku.canteen.controller;

import com.nku.canteen.dto.LoginDTO;
import com.nku.canteen.entity.User;
import com.nku.canteen.service.UserService;
import com.nku.canteen.util.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;

/**
 * 管理员控制器
 */
@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserService userService;

    /**
     * 管理员登录
     */
    @PostMapping("/login")
    public R<?> login(@RequestBody User user) {
        // 参数校验
        if (user.getEmail() == null || user.getPassword() == null) {
            return R.paramError("邮箱和密码不能为空");
        }
        
        LoginDTO loginDTO = userService.adminLogin(user.getEmail(), user.getPassword());
        if (loginDTO == null) {
            return R.paramError("邮箱、密码错误或非管理员账号");
        }
        
        return R.success(loginDTO, "登录成功");
    }

    /**
     * 检查是否为管理员
     */
    private boolean isAdmin(HttpServletRequest request) {
        String userType = (String) request.getAttribute("userType");
        return "admin".equals(userType);
    }
} 