package com.huishao.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.huishao.api.EarthyloveApi;
import com.huishao.connector.WechatInterface;
import com.huishao.connector.WechatConfigProperties;
import com.huishao.config.EmojiConfig;
import com.huishao.handler.Colorful;
import com.huishao.handler.RainbowHandler;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.bean.result.WxMpUserList;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateData;
import me.chanjar.weixin.mp.bean.template.WxMpTemplateMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;

@Service
//@Slf4j
public class WechatService {
    private final static String DEFAULT_KEY = "default";
    public static final RestTemplate REST_TEMPLATE = new RestTemplate();
    @Autowired
    private WechatConfigProperties wechatConfigProperties;
    @Autowired
    private WxMpService wxMpService;

    // 旋宝生日配置文件
    @Value("${birthdayConf.birthdayDate}")
    private String XuanDate;

    // 辉少生日配置文件
    @Value("${birthdayhui.birthdayhuiData}")
    private String HuiDate;

    // 第一次见面配置文件
    @Value("${First.Firstmeeting}")
    private String firstmeeting;

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

    public void sendWechatMessage(String openId, String template) {
        Optional.ofNullable(wechatConfigProperties.getTemplate().get(template)).ifPresent(wechatTemplate -> {
            List<String> filterOpenIds = wechatTemplate.getFilterOpenIds();
            if (filterOpenIds == null) {
                filterOpenIds = new ArrayList<>();
            }
            if (wechatTemplate.getAllSend() && !wechatTemplate.getFilterOpenIds().contains(openId)) {
                //全部发送，排除过滤的
            } else if (!wechatTemplate.getAllSend() && wechatTemplate.getFilterOpenIds().contains(openId)) {
                //只发送过滤的
            } else {
                return;
            }
            Optional.ofNullable(wechatTemplate.getParameter()).map(stringBaseTemplateParameterMap -> {
                Object baseTemplateParameter = stringBaseTemplateParameterMap.get(openId);
                if (baseTemplateParameter != null) {
                    return baseTemplateParameter;
                } else {
                    return stringBaseTemplateParameterMap.get(DEFAULT_KEY);
                }
            }).ifPresent(baseTemplateParameter -> {
                if ("填写模板ID".equals(wechatTemplate.getTemplateId())) {
                    log.error("{}模板ID还没有进行配置，跳过发送", template);
                }

                Random random = new Random();

                WxMpTemplateMessage templateMessage = WxMpTemplateMessage.builder()
                        // 要推送的用户openid
                        .toUser(openId)
                        // 模版id
                        .templateId(wechatTemplate.getTemplateId())
                        // 点击模版消息要访问的网址
//                                .url("www.baidu.com")
                        .build();

                switch (template) {
                    /**
                     * 设置早上好模板
                     */
                    case "morningTemplate":
                        boolean a = false;
                        a = morningTemplate(baseTemplateParameter, random, templateMessage);
                        if (!a) {
                            return;
                        }
                        break;

                    /**
                     * 设置文案模板
                     */
                    case "copywritingTemplate":
                        boolean b = false;
                        b = copywritingTemplate(baseTemplateParameter, random, templateMessage);
                        if (!b) {
                            return;
                        }
                        break;

                    /**
                     * 设置中午模板
                     */
                    case "MiddayTemplate":
                        boolean c = false;
                        c = MiddayTemplate(random, templateMessage);
                        if (!c) {
                            return;
                        }
                        break;

                    /**
                     * 设置晚安模板
                     */
                    case "nightTemplate":
                        boolean d = false;
                        d = nightTemplate(baseTemplateParameter, random, templateMessage);
                        if (!d) {
                            return;
                        }
                        break;

                    /**
                     * 设置颜文字模板
                     */
                    case "emoticonsTemplate":
                        boolean e = false;
                        e = emoticonsTemplate(random, templateMessage);
                        if (!e) {
                            return;
                        }
                        break;

                }
                sendMessage(templateMessage);
            });
        });
    }

