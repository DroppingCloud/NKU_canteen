package com.nku.canteen.controller;

import com.nku.canteen.dto.LoginDTO;
import com.nku.canteen.dto.UserDTO;
import com.nku.canteen.entity.User;
import com.nku.canteen.service.UserService;
import com.nku.canteen.util.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;

/**
 * 用户控制器
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 用户注册
     */
    @PostMapping("/register")
    public R<?> register(@RequestBody User user) {
        // 参数校验
        if (user.getNickname() == null || user.getEmail() == null || user.getPassword() == null) {
            return R.paramError("昵称、邮箱和密码不能为空");
        }
        
        // 处理性别数据，确保男性为1，女性为2
        if (user.getGender() != null) {
            // 如果前端传入0表示女性，则修正为2
            if (user.getGender() == 0) {
                user.setGender(2); // 将0修正为2，表示女性
            }
            // 确保性别值为1或2
            if (user.getGender() != 1 && user.getGender() != 2) {
                user.setGender(1); // 默认设为男性
            }
        }

        int userId = userService.register(user);
        UserDTO userDTO = new UserDTO();
        userDTO.setUserId(userId);
        userDTO.setNickname(user.getNickname());
        
        return R.success(userDTO, "注册成功");
    }

    /**
     * 用户登录
     */
    @PostMapping("/login")
    public R<?> login(@RequestBody User user) {
        // 参数校验
        if (user.getEmail() == null || user.getPassword() == null) {
            return R.paramError("邮箱和密码不能为空");
        }
        
        LoginDTO loginDTO = userService.login(user.getEmail(), user.getPassword());
        if (loginDTO == null) {
            return R.paramError("邮箱或密码错误");
        }
        
        return R.success(loginDTO, "登录成功");
    }

    /**
     * 修改个人信息
     */
    @PostMapping("/update_profile")
    public R<?> updateProfile(@RequestBody User user, HttpServletRequest request) {
        // 获取当前登录用户ID
        Integer loginUserId = (Integer) request.getAttribute("userId");
        
        // 使用令牌中的用户ID，不需要用户提供
        if (loginUserId == null) {
            return R.unauthorized("用户未登录");
        }
        
        // 设置用户ID为当前登录用户ID
        user.setUserId(loginUserId);
        
        // 不允许修改敏感信息
        user.setPassword(null);
        user.setUserType(null);
        user.setEmail(null);
        
        boolean result = userService.updateProfile(user);
        if (!result) {
            return R.error(999, "修改失败");
        }
        
        return R.success(null, "修改成功");
    }

    /**
     * 获取当前登录用户的完整个人信息
     */
    @GetMapping("/profile")
    public R<?> getProfile(HttpServletRequest request) {
        // 获取当前登录用户ID
        Integer loginUserId = (Integer) request.getAttribute("userId");
        
        if (loginUserId == null) {
            return R.unauthorized("用户未登录");
        }
        
        // 获取完整的用户信息
        User user = userService.getUserById(loginUserId);
        if (user == null) {
            return R.notFound("用户不存在");
        }
        
        // 出于安全考虑，不返回密码
        user.setPassword(null);
        
        return R.success(user, "获取成功");
    }
    
    /**
     * 获取指定用户的公开信息
     */
    @GetMapping("/info")
    public R<?> getUserInfo(@RequestParam("user_id") Integer userId) {
        if (userId == null) {
            return R.paramError("用户ID不能为空");
        }
        
        // 获取用户的公开信息（DTO中只包含非敏感字段）
        UserDTO userDTO = userService.getUserDTOById(userId);
        if (userDTO == null) {
            return R.notFound("用户不存在");
        }
        
        return R.success(userDTO, "获取成功");
    }
} 