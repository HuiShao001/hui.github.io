package com.huishao;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class WechatApplication {

    public static void main(String[] args) {
        SpringApplication.run(WechatApplication.class, args);
        System.out.println("访问连接，即可立即推送");
        System.out.println("早晨测试链接发送");
        System.out.println("http://localhost:9090/wechat001/hui/wechat/WechatMorning");
        System.out.println("中午测试链接发送");
        System.out.println("http://localhost:9090/wechat001/hui/wechat/WechatMidday");
        System.out.println("晚上测试链接发送");
        System.out.println("http://localhost:9090/wechat001/hui/wechat/WechatNight");
    }
}
