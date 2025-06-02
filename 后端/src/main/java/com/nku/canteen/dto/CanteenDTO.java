package com.nku.canteen.dto;

import lombok.Data;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * 食堂详情数据传输对象
 */
@Data
public class CanteenDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    /** 食堂编号 */
    @JsonProperty("canteen_id")
    private Integer canteenId;

    /** 食堂名称 */
    private String name;

    /** 食堂地理位置描述 */
    private String location;

    /** 营业时间 */
    @JsonProperty("open_time")
    private String openTime;

    /** 食堂样图 */
    private String img;
    
    /** 食堂校区 */
    private String campus;

    /** 点赞数量 */
    @JsonProperty("like_count")
    private Integer likeCount;

    /** 评论数量 */
    @JsonProperty("comment_count")
    private Integer commentCount;
    
    /** 平均评分 (基于所有档口的平均评分) */
    @JsonProperty("average_rating")
    private BigDecimal averageRating;

    /** 评论列表 */
    private List<CommentDTO> comments;
} 