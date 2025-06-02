package com.nku.canteen.mapper;

import com.nku.canteen.entity.Stall;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * 档口Mapper接口
 */
@Repository
public interface StallMapper {
    
    /**
     * 根据食堂ID查询档口列表
     * @param canteenId 食堂ID
     * @return 档口列表
     */
    List<Stall> selectByCanteenId(@Param("canteenId") Integer canteenId);
    
    /**
     * 根据档口ID查询档口信息
     * @param stallId 档口ID
     * @return 档口信息
     */
    Stall selectById(@Param("stallId") Integer stallId);
    
    /**
     * 新增档口
     * @param stall 档口信息
     * @return 影响行数
     */
    int insert(Stall stall);
    
    /**
     * 更新档口
     * @param stall 档口信息
     * @return 影响行数
     */
    int update(Stall stall);
    
    /**
     * 通过存储过程更新档口
     * @param paramMap 参数Map，包含输入参数和输出参数
     */
    void updateStallByProcedure(Map<String, Object> paramMap);
    
    /**
     * 删除档口
     * @param stallId 档口ID
     * @return 影响行数
     */
    int deleteById(@Param("stallId") Integer stallId);
} 