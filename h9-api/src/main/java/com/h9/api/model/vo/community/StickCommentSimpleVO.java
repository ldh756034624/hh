package com.h9.api.model.vo.community;

import com.h9.api.model.dto.Areas;
import com.h9.common.db.entity.community.StickComment;
import com.h9.common.db.entity.order.China;
import com.h9.common.db.entity.user.User;
import com.h9.common.utils.DateUtil;

import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import lombok.Data;

/**
 * Created by 李圆 on 2018/1/9
 */
@Data
public class StickCommentSimpleVO {

    private Long commentId;

    // 用户id
    private Long commentUserId;

    // 昵称
    private String nickName;

    // 回复内容
    private String content;

    // 被回复人昵称
    private String backNickName;

    // 被回复人id
    private Long backCommentUserId;

    private String spaceTime;

    private List<StickCommentSimpleVO> list;

    public StickCommentSimpleVO(StickComment stickComment){
        this.commentId = stickComment.getId();
        User answerUser = stickComment.getAnswerUser();
        this.commentUserId = answerUser.getId();
        this.nickName = answerUser.getNickName();
        this.content = stickComment.getContent();
        User notifyUserId = stickComment.getNotifyUserId();
        if(notifyUserId!=null){
            this.backCommentUserId = notifyUserId.getId();
            this.backNickName = notifyUserId.getNickName();
        }
        this.spaceTime = DateUtil.getSpaceTime(stickComment.getCreateTime(),new Date());
        List<StickComment> areas = stickComment.getList();
        if (!CollectionUtils.isEmpty(areas)){
            this.list = areas.stream().map(StickCommentSimpleVO::new).collect(Collectors.toList());
        }
    }
}
