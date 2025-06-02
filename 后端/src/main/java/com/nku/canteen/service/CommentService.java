package com.nku.canteen.service;

import com.nku.canteen.dto.CommentDTO;

import java.util.List;

/**
 * 评论服务接口
 */
public interface CommentService {

    /**
     * 添加评论
     * @param userId 用户ID
     * @param content 评论内容
     * @param targetType 目标类型
     * @param targetId 目标ID
     * @return 评论ID
     */
    int addComment(Integer userId, String content, String targetType, Integer targetId);

    /**
     * 删除评论
     * @param commentId 评论ID
     * @param userId 用户ID (当前登录用户)
     * @param userType 用户类型 (用于判断是否管理员)
     * @return 是否删除成功
     */
    boolean deleteComment(Integer commentId, Integer userId, String userType);

    /**
     * 根据目标获取评论列表
     * @param targetType 目标类型
     * @param targetId 目标ID
     * @return 评论列表
     */
    List<CommentDTO> getCommentsByTarget(String targetType, Integer targetId);
}