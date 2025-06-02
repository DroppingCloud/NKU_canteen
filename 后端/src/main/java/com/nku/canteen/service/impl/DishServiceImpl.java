package com.nku.canteen.service.impl;

import com.github.pagehelper.PageHelper;
import com.nku.canteen.dto.CommentDTO;
import com.nku.canteen.dto.DishDTO;
import com.nku.canteen.dto.StallDishViewDTO;
import com.nku.canteen.entity.Comment;
import com.nku.canteen.entity.Dish;
import com.nku.canteen.mapper.CommentMapper;
import com.nku.canteen.mapper.DishMapper;
import com.nku.canteen.mapper.LikeMapper;
import com.nku.canteen.service.DishService;
import com.nku.canteen.service.UserService;
import com.nku.canteen.util.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import java.util.Map;
import java.util.HashMap;

/**
 * 菜品服务实现类
 */
@Slf4j
@Service
public class DishServiceImpl implements DishService {

    @Autowired
    private DishMapper dishMapper;

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private LikeMapper likeMapper;

    @Autowired
    private UserService userService;

    @Override
    @Deprecated
    public List<DishDTO> getDishListByStall(Integer stallId, Integer pageNum, Integer pageSize) {
        if (pageNum != null && pageSize != null) {
            PageHelper.startPage(pageNum, pageSize);
        }
        List<Dish> dishList = dishMapper.selectByStallId(stallId);
        return dishList.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public List<StallDishViewDTO> getDishListByStallFromView(Integer stallId, Integer pageNum, Integer pageSize) {
        if (pageNum != null && pageSize != null) {
            PageHelper.startPage(pageNum, pageSize);
        }
        return dishMapper.selectDishByStallIdFromView(stallId);
    }

    @Override
    public DishDTO getDishDetail(Integer dishId) {
        Dish dish = dishMapper.selectById(dishId);
        if (dish == null) {
            return null;
        }

        DishDTO dishDTO = convertToDTO(dish);

        // 获取评论列表
        List<Comment> commentList = commentMapper.selectByTarget("dish", dishId);
        List<CommentDTO> commentDTOList = new ArrayList<>();
        for (Comment comment : commentList) {
            CommentDTO commentDTO = new CommentDTO();
            commentDTO.setCommentId(comment.getCommentId());
            commentDTO.setContent(comment.getContent());
            commentDTO.setCreateTime(comment.getCreateTime());
            commentDTO.setUser(userService.getUserDTOById(comment.getUserId()));
            commentDTOList.add(commentDTO);
        }
        dishDTO.setComments(commentDTOList);

        return dishDTO;
    }

    @Override
    public int addDish(Dish dish) {
        // 参数校验
        if (dish.getName() == null || dish.getName().trim().isEmpty()) {
            throw new ServiceException("菜品名称不能为空");
        }
        
        if (dish.getPrice() == null) {
            throw new ServiceException("菜品价格不能为空");
        }
        
        if (dish.getStallId() == null) {
            throw new ServiceException("所属档口不能为空");
        }
        
        // 数据库触发器会处理价格验证，如果价格为负数会触发异常
        try {
            int result = dishMapper.insert(dish);
            return result;
        } catch (Exception e) {
            // 捕获可能由触发器引起的异常
            if (e.getMessage() != null && (
                e.getMessage().contains("菜品价格不能为负数") || 
                e.getMessage().contains("1644") || // MySQL错误码
                e.getMessage().contains("45000")   // SQLSTATE
            )) {
                throw new ServiceException("菜品价格不能为负数");
            }
            log.error("添加菜品异常: {}", e.getMessage());
            throw e;
        }
    }

    @Override
    public int updateDish(Dish dish) {
        // 参数校验
        if (dish.getDishId() == null) {
            throw new ServiceException("菜品ID不能为空");
        }
        
        try {
            // 创建参数Map，用于存储输入和输出参数
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("dishId", dish.getDishId());
            paramMap.put("name", dish.getName());
            paramMap.put("intro", dish.getIntro());
            paramMap.put("price", dish.getPrice());
            paramMap.put("imageUrl", dish.getImageUrl());
            paramMap.put("stallId", dish.getStallId());
            paramMap.put("result", null); // 输出参数初始值为null
            
            // 调用存储过程
            dishMapper.updateDishByProcedure(paramMap);
            
            // 从map中获取输出参数
            Integer updateResult = (Integer) paramMap.get("result");
            
            if (updateResult == null || updateResult == 0) {
                log.error("更新菜品失败，存储过程执行异常");
                throw new ServiceException("更新菜品失败，系统异常");
            } else if (updateResult == -1) {
                throw new ServiceException("菜品不存在");
            } else if (updateResult == -2) {
                throw new ServiceException("菜品价格不能为负数");
            }
            
            return updateResult;
        } catch (Exception e) {
            if (e instanceof ServiceException) {
                throw e;
            }
            log.error("更新菜品异常: {}", e.getMessage());
            throw new ServiceException("更新菜品失败，系统异常");
        }
    }

    @Override
    public int deleteDish(Integer dishId) {
        return dishMapper.deleteById(dishId);
    }

    /**
     * 将菜品实体转换为DTO
     * @param dish 菜品实体
     * @return 菜品DTO
     */
    private DishDTO convertToDTO(Dish dish) {
        DishDTO dishDTO = new DishDTO();
        dishDTO.setDishId(dish.getDishId());
        dishDTO.setName(dish.getName());
        dishDTO.setIntro(dish.getIntro());
        dishDTO.setPrice(dish.getPrice());
        dishDTO.setImageUrl(dish.getImageUrl());
        dishDTO.setStallId(dish.getStallId());

        // 获取点赞数量
        Integer likeCount = likeMapper.countByTarget("dish", dish.getDishId());
        dishDTO.setLikeCount(likeCount);

        // 获取评论数量
        Integer commentCount = commentMapper.countByTarget("dish", dish.getDishId());
        dishDTO.setCommentCount(commentCount);

        return dishDTO;
    }
} 