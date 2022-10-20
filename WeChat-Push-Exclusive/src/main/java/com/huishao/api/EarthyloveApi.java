package com.huishao.api;

import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.huishao.connector.WechatInterface;
import com.huishao.handler.RainbowHandler;
import com.huishao.service.WechatService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Random;

@Service
@Slf4j
public class EarthyloveApi {

    /**
     * 获取土味情话api
     * @return
     */
    public static String getLoveyoukiss() {
        String httpUrl = "http://api.tianapi.com/saylove/index?key=1374d9fdb0f1391710e8ca4fe7712c3e";
        BufferedReader reader = null;
        String result = null;
        StringBuffer sbf = new StringBuffer();

        try {
            URL url = new URL(httpUrl);
            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();
            connection.setRequestMethod("GET");
            InputStream is = connection.getInputStream();
            reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            String strRead = null;
            while ((strRead = reader.readLine()) != null) {
                sbf.append(strRead);
                sbf.append("\r\n");
            }
            reader.close();
            result = sbf.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        JSONObject jsonObject = JSONObject.parseObject(result);
        JSONArray newslist = jsonObject.getJSONArray("newslist");
        String content = newslist.getJSONObject(0).getString("content");
        return content;
    }

//    public static String getbing() {
//
//        String bing_url = "https://cn.bing.com/HPImageArchive.aspx?format=js&idx=0&n=1";
//        JSONObject jsonObject = JSONObject.parseObject(bing_url);
//
//        String bing_pic = "https://cn.bing.com/"+jsonObject;
//
//
//
////        res = requests.get(bing_url).json()
////        bing_pic = "https://cn.bing.com/"+res["images"][0]["url"]
////        bing_title = res["images"][0]["title"]
////        bing_content = re.sub(u"\\(.*?\\)", "", res["images"][0]["copyright"])
////        bing_tip = bing_title+"——"+bing_content
////        return {
////                "bing_pic": bing_pic,
////                "bing_tip": bing_tip
////        }
////        except Exception as e:
////        print("获取必应数据出错:", e)
////        return None
//    }

    /**
     * 雷人笑话api
     * @return
     */
    public static String getJoke() {
        String result = null;
        String info = null;
        try {
            result = HttpUtil.get("http://api.tianapi.com/joke/index?key=1374d9fdb0f1391710e8ca4fe7712c3e&num=1");
            JSONObject jsonObject = JSONObject.parseObject(result);
            if (jsonObject.getString("msg").equals("success")) {
                JSONArray newslist = jsonObject.getJSONArray("newslist");//获取 newslist 字段的集合
                JSONObject today = newslist.getJSONObject(0);  //获取集合中0 索引的元素
                info = (String) today.get("content");   //key：content value：要的内容
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return info;

        // 第二种截取key的方法
//        String httpUrl = "http://api.tianapi.com/joke/index?key=1374d9fdb0f1391710e8ca4fe7712c3e&num=1";
//        BufferedReader reader = null;
//        String result = null;
//        StringBuffer sbf = new StringBuffer();
//
//        try {
//            URL url = new URL(httpUrl);
//            HttpURLConnection connection = (HttpURLConnection) url
//                    .openConnection();
//            connection.setRequestMethod("GET");
//            InputStream is = connection.getInputStream();
//            reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
//            String strRead = null;
//            while ((strRead = reader.readLine()) != null) {
//                sbf.append(strRead);
//                sbf.append("\r\n");
//            }
//            reader.close();
//            result = sbf.toString();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        JSONObject jsonObject = JSONObject.parseObject(result);
//        JSONArray newslist = jsonObject.getJSONArray("newslist");
//        String content = newslist.getJSONObject(0).getString("content");
//        String tmp = "";
//        int len = 17;
//        if (len > 0) {
//            if (content.length() > len) {
//                int rows = (content.length() + len - 1) / len;
//                for (int i = 0; i < rows; i++) {
//                    if (i == rows - 1) {
//                        tmp += content.substring(i * len);
//                    }
//                    else {
//                        tmp += content.substring(i * len, i * len + len) + "\r\n";
//                    }
//                }
//            }
//            else {
//                tmp = content;
//            }
//        }
//        return tmp;
    }

//    public JSONObject getTianqi() {
//        String result = null;
//        JSONObject today = new JSONObject();
//        try {
//            result = HttpUtil.getUrl("https://api.map.baidu.com/weather/v1/?district_id=" + district_id + "&data_type=all&ak=" + ak);
//            JSONObject jsonObject = JSONObject.parseObject(result);
//            if (jsonObject.getString("message").equals("success")) {
//                JSONArray arr = jsonObject.getJSONObject("result").getJSONArray("forecasts");
//                today = arr.getJSONObject(0);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return today;
//    }

    /**
     * 获取彩虹屁
     *
     * @return
     */
    public static String getChp() {
        try {
            String url = "https://api.shadiao.pro/chp";
            ResponseEntity<Object> forEntity = WechatService.REST_TEMPLATE.getForEntity(url, Object.class);
            JSONObject jsonObject = JSON.parseObject(JSON.toJSONString(forEntity.getBody()));
            return jsonObject.getJSONObject("data").getString("text");
        } catch (Exception e) {
            log.error("获取彩虹屁失败", e);
            String[] split = RainbowHandler.CHP.split("\\n");
            return split[new Random().nextInt(split.length)].split("、")[1];
        }
    }

    /**
     * 获取毒鸡汤
     *
     * @return
     */
    public static String getDu() {
        try {
            String url = "https://api.shadiao.pro/du";
            ResponseEntity<Object> forEntity = WechatService.REST_TEMPLATE.getForEntity(url, Object.class);
            JSONObject jsonObject = JSON.parseObject(JSON.toJSONString(forEntity.getBody()));
            return jsonObject.getJSONObject("data").getString("text");
        } catch (Exception e) {
            log.error("获取毒鸡汤失败", e);
            return "加油！你是最胖的！！！";
        }
    }

    /**
     * 天行api土味情话
     *
     * @return
     */
    public static String getLoveyou() {
        try {
            String loveapi = EarthyloveApi.getLoveyoukiss();
            return loveapi;
        } catch (Exception e) {
            log.error("获取土味情话失败", e);
            // 获取api失败则使用
            String[] tu = RainbowHandler.tuwei.split("\\n");
            return tu[new Random().nextInt(tu.length)].split("、")[1];
        }
    }

    /**
     * 雷人笑话
     *
     * @return
     */
    public static String getJokes() {
        try {
            String jokes = EarthyloveApi.getJoke();
            return jokes;
        } catch (Exception e) {
            log.error("获取雷人笑话失败", e);
            return "今日没有雷人笑话";
        }
    }


    /**
     * 随机获取幸运颜色
     *
     * @return
     */
    public static String getHuise() {
        String[] sthui = RainbowHandler.huise.split("\\n");
        return sthui[new Random().nextInt(sthui.length)].split("、")[1];
    }

    /**
     * 清早起来第一句话
     *
     * @return
     */
    public static String getearly() {
        String[] early = RainbowHandler.early.split("\\n");
        return early[new Random().nextInt(early.length)].split("、")[1];
    }

    /**
     * 今日心情
     *
     * @param random
     * @return
     */
    public static String getmood(Random random) {
        String[] split = RainbowHandler.HAPPY_STATE.split("、");
        String state = split[random.nextInt(split.length)];
        return state;
    }

    /**
     * 今日的你
     */
    public static String getsummary(Random random) {
        String[] str = RainbowHandler.summary.split("、");
        String sum = str[random.nextInt(str.length)];
        return sum;
    }

    /**
     * 获取星座
     */
//    private JSONObject getConstellation() {
//        try {
//            String url = "http://api.tianapi.com/xingzuo/index?key=" + constellationKey + "&me=" + you + me ;
//            ResponseEntity<Object> forEntity = REST_TEMPLATE.getForEntity(url, Object.class);
//            JSONObject jsonObject = JSON.parseObject(JSON.toJSONString(forEntity.getBody()));
//            JSONArray forecasts = jsonObject.getJSONArray("newslist");
//            return forecasts.getJSONObject(0);
//        } catch (Exception e) {
//            log.error("获取星座失败", e);
//            return new JSONObject();
//        }
//    }

//    private JSONObject gettale() {
//        String url = "http://api.tianapi.com/story/index?key=" + tale;
//        ResponseEntity<Object> responseEntity = REST_TEMPLATE.getForEntity(url, Object.class);
//        JSONObject jsonObject = JSON.parseObject(JSON.toJSONString(responseEntity.getBody()));
//        JSONArray jsonArray = jsonObject.getJSONArray("newslist");
//        return jsonArray.getJSONObject(0);
//    }

}
