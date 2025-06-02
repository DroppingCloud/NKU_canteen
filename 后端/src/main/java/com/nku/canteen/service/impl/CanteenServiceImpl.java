package com.nku.canteen.service.impl;

import com.github.pagehelper.PageHelper;
import com.nku.canteen.dto.CanteenDTO;
import com.nku.canteen.dto.CommentDTO;
import com.nku.canteen.entity.Canteen;
import com.nku.canteen.entity.Comment;
import com.nku.canteen.entity.Stall;
import com.nku.canteen.entity.Dish;
import com.nku.canteen.mapper.CanteenMapper;
import com.nku.canteen.mapper.CommentMapper;
import com.nku.canteen.mapper.LikeMapper;
import com.nku.canteen.mapper.StallMapper;
import com.nku.canteen.mapper.DishMapper;
import com.nku.canteen.mapper.CommentTargetMapper;
import com.nku.canteen.mapper.LikeTargetMapper;
import com.nku.canteen.service.CanteenService;
import com.nku.canteen.service.UserService;
import com.nku.canteen.service.StallService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 食堂服务实现类
 */
@Service
public class CanteenServiceImpl implements CanteenService {

    @Autowired
    private CanteenMapper canteenMapper;

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private LikeMapper likeMapper;

    @Autowired
    private StallMapper stallMapper;
    
    @Autowired
    private DishMapper dishMapper;
    
    @Autowired
    private CommentTargetMapper commentTargetMapper;
    
    @Autowired
    private LikeTargetMapper likeTargetMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private StallService stallService;

    @Override
    public List<CanteenDTO> getCanteenList(Integer pageNum, Integer pageSize) {
        if (pageNum != null && pageSize != null) {
            PageHelper.startPage(pageNum, pageSize);
        }
        List<Canteen> canteenList = canteenMapper.selectAll();
        return canteenList.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public List<CanteenDTO> getCanteenListByCampus(String campus, Integer pageNum, Integer pageSize) {
        if (pageNum != null && pageSize != null) {
            PageHelper.startPage(pageNum, pageSize);
        }
        List<Canteen> canteenList = canteenMapper.selectByCampus(campus);
        return canteenList.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public CanteenDTO getCanteenDetail(Integer canteenId) {
        Canteen canteen = canteenMapper.selectById(canteenId);
        if (canteen == null) {
            return null;
        }
        
        CanteenDTO canteenDTO = convertToDTO(canteen);
        
        // 获取评论列表
        List<Comment> commentList = commentMapper.selectByTarget("canteen", canteenId);
        List<CommentDTO> commentDTOList = new ArrayList<>();
        for (Comment comment : commentList) {
            CommentDTO commentDTO = new CommentDTO();
            commentDTO.setCommentId(comment.getCommentId());
            commentDTO.setContent(comment.getContent());
            commentDTO.setCreateTime(comment.getCreateTime());
            commentDTO.setUser(userService.getUserDTOById(comment.getUserId()));
            commentDTOList.add(commentDTO);
        }
        canteenDTO.setComments(commentDTOList);
        
        return canteenDTO;
    }

    @Override
    public int addCanteen(Canteen canteen) {
        return canteenMapper.insert(canteen);
    }

    @Override
    public int updateCanteen(Canteen canteen) {
        return canteenMapper.update(canteen);
    }

    @Override
    @Transactional
    public int deleteCanteen(Integer canteenId) {
        try {
            // 1. 获取与食堂相关的所有评论ID并删除评论
            List<Integer> canteenCommentIds = commentMapper.selectCommentIdsByTarget("canteen", canteenId);
            for (Integer commentId : canteenCommentIds) {
                // 删除评论关联
                commentTargetMapper.deleteByCommentId(commentId);
                // 删除评论本身
                commentMapper.deleteById(commentId);
            }
            
            // 2. 获取与食堂相关的所有点赞ID并删除点赞
            List<Integer> canteenLikeIds = likeMapper.selectLikeIdsByTarget("canteen", canteenId);
            for (Integer likeId : canteenLikeIds) {
                // 删除点赞关联
                likeTargetMapper.deleteByLikeId(likeId);
                // 删除点赞本身
                likeMapper.deleteById(likeId);
            }
            
            // 3. 获取食堂下所有档口
            List<Stall> stalls = stallMapper.selectByCanteenId(canteenId);
            
            // 4. 对每个档口，调用档口删除服务方法
            for (Stall stall : stalls) {
                // 使用已有的档口删除服务方法，它会处理档口下所有菜品及关联数据
                stallService.deleteStall(stall.getStallId());
            }
            
            // 5. 最后删除食堂本身
            return canteenMapper.deleteById(canteenId);
        } catch (Exception e) {
            // 打印异常信息
            System.err.println("删除食堂时出错: " + e.getMessage());
            e.printStackTrace();
            // 抛出异常，事务会自动回滚
            throw e;
        }
    }
    
    /**
     * 将食堂实体转换为DTO
     * @param canteen 食堂实体
     * @return 食堂DTO
     */
    private CanteenDTO convertToDTO(Canteen canteen) {
        CanteenDTO canteenDTO = new CanteenDTO();
        canteenDTO.setCanteenId(canteen.getCanteenId());
        canteenDTO.setName(canteen.getName());
        canteenDTO.setLocation(canteen.getLocation());
        canteenDTO.setOpenTime(canteen.getOpenTime());
        canteenDTO.setImg(canteen.getImg());
        canteenDTO.setCampus(canteen.getCampus());
        
        // 获取点赞数量
        Integer likeCount = likeMapper.countByTarget("canteen", canteen.getCanteenId());
        canteenDTO.setLikeCount(likeCount);
        
        // 获取评论数量
        Integer commentCount = commentMapper.countByTarget("canteen", canteen.getCanteenId());
        canteenDTO.setCommentCount(commentCount);
        
        return canteenDTO;
    }
} 