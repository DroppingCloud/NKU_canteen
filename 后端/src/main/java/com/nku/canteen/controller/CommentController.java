package com.nku.canteen.controller;

import com.nku.canteen.entity.Comment;
import com.nku.canteen.entity.CommentTarget;
import com.nku.canteen.service.CommentService;
import com.nku.canteen.util.R;
import org.springframework.beans.factory.annotation.Autowired;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 评论控制器
 */
@RestController
@RequestMapping("/comment")
public class CommentController {

    @Autowired
    private CommentService commentService;

    /**
     * 添加评论
     */
    @PostMapping("/add")
    public R<?> addComment(@RequestBody Map<String, Object> params, HttpServletRequest request) {
        // 获取当前登录用户ID
        Integer userId = (Integer) request.getAttribute("userId");
        if (userId == null) {
            return R.unauthorized("请先登录");
        }
        
        // 参数校验
        String content = (String) params.get("content");
        String targetType = (String) params.get("target_type");
        Integer targetId = (Integer) params.get("target_id");
        
        if (content == null || content.trim().isEmpty()) {
            return R.paramError("评论内容不能为空");
        }
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
        
        int commentId = commentService.addComment(userId, content, targetType, targetId);
        
        Map<String, Object> result = new HashMap<>();
        result.put("comment_id", commentId);
        
        return R.success(result, "评论成功");
    }

    /**
     * 删除评论
     */
    @PostMapping("/delete")
    public R<?> deleteComment(@RequestBody Map<String, Object> params, HttpServletRequest request) {
        // 获取当前登录用户ID和类型
        Integer userId = (Integer) request.getAttribute("userId");
        String userType = (String) request.getAttribute("userType");
        
        if (userId == null) {
            return R.unauthorized("请先登录");
        }
        
        // 参数校验
        Integer commentId = (Integer) params.get("comment_id");
        if (commentId == null) {
            return R.paramError("评论ID不能为空");
        }
        
        boolean result = commentService.deleteComment(commentId, userId, userType);
        if (!result) {
            return R.error(999, "删除失败，可能是评论不存在或无权删除");
        }
        
        return R.success(null, "删除成功");
    }
}