package com.h9.common.db.entity.bigrich;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;

/**
 * Created by Ln on 2018/4/15.
 */
@Table(name = "orders_lottery_relation")
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrdersLotteryRelation {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Column(name = "user_id", columnDefinition = "bigint COMMENT 'uid'")
    private Long userId;

    @Column(name = "order_id", columnDefinition = "bigint COMMENT 'order id '")
    private Long orderId;

    @Column(name = "orders_lottery_activity_id", columnDefinition = "bigint COMMENT ''")
    private Long ordersLotteryActivityId;
}
