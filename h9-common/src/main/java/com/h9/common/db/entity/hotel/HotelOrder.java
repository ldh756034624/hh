package com.h9.common.db.entity.hotel;

import com.h9.common.base.BaseEntity;
import com.h9.common.db.entity.order.Orders;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;


@Data
@Entity
@Accessors(chain = true)
@Table(name = "hotel_order")
public class HotelOrder extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "hotel_name",columnDefinition = "varchar(255) comment '酒店名'")
    private String hotelName;

    @Column(name = "hotel_address",columnDefinition = "varchar(255) comment '酒店地址'")
    private String hotelAddress;

    @Column(name = "come_room_time",columnDefinition = "datetime comment '入住时间'")
    private Date comeRoomTime;

    @Column(name = "out_room_time",columnDefinition = "datetime comment '入住时间'")
    private Date outRoomTime;

    @Column(name = "room_type_name",columnDefinition = "varchar(255) comment '房间名'")
    private String roomTypeName;

    @ManyToOne
    @JoinColumn(name = "hotel_room_type_id", referencedColumnName = "id", columnDefinition = "bigint(40)  COMMENT '酒店房间Id'",foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private HotelRoomType hotelRoomType;

    @Column(name = "pay_money4_jiu_yuan",columnDefinition = "decimal(10,2) comment '酒元支付'")
    private BigDecimal PayMoney4JiuYuan;

    @Column(name = "pay_money4_wechat",columnDefinition = "decimal(10,2) comment '微信支付'")
    private BigDecimal PayMoney4Wechat;

    @Column(name = "roomer",columnDefinition = "varchar(255) comment '入住人'")
    private String roomer;

    @Column(name = "phone",columnDefinition = "varchar(255) comment ''")
    private String phone;

    @Column(name = "room_count",columnDefinition = "int comment '房间数'")
    private Integer roomCount;

    @Column(name = "pay_method",columnDefinition = "int comment '支付方式'")
    private Integer payMethod;

    @ManyToOne
    @JoinColumn(name = "hotel_id", referencedColumnName = "id", columnDefinition = "bigint(40)  COMMENT '酒店Id'",foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
    private Hotel hotel;

    @Column(name = "total_money",columnDefinition = "decimal(10,2) comment '订单总金额'")
    private BigDecimal totalMoney;

    //待支付，【待确认，预订成功】，已退款，已取消
    @Getter
    public static enum PayMethodEnum{
        BALANCE_PAY(1, "待支付"),
        WECHAT_PAY(2,"待确认"),
        MIXED_PAY(3,"预订成功");

        public int code;
        public String desc;

        PayMethodEnum(int code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        public static PayMethodEnum findByCode(int code) {
            PayMethodEnum[] values = values();
            for (PayMethodEnum elEnum : values) {
                if (code == elEnum.getCode()) {
                    return elEnum;
                }
            }
            return null;
        }
    }

    /**
     * description:
     * @see OrderStatusEnum
     */
    @Column(name = "order_status",columnDefinition = "int comment ''")
    private Integer orderStatus;

    //待支付，【待确认，预订成功】，已退款，已取消
    @Getter
    public static enum OrderStatusEnum{
        NOT_PAID(1, "待支付"),
        WAIT_ENSURE(2,"待确认"),
        SUCCESS(3,"预订成功"),
        REFUND_MONEY(4,"已退款"),
        CANCEL(5,"已取消");

        public int code;
        public String desc;

        OrderStatusEnum(int code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        public static OrderStatusEnum findByCode(int code) {
            OrderStatusEnum[] values = values();
            for (OrderStatusEnum elEnum : values) {
                if (code == elEnum.getCode()) {
                    return elEnum;
                }
            }
            return null;
        }
    }

}