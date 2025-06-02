package com.nku.canteen.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 档口详情数据传输对象
 */
@Data
public class StallDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 档口编号 */
    @JsonProperty("stall_id")
    private Integer stallId;

    /** 档口名称 */
    private String name;

    /** 所属食堂编号 */
    @JsonProperty("canteen_id")
    private Integer canteenId;

    /** 楼层位置 */
    private String floor;

    /** 档口样图 */
    private String img;

    /** 档口简介 */
    @JsonProperty("intro")
    private String intro;

    /** 点赞数量 */
    @JsonProperty("like_count")
    private Integer likeCount;

    /** 评论数量 */
    @JsonProperty("comment_count")
    private Integer commentCount;

    /** 评论列表 */
    private List<CommentDTO> comments;
} 