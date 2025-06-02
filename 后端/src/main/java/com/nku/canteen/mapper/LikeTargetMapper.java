package com.nku.canteen.mapper;

import com.nku.canteen.entity.LikeTarget;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

/**
 * 点赞目标Mapper接口
 */
@Repository
public interface LikeTargetMapper {
    
    /**
     * 新增点赞目标关联
     * @param likeTarget 点赞目标关联信息
     * @return 影响行数
     */
    int insert(LikeTarget likeTarget);
    
    /**
     * 根据点赞ID删除点赞目标关联
     * @param likeId 点赞ID
     * @return 影响行数
     */
    int deleteByLikeId(@Param("likeId") Integer likeId);
    
    /**
     * 根据目标类型和目标ID删除点赞目标关联
     * @param targetType 目标类型
     * @param targetId 目标ID
     * @return 影响行数
     */
    int deleteByTarget(@Param("targetType") String targetType, @Param("targetId") Integer targetId);
} 