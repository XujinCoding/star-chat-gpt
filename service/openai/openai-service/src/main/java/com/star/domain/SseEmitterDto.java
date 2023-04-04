package com.star.domain;

import com.star.session.MessageAppSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
/**
 * @author XuJ
 */
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
            MessageAppSession.setCurrentAnswer(MessageAppSession.getCurrentAnswer()+message);
        }
    }
}
