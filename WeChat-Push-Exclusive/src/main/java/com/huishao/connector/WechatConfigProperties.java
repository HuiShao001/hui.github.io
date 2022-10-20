package com.huishao.connector;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

@ConfigurationProperties(prefix = "wechat.config")
@Component
@Data
public class WechatConfigProperties {
    private String appId;

    private String appSecret;

    private String token;

    private String singleTemplateId;

    private Map<String, WechatTemplate> template;
}
