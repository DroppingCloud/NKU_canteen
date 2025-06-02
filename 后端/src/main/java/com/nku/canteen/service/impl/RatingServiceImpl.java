package com.nku.canteen.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nku.canteen.dto.RatingDTO;
import com.nku.canteen.dto.RatingRequest;
import com.nku.canteen.entity.Dish;
import com.nku.canteen.entity.Rating;
import com.nku.canteen.entity.Stall;
import com.nku.canteen.entity.User;
import com.nku.canteen.mapper.DishMapper;
import com.nku.canteen.mapper.RatingMapper;
import com.nku.canteen.mapper.StallMapper;
import com.nku.canteen.mapper.UserMapper;
import com.nku.canteen.service.RatingService;
import com.nku.canteen.util.Constants;
import com.nku.canteen.util.ServiceException;

/**
 * 评分服务实现类
 */
@Service
public class RatingServiceImpl implements RatingService {

    @Autowired
    private RatingMapper ratingMapper;
    
    @Autowired
    private UserMapper userMapper;
    
    @Autowired
    private StallMapper stallMapper;
    
    @Autowired
    private DishMapper dishMapper;
    
    @Override
    public BigDecimal getAverageScore(String targetType, Integer targetId) {
        // 验证目标类型
        validateTargetType(targetType);
        
        // 验证目标是否存在
        validateTargetExists(targetType, targetId);
        
        // 获取平均评分
        BigDecimal avgScore = ratingMapper.getAverageScore(targetType, targetId);
        return avgScore != null ? avgScore : BigDecimal.ZERO;
    }

    @Override
    public RatingDTO getUserRating(Integer userId, String targetType, Integer targetId) {
        try {
            System.out.println("Service - 获取用户评分: userId=" + userId + ", targetType=" + targetType + ", targetId=" + targetId);
            
            // 验证目标类型
            validateTargetType(targetType);
            System.out.println("目标类型验证通过");
            
            // 验证目标是否存在
            validateTargetExists(targetType, targetId);
            System.out.println("目标存在验证通过");
            
            // 获取用户评分
            System.out.println("开始查询用户评分记录");
            Rating rating = ratingMapper.findByUserIdAndTarget(userId, targetType, targetId);
            System.out.println("查询结果: " + (rating != null ? "找到评分记录" : "未找到评分记录"));
            
            if (rating == null) {
                return null;
            }
            
            System.out.println("转换为DTO并返回");
            return convertToDTO(rating);
        } catch (Exception e) {
            System.out.println("获取用户评分异常: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    @Transactional
    public RatingDTO rateTarget(Integer userId, RatingRequest request) {
        // 验证目标类型
        validateTargetType(request.getTargetType());
        
        // 验证目标是否存在
        validateTargetExists(request.getTargetType(), request.getTargetId());
        
        // 查找用户是否已有评分
        Rating existingRating = ratingMapper.findByUserIdAndTarget(
            userId, request.getTargetType(), request.getTargetId());
        
        Rating rating;
        if (existingRating == null) {
            // 新增评分
            rating = new Rating();
            rating.setUserId(userId);
            rating.setTargetType(request.getTargetType());
            rating.setTargetId(request.getTargetId());
            rating.setScore(request.getScore());
            ratingMapper.insertRating(rating);
        } else {
            // 更新评分
            existingRating.setScore(request.getScore());
            ratingMapper.updateRating(existingRating);
            rating = existingRating;
        }
        
        return convertToDTO(rating);
    }

    @Override
    public List<RatingDTO> getTargetRatings(String targetType, Integer targetId) {
        // 验证目标类型
        validateTargetType(targetType);
        
        // 验证目标是否存在
        validateTargetExists(targetType, targetId);
        
        // 获取评分列表
        List<Rating> ratings = ratingMapper.getRatingsByTarget(targetType, targetId);
        List<RatingDTO> ratingDTOs = new ArrayList<>();
        
        for (Rating rating : ratings) {
            ratingDTOs.add(convertToDTO(rating));
        }
        
        return ratingDTOs;
    }

    @Override
    public List<RatingDTO> getUserRatings(Integer userId) {
        // 获取用户所有评分
        List<Rating> ratings = ratingMapper.getRatingsByUserId(userId);
        List<RatingDTO> ratingDTOs = new ArrayList<>();
        
        for (Rating rating : ratings) {
            ratingDTOs.add(convertToDTO(rating));
        }
        
        return ratingDTOs;
    }
    
    /**
     * 验证目标类型
     */
    private void validateTargetType(String targetType) {
        if (!Constants.TARGET_TYPE_STALL.equals(targetType) && 
            !Constants.TARGET_TYPE_DISH.equals(targetType)) {
            throw new ServiceException("无效的目标类型，只能是'stall'或'dish'");
        }
    }
    
    /**
     * 验证目标是否存在
     */
    private void validateTargetExists(String targetType, Integer targetId) {
        try {
            System.out.println("验证目标是否存在: 类型=" + targetType + ", ID=" + targetId);
            
            if (Constants.TARGET_TYPE_STALL.equals(targetType)) {
                // 验证档口存在
                System.out.println("验证档口存在，ID: " + targetId);
                Stall stall = stallMapper.selectById(targetId);
                System.out.println("查询结果: " + (stall != null ? "找到档口" : "档口不存在"));
                
                if (stall == null) {
                    throw new ServiceException("档口不存在，ID: " + targetId);
                }
            } else if (Constants.TARGET_TYPE_DISH.equals(targetType)) {
                // 验证菜品存在
                System.out.println("验证菜品存在，ID: " + targetId);
                Dish dish = dishMapper.selectById(targetId);
                System.out.println("查询结果: " + (dish != null ? "找到菜品" : "菜品不存在"));
                
                if (dish == null) {
                    throw new ServiceException("菜品不存在，ID: " + targetId);
                }
            }
        } catch (Exception e) {
            if (e instanceof ServiceException) {
                throw e;
            }
            // 记录具体错误
            System.out.println("验证目标时发生异常: " + e.getMessage());
            e.printStackTrace();
            throw new ServiceException("验证目标存在时发生错误: " + e.getMessage());
        }
    }
    
    /**
     * 将实体对象转换为DTO
     */
    private RatingDTO convertToDTO(Rating rating) {
        RatingDTO dto = new RatingDTO();
        dto.setRatingId(rating.getRatingId());
        dto.setUserId(rating.getUserId());
        dto.setTargetType(rating.getTargetType());
        dto.setTargetId(rating.getTargetId());
        dto.setScore(rating.getScore());
        
        // 获取用户信息
        User user = userMapper.selectById(rating.getUserId());
        if (user != null) {
            dto.setNickname(user.getNickname());
        }
        
        // 获取目标名称
        if (Constants.TARGET_TYPE_STALL.equals(rating.getTargetType())) {
            Stall stall = stallMapper.selectById(rating.getTargetId());
            if (stall != null) {
                dto.setTargetName(stall.getName());
            }
        } else if (Constants.TARGET_TYPE_DISH.equals(rating.getTargetType())) {
            Dish dish = dishMapper.selectById(rating.getTargetId());
            if (dish != null) {
                dto.setTargetName(dish.getName());
            }
        }
        
        return dto;
    }
} 