package com.nku.canteen.controller;

import com.nku.canteen.entity.Like;
import com.nku.canteen.entity.LikeTarget;
import com.nku.canteen.service.LikeService;
import com.nku.canteen.util.R;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 点赞控制器
 */
@RestController
@RequestMapping("/like")
public class LikeController {

    @Autowired
    private LikeService likeService;

    /**
     * 点赞/取消点赞
     */
    @PostMapping("/toggle")
    public R<?> toggleLike(@RequestBody Map<String, Object> params, HttpServletRequest request) {
        // 获取当前登录用户ID
        Integer userId = (Integer) request.getAttribute("userId");
        if (userId == null) {
            return R.unauthorized("请先登录");
        }
        
        // 参数校验
        String targetType = (String) params.get("target_type");
        Integer targetId = (Integer) params.get("target_id");
        
        if (targetType == null) {
            return R.paramError("目标类型不能为空");
        }
        if (targetId == null) {
            return R.paramError("目标ID不能为空");
        }
        
        // 校验目标类型
        if (!targetType.equals("canteen") && !targetType.equals("stall") && 
            !targetType.equals("dish") && !targetType.equals("comment")) {
            return R.paramError("不支持的目标类型");
        }
        
        boolean isLiked = likeService.toggleLike(userId, targetType, targetId);
        
        Map<String, Object> result = new HashMap<>();
        result.put("liked", isLiked);
        
        return R.success(result, isLiked ? "已点赞" : "已取消点赞");
    }

    /**
     * 查询点赞状态
     */
    @GetMapping("/status")
    public R<?> getLikeStatus(
            @RequestParam("target_type") String targetType,
            @RequestParam("target_id") Integer targetId,
            HttpServletRequest request) {
        // 获取当前登录用户ID
        Integer userId = (Integer) request.getAttribute("userId");
        if (userId == null) {
            return R.unauthorized("请先登录");
        }
        
        // 校验目标类型
        if (!targetType.equals("canteen") && !targetType.equals("stall") && 
            !targetType.equals("dish") && !targetType.equals("comment")) {
            return R.paramError("不支持的目标类型");
        }
        
        boolean isLiked = likeService.getLikeStatus(userId, targetType, targetId);
        
        Map<String, Object> result = new HashMap<>();
        result.put("liked", isLiked);
        
        return R.success(result);
    }
} 