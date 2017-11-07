package com.h9.admin.model.dto.basis;

import com.h9.common.db.entity.BankType;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.BeanUtils;

import javax.persistence.Column;


/**
 * @author: George
 * @date: 2017/11/7 14:38
 */
public class BankTypeAddDTO {

    @ApiModelProperty(value = "名称",required = true)
    @NotEmpty(message = "名称不能为空")
    private String bankName;

    @ApiModelProperty(value = "图标",required = true)
    @NotEmpty(message = "图标不能为空")
    private String bankImg;

    @ApiModelProperty(value = "背景色",required = true)
    @NotEmpty(message = "背景色不能为空")
    private String color;

    @ApiModelProperty(value = "状态， 1：启用，0：禁用",required = true)
    @NotEmpty(message = "状态不能为空")
    private Integer status;

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getBankImg() {
        return bankImg;
    }

    public void setBankImg(String bankImg) {
        this.bankImg = bankImg;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public BankType toBankType(){
        BankType bankType = new BankType();
        BeanUtils.copyProperties(this,bankType);
        return  bankType;
    }
}
