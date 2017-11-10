package com.h9.admin.model.vo;

import com.h9.common.db.entity.OrderItems;
import com.h9.common.db.entity.Orders;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.beans.BeanUtils;

import java.math.BigDecimal;
import java.util.stream.Collectors;

/**
 * Created by Gonyb on 2017/11/10.
 */
public class OrderItemVO {
    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "订单编号")
    private String no;

    @ApiModelProperty(value ="收货人姓名")
    private String userName;

    @ApiModelProperty(value ="收货人号码")
    private String userPhone;

    @ApiModelProperty(value ="用户收货地址")
    private String userAddres;

    @ApiModelProperty(value ="支付方式")
    private Integer payMethond;

    @ApiModelProperty(value ="订单金额")
    private BigDecimal money = new BigDecimal(0);

    @ApiModelProperty(value ="需要支付的金额")
    private BigDecimal payMoney = new BigDecimal(0);

    @ApiModelProperty(value ="支付状态 1待支付 2 已支付")
    private Integer payStatus = 1;

    @ApiModelProperty(value ="订单状态")
    private Integer status = 1;

    @ApiModelProperty(value ="用户id")
    private Long userId;
    
    @ApiModelProperty(value ="商品")
    private String goods;
    
    @ApiModelProperty(value ="订单类别 1手机卡 2滴滴卡 3实物")
    private Integer orderType ;
    
    @ApiModelProperty(value ="物流单号")
    private String logisticsNumber;

    @ApiModelProperty(value ="滴滴券号")
    private String didiCardNumber;

    public static OrderItemVO toOrderItemVO(Orders orders){
        OrderItemVO orderItemVO = new OrderItemVO();
        BeanUtils.copyProperties(orders,orderItemVO);
        String collect = orders.getOrderItems().stream()
                .map(orderItem -> orderItem.getName() + " *" + orderItem.getCount())
                .collect(Collectors.joining(","));
        orderItemVO.setGoods(collect);
        String didi = orders.getOrderItems().stream()
                .map(OrderItems::getDidiCardNumber)
                .collect(Collectors.joining(","));
        orderItemVO.setDidiCardNumber(didi);
        return orderItemVO;
    }

    public String getDidiCardNumber() {
        return didiCardNumber;
    }

    public void setDidiCardNumber(String didiCardNumber) {
        this.didiCardNumber = didiCardNumber;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
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

    public Integer getPayMethond() {
        return payMethond;
    }

    public void setPayMethond(Integer payMethond) {
        this.payMethond = payMethond;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    public BigDecimal getPayMoney() {
        return payMoney;
    }

    public void setPayMoney(BigDecimal payMoney) {
        this.payMoney = payMoney;
    }

    public Integer getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(Integer payStatus) {
        this.payStatus = payStatus;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getGoods() {
        return goods;
    }

    public void setGoods(String goods) {
        this.goods = goods;
    }

    public Integer getOrderType() {
        return orderType;
    }

    public void setOrderType(Integer orderType) {
        this.orderType = orderType;
    }

    public String getLogisticsNumber() {
        return logisticsNumber;
    }

    public void setLogisticsNumber(String logisticsNumber) {
        this.logisticsNumber = logisticsNumber;
    }
    
    
}
