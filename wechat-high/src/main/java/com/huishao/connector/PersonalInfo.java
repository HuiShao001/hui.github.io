package com.huishao.connector;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "personal.info")
@Component
@Data
public class PersonalInfo {
    /**
     * 星座
     */
    private String constellation;

}
