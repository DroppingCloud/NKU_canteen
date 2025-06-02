package com.nku.canteen.service;

import com.nku.canteen.dto.DishDTO;
import com.nku.canteen.dto.StallDishViewDTO;
import com.nku.canteen.entity.Dish;

import java.util.List;

/**
 * 菜品服务接口
 */
public interface DishService {

    /**
     * 获取指定档口下的菜品列表
     * @param stallId 档口ID
     * @param pageNum 页码
     * @param pageSize 每页记录数
     * @return 菜品列表
     * @deprecated 使用 {@link #getDishListByStallFromView(Integer, Integer, Integer)} 代替
     */
    @Deprecated
    List<DishDTO> getDishListByStall(Integer stallId, Integer pageNum, Integer pageSize);
    
    /**
     * 通过视图获取指定档口下的菜品列表
     * @param stallId 档口ID
     * @param pageNum 页码
     * @param pageSize 每页记录数
     * @return 档口菜品视图DTO列表
     */
    List<StallDishViewDTO> getDishListByStallFromView(Integer stallId, Integer pageNum, Integer pageSize);

    /**
     * 获取菜品详情
     * @param dishId 菜品ID
     * @return 菜品详情
     */
    DishDTO getDishDetail(Integer dishId);

    /**
     * 新增菜品
     * @param dish 菜品信息
     * @return 影响行数
     */
    int addDish(Dish dish);

    /**
     * 更新菜品
     * @param dish 菜品信息
     * @return 影响行数
     */
    int updateDish(Dish dish);

    /**
     * 删除菜品
     * @param dishId 菜品ID
     * @return 影响行数
     */
    int deleteDish(Integer dishId);
} 