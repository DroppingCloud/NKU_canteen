package com.nku.canteen.mapper;

import com.nku.canteen.entity.CommentTarget;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * 评论目标Mapper接口
 */
@Repository
public interface CommentTargetMapper {
    
    /**
     * 新增评论目标关联
     * @param commentTarget 评论目标关联信息
     * @return 影响行数
     */
    int insert(CommentTarget commentTarget);
    
    /**
     * 根据评论ID删除评论目标关联
     * @param commentId 评论ID
     * @return 影响行数
     */
    int deleteByCommentId(@Param("commentId") Integer commentId);
    
    /**
     * 根据目标类型和目标ID删除评论目标关联
     * @param targetType 目标类型
     * @param targetId 目标ID
     * @return 影响行数
     */
    int deleteByTarget(@Param("targetType") String targetType, @Param("targetId") Integer targetId);
} 