package com.nku.canteen.mapper;

import com.nku.canteen.entity.Canteen;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 食堂Mapper接口
 */
@Repository
public interface CanteenMapper {
    
    /**
     * 查询所有食堂
     * @return 食堂列表
     */
    List<Canteen> selectAll();
    
    /**
     * 根据食堂ID查询食堂信息
     * @param canteenId 食堂ID
     * @return 食堂信息
     */
    Canteen selectById(@Param("canteenId") Integer canteenId);
    
    /**
     * 根据校区查询食堂
     * @param campus 校区名称
     * @return 食堂列表
     */
    List<Canteen> selectByCampus(@Param("campus") String campus);
    
    /**
     * 新增食堂
     * @param canteen 食堂信息
     * @return 影响行数
     */
    int insert(Canteen canteen);
    
    /**
     * 更新食堂
     * @param canteen 食堂信息
     * @return 影响行数
     */
    int update(Canteen canteen);
    
    /**
     * 删除食堂
     * @param canteenId 食堂ID
     * @return 影响行数
     */
    int deleteById(@Param("canteenId") Integer canteenId);
} 