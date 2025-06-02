package com.nku.canteen.service;

/**
 * 点赞服务接口
 */
public interface LikeService {

    /**
     * 点赞/取消点赞
     * @param userId 用户ID
     * @param targetType 目标类型
     * @param targetId 目标ID
     * @return true-已点赞，false-已取消
     */
    boolean toggleLike(Integer userId, String targetType, Integer targetId);

    /**
     * 查询点赞状态
     * @param userId 用户ID
     * @param targetType 目标类型
     * @param targetId 目标ID
     * @return 是否已点赞
     */
    boolean getLikeStatus(Integer userId, String targetType, Integer targetId);

    /**
     * 获取点赞数量
     * @param targetType 目标类型
     * @param targetId 目标ID
     * @return 点赞数量
     */
    int getLikeCount(String targetType, Integer targetId);
} 