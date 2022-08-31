package com.huishao.connector;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class WechatTemplate {

    private String templateId;

    private Boolean allSend;

    private List<String> filterOpenIds;

    private Map<String, Object> parameter;

}
