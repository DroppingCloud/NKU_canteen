package com.nku.canteen.service.impl;

import com.nku.canteen.dto.CommentDTO;
import com.nku.canteen.dto.StallDTO;
import com.nku.canteen.entity.Comment;
import com.nku.canteen.entity.Stall;
import com.nku.canteen.entity.Dish;
import com.nku.canteen.mapper.CommentMapper;
import com.nku.canteen.mapper.LikeMapper;
import com.nku.canteen.mapper.StallMapper;
import com.nku.canteen.mapper.CommentTargetMapper;
import com.nku.canteen.mapper.LikeTargetMapper;
import com.nku.canteen.mapper.DishMapper;
import com.nku.canteen.service.StallService;
import com.nku.canteen.service.UserService;
import com.nku.canteen.util.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 档口服务实现类
 */
@Slf4j
@Service
public class StallServiceImpl implements StallService {

    @Autowired
    private StallMapper stallMapper;

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private LikeMapper likeMapper;

    @Autowired
    private UserService userService;

    @Autowired
    private CommentTargetMapper commentTargetMapper;

    @Autowired
    private LikeTargetMapper likeTargetMapper;

    @Autowired
    private DishMapper dishMapper;

    @Override
    public List<StallDTO> getStallListByCanteen(Integer canteenId) {
        List<Stall> stallList = stallMapper.selectByCanteenId(canteenId);
        return stallList.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public StallDTO getStallDetail(Integer stallId) {
        Stall stall = stallMapper.selectById(stallId);
        if (stall == null) {
            return null;
        }

        StallDTO stallDTO = convertToDTO(stall);

        // 获取评论列表
        List<Comment> commentList = commentMapper.selectByTarget("stall", stallId);
        List<CommentDTO> commentDTOList = new ArrayList<>();
        for (Comment comment : commentList) {
            CommentDTO commentDTO = new CommentDTO();
            commentDTO.setCommentId(comment.getCommentId());
            commentDTO.setContent(comment.getContent());
            commentDTO.setCreateTime(comment.getCreateTime());
            commentDTO.setUser(userService.getUserDTOById(comment.getUserId()));
            commentDTOList.add(commentDTO);
        }
        stallDTO.setComments(commentDTOList);

        return stallDTO;
    }

    @Override
    public int addStall(Stall stall) {
        return stallMapper.insert(stall);
    }

    @Override
    public int updateStall(Stall stall) {
        // 参数校验
        if (stall.getStallId() == null) {
            throw new ServiceException("档口ID不能为空");
        }
        
        try {
            // 创建参数Map，用于存储输入和输出参数
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("stallId", stall.getStallId());
            paramMap.put("name", stall.getName());
            paramMap.put("canteenId", stall.getCanteenId());
            paramMap.put("floor", stall.getFloor());
            paramMap.put("img", stall.getImg());
            paramMap.put("intro", stall.getIntro());
            paramMap.put("result", null); // 输出参数初始值为null
            
            // 调用存储过程
            stallMapper.updateStallByProcedure(paramMap);
            
            // 从map中获取输出参数
            Integer updateResult = (Integer) paramMap.get("result");
            
            if (updateResult == null || updateResult == 0) {
                log.error("更新档口失败，存储过程执行异常");
                throw new ServiceException("更新档口失败，系统异常");
            } else if (updateResult == -1) {
                throw new ServiceException("档口不存在");
            } else if (updateResult == -2) {
                throw new ServiceException("所属食堂不存在");
            } else if (updateResult == -3) {
                throw new ServiceException("档口名称不能为空");
            } else if (updateResult == -4) {
                throw new ServiceException("食堂ID无效");
            } else if (updateResult == -5) {
                throw new ServiceException("楼层不能为空");
            }
            
            return updateResult;
        } catch (Exception e) {
            if (e instanceof ServiceException) {
                throw e;
            }
            log.error("更新档口异常: {}", e.getMessage());
            throw new ServiceException("更新档口失败，系统异常");
        }
    }

    @Override
    @Transactional
    public int deleteStall(Integer stallId) {
        try {
            System.out.println("StallServiceImpl.deleteStall开始，ID: " + stallId);
            
            // 1. 获取档口下所有菜品
            List<Dish> dishes = dishMapper.selectByStallId(stallId);
            System.out.println("找到档口下菜品数量: " + dishes.size());
            
            // 2. 对每个菜品，删除相关点赞和评论
            for (Dish dish : dishes) {
                System.out.println("处理菜品: " + dish.getDishId());
                
                try {
                    // 获取与菜品相关的所有评论ID并删除评论
                    List<Integer> dishCommentIds = commentMapper.selectCommentIdsByTarget("dish", dish.getDishId());
                    System.out.println("菜品相关评论数量: " + dishCommentIds.size());
                    
                    for (Integer commentId : dishCommentIds) {
                        // 删除评论关联
                        int commentTargetResult = commentTargetMapper.deleteByCommentId(commentId);
                        System.out.println("删除评论关联结果: " + commentTargetResult + ", 评论ID: " + commentId);
                        
                        // 删除评论本身
                        int commentResult = commentMapper.deleteById(commentId);
                        System.out.println("删除评论结果: " + commentResult + ", 评论ID: " + commentId);
                    }
                    
                    // 获取与菜品相关的所有点赞ID并删除点赞
                    List<Integer> dishLikeIds = likeMapper.selectLikeIdsByTarget("dish", dish.getDishId());
                    System.out.println("菜品相关点赞数量: " + dishLikeIds.size());
                    
                    for (Integer likeId : dishLikeIds) {
                        // 删除点赞关联
                        int likeTargetResult = likeTargetMapper.deleteByLikeId(likeId);
                        System.out.println("删除点赞关联结果: " + likeTargetResult + ", 点赞ID: " + likeId);
                        
                        // 删除点赞本身
                        int likeResult = likeMapper.deleteById(likeId);
                        System.out.println("删除点赞结果: " + likeResult + ", 点赞ID: " + likeId);
                    }
                } catch (Exception e) {
                    System.err.println("处理菜品相关数据时出错: " + e.getMessage());
                    e.printStackTrace();
                    throw e;
                }
                
                // 删除菜品
                int dishResult = dishMapper.deleteById(dish.getDishId());
                System.out.println("删除菜品结果: " + dishResult + ", 菜品ID: " + dish.getDishId());
            }
            
            try {
                // 3. 获取与档口相关的所有评论ID并删除评论
                List<Integer> stallCommentIds = commentMapper.selectCommentIdsByTarget("stall", stallId);
                System.out.println("档口相关评论数量: " + stallCommentIds.size());
                
                for (Integer commentId : stallCommentIds) {
                    // 删除评论关联
                    int commentTargetResult = commentTargetMapper.deleteByCommentId(commentId);
                    System.out.println("删除评论关联结果: " + commentTargetResult + ", 评论ID: " + commentId);
                    
                    // 删除评论本身
                    int commentResult = commentMapper.deleteById(commentId);
                    System.out.println("删除评论结果: " + commentResult + ", 评论ID: " + commentId);
                }
            } catch (Exception e) {
                System.err.println("处理档口评论时出错: " + e.getMessage());
                e.printStackTrace();
                throw e;
            }
            
            try {
                // 4. 获取与档口相关的所有点赞ID并删除点赞
                List<Integer> stallLikeIds = likeMapper.selectLikeIdsByTarget("stall", stallId);
                System.out.println("档口相关点赞数量: " + stallLikeIds.size());
                
                for (Integer likeId : stallLikeIds) {
                    // 删除点赞关联
                    int likeTargetResult = likeTargetMapper.deleteByLikeId(likeId);
                    System.out.println("删除点赞关联结果: " + likeTargetResult + ", 点赞ID: " + likeId);
                    
                    // 删除点赞本身
                    int likeResult = likeMapper.deleteById(likeId);
                    System.out.println("删除点赞结果: " + likeResult + ", 点赞ID: " + likeId);
                }
            } catch (Exception e) {
                System.err.println("处理档口点赞时出错: " + e.getMessage());
                e.printStackTrace();
                throw e;
            }
            
            // 5. 最后删除档口本身
            int stallResult = stallMapper.deleteById(stallId);
            System.out.println("删除档口结果: " + stallResult + ", 档口ID: " + stallId);
            
            return stallResult;
        } catch (Exception e) {
            // 打印异常信息
            System.err.println("删除档口时出错: " + e.getMessage());
            e.printStackTrace();
            // 抛出异常，事务会自动回滚
            throw e;
        }
    }

    /**
     * 将档口实体转换为DTO
     * @param stall 档口实体
     * @return 档口DTO
     */
    private StallDTO convertToDTO(Stall stall) {
        StallDTO stallDTO = new StallDTO();
        stallDTO.setStallId(stall.getStallId());
        stallDTO.setName(stall.getName());
        stallDTO.setCanteenId(stall.getCanteenId());
        stallDTO.setFloor(stall.getFloor());
        stallDTO.setImg(stall.getImg());
        stallDTO.setIntro(stall.getIntro());

        // 获取点赞数量
        Integer likeCount = likeMapper.countByTarget("stall", stall.getStallId());
        stallDTO.setLikeCount(likeCount);

        // 获取评论数量
        Integer commentCount = commentMapper.countByTarget("stall", stall.getStallId());
        stallDTO.setCommentCount(commentCount);

        return stallDTO;
    }
} 