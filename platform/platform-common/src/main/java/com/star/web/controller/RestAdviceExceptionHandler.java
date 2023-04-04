package com.star.web.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.star.web.result.BasicResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.annotation.Order;
import org.springframework.util.function.SingletonSupplier;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletResponse;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Locale;


@Slf4j
@Order(0)
@ControllerAdvice(assignableTypes = BasicController.class)
public class RestAdviceExceptionHandler {

    private final SingletonSupplier<MessageSource> messageSourceSupplier = SingletonSupplier.of(() -> {
        var messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:messages/messageToErrCode");
        return messageSource;
    });


    @ExceptionHandler(value = {Exception.class})
    public final ModelAndView handleException(Exception ex, ServletResponse response) {
        try {
            log.error(ex.getMessage(), ex);
            if (!response.isCommitted()) {
                response.setCharacterEncoding(StandardCharsets.UTF_8.name());
                try (PrintWriter writer = response.getWriter()) {
                    var message =  "system.error";
                    BasicResult rs = BasicResult.builder()
                            // .status(false).code(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()))
                            .status(false)
                            .code(messageSourceSupplier.obtain().getMessage(message, null, message, Locale.getDefault()))
                            .message(ex.getMessage())
                            .build();

                    ObjectMapper objectMapper = new ObjectMapper();
                    writer.print(objectMapper.writeValueAsString(rs));
                    writer.flush();
                }
            }
        } catch (Exception e) {
            log.error("同一异常处理错误");
            log.error(e.getMessage(), e);
        }
        return new ModelAndView();
    }

}
