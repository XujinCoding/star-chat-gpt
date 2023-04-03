package com.openai.domain;

import com.openai.session.AppMessageSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
@Slf4j
public class SseEmitterDto extends SseEmitter {
    public SseEmitterDto(long timeOut) {
        super(timeOut);
    }

    @Override
    public void send(Object object) throws IOException {
        super.send(object);
        saveMessage(object);
    }
    private void saveMessage(Object message){
        if (message!=null){
            AppMessageSession.setCurrentMessage(AppMessageSession.getCurrentMessage()+message);
        }
    }
}
