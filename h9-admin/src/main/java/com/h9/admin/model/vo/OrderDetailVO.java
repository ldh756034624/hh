package com.h9.admin.model.vo;

import com.h9.common.db.entity.PayInfo;
import com.h9.common.db.entity.RechargeOrder;
import com.h9.common.db.entity.order.Goods;
import com.h9.common.db.entity.order.OrderItems;
import com.h9.common.db.entity.order.Orders;
import com.h9.common.db.entity.account.RechargeRecord;
import com.h9.common.utils.DateUtil;
import com.h9.common.utils.MoneyUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.BeanUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author: George
 * @date: 2017/12/7 19:33
 */
public class OrderDetailVO {
    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "订单编号")
    private String no;

    @ApiModelProperty(value = "收货人姓名")
    private String userName;

    @ApiModelProperty(value = "收货人号码")
    private String userPhone;

    @ApiModelProperty(value = "用户收货地址")
    private String userAddres;

    @ApiModelProperty(value = "支付方式描述")
    private Integer payMethond;

    @ApiModelProperty(value = "支付方式描述")
    private String payMethodDesc;

    @ApiModelProperty(value = "订单金额")
    private BigDecimal money = new BigDecimal(0);

    @ApiModelProperty(value = "需要支付的金额")
    private BigDecimal payMoney = new BigDecimal(0);

    @ApiModelProperty(value = "支付状态描述")
    private String payStatusDesc;

    @ApiModelProperty(value = "支付状态 1待支付 2 已支付")
    private Integer payStatus = 1;

    @ApiModelProperty(value = "订单状态")
    private Integer status = 1;

    @ApiModelProperty(value = "订单状态描述")
    private String statusDesc;

    @ApiModelProperty(value = "用户id")
    private Long userId;

    @ApiModelProperty(value = "商品")
    private String goods;

    @ApiModelProperty(value = "订单类别 1手机卡 2滴滴卡 3实物")
    private String orderType;


    /**
     * 1 微信充值订单，2为其他
     */
    @ApiModelProperty(value = "订单类别")
    private String orderTypeCode;

    @ApiModelProperty(value = "物流单号")
    private String expressNum;

    @ApiModelProperty(value = "滴滴券号")
    private String didiCardNumber;

    @ApiModelProperty(value = "快递公司名")
    private String expressName;

    @ApiModelProperty(value = "商品数量")
    private Long count;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

    @ApiModelProperty(value = "充值手机号")
    private String tel;

    @ApiModelProperty(value = "供应商")
    private String supplier = "徽酒";

    @ApiModelProperty("订单来源")
    private String orderFrom;

    List<Goods> goodsList = new ArrayList<>();

    @ApiModelProperty("订单总金额")
    private String orderMoney;

    @ApiModelProperty("微信支付金额")
    private String payMoney4wx;

    @ApiModelProperty("余额支付")
    private String payMoney4Balance;

    @ApiModelProperty("微信订单流水号")
    private String wxOrderId;

    @ApiModel
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    public static class Goods{
        @ApiModelProperty("商品图片")
        private String goodsImg;

        @ApiModelProperty("商品名称")
        private String goodsName;

        @ApiModelProperty("数量")
        private String goodsCount;

        @ApiModelProperty("价格")
        private String goodsPrice;

    }

    public static OrderDetailVO toOrderDetailVO(Orders orders, RechargeRecord rechargeRecord, String wxOrderId) {
        OrderDetailVO orderDetailVO = new OrderDetailVO();
        BeanUtils.copyProperties(orders, orderDetailVO);
        orderDetailVO.setPayStatusDesc(orders.getPayStatus());
        orderDetailVO.setUserId(orders.getUser().getId());
        String collect = orders.getOrderItems().stream()
                .map(orderItem -> orderItem.getName() + " *" + orderItem.getCount())
                .collect(Collectors.joining(","));
        orderDetailVO.setGoods(collect);
        String didi = orders.getOrderItems().stream()
                .map(OrderItems::getDidiCardNumber)
                .collect(Collectors.joining(","));
        orderDetailVO.setDidiCardNumber(didi);
        Orders.PayMethodEnum byCode = Orders.PayMethodEnum.findByCode(orders.getPayMethond());
        orderDetailVO.setPayMethodDesc(byCode == null ? "未知的支付方式" : byCode.getDesc());
        //统计订单商品数量
        long sum = orders.getOrderItems().stream().parallel().mapToInt(OrderItems::getCount).summaryStatistics().getSum();
        orderDetailVO.setCount(sum);
        Orders.statusEnum statusEnum = Orders.statusEnum.findByCode(orders.getStatus());
        orderDetailVO.setStatusDesc(statusEnum == null ? null : statusEnum.getDesc());
        orderDetailVO.setTel(rechargeRecord == null ? null : orders.getUserPhone());

        orderDetailVO.setOrderTypeCode("2");
        orderDetailVO.orderFrom = "酒元商城";

        List<OrderItems> orderItems = orders.getOrderItems();

        List<Goods> goodsList = orderItems.stream().map(items -> {
            com.h9.common.db.entity.order.Goods goods = items.getGoods();
            Goods tempGoods = new Goods(goods.getImg(), goods.getName(), items.getCount() + " "+goods.getUnit(),
                    MoneyUtils.formatMoney(goods.getRealPrice().multiply(new BigDecimal(items.getCount()))));
            return tempGoods;
        }).collect(Collectors.toList());


        orderDetailVO.setGoodsList(goodsList);

        orderDetailVO.setOrderMoney(MoneyUtils.formatMoney(orders.getMoney()));
        int payMethod = orders.getPayMethond();
        if (payMethod == Orders.PayMethodEnum.WX_PAY.getCode()) {
            orderDetailVO.setPayMoney4wx(MoneyUtils.formatMoney(orders.getPayMoney()));
            orderDetailVO.setPayMoney4Balance(MoneyUtils.formatMoney(new BigDecimal(0)));
            orderDetailVO.setWxOrderId(wxOrderId);
        }else{
            orderDetailVO.setPayMoney4wx(MoneyUtils.formatMoney(new BigDecimal(0)));
            orderDetailVO.setPayMoney4Balance(MoneyUtils.formatMoney(orders.getPayMoney()));
        }

        return orderDetailVO;
    }

    public static OrderDetailVO toOrderDetailVO(RechargeOrder rechargeOrder,String wxOrderId) {
        OrderDetailVO orderDetailVO = new OrderDetailVO();
        orderDetailVO.setId(rechargeOrder.getId());
        orderDetailVO.setOrderType(PayInfo.OrderTypeEnum.Recharge.getName());
        orderDetailVO.setOrderFrom(PayInfo.OrderTypeEnum.Recharge.getName());
        orderDetailVO.setCreateTime(rechargeOrder.getCreateTime());
        orderDetailVO.setMoney(rechargeOrder.getMoney());
        orderDetailVO.setPayMethodDesc("微信支付");
        orderDetailVO.setPayMoney4wx(MoneyUtils.formatMoney(rechargeOrder.getMoney()));
        orderDetailVO.setWxOrderId(wxOrderId);
        orderDetailVO.setPayMoney4Balance("0.00元");
        orderDetailVO.setPayStatus(Orders.PayStatusEnum.PAID.getCode());
        orderDetailVO.setOrderTypeCode("1");
        return orderDetailVO;
    }

    public String getOrderTypeCode() {
        return orderTypeCode;
    }

    public void setOrderTypeCode(String orderTypeCode) {
        this.orderTypeCode = orderTypeCode;
    }

    public void setPayStatusDesc(String payStatusDesc) {
        this.payStatusDesc = payStatusDesc;
    }

    public void setCount(Long count) {
        this.count = count;
    }

    public String getSupplier() {
        return supplier;
    }

    public void setSupplier(String supplier) {
        this.supplier = supplier;
    }

    public String getOrderFrom() {
        return orderFrom;
    }

    public void setOrderFrom(String orderFrom) {
        this.orderFrom = orderFrom;
    }


    public List<Goods> getGoodsList() {
        return goodsList;
    }

    public void setGoodsList(List<Goods> goodsList) {
        this.goodsList = goodsList;
    }

    public String getOrderMoney() {
        return orderMoney;
    }

    public void setOrderMoney(String orderMoney) {
        this.orderMoney = orderMoney;
    }

    public String getPayMoney4wx() {
        return payMoney4wx;
    }

    public void setPayMoney4wx(String payMoney4wx) {
        this.payMoney4wx = payMoney4wx;
    }

    public String getPayMoney4Balance() {
        return payMoney4Balance;
    }

    public void setPayMoney4Balance(String payMoney4Balance) {
        this.payMoney4Balance = payMoney4Balance;
    }

    public String getWxOrderId() {
        return wxOrderId;
    }

    public void setWxOrderId(String wxOrderId) {
        this.wxOrderId = wxOrderId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public String getExpressName() {
        return expressName;
    }

    public void setExpressName(String expressName) {
        this.expressName = expressName;
    }

    public String getPayMethodDesc() {
        return payMethodDesc;
    }

    public void setPayMethodDesc(String payMethodDesc) {
        this.payMethodDesc = payMethodDesc;
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

    public String getStatusDesc() {
        return statusDesc;
    }

    public void setStatusDesc(String statusDesc) {
        this.statusDesc = statusDesc;
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

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getExpressNum() {
        return expressNum;
    }

    public void setExpressNum(String expressNum) {
        this.expressNum = expressNum;
    }

    public String getPayStatusDesc() {
        return payStatusDesc;
    }

    public void setPayStatusDesc(int payStatus) {
        this.payStatusDesc = Orders.PayStatusEnum.findByCode(payStatus).getDesc();
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }
}
