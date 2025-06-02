package com.nku.canteen.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 评论实体类
 */
@Data
public class Comment implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 评论编号，主键 */
    private Integer commentId;

    /** 评论者编号 */
    private Integer userId;

    /** 评论内容 */
    private String content;

    /** 评论创建时间 */
    private Date createTime;
} 