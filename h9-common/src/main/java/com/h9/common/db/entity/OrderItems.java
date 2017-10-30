package com.h9.common.db.entity;

import com.h9.common.base.BaseEntity;

import javax.persistence.*;

import java.math.BigDecimal;

import static javax.persistence.GenerationType.IDENTITY;

/**
 * Created with IntelliJ IDEA.
 * Description:订单商品
 * User:刘敏华 shadow.liu@hey900.com
 * Date: 2017/10/28
 * Time: 15:57
 */

@Entity
@Table(name = "orderItems")
public class OrderItems extends BaseEntity {


    @Id
    @SequenceGenerator(name = "h9-apiSeq", sequenceName = "h9-api_SEQ", allocationSize = 1, initialValue = 1)
    @GeneratedValue(strategy = IDENTITY, generator = "h9-apiSeq")
    private Long id;

    @Column(name = "name", nullable = false, columnDefinition = "varchar(16) default '' COMMENT '商品名称'")
    private String name;

    @Column(name = "image", nullable = false, columnDefinition = "varchar(256) default '' COMMENT '商品图片'")
    private String image;

    @Column(name = "price",columnDefinition = "DECIMAL(10,2) default 0.00 COMMENT '价格'")
    private BigDecimal price = new BigDecimal(0);

    @Column(name = "money",columnDefinition = "DECIMAL(10,2) default 0.00 COMMENT '金额'")
    private BigDecimal money = new BigDecimal(0);

    @Column(name = "pay_methond",nullable = false,columnDefinition = "int default 0 COMMENT '支付类型'")
    private Integer payMethond;

    @ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JoinColumn(name = "orders_id",nullable = false,referencedColumnName="id",columnDefinition = "bigint(20) default 0 COMMENT '订单商品id'")
    private Orders orders;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Orders getOrders() {
        return orders;
    }

    public void setOrders(Orders orders) {
        this.orders = orders;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    public Integer getPayMethond() {
        return payMethond;
    }

    public void setPayMethond(Integer payMethond) {
        this.payMethond = payMethond;
    }
}