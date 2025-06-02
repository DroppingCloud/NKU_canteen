package com.nku.canteen.mapper;

import com.nku.canteen.entity.Comment;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 评论Mapper接口
 */
@Repository
public interface CommentMapper {
    
    /**
     * 根据评论ID查询评论信息
     * @param commentId 评论ID
     * @return 评论信息
     */
    Comment selectById(@Param("commentId") Integer commentId);
    
    /**
     * 新增评论
     * @param comment 评论信息
     * @return 影响行数
     */
    int insert(Comment comment);
    
    /**
     * 删除评论
     * @param commentId 评论ID
     * @return 影响行数
     */
    int deleteById(@Param("commentId") Integer commentId);
    
    /**
     * 根据目标类型和目标ID查询评论列表
     * @param targetType 目标类型
     * @param targetId 目标ID
     * @return 评论列表
     */
    List<Comment> selectByTarget(@Param("targetType") String targetType, @Param("targetId") Integer targetId);
    
    /**
     * 根据目标类型和目标ID查询评论数量
     * @param targetType 目标类型
     * @param targetId 目标ID
     * @return 评论数量
     */
    int countByTarget(@Param("targetType") String targetType, @Param("targetId") Integer targetId);
    
    /**
     * 根据目标类型和目标ID获取所有评论ID
     * @param targetType 目标类型
     * @param targetId 目标ID
     * @return 评论ID列表
     */
    List<Integer> selectCommentIdsByTarget(@Param("targetType") String targetType, @Param("targetId") Integer targetId);
}