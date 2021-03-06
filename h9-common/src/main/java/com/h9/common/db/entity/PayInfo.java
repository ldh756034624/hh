package com.h9.common.db.entity;

import com.h9.common.base.BaseEntity;
import lombok.Data;

import javax.persistence.*;

import java.math.BigDecimal;

import static javax.persistence.GenerationType.IDENTITY;

/**
 * Created with IntelliJ IDEA.
 * PayInfo:刘敏华 shadow.liu@hey900.com
 * Date: 2018/1/11
 * Time: 16:32
 */
@Data
@Entity
@Table(name = "pay_info")
public class PayInfo extends BaseEntity {


    @Id
    @SequenceGenerator(name = "h9-parentSeq", sequenceName = "h9-parent_SEQ", allocationSize = 1, initialValue = 1)
    @GeneratedValue(strategy = IDENTITY, generator = "h9-parentSeq")
    private Long id;

    @Column(name = "money",columnDefinition = "DECIMAL(10,2) default 0.00 COMMENT '支付金额'")
    private BigDecimal money = new BigDecimal(0);
    
    @Column(name = "order_id", columnDefinition = "bigint(20) default null COMMENT ''")
    private Long orderId;

    /**
     * @see OrderTypeEnum
     */
    @Column(name = "order_type",nullable = false,columnDefinition = "int default 0 COMMENT '订单类型'")
    private Integer orderType;

    @Column(name = "pay_order_id", columnDefinition = "bigint(20) default null COMMENT '支付订单号'")
    private Long payOrderId;

    @Column(name = "status",nullable = false,columnDefinition = "tinyint default 1 COMMENT '1 待支付， 2 已回调成功 3 回调出错'")
    private Integer status = 1;


    public PayInfo( ) {
    }

    public PayInfo(BigDecimal money, Long orderId, Integer orderType, Long payOrderId, Integer status) {
        this.money = money;
        this.orderId = orderId;
        this.orderType = orderType;
        this.payOrderId = payOrderId;
        this.status = status;
    }

    public enum OrderTypeEnum{
        Recharge(0,"充值"),
        HOTEL(1,"酒店"),
        STORE_ORDER(2,"商城订单");

        OrderTypeEnum(int id,String name){
            this.id = id;
            this.name = name;
        }

        private int id;
        private String name;

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }


    }



}
