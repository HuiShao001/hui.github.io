package com.huishao.controller;

import com.huishao.service.WechatService;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.mp.api.WxMpMessageRouter;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.message.WxMpXmlMessage;
import me.chanjar.weixin.mp.bean.message.WxMpXmlOutMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("wechat")
@Slf4j
public class WechatController {

    @Autowired
    public WechatService wechatService;

    @Autowired
    public WxMpService wxMpService;

    /**
     * 微信早上测试发送
     * @return
     */
    @GetMapping("WechatMorning")
    public Map<String, String> WechatMorningTiming() {
        Map<String, String> Morning = new HashMap<>();
        try {
            wechatService.WechatMorningTiming();
            Morning.put("WechatMorning", "发送成功");
        } catch (Exception e) {
            Morning.put("WechatMorning", "发送失败：" + e.getMessage());
        }
        return Morning;
    }

    /**
     * 微信中午测试发送
     * @return
     */
    @GetMapping("WechatMidday")
    public Map<String, String> WechatMiddayTiming() {
        Map<String, String> Midday = new HashMap<>();
        try {
            wechatService.WechatMiddayTiming();
            Midday.put("WechatMidday", "发送成功");
        } catch (Exception e) {
            Midday.put("WechatMidday", "发送失败：" + e.getMessage());
        }
        return Midday;
    }

    /**
     * 微信晚上测试发送
     * @return
     */
    @GetMapping("WechatNight")
    public Map<String, String> WechatNightTiming() {
        Map<String, String> Night = new HashMap<>();
        try {
            wechatService.WechatNightTiming();
            Night.put("WechatNight", "发送成功");
        } catch (Exception e) {
            Night.put("WechatNight", "发送失败：" + e.getMessage());
        }
        return Night;
    }

}
