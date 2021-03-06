package com.h9.api.service.wxEvent.impl;

import com.h9.api.service.wxEvent.EventHandlerStrategy;
import com.h9.api.service.wxEvent.EventHandlerStrategyFactory;
import com.h9.api.service.wxEvent.model.Message4wx;
import com.h9.common.db.entity.wxEvent.ReplyMessage;
import com.h9.common.db.repo.ReplyMessageRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;


/**
 * 文本消息处理
 */
@Service
public class TextEventHandlerStrategy implements EventHandlerStrategy<Message4wx> {

    @Resource
    private ReplyMessageRepository replyMessageRepository;

    /**
     * 处理关键字回复和自动回复,先判断关键字，再自动回复
     * @param map httpRequest中的请求参数转换成map
     * @return
     */
    @Transactional
    @Override
    public Message4wx handler(Map map, List<ReplyMessage> replyMessageList) {
        String key = map.get("Content").toString();
        if (key != null){
            // 全匹配关键字回复
            ReplyMessage replyMessage = replyMessageRepository.fingByAllKey(key);
            // 半匹配关键字回复
            if (replyMessage == null){
               replyMessage = replyMessageRepository.fingByHalfKey("%"+key+"%");
               // 正则匹配
               if (replyMessage == null){
                    replyMessage = replyMessageRepository.fingByRegexp(key);
                   // 自动回复
                   if (replyMessage == null){
                       replyMessage = replyMessageRepository.findOneKey();
                   }
                }
            }
            return  EventHandlerStrategyFactory.getXml(map,replyMessage);
        }
        // 自动回复
        ReplyMessage replyMessage = replyMessageRepository.findOneKey();
        if (replyMessage == null){
            return null;
        }
        return EventHandlerStrategyFactory.getXml(map,replyMessage);
    }
}
