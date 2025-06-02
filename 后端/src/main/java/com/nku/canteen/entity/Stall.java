package com.nku.canteen.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * 档口实体类
 */
@Data
public class Stall implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 档口编号，主键 */
    private Integer stallId;

    /** 档口名称 */
    private String name;

    /** 所属食堂编号 */
    private Integer canteenId;

    /**.楼层位置，如"1F"、"2F"等 */
    private String floor;

    /** 档口样图 */
    private String img;
    
    /** 档口简介 */
    private String intro;
} 