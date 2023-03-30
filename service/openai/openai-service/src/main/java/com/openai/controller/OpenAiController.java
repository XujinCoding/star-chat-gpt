package com.openai.controller;

import com.plexpt.chatgpt.ChatGPT;
import com.plexpt.chatgpt.util.Proxys;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.net.Proxy;

@RestController
@RequestMapping("/api/open-ai")
public class OpenAiController {
    @RequestMapping(value = "/message",method = RequestMethod.GET)
    private String getAnswerByMessage(){
        Proxy proxy = Proxys.http("127.0.0.1", 1080);

        ChatGPT chatGPT = ChatGPT.builder()
                .apiKey("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")
                .proxy(proxy)
                .apiHost("https://api.openai.com/")
                .build()
                .init();

        String res = chatGPT.chat("写一段七言绝句诗，题目是：火锅！");
        return res;
    }

}
