package com.nku.canteen.service.impl;

import com.nku.canteen.dto.CommentDTO;
import com.nku.canteen.entity.Comment;
import com.nku.canteen.entity.CommentTarget;
import com.nku.canteen.mapper.CommentMapper;
import com.nku.canteen.mapper.CommentTargetMapper;
import com.nku.canteen.service.CommentService;
import com.nku.canteen.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 评论服务实现类
 */
@Service
public class CommentServiceImpl implements CommentService {

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private CommentTargetMapper commentTargetMapper;

    @Autowired
    private UserService userService;

    @Override
    @Transactional
    public int addComment(Integer userId, String content, String targetType, Integer targetId) {
        // 创建评论
        Comment comment = new Comment();
        comment.setUserId(userId);
        comment.setContent(content);
        commentMapper.insert(comment);

        // 创建评论目标关联
        CommentTarget commentTarget = new CommentTarget();
        commentTarget.setCommentId(comment.getCommentId());
        commentTarget.setTargetType(targetType);
        commentTarget.setTargetId(targetId);
        commentTargetMapper.insert(commentTarget);

        return comment.getCommentId();
    }

    @Override
    @Transactional
    public boolean deleteComment(Integer commentId, Integer userId, String userType) {
        // 获取评论信息
        Comment comment = commentMapper.selectById(commentId);
        if (comment == null) {
            return false;
        }

        // 只有评论者自己或管理员才能删除评论
        if (!comment.getUserId().equals(userId) && !"admin".equals(userType)) {
            return false;
        }

        // 删除评论关联
        commentTargetMapper.deleteByCommentId(commentId);
        // 删除评论
        return commentMapper.deleteById(commentId) > 0;
    }

    @Override
    public List<CommentDTO> getCommentsByTarget(String targetType, Integer targetId) {
        List<Comment> comments = commentMapper.selectByTarget(targetType, targetId);
        List<CommentDTO> commentDTOs = new ArrayList<>();

        for (Comment comment : comments) {
            CommentDTO commentDTO = new CommentDTO();
            commentDTO.setCommentId(comment.getCommentId());
            commentDTO.setContent(comment.getContent());
            commentDTO.setCreateTime(comment.getCreateTime());
            commentDTO.setUser(userService.getUserDTOById(comment.getUserId()));
            commentDTOs.add(commentDTO);
        }

        return commentDTOs;
    }
} 