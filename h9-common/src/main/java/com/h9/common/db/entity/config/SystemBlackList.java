package com.h9.common.db.entity.config;

import com.h9.common.base.BaseEntity;

import javax.persistence.*;
import java.util.Date;

import static javax.persistence.GenerationType.IDENTITY;
import static javax.persistence.TemporalType.TIMESTAMP;

/**
 * Created with IntelliJ IDEA.
 * Description:系统黑名单
 * User:刘敏华 shadow.liu@hey900.com
 * Date: 2017/10/28
 * Time: 10:27
 */

@Entity
@Table(name = "system_black_list")
public class SystemBlackList extends BaseEntity {

    @Id
    @SequenceGenerator(name = "h9-apiSeq", sequenceName = "h9-api_SEQ", allocationSize = 1, initialValue = 1)
    @GeneratedValue(strategy = IDENTITY, generator = "h9-apiSeq")
    private Long id;

    @Column(name = "user_id", columnDefinition = "bigint(20) default null COMMENT '黑明单用户'")
    private Long userId;

    @Temporal(TIMESTAMP)
    @Column(name = "start_time", columnDefinition = "datetime COMMENT '黑名单生效时间'")
    private Date startTime;

    @Temporal(TIMESTAMP)
    @Column(name = "end_time", columnDefinition = "datetime COMMENT '黑名单结束时间'")
    private Date endTime;

    @Column(name = "status",nullable = false,columnDefinition = "int default 1 COMMENT '黑名单状态 1 正常 2 禁用，失效黑名单'")
    private Integer status = 1;

    
    @Column(name = "cause", nullable = false, columnDefinition = "varchar(64) default '' COMMENT '加入黑名单原因'")
    private String cause;
    
    @Column(name = "reward_id", columnDefinition = "bigint(20) default null COMMENT '关联订单id'")
    private Long rewardId;

    @Column(name = "order_id", columnDefinition = "bigint(20) default null COMMENT '关联订单id'")
    private Long orderId;
    
    @Column(name = "imei", columnDefinition = "varchar(50) default null COMMENT '设备号,与用户id不能同时存在'")
    private String imei;


    public SystemBlackList(Long userId, Date startTime, Date endTime, Integer status, String cause, Long rewardId, Long orderId, String imei) {
        this.userId = userId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = status;
        this.cause = cause;
        this.rewardId = rewardId;
        this.orderId = orderId;
        this.imei = imei;
    }

    public SystemBlackList( ) {
    }

    public String getCause() {
        return cause;
    }

    public void setCause(String cause) {
        this.cause = cause;
    }

    public Long getRewardId() {
        return rewardId;
    }

    public void setRewardId(Long rewardId) {
        this.rewardId = rewardId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

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

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }


}
