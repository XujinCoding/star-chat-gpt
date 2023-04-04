package com.star.config;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

/**
 * @author XuJ
 */
@Configuration
@RefreshScope
@Validated
@Getter
@Setter
@ToString
@Slf4j
@ConfigurationProperties(prefix = OpenAiConfig.OPEN_AI_PREFIX, ignoreInvalidFields = false)
public class OpenAiConfig {
    public static final String OPEN_AI_PREFIX = "open.ai";
    private String proxyIp;
    private Integer proxyPort;
}
