package com.star.controller;

import com.plexpt.chatgpt.ChatGPT;
import com.plexpt.chatgpt.ChatGPTStream;
import com.plexpt.chatgpt.entity.chat.Message;
import com.plexpt.chatgpt.listener.SseStreamListener;
import com.plexpt.chatgpt.util.Proxys;
import com.star.domain.Jwt;
import com.star.domain.MessageMap;
import com.star.util.EncryptionUtil;
import com.star.util.JwtUtils;
import com.star.web.controller.BasicController;
import com.star.web.result.BasicResult;
import com.star.web.session.WebAppSession;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
    ChatGptInstance chatGptInstance;

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
        ChatGPTStream chatGPTStream = chatGptInstance.getChatGptSteam(apiKey);
        SseEmitter sseEmitter = new SseEmitter(-1L);
        List<Message> messages = getMessages(question, apiKey);
        SseStreamListener listener = new SseStreamListener(sseEmitter);
        listener.setOnComplate(msg -> extracted(apiKey, messages, msg));
        chatGPTStream.streamChatCompletion(messages, listener);
        return sseEmitter;
    }

    private static void extracted(String apiKey, List<Message> messages, String msg) {
        MESSAGE_MAP.put(apiKey, messages);
        MESSAGE_MAP.putAssistantMessage(apiKey, msg);
    }

    private static List<Message> getMessages(String question, String apiKey) {
        List<Message> messages = MESSAGE_MAP.get(apiKey);
        Message message = Message.of(question);
        messages.add(message);
        return messages;
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
