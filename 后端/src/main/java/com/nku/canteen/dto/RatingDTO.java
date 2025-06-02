package com.nku.canteen.dto;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 评分数据传输对象
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class RatingDTO {
    
    /**
     * 评分记录ID
     */
    private Integer ratingId;
    
    /**
     * 用户ID
     */
    private Integer userId;
    
    /**
     * 用户昵称
     */
    private String nickname;
    
    /**
     * 目标类型：stall(档口)或dish(菜品)
     */
    private String targetType;
    
    /**
     * 目标ID
     */
    private Integer targetId;
    
    /**
     * 目标名称
     */
    private String targetName;
    
    /**
     * 评分分数，0.0-5.0
     */
    private BigDecimal score;
} 