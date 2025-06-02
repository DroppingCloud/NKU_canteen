package com.nku.canteen.service;

import com.nku.canteen.dto.CanteenDTO;
import com.nku.canteen.entity.Canteen;

import java.util.List;

/**
 * 食堂服务接口
 */
public interface CanteenService {

    /**
     * 获取食堂列表
     * @param pageNum 页码
     * @param pageSize 每页记录数
     * @return 食堂列表
     */
    List<CanteenDTO> getCanteenList(Integer pageNum, Integer pageSize);
    
    /**
     * 根据校区获取食堂列表
     * @param campus 校区名称
     * @param pageNum 页码
     * @param pageSize 每页记录数
     * @return 食堂列表
     */
    List<CanteenDTO> getCanteenListByCampus(String campus, Integer pageNum, Integer pageSize);

    /**
     * 获取食堂详情
     * @param canteenId 食堂ID
     * @return 食堂详情
     */
    CanteenDTO getCanteenDetail(Integer canteenId);

    /**
     * 新增食堂
     * @param canteen 食堂信息
     * @return 影响行数
     */
    int addCanteen(Canteen canteen);

    /**
     * 更新食堂
     * @param canteen 食堂信息
     * @return 影响行数
     */
    int updateCanteen(Canteen canteen);

    /**
     * 删除食堂
     * @param canteenId 食堂ID
     * @return 影响行数
     */
    int deleteCanteen(Integer canteenId);
} 