package com.star.interceptor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.star.web.result.BasicResult;
import com.star.web.session.WebAppSession;
import com.star.util.JwtUtils;
import com.star.util.EncryptionUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;

@Slf4j
@Component
public class TokenInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        log.info("-----------正在进行Token解密");
        String token = getString(request.getParameter("token"));
        if (JwtUtils.validateToken(token)) {
            WebAppSession.setUserName(JwtUtils.getUserNameFromJwtClaims(token));
            WebAppSession.setApiKey(JwtUtils.getApiKeyFromJwtClaims(token));
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

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        WebAppSession.destroy();
    }

    private static String getString(String ciphertext) {
        EncryptionUtil encryptionUtil;
        try {
            encryptionUtil = new EncryptionUtil();
            return encryptionUtil.decrypt(ciphertext);
        } catch (Exception e) {
            log.info("-----------token解密异常");
            throw new RuntimeException(e);
        }
    }
}
