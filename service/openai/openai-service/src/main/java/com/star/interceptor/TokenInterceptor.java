package com.star.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.star.domain.BasicResult;
import com.star.session.WebAppSession;
import com.star.util.OpenAiJwtUtils;
import com.star.util.SecurityUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

@Slf4j
@Component
public class TokenInterceptor extends HandlerInterceptorAdapter {

    private static final String INTERCEPT_ADDRESS = "/api/open-ai/chat/sse";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (!Objects.equals(request.getServletPath(), INTERCEPT_ADDRESS)) {
            return true;
        }
        log.info("-----------正在进行Token解密");
        String token = getString(request.getParameter("token"));
        if (OpenAiJwtUtils.validateToken(token)) {
            WebAppSession.setUserName(OpenAiJwtUtils.getUserNameFromJwtClaims(token));
            WebAppSession.setApiKey(OpenAiJwtUtils.getApiKeyFromJwtClaims(token));
            return true;
        } else {
            response.setCharacterEncoding(StandardCharsets.UTF_8.name());
            try (PrintWriter writer = response.getWriter()) {
                var message = "system.error";
                BasicResult rs = BasicResult.builder()
                        .status(false)
                        .code(message)
                        .message("请重新获取token")
                        .build();
                ObjectMapper objectMapper = new ObjectMapper();
                writer.print(objectMapper.writeValueAsString(rs));
                writer.flush();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }

    }

    private static String getString(String ciphertext) {
        SecurityUtil securityUtil;
        try {
            securityUtil = new SecurityUtil();
            return securityUtil.decrypt(ciphertext);
        } catch (Exception e) {
            log.info("-----------token解密异常");
            throw new RuntimeException(e);
        }
    }
}
