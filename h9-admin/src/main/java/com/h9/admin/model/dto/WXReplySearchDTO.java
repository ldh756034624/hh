package com.h9.admin.model.dto;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * <p>Title:h9-parent</p>
 * <p>Desription:</p>
 *
 * @author LiYuan
 * @Date 2018/3/15
 */
@Data
@ApiModel(description = "微信自动回复搜索条件")
public class WXReplySearchDTO {
    private Integer page = 1;
    private Integer limit = 10;
    private String orderName;
    private Integer matchStrategy;
    private Integer status;
}
