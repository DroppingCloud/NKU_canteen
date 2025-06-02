package com.nku.canteen.mapper;

import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.nku.canteen.entity.Rating;

/**
 * 评分Mapper接口
 */
@Mapper
public interface RatingMapper {
    
    /**
     * 根据用户ID和目标信息查找评分
     */
    Rating findByUserIdAndTarget(@Param("userId") Integer userId, 
                                @Param("targetType") String targetType, 
                                @Param("targetId") Integer targetId);
    
    /**
     * 创建新的评分记录
     */
    int insertRating(Rating rating);
    
    /**
     * 更新评分记录
     */
    int updateRating(Rating rating);
    
    /**
     * 查询目标的平均评分
     */
    BigDecimal getAverageScore(@Param("targetType") String targetType, 
                             @Param("targetId") Integer targetId);
    
    /**
     * 获取目标的所有评分记录
     */
    List<Rating> getRatingsByTarget(@Param("targetType") String targetType,
                                  @Param("targetId") Integer targetId);
    
    /**
     * 获取用户的所有评分记录
     */
    List<Rating> getRatingsByUserId(@Param("userId") Integer userId);
} 