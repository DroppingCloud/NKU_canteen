package com.nku.canteen.service;

import java.math.BigDecimal;
import java.util.List;

import com.nku.canteen.dto.RatingDTO;
import com.nku.canteen.dto.RatingRequest;
import com.nku.canteen.entity.Rating;

/**
 * 评分服务接口
 */
public interface RatingService {
    
    /**
     * 获取目标的平均评分
     * 
     * @param targetType 目标类型
     * @param targetId 目标ID
     * @return 平均评分
     */
    BigDecimal getAverageScore(String targetType, Integer targetId);
    
    /**
     * 获取用户对目标的评分
     * 
     * @param userId 用户ID
     * @param targetType 目标类型
     * @param targetId 目标ID
     * @return 评分记录
     */
    RatingDTO getUserRating(Integer userId, String targetType, Integer targetId);
    
    /**
     * 用户评分或更新评分
     * 
     * @param userId 用户ID
     * @param request 评分请求
     * @return 更新后的评分DTO
     */
    RatingDTO rateTarget(Integer userId, RatingRequest request);
    
    /**
     * 获取目标的所有评分
     * 
     * @param targetType 目标类型
     * @param targetId 目标ID
     * @return 评分列表
     */
    List<RatingDTO> getTargetRatings(String targetType, Integer targetId);
    
    /**
     * 获取用户的所有评分
     * 
     * @param userId 用户ID
     * @return 评分列表
     */
    List<RatingDTO> getUserRatings(Integer userId);
} 