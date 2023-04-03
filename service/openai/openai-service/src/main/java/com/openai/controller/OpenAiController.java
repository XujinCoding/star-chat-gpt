package com.openai.controller;

import com.openai.domain.MessageMap;
import com.openai.domain.RequestReceiver;
import com.openai.domain.SseEmitterDto;
import com.openai.session.AppMessageSession;
import com.plexpt.chatgpt.ChatGPT;
import com.plexpt.chatgpt.ChatGPTStream;
import com.plexpt.chatgpt.entity.chat.Message;
import com.plexpt.chatgpt.listener.SseStreamListener;
import com.plexpt.chatgpt.util.Proxys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.net.Proxy;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/open-ai")
public class OpenAiController {
    private static final Proxy PROXY = Proxys.http("127.0.0.1", 4196);
    private static final String KEY = "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";


    private static final MessageMap MESSAGE_MAP = new MessageMap();

    @RequestMapping(value = "/message/{message}", method = RequestMethod.GET)
    private String getAnswerByMessage(@PathVariable("message") String message) {
        System.out.println("访问成功");
        ChatGPT chatGPT = ChatGPT.builder().apiKey(KEY).proxy(PROXY).apiHost("https://api.openai.com/").build().init();

        return chatGPT.chat(message);
    }


    @GetMapping("/chat/sse")
    @CrossOrigin
    public SseEmitter sseEmitter(RequestReceiver receiver) {
        log.info("");
        ChatGPTStream chatGPTStream = ChatGPTStream.builder()
                .timeout(600)
                .apiKey(receiver.getKey())
                .proxy(PROXY)
                .apiHost("https://api.openai.com/")
                .build().init();
        SseEmitterDto sseEmitterDto = new SseEmitterDto(-1L);
        List<Message> messages = MESSAGE_MAP.get(1);
        SseStreamListener listener = new SseStreamListener(sseEmitterDto);
        Message message = Message.of(receiver.getMessage());
        messages.add(message);
        listener.setOnComplate(msg -> {
            //回答完成，可以做一些事情
            MESSAGE_MAP.put(1, messages);
            MESSAGE_MAP.putAssistantMessage(1,AppMessageSession.getCurrentMessage());
        });
        chatGPTStream.streamChatCompletion(messages, listener);
        return sseEmitterDto;
    }

    @GetMapping("/token")
    @CrossOrigin
    public String getToken(String encrypted) {
        log.info("获取信息");
        return "";
    }


}
