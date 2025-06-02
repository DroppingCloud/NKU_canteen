package com.nku.canteen.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 菜品详情数据传输对象
 */
@Data
public class DishDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 菜品编号 */
    private Integer dishId;

    /** 菜品名称 */
    private String name;

    /** 菜品简介 */
    private String intro;

    /** 菜品价格 */
    private BigDecimal price;

    /** 菜品图片地址 */
    private String imageUrl;

    /** 所属档口编号 */
    private Integer stallId;

    /** 点赞数量 */
    private Integer likeCount;

    /** 评论数量 */
    private Integer commentCount;

    /** 评论列表 */
    private List<CommentDTO> comments;
} 