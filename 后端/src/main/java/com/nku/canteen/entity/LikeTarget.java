package com.nku.canteen.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * 点赞目标实体类
 */
@Data
public class LikeTarget implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 点赞目标记录编号，主键 */
    private Integer likeTargetId;

    /** 点赞编号 */
    private Integer likeId;

    /** 目标类型 */
    private String targetType;

    /** 目标编号，对应目标实体主键 */
    private Integer targetId;
} 