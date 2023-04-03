package com.openai.session;

import com.alibaba.fastjson.JSON;
import com.openai.domain.OpenAiSessionAttribute;
import com.plexpt.chatgpt.entity.chat.Message;

import java.util.List;

public class AppMessageSession {


    private static final ThreadLocal<OpenAiSessionAttribute> SESSION_ATTRIBUTE = new ThreadLocal<>();
    private static final String MESSAGE_HISTORY = "message_history";
    private static final String CURRENT_HISTORY = "current_history";

    public static List<Message> getHistoryMessage() {
        return JSON.parseArray(getSessionAttribute().getStringValue(MESSAGE_HISTORY), Message.class);
    }

    public static void setHistoryMessage(List<Message> messages) {
        getSessionAttribute().addValue(MESSAGE_HISTORY, JSON.toJSONString(messages));
    }
    public static String getCurrentMessage() {
        return getSessionAttribute().getStringValue(CURRENT_HISTORY);
    }

    public static void setCurrentMessage(String message) {
        getSessionAttribute().addValue(CURRENT_HISTORY, message);
    }
    public static OpenAiSessionAttribute getSessionAttribute() {
        if (SESSION_ATTRIBUTE.get() == null) {
            SESSION_ATTRIBUTE.set(new OpenAiSessionAttribute());
        }
        return SESSION_ATTRIBUTE.get();
    }
}
