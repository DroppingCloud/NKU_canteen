package com.nku.canteen.entity;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 评分实体类
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Rating {
    
    /**
     * 评分记录ID
     */
    private Integer ratingId;
    
    /**
     * 用户ID
     */
    private Integer userId;
    
    /**
     * 目标类型：stall(档口)或dish(菜品)
     */
    private String targetType;
    
    /**
     * 目标ID
     */
    private Integer targetId;
    
    /**
     * 评分分数，0.0-5.0
     */
    private BigDecimal score;
} 