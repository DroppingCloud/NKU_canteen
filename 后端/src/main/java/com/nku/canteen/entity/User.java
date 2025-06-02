package com.nku.canteen.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户实体类
 */
@Data
public class User implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /** 用户编号，主键 */
    private Integer userId;
    
    /** 用户昵称 */
    private String nickname;
    
    /** 性别：1-男，2-女 */
    private Integer gender;
    
    /** 用户头像 URL */
    private String avatar;
    
    /** 用户类型：user-普通用户，admin-管理员 */
    private String userType;
    
    /** 用户密码 */
    private String password;
    
    /** 注册时间 */
    private Date registerTime;
    
    /** 电子邮箱，唯一 */
    private String email;
} 