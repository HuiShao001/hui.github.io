package com.huishao.config;

import com.huishao.handler.MsgHandler;
import com.huishao.connector.WechatConfigProperties;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.mp.api.WxMpMessageRouter;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;
import me.chanjar.weixin.mp.config.impl.WxMpDefaultConfigImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@AllArgsConstructor
@Configuration
@Slf4j
public class WechatAppConfig {

    @Autowired
    private WechatConfigProperties wechatConfigProperties;

    @Autowired
    private MsgHandler msgHandler;


    @Bean
    public WxMpService wxMpService() {
        WxMpDefaultConfigImpl config = new WxMpDefaultConfigImpl();
        // 设置微信测试号的appid
        config.setAppId(wechatConfigProperties.getAppId());
        // 设置微信测试号的app corpSecret
        config.setSecret(wechatConfigProperties.getAppSecret());
        config.setToken(wechatConfigProperties.getToken());
        WxMpService wxService = new WxMpServiceImpl();
        wxService.setWxMpConfigStorage(config);
        return wxService;
    }

    @Bean
    public WxMpMessageRouter messageRouter(WxMpService wxMpService) {
        final WxMpMessageRouter newRouter = new WxMpMessageRouter(wxMpService);
        newRouter.rule().async(false).handler(this.msgHandler).end();
        return newRouter;
    }
}
