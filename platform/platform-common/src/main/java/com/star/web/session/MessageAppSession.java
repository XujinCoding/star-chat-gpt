package com.star.web.session;

import com.alibaba.fastjson.JSON;
import com.plexpt.chatgpt.entity.chat.Message;

import java.util.List;

/**
 * @author XuJ
 */
public class MessageAppSession extends AppSession {
    private static final String MESSAGE_HISTORY = "message_history";
    private static final String CURRENT_ANSWER = "current_answer";

    public static List<Message> getHistoryMessage() {
        return JSON.parseArray(getSessionAttribute().getStringValue(MESSAGE_HISTORY), Message.class);
    }

    public static void setHistoryMessage(List<Message> messages) {
        getSessionAttribute().addValue(MESSAGE_HISTORY, JSON.toJSONString(messages));
    }
    public static String getCurrentAnswer() {
        return getSessionAttribute().getStringValue(CURRENT_ANSWER);
    }

    public static void setCurrentAnswer(String answer) {
        getSessionAttribute().addValue(CURRENT_ANSWER, answer);
    }
}
