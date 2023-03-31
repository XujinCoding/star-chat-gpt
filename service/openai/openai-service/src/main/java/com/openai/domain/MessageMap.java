package com.openai.domain;

import com.plexpt.chatgpt.entity.chat.Message;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Message消息Map
 * 每个Key 对应的List不能超过10 -> 每个人历史记录缓存不能超过10条
 */
public class MessageMap extends HashMap<Object, List<Message>> {
    private static final Integer MAX_SIZE = 10;
    public List<Message> putMessage(Integer key, Message value) {
        value.setRole(Message.Role.SYSTEM.getValue());
        List<Message> messages = get(key);
        messages.add(value);
        return super.put(key, messages);
    }

    @Override
    public List<Message> put(Object key, List<Message> value) {
        List<Message> messages = value.stream().map(message -> {
            message.setRole(Message.Role.SYSTEM.getValue());
            return message;
        }).collect(Collectors.toList());
        if (value.size()>MAX_SIZE){
            value.subList(value.size()-MAX_SIZE,value.size());
        }
        return super.put(key, messages);
    }

    @Override
    public List<Message> get(Object key) {
        if (CollectionUtils.isEmpty(super.get(key))) {
            super.put(key, new ArrayList<>());
        }
        return super.get(key);
    }
}
