package com.nku.canteen.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 登录结果数据传输对象
 */
@Data
public class LoginDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /** JWT令牌 */
    private String token;

    /** 用户信息 */
    private UserDTO user;
} 