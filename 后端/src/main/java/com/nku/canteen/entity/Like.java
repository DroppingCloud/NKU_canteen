package com.nku.canteen.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * 点赞实体类
 */
@Data
public class Like implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 点赞编号，主键 */
    private Integer likeId;

    /** 点赞者编号 */
    private Integer userId;
} 