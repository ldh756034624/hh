package com.h9.common.db.entity.community;

import com.h9.common.base.BaseEntity;
import com.h9.common.db.entity.user.User;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;

/**
 * Created with IntelliJ IDEA.
 * Description:
 * User:刘敏华 shadow.liu@hey900.com
 * Date: 2017/12/29
 * Time: 15:36
 */

@Entity
@Table(name = "stick_comment")
public class StickComment extends BaseEntity {


    @Id
    @SequenceGenerator(name = "h9-parentSeq", sequenceName = "h9-parent_SEQ", allocationSize = 1, initialValue = 1)
    @GeneratedValue(strategy = IDENTITY, generator = "h9-parentSeq")
    private Long id;

    @ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JoinColumn(name = "answer_user_id",nullable = false,referencedColumnName="id",columnDefinition = "bigint(20) default 0 COMMENT ''",foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private User answerUser;

    @ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JoinColumn(name = "stick_id",nullable = false,referencedColumnName="id",columnDefinition = "bigint(20) default 0 COMMENT '帖子'",foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Stick stick;

    @Column(name = "content", nullable = false, columnDefinition = "varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci default '' COMMENT '内容'")
    private String content;

    @ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JoinColumn(name = "parent_id",referencedColumnName="id",columnDefinition = "bigint(20) default 0 COMMENT '父级'",foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private StickComment stickComment;

    @Column(name = "level",nullable = false,columnDefinition = "int default 1 COMMENT '评论级别'")
    private Integer level = 1;

    @Column(name = "floor",nullable = false,columnDefinition = "int default 1 COMMENT '楼层信息'")
    private Integer floor = 1;

    @ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    @JoinColumn(name = "notify_user_id",referencedColumnName="id",columnDefinition = "bigint(20) default 0 COMMENT '@的用户'",foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private User notify_user;
    
    @Column(name = "like_count",nullable = false,columnDefinition = "int default 0 COMMENT '点赞数'")
    private Integer likeCount;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getAnswerUser() {
        return answerUser;
    }

    public void setAnswerUser(User answerUser) {
        this.answerUser = answerUser;
    }

    public Stick getStick() {
        return stick;
    }

    public void setStick(Stick stick) {
        this.stick = stick;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public StickComment getStickComment() {
        return stickComment;
    }

    public void setStickComment(StickComment stickComment) {
        this.stickComment = stickComment;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getFloor() {
        return floor;
    }

    public void setFloor(Integer floor) {
        this.floor = floor;
    }

    public User getNotify_user() {
        return notify_user;
    }

    public void setNotify_user(User notify_user) {
        this.notify_user = notify_user;
    }

    public Integer getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(Integer likeCount) {
        this.likeCount = likeCount;
    }
}
