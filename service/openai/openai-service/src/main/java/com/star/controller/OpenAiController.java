package com.star.controller;

import com.star.domain.*;
import com.star.web.controller.BasicController;
import com.star.web.result.BasicResult;
import com.star.web.session.MessageAppSession;
import com.star.web.session.WebAppSession;
import com.star.util.JwtUtils;
import com.star.util.EncryptionUtil;
import com.plexpt.chatgpt.ChatGPT;
import com.plexpt.chatgpt.ChatGPTStream;
import com.plexpt.chatgpt.entity.chat.Message;
import com.plexpt.chatgpt.listener.SseStreamListener;
import com.plexpt.chatgpt.util.Proxys;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.net.Proxy;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/open-ai")
public class OpenAiController extends BasicController {
    private static final Proxy PROXY = Proxys.http("127.0.0.1", 13764);

    private static final MessageMap MESSAGE_MAP = new MessageMap();

    @RequestMapping(value = "/message/{message}", method = RequestMethod.GET)
    private BasicResult getAnswerByMessage(@PathVariable("message") String message) {
        System.out.println("访问成功");
        ChatGPT chatGPT = ChatGPT.builder().apiKey(WebAppSession.getApiKey()).proxy(PROXY).apiHost("https://api.openai.com/").build().init();
        return buildDefaultObject(chatGPT.chat(message));
    }


    @GetMapping("/chat/sse")
    @CrossOrigin
    public SseEmitter sseEmitter(String question) {
        if (StringUtils.isEmpty(question)){
            throw new RuntimeException("问题不能为空");
        }
        String apiKey = WebAppSession.getApiKey();
        log.info("访问成功");
        ChatGPTStream chatGPTStream = ChatGPTStream.builder()
                .timeout(600)
                .apiKey(apiKey)
                .proxy(PROXY)
                .apiHost("https://api.openai.com/")
                .build().init();
        SseEmitter sseEmitter = new SseEmitter(-1L);
        List<Message> messages = MESSAGE_MAP.get(apiKey);
        SseStreamListener listener = new SseStreamListener(sseEmitter);
        Message message = Message.of(question);
        messages.add(message);
        listener.setOnComplate(msg -> {
            //回答完成，可以做一些事情
            MESSAGE_MAP.put(apiKey, messages);
            MESSAGE_MAP.putAssistantMessage(apiKey, msg);
        });
        chatGPTStream.streamChatCompletion(messages, listener);
        return sseEmitter;
    }

    @GetMapping("/token")
    @CrossOrigin
    public BasicResult getToken(Jwt jwt) {
        log.info("-----------------正在获取Token");
        try {
            EncryptionUtil encryptionUtil = new EncryptionUtil();
            return buildDefaultObject(encryptionUtil.encrypt(JwtUtils.createJwtToken(jwt)));
        } catch (Exception ignored) {
            throw new RuntimeException("获取token失败");
        }
    }
}
