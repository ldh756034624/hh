package com.h9.common.db.repo;

import com.h9.common.base.BaseRepository;
import com.h9.common.db.entity.wxEvent.ReplyMessage;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Created by Ln on 2018/3/5.
 */
public interface ReplyMessageRepository extends BaseRepository<ReplyMessage> {

//    List<ReplyMessage> findByKeyWordRegex(String regex);

    List<ReplyMessage> findByEventType(String eventType);

    @Query("select r from ReplyMessage r where r.orderName = ?1")
    ReplyMessage fingByOrderName(String orderName);
}