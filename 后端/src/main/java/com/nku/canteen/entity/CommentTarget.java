package com.nku.canteen.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * 评论目标实体类
 */
@Data
public class CommentTarget implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 评论目标记录编号，主键 */
    private Integer commentTargetId;

    /** 评论编号 */
    private Integer commentId;

    /** 目标类型 */
    private String targetType;

    /** 目标编号，对应目标实体主键 */
    private Integer targetId;
} 