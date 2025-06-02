package com.nku.canteen.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 评论数据传输对象
 */
@Data
public class CommentDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 评论编号 */
    private Integer commentId;

    /** 评论内容 */
    private String content;

    /** 评论创建时间 */
    private Date createTime;

    /** 评论者信息 */
    private UserDTO user;
} 