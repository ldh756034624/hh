package com.h9.admin.model.dto.order;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

/**
 * Created by Gonyb on 2017/11/10.
 */
@ApiModel("快递信息")
public class ExpressDTO {

    @ApiModelProperty(value = "订单id",required = true)
    @NotNull(message = "订单id不能为空")
    private Long id;
    
    
    @ApiModelProperty(value = "收货人名称",required = true)
    @NotBlank(message = "userName不能为null")
    private String userName;
    
    @ApiModelProperty(value = "收货人手机",required = true)
    @NotBlank(message = "userPhone不能为null")
    private String userPhone;

    @ApiModelProperty(value = "收货地址",required = true)
    @NotBlank(message = "userAddress不能为null")
    private String userAddres;
    
    @ApiModelProperty(value = "快递公司",required = true)
    @NotBlank(message = "expressName不能为null")
    private String expressName;

    @ApiModelProperty(value = "快递单号",required = true)
    @NotBlank(message = "logisticsNumber不能为null")
    private String logisticsNumber;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getUserAddres() {
        return userAddres;
    }

    public void setUserAddres(String userAddres) {
        this.userAddres = userAddres;
    }

    public String getExpressName() {
        return expressName;
    }

    public void setExpressName(String expressName) {
        this.expressName = expressName;
    }

    public String getLogisticsNumber() {
        return logisticsNumber;
    }

    public void setLogisticsNumber(String logisticsNumber) {
        this.logisticsNumber = logisticsNumber;
    }
}
