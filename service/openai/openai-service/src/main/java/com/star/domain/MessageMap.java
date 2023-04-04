package com.star.domain;

import com.plexpt.chatgpt.entity.chat.Message;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Message消息Map
 * 每个Key 对应的List不能超过10 -> 每个人历史记录缓存不能超过10条
 */
public class MessageMap extends HashMap<Object, List<Message>> {
    private static final Integer MAX_SIZE = 10;
    public void putAssistantMessage(Object key, String value) {
        Message message = Message.of(value);
        message.setRole(Message.Role.ASSISTANT.getValue());
        List<Message> messages = get(key);
        messages.add(message);
        super.put(key, messages);
    }

    @Override
    public List<Message> put(Object key, List<Message> value) {
        value.get(value.size()-1).setRole(Message.Role.ASSISTANT.getValue());
        if (value.size()>MAX_SIZE){
            value.subList(value.size()-MAX_SIZE,value.size());
        }
        return super.put(key, value);
    }

    @Override
    public List<Message> get(Object key) {
        if (CollectionUtils.isEmpty(super.get(key))) {
            super.put(key, new ArrayList<>());
        }
        return super.get(key);
    }
}
