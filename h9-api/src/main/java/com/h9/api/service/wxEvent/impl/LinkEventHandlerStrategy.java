package com.h9.api.service.wxEvent.impl;

import com.h9.api.service.wxEvent.EventHandlerStrategy;
import com.h9.api.service.wxEvent.model.Message4wxText;

import java.util.Map;

/**
 * 链接消息处理
 */
public class LinkEventHandlerStrategy implements EventHandlerStrategy<Message4wxText> {
    @Override
    public Message4wxText handler(Map map) {
        return null;
    }
}
