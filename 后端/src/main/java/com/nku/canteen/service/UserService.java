package com.nku.canteen.service;

import com.nku.canteen.dto.LoginDTO;
import com.nku.canteen.dto.UserDTO;
import com.nku.canteen.entity.User;

/**
 * 用户服务接口
 */
public interface UserService {
    
    /**
     * 用户注册
     * @param user 用户信息
     * @return 注册成功返回用户ID
     */
    int register(User user);
    
    /**
     * 用户登录
     * @param email 邮箱
     * @param password 密码
     * @return 登录成功返回登录结果，包含token和用户信息
     */
    LoginDTO login(String email, String password);
    
    /**
     * 管理员登录
     * @param email 邮箱
     * @param password 密码
     * @return 登录成功返回登录结果，包含token和用户信息
     */
    LoginDTO adminLogin(String email, String password);
    
    /**
     * 修改用户个人信息
     * @param user 用户信息
     * @return 修改结果
     */
    boolean updateProfile(User user);
    
    /**
     * 根据用户ID查询用户DTO
     * @param userId 用户ID
     * @return 用户DTO
     */
    UserDTO getUserDTOById(Integer userId);
    
    /**
     * 根据用户ID查询用户实体
     * @param userId 用户ID
     * @return 用户实体
     */
    User getUserById(Integer userId);
} 