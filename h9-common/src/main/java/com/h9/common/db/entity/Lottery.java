package com.h9.common.db.entity;

import com.h9.common.base.BaseEntity;

import javax.persistence.*;

import java.math.BigDecimal;
import java.util.Date;

import static javax.persistence.GenerationType.IDENTITY;
import static javax.persistence.TemporalType.TIMESTAMP;

/**
 * Created with IntelliJ IDEA.
 * Description: 具体用户的抽奖信息
 * User:刘敏华 shadow.liu@hey900.com
 * Date: 2017/10/28
 * Time: 10:21
 */

@Entity
@Table(name = "lottery")
public class Lottery extends BaseEntity {

    @Id
    @SequenceGenerator(name = "h9-apiSeq", sequenceName = "h9-api_SEQ", allocationSize = 1, initialValue = 1)
    @GeneratedValue(strategy = IDENTITY, generator = "h9-apiSeq")
    private Long id;

    @Column(name = "user_id", columnDefinition = "bigint(20) default null COMMENT '奖励领取用户id'")
    private Long userId;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "reward_id", nullable = false, referencedColumnName = "id", columnDefinition = "bigint(20) default 0 COMMENT '奖励id'")
    private Reward reward;

    @Column(name = "money", columnDefinition = "DECIMAL(10,2) default 0.00 COMMENT '奖励领取金额'")
    private BigDecimal money = new BigDecimal(0);

    @OneToOne(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JoinColumn(name = "user_record_id",nullable = false,referencedColumnName="id",columnDefinition = "bigint(20) default 0 COMMENT ''")
    private UserRecord userRecord;

    @Column(name = "status", nullable = false, columnDefinition = "tinyint default 1 COMMENT '1 未分配奖励 2已分配奖励 3完成'")
    private Integer status = 1;

    @Temporal(TIMESTAMP)
    @Column(name = "finish_time", columnDefinition = "datetime COMMENT '获奖时间'")
    private Date finishTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Reward getReward() {
        return reward;
    }

    public void setReward(Reward reward) {
        this.reward = reward;
    }

    public BigDecimal getMoney() {
        return money;
    }

    public void setMoney(BigDecimal money) {
        this.money = money;
    }

    public UserRecord getUserRecord() {
        return userRecord;
    }

    public void setUserRecord(UserRecord userRecord) {
        this.userRecord = userRecord;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(Date finishTime) {
        this.finishTime = finishTime;
    }
}