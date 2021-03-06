package com.h9.api.model.vo;

import com.h9.common.db.entity.coupon.UserCoupon;
import com.h9.common.db.entity.order.GoodsType;
import com.h9.common.db.entity.order.OrderItems;
import com.h9.common.db.entity.order.Orders;
import com.h9.common.utils.DateUtil;
import com.h9.common.utils.MoneyUtils;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by itservice on 2017/11/1.
 */
public class OrderDetailVO {

    private String company = "";
    private String orderStatus = "";
    private String orderType;
    private String accepterName = "";
    private String tel = "";
    private String address = "";
    private String orderId = "";
    private String payMethod = "";
    private String payMoney = "";
    private String createOrderDate = "";
    private List<GoodsInfo> goodsInfoList;
    private String couponsNumber = "";
    private String companyIcon = "";
    private String logisticsNumber = "";
    // 优惠信息
    private String couponMessage = "";

    /**
     * description:  充值面额
     */
    private String rechargeMoney = "";


    public static OrderDetailVO convert(Orders order, UserCoupon userCoupon) {
        OrderDetailVO vo = new OrderDetailVO();
        vo.setCompany(order.getSupplierName());


        Orders.statusEnum statusEnum = Orders.statusEnum.findByCode(order.getStatus());
        vo.setOrderStatus(statusEnum.getDesc());

        if (order.getOrderItems().get(0).getGoods().getGoodsType().getReality() == 1) {
            vo.setOrderType("3");
        } else if (GoodsType.GoodsTypeEnum.MOBILE_RECHARGE.getCode().equals(order.getOrderItems().get(0).getGoods().getGoodsType().getCode())) {
            vo.setOrderType("1");
        } else {
            vo.setOrderType("2");
        }

        vo.setCompanyIcon("https://cdn-h9-img.thy360.com/FtXvdZ8JOfbF6YmzFWHHMpgmTo6r");
        vo.setTel(order.getUserPhone());
        vo.setRechargeMoney(MoneyUtils.formatMoney(order.getPayMoney()));
        if (order.getOrderType().equals(Orders.orderTypeEnum.MATERIAL_GOODS.getCode() + "")) {
            vo.setAccepterName(order.getUserName());
            vo.setAddress(order.getUserAddres());
            String expressNum = order.getExpressNum();
            vo.setLogisticsNumber(expressNum == null ? "" : expressNum);
        }


        List<OrderItems> orderItems = order.getOrderItems();
        if (!CollectionUtils.isEmpty(orderItems)) {
            vo.setCouponsNumber(orderItems.get(0).getDidiCardNumber());
        }
        vo.setOrderId(order.getId() + "");
        int payMethond = order.getPayMethond();
        Orders.PayMethodEnum byCode = Orders.PayMethodEnum.findByCode(payMethond);
        if (byCode != null) {
            vo.setPayMethod(byCode.getDesc());
        } else {
            vo.setPayMethod("余额支付");

        }

        if (userCoupon != null) {
            vo.setCouponMessage("免单券抵扣￥" + orderItems.get(0).getGoods().getRealPrice());
        } else {
            vo.setCouponMessage("无优惠");
        }

        vo.setPayMoney(order.getPayMoney() + "");
        vo.setCreateOrderDate(DateUtil.formatDate(order.getCreateTime(), DateUtil.FormatType.GBK_MINUTE));
        List<OrderItems> itemList = order.getOrderItems();
        List<GoodsInfo> goodsInfos = itemList.stream().map(item -> {
            GoodsInfo goodsInfo = new GoodsInfo();
            goodsInfo.setGoodsName(item.getName());
            goodsInfo.setImgUrl(item.getImage());
            goodsInfo.setCount(item.getCount() + "");
            return goodsInfo;
        }).collect(Collectors.toList());

        vo.setGoodsInfoList(goodsInfos);
        return vo;
    }


    public String getRechargeMoney() {
        return rechargeMoney;
    }

    public void setRechargeMoney(String rechargeMoney) {
        this.rechargeMoney = rechargeMoney;
    }

    public String getLogisticsNumber() {
        return logisticsNumber;
    }

    public void setLogisticsNumber(String logisticsNumber) {
        this.logisticsNumber = logisticsNumber;
    }

    public String getCompanyIcon() {
        return companyIcon;
    }

    public void setCompanyIcon(String companyIcon) {
        this.companyIcon = companyIcon;
    }

    public String getCouponsNumber() {
        return couponsNumber;
    }

    public void setCouponsNumber(String couponsNumber) {
        this.couponsNumber = couponsNumber;
    }

    public List<GoodsInfo> getGoodsInfoList() {
        return goodsInfoList;
    }

    public void setGoodsInfoList(List<GoodsInfo> goodsInfoList) {
        this.goodsInfoList = goodsInfoList;
    }

    private static class GoodsInfo {
        private String imgUrl;
        private String GoodsName;
        private String count = "1";

        public String getCount() {
            return count;
        }

        public void setCount(String count) {
            this.count = count;
        }

        public String getImgUrl() {
            return imgUrl;
        }

        public void setImgUrl(String imgUrl) {
            this.imgUrl = imgUrl;
        }

        public String getGoodsName() {
            return GoodsName;
        }

        public void setGoodsName(String goodsName) {
            GoodsName = goodsName;
        }
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }


    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public String getAccepterName() {
        return accepterName;
    }

    public void setAccepterName(String accepterName) {
        this.accepterName = accepterName;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getPayMethod() {
        return payMethod;
    }

    public void setPayMethod(String payMethod) {
        this.payMethod = payMethod;
    }

    public String getPayMoney() {
        return payMoney;
    }

    public void setPayMoney(String payMoney) {
        this.payMoney = payMoney;
    }

    public String getCreateOrderDate() {
        return createOrderDate;
    }

    public void setCreateOrderDate(String createOrderDate) {
        this.createOrderDate = createOrderDate;
    }

    public String getCouponMessage() {
        return couponMessage;
    }

    public void setCouponMessage(String couponMessage) {
        this.couponMessage = couponMessage;
    }
}
