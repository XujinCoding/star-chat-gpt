package com.star.config;

import com.star.interceptor.TokenInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author XuJ
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {
    private static final String GET_TOKEN_ADDRESS = "/api/open-ai/token";
    private static final String INTERCEPT_ADDRESS = "/api/open-ai/chat/sse";


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new TokenInterceptor());
    }
}