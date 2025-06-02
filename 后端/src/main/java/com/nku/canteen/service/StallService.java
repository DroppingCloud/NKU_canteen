package com.nku.canteen.service;

import com.nku.canteen.dto.StallDTO;
import com.nku.canteen.entity.Stall;

import java.util.List;

/**
 * 档口服务接口
 */
public interface StallService {

    /**
     * 获取指定食堂下的档口列表
     * @param canteenId 食堂ID
     * @return 档口列表
     */
    List<StallDTO> getStallListByCanteen(Integer canteenId);

    /**
     * 获取档口详情
     * @param stallId 档口ID
     * @return 档口详情
     */
    StallDTO getStallDetail(Integer stallId);

    /**
     * 新增档口
     * @param stall 档口信息
     * @return 影响行数
     */
    int addStall(Stall stall);

    /**
     * 更新档口
     * @param stall 档口信息
     * @return 影响行数
     */
    int updateStall(Stall stall);

    /**
     * 删除档口
     * @param stallId 档口ID
     * @return 影响行数
     */
    int deleteStall(Integer stallId);
} 