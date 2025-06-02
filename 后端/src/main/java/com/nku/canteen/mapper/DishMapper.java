package com.nku.canteen.mapper;

import com.nku.canteen.dto.StallDishViewDTO;
import com.nku.canteen.entity.Dish;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 菜品Mapper接口
 */
@Repository
public interface DishMapper {
    
    /**
     * 根据档口ID查询菜品列表
     * @param stallId 档口ID
     * @return 菜品列表
     */
    List<Dish> selectByStallId(@Param("stallId") Integer stallId);
    
    /**
     * 通过视图根据档口ID查询菜品列表
     * @param stallId 档口ID
     * @return 档口菜品视图DTO列表
     */
    List<StallDishViewDTO> selectDishByStallIdFromView(@Param("stallId") Integer stallId);
    
    /**
     * 根据菜品ID查询菜品信息
     * @param dishId 菜品ID
     * @return 菜品信息
     */
    Dish selectById(@Param("dishId") Integer dishId);
    
    /**
     * 新增菜品
     * 注意：请确保传入的价格参数(price)不会被自动转换，包括负值
     * @param dish 菜品信息
     * @return 影响行数
     */
    int insert(Dish dish);
    
    /**
     * 更新菜品
     * 注意：请确保传入的价格参数(price)不会被自动转换，包括负值
     * @param dish 菜品信息
     * @return 影响行数
     */
    int update(Dish dish);
    
    /**
     * 通过存储过程更新菜品
     * @param paramMap 参数Map，包含输入参数和输出参数
     */
    void updateDishByProcedure(Map<String, Object> paramMap);
    
    /**
     * 删除菜品
     * @param dishId 菜品ID
     * @return 影响行数
     */
    int deleteById(@Param("dishId") Integer dishId);
} 