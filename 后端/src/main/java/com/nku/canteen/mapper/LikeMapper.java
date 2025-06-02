package com.nku.canteen.mapper;

import com.nku.canteen.entity.Like;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 点赞Mapper接口
 */
@Repository
public interface LikeMapper {
    
    /**
     * 根据点赞ID查询点赞信息
     * @param likeId 点赞ID
     * @return 点赞信息
     */
    Like selectById(@Param("likeId") Integer likeId);
    
    /**
     * 根据用户ID和目标类型、目标ID查询点赞记录
     * @param userId 用户ID
     * @param targetType 目标类型
     * @param targetId 目标ID
     * @return 点赞记录
     */
    Like selectByUserAndTarget(@Param("userId") Integer userId, @Param("targetType") String targetType, @Param("targetId") Integer targetId);
    
    /**
     * 新增点赞记录
     * @param like 点赞信息
     * @return 影响行数
     */
    int insert(Like like);
    
    /**
     * 删除点赞记录
     * @param likeId 点赞ID
     * @return 影响行数
     */
    int deleteById(@Param("likeId") Integer likeId);
    
    /**
     * 根据目标类型和目标ID查询点赞数量
     * @param targetType 目标类型
     * @param targetId 目标ID
     * @return 点赞数量
     */
    int countByTarget(@Param("targetType") String targetType, @Param("targetId") Integer targetId);
    
    /**
     * 根据目标类型和目标ID获取所有点赞ID
     * @param targetType 目标类型
     * @param targetId 目标ID
     * @return 点赞ID列表
     */
    List<Integer> selectLikeIdsByTarget(@Param("targetType") String targetType, @Param("targetId") Integer targetId);
} 