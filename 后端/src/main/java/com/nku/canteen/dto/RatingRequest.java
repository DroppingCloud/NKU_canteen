package com.nku.canteen.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 评分请求对象
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RatingRequest {
    
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