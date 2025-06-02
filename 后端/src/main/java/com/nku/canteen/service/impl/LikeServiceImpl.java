package com.nku.canteen.service.impl;

import com.nku.canteen.entity.Like;
import com.nku.canteen.entity.LikeTarget;
import com.nku.canteen.mapper.LikeMapper;
import com.nku.canteen.mapper.LikeTargetMapper;
import com.nku.canteen.service.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 点赞服务实现类
 */
@Service
public class LikeServiceImpl implements LikeService {

    @Autowired
    private LikeMapper likeMapper;

    @Autowired
    private LikeTargetMapper likeTargetMapper;

    @Override
    @Transactional
    public boolean toggleLike(Integer userId, String targetType, Integer targetId) {
        // 查询是否已点赞
        Like like = likeMapper.selectByUserAndTarget(userId, targetType, targetId);
        
        if (like != null) {
            // 已点赞，取消点赞
            likeTargetMapper.deleteByLikeId(like.getLikeId());
            likeMapper.deleteById(like.getLikeId());
            return false;
        } else {
            // 未点赞，添加点赞
            like = new Like();
            like.setUserId(userId);
            likeMapper.insert(like);
            
            // 添加点赞目标
            LikeTarget likeTarget = new LikeTarget();
            likeTarget.setLikeId(like.getLikeId());
            likeTarget.setTargetType(targetType);
            likeTarget.setTargetId(targetId);
            likeTargetMapper.insert(likeTarget);
            
            return true;
        }
    }

    @Override
    public boolean getLikeStatus(Integer userId, String targetType, Integer targetId) {
        Like like = likeMapper.selectByUserAndTarget(userId, targetType, targetId);
        return like != null;
    }

    @Override
    public int getLikeCount(String targetType, Integer targetId) {
        return likeMapper.countByTarget(targetType, targetId);
    }
} 