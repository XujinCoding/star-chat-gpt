package com.openai.controller;

import com.plexpt.chatgpt.ChatGPT;
import com.plexpt.chatgpt.ChatGPTStream;
import com.plexpt.chatgpt.entity.chat.Message;
import com.plexpt.chatgpt.listener.SseStreamListener;
import com.plexpt.chatgpt.util.Proxys;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.net.Proxy;
import java.util.Arrays;

@RestController
@RequestMapping("/api/open-ai")
public class OpenAiController {
    private static final Proxy proxy = Proxys.http("127.0.0.1", 53859);
    private static final String key = "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";

    @RequestMapping(value = "/message/{message}",method = RequestMethod.GET)
    private String getAnswerByMessage(@PathVariable("message") String message){
        ChatGPT chatGPT = ChatGPT.builder()
                .apiKey(key)
                .proxy(proxy)
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
                .apiKey(key)
                .proxy(proxy)
                .apiHost("https://api.openai.com/")
                .build()
                .init();

        SseEmitter sseEmitter = new SseEmitter(-1L);

        SseStreamListener listener = new SseStreamListener(sseEmitter);
        Message message = Message.of(prompt);

        listener.setOnComplate(msg -> {
            //回答完成，可以做一些事情
            System.out.println("回答完成");
        });
        chatGPTStream.streamChatCompletion(Arrays.asList(message), listener);


        return sseEmitter;
    }


}
