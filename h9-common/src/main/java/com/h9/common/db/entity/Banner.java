package com.h9.common.db.entity;

import com.h9.common.base.BaseEntity;

import javax.persistence.*;

import java.util.Date;

import static javax.persistence.GenerationType.IDENTITY;
import static javax.persistence.TemporalType.TIMESTAMP;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User:刘敏华 shadow.liu@hey900.com
 * Date: 2017/10/27
 * Time: 18:14
 */

@Entity
@Table(name = "banner")
public class Banner extends BaseEntity {


    @Id
    @SequenceGenerator(name = "h9-apiSeq", sequenceName = "h9-api_SEQ", allocationSize = 1, initialValue = 1)
    @GeneratedValue(strategy = IDENTITY, generator = "h9-apiSeq")
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JoinColumn(name = "banner_type_id",nullable = false,referencedColumnName="id",columnDefinition = "bigint(20) default 0 COMMENT 'banner类型'")
    private BannerType bannerType;

    @Column(name = "title", nullable = false, columnDefinition = "varchar(128) default '' COMMENT '标题'")
    private String title;

    @Column(name = "content", nullable = false, columnDefinition = "varchar(512) default '' COMMENT '标题内容'")
    private String content;

    @Column(name = "url", nullable = false, columnDefinition = "varchar(256) default '' COMMENT '跳转链接'")
    private String url;

    @Column(name = "user_name", nullable = false, columnDefinition = "varchar(128) default '' COMMENT '用户名'")
    private String userName;

    @Column(name = "enable",nullable = false,columnDefinition = "tinyint default 1 COMMENT ' 1 启用 0禁用'")
    private Integer enable;

    @Temporal(TIMESTAMP)
    @Column(name = "start_time", columnDefinition = "datetime COMMENT '开始时间'")
    private Date startTime;

    @Temporal(TIMESTAMP)
    @Column(name = "end_time", columnDefinition = "datetime COMMENT '结束时间'")
    private Date endTime;



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BannerType getBannerType() {
        return bannerType;
    }

    public void setBannerType(BannerType bannerType) {
        this.bannerType = bannerType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getEnable() {
        return enable;
    }

    public void setEnable(Integer enable) {
        this.enable = enable;
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
}