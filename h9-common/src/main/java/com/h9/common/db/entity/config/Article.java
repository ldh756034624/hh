package com.h9.common.db.entity.config;

import com.h9.common.base.BaseEntity;

import javax.persistence.*;
import java.util.Date;

import static javax.persistence.GenerationType.IDENTITY;
import static javax.persistence.TemporalType.TIMESTAMP;


@Entity
@Table(name = "article")
public class Article extends BaseEntity {


    @Id
    @SequenceGenerator(name = "h9-apiSeq", sequenceName = "h9-api_SEQ", allocationSize = 1, initialValue = 1)
    @GeneratedValue(strategy = IDENTITY, generator = "h9-apiSeq")
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JoinColumn(name = "article_type_id",nullable = false,referencedColumnName="id",columnDefinition = "bigint(20) default 0 COMMENT '标题类型'")
    private ArticleType articleType;

    @Column(name = "title", nullable = false, columnDefinition = "varchar(128) default '' COMMENT '标题'")
    private String title;

    @Column(name = "content", nullable = false, columnDefinition = "text  COMMENT '内容'")
    private String content;

    @Column(name = "url",  columnDefinition = "varchar(256) default '' COMMENT '跳转链接'")
    private String url;

    @Column(name = "user_name", nullable = false, columnDefinition = "varchar(128) default '' COMMENT '用户名'")
    private String userName;

    @Column(name = "enable",nullable = false,columnDefinition = "tinyint default 1 COMMENT ' 1 启用 0禁用 2删除'")
    private Integer enable;

    @Temporal(TIMESTAMP)
    @Column(name = "start_time", columnDefinition = "datetime COMMENT '开始时间'")
    private Date startTime;

    @Temporal(TIMESTAMP)
    @Column(name = "end_time", columnDefinition = "datetime COMMENT '结束时间'")
    private Date endTime;

    @Column(name = "sort",nullable = false,columnDefinition = "tinyint default 1 COMMENT '排序'")
    private Integer sort = 1;

    @Column(name = "recommend",nullable = false,columnDefinition = "tinyint default 1 COMMENT '1 推荐 2 不推荐'")
    private Integer recommend = 1;


    @Column(name = "img_url", columnDefinition = "varchar(200) COMMENT '文章显示图片'")
    private String imgUrl;
    public Long getId() {
        return id;
    }

    @Temporal(TIMESTAMP)
    @Column(name = "publish_time", columnDefinition = "datetime COMMENT '发布时间'")
    private Date publishTime;

    @Transient
    private String jointUrl;//拼接跳转链接

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ArticleType getArticleType() {
        return articleType;
    }

    public void setArticleType(ArticleType articleType) {
        this.articleType = articleType;
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

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public Integer getRecommend() {
        return recommend;
    }

    public void setRecommend(Integer recommend) {
        this.recommend = recommend;
    }

    public Date getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(Date publishTime) {
        this.publishTime = publishTime;
    }

    public String getJointUrl() {
        return jointUrl;
    }

    public void setJointUrl(String jointUrl) {
        this.jointUrl = jointUrl;
    }
}
