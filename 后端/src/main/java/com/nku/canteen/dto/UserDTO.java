package com.nku.canteen.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户数据传输对象
 */
@Data
public class UserDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 用户编号 */
    private Integer userId;

    /** 用户昵称 */
    private String nickname;

    /** 用户类型 */
    private String userType;

    /** 用户头像URL */
    private String avatar;
} 