package com.nku.canteen.service.impl;

import com.nku.canteen.dto.LoginDTO;
import com.nku.canteen.dto.UserDTO;
import com.nku.canteen.entity.User;
import com.nku.canteen.mapper.UserMapper;
import com.nku.canteen.service.UserService;
import com.nku.canteen.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

/**
 * 用户服务实现类
 */
@Service
public class UserServiceImpl implements UserService {
    
    @Autowired
    private UserMapper userMapper;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    @Override
    public int register(User user) {
        // 密码加密
        user.setPassword(DigestUtils.md5DigestAsHex(user.getPassword().getBytes()));
        // 设置用户类型为普通用户
        user.setUserType("user");
        
        // 再次处理性别数据，确保男性为1，女性为2
        if (user.getGender() != null) {
            // 如果值为0表示女性，修正为2
            if (user.getGender() == 0) {
                user.setGender(2);
            }
            // 确保性别值为1或2
            if (user.getGender() != 1 && user.getGender() != 2) {
                user.setGender(1); // 默认设为男性
            }
        }
        
        // 如果用户没有设置头像，设置默认头像
        if (user.getAvatar() == null || user.getAvatar().trim().isEmpty()) {
            // 根据性别设置默认头像
            if (user.getGender() != null) {
                if (user.getGender() == 1) {
                    // 男性默认头像
                    user.setAvatar("https://cdn.jsdelivr.net/gh/alohe/avatars/png/upstream_21.png");
                } else if (user.getGender() == 2) {
                    // 女性默认头像
                    user.setAvatar("https://cdn.jsdelivr.net/gh/alohe/avatars/png/upstream_22.png");
                } else {
                    // 其他情况，使用通用默认头像
                    user.setAvatar("https://cdn.jsdelivr.net/gh/alohe/avatars/png/upstream_22.png");
                }
            } else {
                // 用户没有提供性别信息，使用通用默认头像
                user.setAvatar("https://cdn.jsdelivr.net/gh/alohe/avatars/png/upstream_22.png");
            }
        }
        
        // 新增用户
        userMapper.insert(user);
        return user.getUserId();
    }
    
    @Override
    public LoginDTO login(String email, String password) {
        // 根据邮箱查询用户
        User user = userMapper.selectByEmail(email);
        if (user == null) {
            return null;
        }
        
        // 校验密码
        String encryptedPassword = DigestUtils.md5DigestAsHex(password.getBytes());
        if (!encryptedPassword.equals(user.getPassword())) {
            return null;
        }
        
        // 生成token
        String token = jwtUtil.generateToken(user.getUserId(), user.getNickname(), user.getUserType());
        
        // 构建登录结果
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setToken(token);
        
        // 构建用户DTO
        UserDTO userDTO = new UserDTO();
        userDTO.setUserId(user.getUserId());
        userDTO.setNickname(user.getNickname());
        userDTO.setUserType(user.getUserType());
        userDTO.setAvatar(user.getAvatar());
        
        loginDTO.setUser(userDTO);
        
        return loginDTO;
    }
    
    @Override
    public LoginDTO adminLogin(String email, String password) {
        // 根据邮箱查询用户
        User user = userMapper.selectByEmail(email);
        if (user == null || !"admin".equals(user.getUserType())) {
            return null;
        }
        
        // 校验密码
        String encryptedPassword = DigestUtils.md5DigestAsHex(password.getBytes());
        if (!encryptedPassword.equals(user.getPassword())) {
            return null;
        }
        
        // 生成token
        String token = jwtUtil.generateToken(user.getUserId(), user.getNickname(), user.getUserType());
        
        // 构建登录结果
        LoginDTO loginDTO = new LoginDTO();
        loginDTO.setToken(token);
        
        // 构建用户DTO
        UserDTO userDTO = new UserDTO();
        userDTO.setUserId(user.getUserId());
        userDTO.setNickname(user.getNickname());
        userDTO.setUserType(user.getUserType());
        userDTO.setAvatar(user.getAvatar());
        
        loginDTO.setUser(userDTO);
        
        return loginDTO;
    }
    
    @Override
    public boolean updateProfile(User user) {
        return userMapper.update(user) > 0;
    }
    
    @Override
    public UserDTO getUserDTOById(Integer userId) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            return null;
        }
        
        UserDTO userDTO = new UserDTO();
        userDTO.setUserId(user.getUserId());
        userDTO.setNickname(user.getNickname());
        userDTO.setUserType(user.getUserType());
        userDTO.setAvatar(user.getAvatar());
        
        return userDTO;
    }
    
    @Override
    public User getUserById(Integer userId) {
        return userMapper.selectById(userId);
    }
} 