package com.nku.canteen.entity;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 菜品实体类
 */
@Data
public class Dish implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 菜品编号，主键 */
    private Integer dishId;

    /** 菜品名称 */
    private String name;

    /** 菜品简介 */
    private String intro;

    /** 菜品价格，单位元，保留两位小数 */
    private BigDecimal price;

    /** 菜品图片地址 */
    private String imageUrl;

    /** 所属档口编号 */
    private Integer stallId;
} 