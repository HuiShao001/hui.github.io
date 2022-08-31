## 这是一个spring boot微信测试号推送项目

说明：目前总结了四个版本：初代、进阶、高、终，4个版本，这个wechathgih为高版本

使用方法：

1.在application.yaml中编辑好自己的内容，根据提示填写

```java
# 微信appId（必写）
appId: 
# 微信appSecret（必写）
appSecret: 

# 配置一个文字的模板ID
singleTemplateId:
template:
# 早安模板
morningTemplate:
# 模板ID（必写）
templateId: 
# 是否关注的用户全发送，ture为是，当值为ture时，sendOpenId 为排除发送对象的微信用户，当值为false为否，sendOpenId为发送对象的微信用户
allSend: true
filterOpenIds:

parameter:
# 这是默认的设置，如果有用户没设置参数，就按此参数
default:
# 早安话语
title: 早上好呀
# 你们的相遇时间（必写）
meetDate: ''
# 居住地点，高德地图api下载查询（必写）
city: 城市名称
# 城市名称的cityCode代码（必写）
cityCode: 
constellation: 星座（必写）

# 颜文字模板
emoticonsTemplate:
templateId: （必写）

# 获取天气的key
lbs:
  key: （必写）

#第一次见面时间（必写）
First:
  Firstmeeting: ''

#生日倒计时（必写）
birthdayConf:
  birthdayDate: ''

```

2.我已经准备好模板了，访问微信测试号网站，登录自己的微信即可

https://mp.weixin.qq.com/debug/cgi-bin/sandbox?t=sandbox/login

住：参考一下图片

https://github.com/HuiShao001/hui.github.io/blob/wechathigh/huishao001%20(2).png
https://github.com/HuiShao001/hui.github.io/blob/wechathigh/huishao001%20(3).png
https://github.com/HuiShao001/hui.github.io/blob/wechathigh/huishao001%20(4).png
https://github.com/HuiShao001/hui.github.io/blob/wechathigh/huishao001%20(1).png

注：这里需要新建两个测试模板，第一个是推送内容模板，第二个是颜文字模板，然后将新建的模板ID写入配置文件中（配置文件请参考项目中src/main/resource/application.yaml）

```
# 内容模板
{{time.DATA}}
{{title.DATA}}
今天是我们认识的第{{day.DATA}}天 
距离XX的生日还有{{birthdayDate.DATA}}天
第一次见面已经过了{{Firstmeeting.DATA}}天
我希望今天的你心情是 {{state.DATA}} 
你的城市：{{city.DATA}} 
今天天气：{{dayWeather.DATA}} 
今天温度：{{daytemp.DATA}} 
你的幸运颜色：{{color.DATA}} 
{{constellationName.DATA}}的你今天的爱情指数是 {{love.DATA}} 
今天的你:{{summary.DATA}} 
{{chp.DATA}}
{{du.DATA}}

# 颜文字模板
{{data1.DATA}}
{{data2.DATA}}
{{data3.DATA}}
{{data4.DATA}}
{{data5.DATA}}
```

3.配置环境

需要一个具备Java环境的电脑，安装IDEA软件，将项目导入IDEA，运行即可

注：项目默认早上7点半准时推送，访问以下网址即可立即推送

http://localhost:9090/wechat001/hui/wechat/sendMessage
