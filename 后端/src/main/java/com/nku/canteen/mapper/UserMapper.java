package com.nku.canteen.mapper;

import com.nku.canteen.entity.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * 用户Mapper接口
 */
@Repository
public interface UserMapper {
    
    /**
     * 根据用户ID查询用户信息
     * @param userId 用户ID
     * @return 用户信息
     */
    User selectById(@Param("userId") Integer userId);
    
    /**
     * 根据邮箱查询用户信息
     * @param email 邮箱
     * @return 用户信息
     */
    User selectByEmail(@Param("email") String email);
    
    /**
     * 新增用户
     * @param user 用户信息
     * @return 影响行数
     */
    int insert(User user);
    
    /**
     * 更新用户信息
     * @param user 用户信息
     * @return 影响行数
     */
    int update(User user);
    
    /**
     * 删除用户
     * @param userId 用户ID
     * @return 影响行数
     */
    int deleteById(@Param("userId") Integer userId);
} 