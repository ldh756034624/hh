package com.h9.common.modle.dto;

import com.h9.common.modle.dto.PageDTO;
import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.Size;
import java.util.Date;

/**
 * @author: George
 * @date: 2017/11/7 20:04
 */
public class RewardQueryDTO extends PageDTO{

    @Size(max = 64,message = "兑奖码过长")
    @ApiModelProperty(value = "兑奖码")
    private String code;

    @ApiModelProperty(value = "开始时间")
    private Date startTime;

    @ApiModelProperty(value = "结束时间")
    private Date endTime;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = new Date(startTime);
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = new Date(endTime);
    }
}
