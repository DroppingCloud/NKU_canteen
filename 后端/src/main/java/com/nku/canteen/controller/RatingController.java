package com.nku.canteen.controller;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nku.canteen.dto.RatingDTO;
import com.nku.canteen.dto.RatingRequest;
import com.nku.canteen.service.RatingService;
import com.nku.canteen.util.R;

/**
 * 评分控制器
 */
@RestController
@RequestMapping("/rating")
public class RatingController {
    
    @Autowired
    private RatingService ratingService;
    
    /**
     * 获取目标的平均评分
     * 
     * @param targetType 目标类型
     * @param targetId 目标ID
     * @return 目标的平均评分
     */
    @GetMapping("/average")
    public R<BigDecimal> getAverageScore(
            @RequestParam("target_type") String targetType,
            @RequestParam("target_id") Integer targetId) {
        try {
            System.out.println("获取平均评分: target_type=" + targetType + ", target_id=" + targetId);
            BigDecimal averageScore = ratingService.getAverageScore(targetType, targetId);
            return R.success(averageScore);
        } catch (Exception e) {
            e.printStackTrace();
            return R.error(999, "获取平均评分异常: " + e.getMessage());
        }
    }
    
    /**
     * 获取用户对特定目标的评分
     * 
     * @param userId 用户ID
     * @param targetType 目标类型
     * @param targetId 目标ID
     * @return 用户对目标的评分
     */
    @GetMapping("/user_rating")
    public R<RatingDTO> getUserRating(
            @RequestAttribute("userId") Integer userId,
            @RequestParam("target_type") String targetType,
            @RequestParam("target_id") Integer targetId) {
        try {
            System.out.println("获取用户评分: userId=" + userId + ", target_type=" + targetType + ", target_id=" + targetId);
            RatingDTO rating = ratingService.getUserRating(userId, targetType, targetId);
            System.out.println("获取结果: " + (rating != null ? "找到评分" : "未找到评分"));
            return R.success(rating);
        } catch (Exception e) {
            e.printStackTrace();
            return R.error(999, "获取用户评分异常: " + e.getMessage());
        }
    }
    
    /**
     * 用户对目标进行评分
     * 
     * @param userId 用户ID
     * @param params 请求参数
     * @return 评分结果
     */
    @PostMapping("/rate")
    public R<RatingDTO> rateTarget(
            @RequestAttribute("userId") Integer userId,
            @RequestBody Map<String, Object> params) {
        try {
            // 参数校验
            String targetType = (String) params.get("target_type");
            Integer targetId = null;
            BigDecimal score = null;
            
            // 安全地获取 target_id
            if (params.get("target_id") instanceof Integer) {
                targetId = (Integer) params.get("target_id");
            } else if (params.get("target_id") instanceof String) {
                try {
                    targetId = Integer.parseInt((String) params.get("target_id"));
                } catch (NumberFormatException e) {
                    return R.paramError("目标ID必须是整数");
                }
            }
            
            // 安全地获取并解析 score
            Object scoreObj = params.get("score");
            if (scoreObj != null) {
                try {
                    if (scoreObj instanceof BigDecimal) {
                        score = (BigDecimal) scoreObj;
                    } else if (scoreObj instanceof Double) {
                        score = new BigDecimal(scoreObj.toString());
                    } else if (scoreObj instanceof Integer) {
                        score = new BigDecimal((Integer) scoreObj);
                    } else if (scoreObj instanceof String) {
                        score = new BigDecimal((String) scoreObj);
                    }
                } catch (NumberFormatException e) {
                    return R.paramError("评分必须是数值");
                }
            }
            
            if (targetType == null || targetType.trim().isEmpty()) {
                return R.paramError("目标类型不能为空");
            }
            if (targetId == null) {
                return R.paramError("目标ID不能为空");
            }
            if (score == null) {
                return R.paramError("评分不能为空");
            }
            if (score.compareTo(BigDecimal.ZERO) < 0 || score.compareTo(new BigDecimal("5")) > 0) {
                return R.paramError("评分必须在0-5之间");
            }
            
            // 构造评分请求对象
            RatingRequest request = new RatingRequest();
            request.setTargetType(targetType);
            request.setTargetId(targetId);
            request.setScore(score);
            
            RatingDTO rating = ratingService.rateTarget(userId, request);
            return R.success(rating, "评分成功");
        } catch (Exception e) {
            // 记录异常详情
            e.printStackTrace();
            return R.error(999, "系统异常: " + e.getMessage());
        }
    }
    
    /**
     * 获取目标的所有评分
     * 
     * @param targetType 目标类型
     * @param targetId 目标ID
     * @return 目标的评分列表
     */
    @GetMapping("/list")
    public R<List<RatingDTO>> getTargetRatings(
            @RequestParam("target_type") String targetType,
            @RequestParam("target_id") Integer targetId) {
        List<RatingDTO> ratings = ratingService.getTargetRatings(targetType, targetId);
        return R.success(ratings);
    }
    
    /**
     * 获取当前用户的所有评分
     * 
     * @param userId 用户ID
     * @return 用户的评分列表
     */
    @GetMapping("/my_ratings")
    public R<List<RatingDTO>> getMyRatings(
            @RequestAttribute("userId") Integer userId) {
        List<RatingDTO> ratings = ratingService.getUserRatings(userId);
        return R.success(ratings);
    }

    /**
     * 测试接口，用于验证控制器是否正常工作
     */
    @GetMapping("/test")
    public R<String> test() {
        return R.success("Rating API 正常工作");
    }
} 