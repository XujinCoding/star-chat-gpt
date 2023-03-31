package com.openai.controller;

import com.openai.domain.MessageMap;
import com.plexpt.chatgpt.ChatGPT;
import com.plexpt.chatgpt.ChatGPTStream;
import com.plexpt.chatgpt.entity.chat.ChatCompletion;
import com.plexpt.chatgpt.entity.chat.ChatCompletionResponse;
import com.plexpt.chatgpt.entity.chat.Message;
import com.plexpt.chatgpt.listener.SseStreamListener;
import com.plexpt.chatgpt.util.Proxys;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.net.Proxy;
import java.util.*;

@RestController
@RequestMapping("/api/open-ai")
public class OpenAiController {
    private static final Proxy PROXY = Proxys.http("167.179.75.185", 9902);
    private static final String KEY = "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";

    private static final MessageMap MESSAGE_MAP = new MessageMap();

    @RequestMapping(value = "/message/{message}", method = RequestMethod.GET)
    private String getAnswerByMessage(@PathVariable("message") String message) {
        ChatGPT chatGPT = ChatGPT.builder()
                .apiKey(KEY)
                .proxy(PROXY)
                .apiHost("https://api.openai.com/")
                .build()
                .init();

        return chatGPT.chat(message);
    }


    @GetMapping("/chat/sse")
    @CrossOrigin
    public SseEmitter sseEmitter(String prompt) {
        ChatGPTStream chatGPTStream = ChatGPTStream.builder()
                .timeout(600)
                .apiKey(KEY)
                .proxy(PROXY)
                .apiHost("https://api.openai.com/")
                .build()
                .init();

        SseEmitter sseEmitter = new SseEmitter(-1L);
        List<Message> messages = MESSAGE_MAP.get(1);
        SseStreamListener listener = new SseStreamListener(sseEmitter);
        Message message = Message.of(prompt);
        messages.add(message);
        listener.setOnComplate(msg -> {
            //回答完成，可以做一些事情
            MESSAGE_MAP.put(1, messages);
            System.out.println("回答完成");
        });
        chatGPTStream.streamChatCompletion(messages, listener);
        return sseEmitter;
    }


}
