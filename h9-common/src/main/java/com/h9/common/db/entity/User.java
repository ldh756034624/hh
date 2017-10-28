package com.h9.common.db.entity;

import com.h9.common.base.BaseEntity;
import org.hibernate.dialect.unique.DB2UniqueDelegate;

import javax.naming.Name;
import javax.persistence.*;

import java.math.BigDecimal;
import java.util.Date;

import static javax.persistence.GenerationType.IDENTITY;
import static javax.persistence.TemporalType.DATE;
import static javax.persistence.TemporalType.TIMESTAMP;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User:刘敏华 shadow.liu@hey900.com
 * Date: 2017/10/26
 * Time: 11:16
 */

@Entity
@Table(name = "user")
public class User extends BaseEntity {

    @Id
    @SequenceGenerator(name = "h9-apiSeq", sequenceName = "h9-api_SEQ", allocationSize = 1, initialValue = 1)
    @GeneratedValue(strategy = IDENTITY, generator = "h9-apiSeq")
    private Long id;
    
    @Column(name = "phone", nullable = false, columnDefinition = "varchar(11) default '' COMMENT '手机号'")
    private String phone;

    @Column(name = "open_id",  columnDefinition = "varchar(64) default null COMMENT '微信openId'")
    private String openId;

    @Column(name = "avatar", nullable = false, columnDefinition = "varchar(128) default '' COMMENT '用户头像'")
    private String avatar;

    @Column(name = "nick_name", nullable = false, columnDefinition = "varchar(64) default '' COMMENT '昵称'")
    private String nickName;


    @Temporal(TIMESTAMP)
    @Column(name = "last_login_time", columnDefinition = "datetime COMMENT '最后登陆时间'")
    private Date lastLoginTime;

    @Column(name = "login_count",nullable = false,columnDefinition = "int default 0 COMMENT '登录次数'")
    private Integer loginCount;

    @Column(name = "status",nullable = false,columnDefinition = "tinyint default 1 COMMENT ' 1 启用 2禁用 3失效'")
    private Integer status = 1;

    //TODO 待定
    @Column(name = "type",nullable = false,columnDefinition = "tinyint default 1 COMMENT ' 1 正常用户'")
    private Integer type = 1;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }


    public String getOpenId() {
        return openId;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public Date getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(Date lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public Integer getLoginCount() {
        return loginCount;
    }

    public void setLoginCount(Integer loginCount) {
        this.loginCount = loginCount;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
