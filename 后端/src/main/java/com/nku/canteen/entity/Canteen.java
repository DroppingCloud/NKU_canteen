package com.nku.canteen.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * 食堂实体类
 */
@Data
public class Canteen implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 食堂编号，主键 */
    private Integer canteenId;

    /** 食堂名称 */
    private String name;

    /** 食堂地理位置描述 */
    private String location;

    /** 营业时间 */
    private String openTime;

    /** 食堂样图 */
    private String img;
    
    /** 食堂校区 */
    private String campus;
} 