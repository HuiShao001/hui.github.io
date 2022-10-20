## 微信测试号推送项目

一个普通的测试号推送项目，目前具备早晨、中午、晚上三个时间段发送，自定义功能较多，很强的随机性

代码内部已经提供了各种API请求，生日倒计时、相遇倒计、各种随机情话、天气预报、星座匹配、颜文字等等



部署环境👇

1.需要具备：Java、Maven

2.安装IDEA软件并将WeChat-Push-Exclusive项目导入

3.启动WechatApplication.java类即可

注：可访问链接立即推送，也可定时推送



定时推送👇

注：可在WechatService.java类中设置早中晚发送时间，切记具备网络的环境下和电脑持续待机的情况下才可以

```Java
    /**
     * 早上发送时间
     */
    @Scheduled(cron = "0 30 7 * * ?")
    public void WechatMorningTiming() {
        try {
            WxMpUserList wxMpUserList = wxMpService.getUserService().userList(null);
            for (String openId : wxMpUserList.getOpenids()) {
                //早上问好
                sendWechatMessage(openId, "morningTemplate");
                // 发送文字模板
                sendWechatMessage(openId, "copywritingTemplate");
                //颜文字
                sendWechatMessage(openId, "emoticonsTemplate");
            }
        } catch (WxErrorException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 中午发送时间
     */
    @Scheduled(cron = "0 0 12 * * ?")
    public void WechatMiddayTiming() {
        try {
            WxMpUserList wxMpUserList = wxMpService.getUserService().userList(null);
            for (String openId : wxMpUserList.getOpenids()) {
                //中午问好
                sendWechatMessage(openId, "MiddayTemplate");
            }
        } catch (WxErrorException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 晚上发送时间
     */
    @Scheduled(cron = "0 0 21 * * ?")
    public void WechatNightTiming() {
        try {
            WxMpUserList wxMpUserList = wxMpService.getUserService().userList(null);
            for (String openId : wxMpUserList.getOpenids()) {
                //晚上问好
                sendWechatMessage(openId, "nightTemplate");
            }
        } catch (WxErrorException e) {
            throw new RuntimeException(e);
        }
    }
```



使用方法👇

1.在application.yaml中编辑好自己的内容

```java
server:
  servlet:
    context-path: /wechat001/hui
  port: 9090
wechat:
  config:
    appId:#微信appId
    appSecret:#微信appSecret
    token:#token可不填
    singleTemplateId:
    template:
      #早安模板
      morningTemplate:
        templateId:#早安模板ID
        allSend: true
        filterOpenIds:
        parameter:
          default:
            title: 早上好呀
            meetDate:#相遇时间
            city:#地区
            cityCode:#地区的cityCode代码

      #文字模板
      copywritingTemplate:
        templateId:#文字模板ID
        allSend: true
        filterOpenIds:
        parameter:
          default:
            city:#地区
            cityCode:#地区的cityCode代码

      #颜文字模板
      emoticonsTemplate:
        templateId:#颜文字模板ID
        allSend: true
        filterOpenIds:
        parameter:
          default:

      #中午模板
      MiddayTemplate:
        templateId:#中午模板ID
        allSend: true
        filterOpenIds:
        parameter:
          default:

      #晚安模板
      nightTemplate:
        templateId:#晚安模板
        allSend: true
        filterOpenIds:
        parameter:
          default:
            nightcity:#地区


#第一次见面时间
First:
  Firstmeeting: 'xxxx-xx-xx'

# xuan
birthdayConf:
  birthdayDate: 'xxxx-xx-xx'

# hui
birthdayhui:
  birthdayhuiData: 'xx-xx'



```



提示👇

注：登录自己的微信测试号网站

https://mp.weixin.qq.com/debug/cgi-bin/sandbox?t=sandbox/login

第一步：分别创建5个模板

```
# 早安模板
{{time.DATA}}
{{title.DATA}}
{{titleone.DATA}}
{{days.DATA}}{{day.DATA}}
{{Datename.DATA}}{{xuan.DATA}}{{hui.DATA}}
{{fss.DATA}}{{fs.DATA}}
{{states.DATA}}{{state.DATA}}
{{citys.DATA}}{{city.DATA}}
{{weathers.DATA}}{{weather.DATA}}
{{sunrises.DATA}}{{sunrise.DATA}}
{{sunsets.DATA}}{{sunset.DATA}}
{{reals.DATA}}{{real.DATA}}
{{highests.DATA}}{{highest.DATA}}
{{lowests.DATA}}{{lowest.DATA}}
{{humiditys.DATA}}{{humidity.DATA}}
{{colors.DATA}}{{color.DATA}}
```

```
# 文字模板
{{tipshui.DATA}}{{tips.DATA}}
{{chps.DATA}}{{chp.DATA}}
{{loveyous.DATA}}{{loveyou.DATA}}
{{dus.DATA}}{{du.DATA}}
{{jokess.DATA}}{{jokes.DATA}}
{{constellationName.DATA}}{{love.DATA}}
```

```
# 颜文字模板
{{emojiwen.DATA}}
{{emoji.DATA}}
```

```
# 晚安模板
{{title.DATA}}
{{timewen.DATA}}{{time.DATA}}
{{rainbow.DATA}}
{{night.DATA}}
{{nightlove.DATA}}
{{nighttitle.DATA}}
{{weathers.DATA}}{{weather.DATA}}
{{reals.DATA}}{{real.DATA}}
{{lowests.DATA}}{{lowest.DATA}}
{{highests.DATA}}{{highest.DATA}}
```

```
# 中午模板
{{title.DATA}}
```

第二步：将模板ID对应application.yaml文件填写完毕即可