    /**
     * 颜文字
     *
     * @param random
     * @param templateMessage
     * @return
     */
    private boolean emoticonsTemplate(Random random, WxMpTemplateMessage templateMessage) {
        String s = EmojiConfig.Emoji_LIST.get(random.nextInt(EmojiConfig.Emoji_LIST.size()));
        List<String> splitExpression = EmojiConfig.splitExpression(s);
        for (String sp : splitExpression) {
            // 颜文字加入颜色
            templateMessage.addData(new WxMpTemplateData("emojiwen", "今日颜文字：", Colorful.Next.get(random.nextInt(Colorful.Next.size()))));
            templateMessage.addData(new WxMpTemplateData("emoji", sp, Colorful.Next.get(random.nextInt(Colorful.Next.size()))));
        }
        return true;
    }

    /**
     * 早安模板推送
     *
     * @param baseTemplateParameter
     * @param random
     * @param templateMessage
     * @return
     * @throws ParseException
     */
    private boolean morningTemplate(Object baseTemplateParameter, Random random, WxMpTemplateMessage templateMessage) {
        WechatInterface wechatInterface = JSON.parseObject(JSON.toJSONString(baseTemplateParameter), WechatInterface.class);
        // 天行数据api
        JSONObject WeatherAPI = getTodayWeather(wechatInterface.getCityCode());
        // 认识时间
        LocalDate MeetDate = LocalDate.parse(wechatInterface.getMeetDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        // 自定义颜色
        String MeetColor = Colorful.Next.get(random.nextInt(Colorful.Next.size()));
        // 颜色
        String color = EarthyloveApi.getHuise();
        // 心情
        String state = EarthyloveApi.getmood(random);
        // 今日的你
        String summary = EarthyloveApi.getsummary(random);
        // 清早第一句话
        String early = EarthyloveApi.getearly();

        // 更新日期
        templateMessage.addData(new WxMpTemplateData("time", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy年MM月dd日 HH:mm EEEE")) + "\uD83E\uDD73", Colorful.Next.get(random.nextInt(Colorful.Next.size()))));
        // 早上好title
        templateMessage.addData(new WxMpTemplateData("title", wechatInterface.getTitle() + "☆(≧∀≦*)ﾉ", Colorful.Next.get(random.nextInt(Colorful.Next.size()))));
        // 清早的第一句话
        templateMessage.addData(new WxMpTemplateData("titleone", early, Colorful.Next.get(random.nextInt(Colorful.Next.size()))));
        // 心情
        templateMessage.addData(new WxMpTemplateData("states", "XX今日的心情是：", Colorful.Next.get(random.nextInt(Colorful.Next.size()))));
        templateMessage.addData(new WxMpTemplateData("state", state, Colorful.Next.get(random.nextInt(Colorful.Next.size()))));
        // 相遇
        templateMessage.addData(new WxMpTemplateData("days", "\uD83D\uDC8C" + "XX与XX的故事：", MeetColor));
        templateMessage.addData(new WxMpTemplateData("day", String.valueOf(LocalDate.now().toEpochDay() - MeetDate.toEpochDay() + 1) + "天", Colorful.Next.get(random.nextInt(Colorful.Next.size()))));
        // 第一次见面时间
        templateMessage.addData(new WxMpTemplateData("fss", "第一次见面已经过了 ", Colorful.Next.get(random.nextInt(Colorful.Next.size()))));
        templateMessage.addData(new WxMpTemplateData("fs", getfirstmeeting(firstmeeting) + "天", Colorful.Next.get(random.nextInt(Colorful.Next.size()))));
        // 生日列表
        templateMessage.addData(new WxMpTemplateData("Datename", "距离生日 ", Colorful.Next.get(random.nextInt(Colorful.Next.size()))));
        // XX生日倒计时
        templateMessage.addData(new WxMpTemplateData("xuan", getXuan(XuanDate) + "天", Colorful.Next.get(random.nextInt(Colorful.Next.size()))));
        // XX生日倒计时
        templateMessage.addData(new WxMpTemplateData("hui", getHui(HuiDate) + "天", Colorful.Next.get(random.nextInt(Colorful.Next.size()))));
        // 城市
        templateMessage.addData(new WxMpTemplateData("citys", "\uD83C\uDF0F" + "城市：", Colorful.Next.get(random.nextInt(Colorful.Next.size()))));
        templateMessage.addData(new WxMpTemplateData("city", wechatInterface.getCity(), Colorful.Next.get(random.nextInt(Colorful.Next.size()))));
        // 接收天气
        templateMessage.addData(new WxMpTemplateData("weathers", "☁" + "天气：", Colorful.Next.get(random.nextInt(Colorful.Next.size()))));
        templateMessage.addData(new WxMpTemplateData("weather", WeatherAPI.getOrDefault("weather", "").toString(), Colorful.Next.get(random.nextInt(Colorful.Next.size()))));
        // 接受日出
        templateMessage.addData(new WxMpTemplateData("sunrises", "\uD83C\uDF04" + "日出时间：", Colorful.Next.get(random.nextInt(Colorful.Next.size()))));
        templateMessage.addData(new WxMpTemplateData("sunrise", WeatherAPI.getOrDefault("sunrise", "") + "分", Colorful.Next.get(random.nextInt(Colorful.Next.size()))));
        // 接受日落
        templateMessage.addData(new WxMpTemplateData("sunsets", "\uD83C\uDF07" + "日落时间：", Colorful.Next.get(random.nextInt(Colorful.Next.size()))));
        templateMessage.addData(new WxMpTemplateData("sunset", WeatherAPI.getOrDefault("sunset", "") + "分", Colorful.Next.get(random.nextInt(Colorful.Next.size()))));
        // 接收温度
        templateMessage.addData(new WxMpTemplateData("reals", "\uD83C\uDF21" + "平均气温：", Colorful.Next.get(random.nextInt(Colorful.Next.size()))));
        templateMessage.addData(new WxMpTemplateData("real", WeatherAPI.getOrDefault("real", "").toString(), Colorful.Next.get(random.nextInt(Colorful.Next.size()))));
        // 最高温度
        templateMessage.addData(new WxMpTemplateData("highests", "☀" + "最高气温：", Colorful.Next.get(random.nextInt(Colorful.Next.size()))));
        templateMessage.addData(new WxMpTemplateData("highest", WeatherAPI.getOrDefault("highest", "").toString(), Colorful.Next.get(random.nextInt(Colorful.Next.size()))));
        // 最低温度
        templateMessage.addData(new WxMpTemplateData("lowests", "❄" + "最低气温：", Colorful.Next.get(random.nextInt(Colorful.Next.size()))));
        templateMessage.addData(new WxMpTemplateData("lowest", WeatherAPI.getOrDefault("lowest", "").toString(), Colorful.Next.get(random.nextInt(Colorful.Next.size()))));
        // 湿度
        templateMessage.addData(new WxMpTemplateData("humiditys", "\uD83D\uDCA7" + "空气湿度：", Colorful.Next.get(random.nextInt(Colorful.Next.size()))));
        templateMessage.addData(new WxMpTemplateData("humidity", WeatherAPI.getOrDefault("humidity", "").toString(), Colorful.Next.get(random.nextInt(Colorful.Next.size()))));
        // 幸运颜色
        templateMessage.addData(new WxMpTemplateData("colors", "\uD83C\uDFA8" + "幸运颜色：", Colorful.Next.get(random.nextInt(Colorful.Next.size()))));
        templateMessage.addData(new WxMpTemplateData("color", color, Colorful.Next.get(random.nextInt(Colorful.Next.size()))));

        return true;
    }

    /**
     * 文字模板推送
     *
     * @param random
     * @param templateMessage
     * @return
     */
    private boolean copywritingTemplate(Object baseTemplateParameter, Random random, WxMpTemplateMessage templateMessage) {
        WechatInterface wechatInterface = JSON.parseObject(JSON.toJSONString(baseTemplateParameter), WechatInterface.class);
        // 天行数据api
        JSONObject WeatherAPI = getTodayWeather(wechatInterface.getCityCode());
        // 彩虹屁
        String chp = EarthyloveApi.getChp();
        // 土味情话
        String loveyou = EarthyloveApi.getLoveyou();
        // 毒鸡汤
        String du = EarthyloveApi.getDu();
        // 雷人笑话
        String jokess = EarthyloveApi.getJokes();

        // 小贴士
        // templateMessage.addData(new WxMpTemplateData("tipshui", "\uD83D\uDC97" + "小贴士：", Colorful.Next.get(random.nextInt(Colorful.Next.size()))));
        // templateMessage.addData(new WxMpTemplateData("tips", WeatherAPI.getOrDefault("tips", "").toString(), Colorful.Next.get(random.nextInt(Colorful.Next.size()))));
        // 今日情话
        templateMessage.addData(new WxMpTemplateData("chps", "今日情话：", Colorful.Next.get(random.nextInt(Colorful.Next.size()))));
        templateMessage.addData(new WxMpTemplateData("chp", chp, Colorful.Next.get(random.nextInt(Colorful.Next.size()))));
        // 土味情话
        templateMessage.addData(new WxMpTemplateData("loveyous", "土味情话：", Colorful.Next.get(random.nextInt(Colorful.Next.size()))));
        templateMessage.addData(new WxMpTemplateData("loveyou", loveyou, Colorful.Next.get(random.nextInt(Colorful.Next.size()))));
        // 毒鸡汤
        templateMessage.addData(new WxMpTemplateData("dus", "心灵鸡汤：", Colorful.Next.get(random.nextInt(Colorful.Next.size()))));
        templateMessage.addData(new WxMpTemplateData("du", du, Colorful.Next.get(random.nextInt(Colorful.Next.size()))));
        // 雷人笑话
        // templateMessage.addData(new WxMpTemplateData("jokess", "雷人笑话：", ColorHandler.COLOR_LIST.get(random.nextInt(ColorHandler.COLOR_LIST.size()))));
        // templateMessage.addData(new WxMpTemplateData("jokes", jokess, ColorHandler.COLOR_LIST.get(random.nextInt(ColorHandler.COLOR_LIST.size()))));
        // 来自玫瑰星云的提示
        // templateMessage.addData(new WxMpTemplateData("constellationName", "来自玫瑰星云的提示：", Colorful.Next.get(random.nextInt(Colorful.Next.size()))));
        // templateMessage.addData(new WxMpTemplateData("love", "亲爱的XX今天有没有拉臭臭呀！", Colorful.Next.get(random.nextInt(Colorful.Next.size()))));

        return true;
    }

    /**
     * 中午模板
     *
     * @param random
     * @param templateMessage
     * @return
     */
    private boolean MiddayTemplate(Random random, WxMpTemplateMessage templateMessage) {
        // 更新日期
        templateMessage.addData(new WxMpTemplateData("time", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy年MM月dd日 HH:mm EEEE")), Colorful.Next.get(random.nextInt(Colorful.Next.size()))));
        // 标题
        templateMessage.addData(new WxMpTemplateData("title", "亲爱的XX中午好呀", Colorful.Next.get(random.nextInt(Colorful.Next.size()))));
        return true;
    }

    /**
     * 晚安模板
     *
     * @param random
     * @param templateMessage
     * @return
     */
    private boolean nightTemplate(Object baseTemplateParameter, Random random, WxMpTemplateMessage templateMessage) {
        WechatInterface wechatInterface = JSON.parseObject(JSON.toJSONString(baseTemplateParameter), WechatInterface.class);
        // 预测明天天气如何
        JSONObject TomorrowWeather = getTomorrowWeather(wechatInterface.getNightcity());

        // 彩虹屁
        String chp = EarthyloveApi.getChp();
        // 晚上好title
        templateMessage.addData(new WxMpTemplateData("title", "亲爱的XX晚上好呀" + "☾", Colorful.Next.get(random.nextInt(Colorful.Next.size()))));
        // 更新日期
        templateMessage.addData(new WxMpTemplateData("timewen", "现在是晚上", Colorful.Next.get(random.nextInt(Colorful.Next.size()))));
        templateMessage.addData(new WxMpTemplateData("time", LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm")) + "分(つω｀)～", Colorful.Next.get(random.nextInt(Colorful.Next.size()))));
        // 今日情话
        templateMessage.addData(new WxMpTemplateData("rainbow", chp, Colorful.Next.get(random.nextInt(Colorful.Next.size()))));
        templateMessage.addData(new WxMpTemplateData("night", "今天也要早睡哦" + "\uD83C\uDF19", Colorful.Next.get(random.nextInt(Colorful.Next.size()))));
        // 晚安故事
        // templateMessage.addData(new WxMpTemplateData("tale", gettale().getOrDefault("content", "").toString(), Colorful.Next.get(random.nextInt(Colorful.Next.size()))));
        // 晚安金言
        templateMessage.addData(new WxMpTemplateData("nightlove", RainbowHandler.nightList.get(random.nextInt(RainbowHandler.nightList.size())), Colorful.Next.get(random.nextInt(Colorful.Next.size()))));
        // 预测明天天气标题
        templateMessage.addData(new WxMpTemplateData("nighttitle", "预测明天天气", Colorful.Next.get(random.nextInt(Colorful.Next.size()))));
        // 设置地区
        templateMessage.addData(new WxMpTemplateData("nightcity", wechatInterface.getNightcity(), Colorful.Next.get(random.nextInt(Colorful.Next.size()))));
        // 接收天气
        templateMessage.addData(new WxMpTemplateData("weathers", "天气：", Colorful.Next.get(random.nextInt(Colorful.Next.size()))));
        templateMessage.addData(new WxMpTemplateData("weather", TomorrowWeather.getOrDefault("weather", "").toString(), Colorful.Next.get(random.nextInt(Colorful.Next.size()))));
        // 接收温度
        templateMessage.addData(new WxMpTemplateData("reals", "平均：", Colorful.Next.get(random.nextInt(Colorful.Next.size()))));
        templateMessage.addData(new WxMpTemplateData("real", TomorrowWeather.getOrDefault("real", "").toString(), Colorful.Next.get(random.nextInt(Colorful.Next.size()))));
        // 最高温度
        templateMessage.addData(new WxMpTemplateData("highests", "最高：", Colorful.Next.get(random.nextInt(Colorful.Next.size()))));
        templateMessage.addData(new WxMpTemplateData("highest", TomorrowWeather.getOrDefault("highest", "").toString(), Colorful.Next.get(random.nextInt(Colorful.Next.size()))));
        // 最低温度
        templateMessage.addData(new WxMpTemplateData("lowests", "最低：", Colorful.Next.get(random.nextInt(Colorful.Next.size()))));
        templateMessage.addData(new WxMpTemplateData("lowest", TomorrowWeather.getOrDefault("lowest", "").toString(), Colorful.Next.get(random.nextInt(Colorful.Next.size()))));

        return true;

    }

    /**
     * 发送微信模板
     */
    private void sendMessage(WxMpTemplateMessage templateMessage) {
        try {
            log.info("发送模板成功：{}", wxMpService.getTemplateMsgService().sendTemplateMsg(templateMessage));
        } catch (WxErrorException e) {
            log.error("发送模板失败", e);
        }
        log.info("用户：{}发送消息：{}", templateMessage.getToUser(), JSON.toJSONString(templateMessage));
    }

    /**
     * 天行数据api获取天气
     *
     * @param Today
     * @return
     */
    private JSONObject getTodayWeather(String Today) {
        try {
            String url = "http://api.tianapi.com/tianqi/index?key=" + "1374d9fdb0f1391710e8ca4fe7712c3e" + "&city=" + Today;
            ResponseEntity<Object> forEntity = REST_TEMPLATE.getForEntity(url, Object.class);
            JSONObject jsonObject = JSON.parseObject(JSON.toJSONString(forEntity.getBody()));
            JSONArray forecasts = jsonObject.getJSONArray("newslist");
            return forecasts.getJSONObject(0);
        } catch (Exception e) {
            log.error("获取天气失败", e);
            return new JSONObject();
        }
    }

    /**
     * 天行数据api获取明日天气
     *
     * @param Tomorrow
     * @return
     */
    private JSONObject getTomorrowWeather(String Tomorrow) {
        try {
            String url = "http://api.tianapi.com/tianqi/index?key=" + "1374d9fdb0f1391710e8ca4fe7712c3e" + "&city=" + Tomorrow;
            ResponseEntity<Object> forEntity = REST_TEMPLATE.getForEntity(url, Object.class);
            JSONObject jsonObject = JSON.parseObject(JSON.toJSONString(forEntity.getBody()));
            JSONArray forecasts = jsonObject.getJSONArray("newslist");
            return forecasts.getJSONObject(1);
        } catch (Exception e) {
            log.error("获取天气失败", e);
            return new JSONObject();
        }
    }

    /**
     * hui生日计算倒计时
     */
    private static final org.apache.logging.log4j.Logger log = org.apache.logging.log4j.LogManager.getLogger(HttpUtils.class);
    private static final Integer DAY_TIME_IN_MILLIS = 86400000; // 1000 * 24 * 60 * 60

    public static String getHui(String date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
        Date now = new Date();
        String year = sdf.format(now);
        String dateStr = year + "-" + date;
        SimpleDateFormat parseSdf = new SimpleDateFormat("yyyy-MM-dd");
        Date time = null;
        try {
            time = parseSdf.parse(dateStr);
        } catch (Exception e) {
            log.error(e);
            return null;
        }
        long diff = time.getTime() - now.getTime();
        long day = diff / DAY_TIME_IN_MILLIS;
        if (day < 0) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(time);
            calendar.add(Calendar.YEAR, 1);
            diff = calendar.getTimeInMillis() - now.getTime();
            day = diff / DAY_TIME_IN_MILLIS;
        }
        return "(ᕑᗢᓫ∗)˒" + String.valueOf(day);
    }

    /**
     * xuan生日计算倒计时
     *
     * @param date
     * @return
     */
    private String getXuan(String date) {
        SimpleDateFormat myFormatter = new SimpleDateFormat("yyyy-MM-dd");
        String clidate = date;
        Calendar cToday = Calendar.getInstance(); // 存今天
        Calendar cBirth = Calendar.getInstance(); // 存生日
        try {
            cBirth.setTime(myFormatter.parse(clidate)); // 设置生日
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        cBirth.set(Calendar.YEAR, cToday.get(Calendar.YEAR)); // 修改为本年
        String days;
        if (cBirth.get(Calendar.DAY_OF_YEAR) < cToday.get(Calendar.DAY_OF_YEAR)) {
            // 生日已经过了，要算明年的了
            days = String.valueOf(cToday.getActualMaximum(Calendar.DAY_OF_YEAR) - cToday.get(Calendar.DAY_OF_YEAR));
            days += cBirth.get(Calendar.DAY_OF_YEAR);
        } else {
            // 生日还没过
            days = String.valueOf(cBirth.get(Calendar.DAY_OF_YEAR) - cToday.get(Calendar.DAY_OF_YEAR));
        }
        // 输出结果
        return "₍ᐢ.ˬ.⑅ᐢ₎" + days;
    }

    /**
     * 第一次见面时间计算
     *
     * @param firstmeeting
     * @return
     */
    private String getfirstmeeting(String firstmeeting) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String meeting = firstmeeting;
        Date date = new Date();
        Date dates = null;
        try {
            dates = simpleDateFormat.parse(meeting);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        meeting = String.valueOf(Math.abs(dates.getTime() - date.getTime()) / (24 * 3600 * 1000));
        return meeting;
    }

}
