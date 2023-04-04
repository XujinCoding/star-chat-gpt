package com.star.controller;

import com.plexpt.chatgpt.ChatGPTStream;
import com.plexpt.chatgpt.util.Proxys;
import com.star.config.OpenAiConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.Proxy;

@Component
public class ChatGptInstance {

    @Autowired
    private OpenAiConfig openAiConfig;
    public ChatGPTStream chatGPTStream;

    public ChatGPTStream getChatGptSteam(String apiKey) {
        if (chatGPTStream == null) {
            chatGPTStream = ChatGPTStream.builder()
                    .timeout(600)
                    .apiKey(apiKey)
                    .proxy(getProxy())
                    .apiHost("https://api.openai.com/")
                    .build().init();
        }
        return chatGPTStream;
    }
    public Proxy getProxy() {
        return Proxys.http(openAiConfig.getProxyIp(), openAiConfig.getProxyPort());
    }
}
