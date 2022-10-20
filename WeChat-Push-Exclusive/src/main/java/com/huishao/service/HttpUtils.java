package com.huishao.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;

import static jdk.nashorn.internal.runtime.regexp.joni.Config.log;


public class HttpUtils {


        //密码可能会包含的字符集合
//        private static char[] fullCharSource = {'1', '2', '3', '4', '5', '6', '7', '8', '9', '0',
//                'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
//                'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
//                '~', '!', '@', '#', '$', '%', '^', '&', '*', '(', ')', '_', '+', '{', '}', '|', ':', '"', '<', '>', '?', ';', '"', ',', '.', '/', '-', '=' ,'`'};

        private static char[] fullCharSource = {
                '1', '2', '3', '4', '5', '6', '7', '8', '9', '0',
                'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
                'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
        };



        //将可能的密码集合长度
        private static int fullCharLength = fullCharSource.length;

        //maxLength：生成的字符串的最大长度
        public static void generate(int maxLength) throws FileNotFoundException, UnsupportedEncodingException {
            //计数器，多线程时可以对其加锁，当然得先转换成Integer类型。
            int counter = 0;
            StringBuilder buider = new StringBuilder();

            PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream("F://dictionaries.txt"), "utf-8"));

            while (buider.toString().length() <= maxLength) {
                buider = new StringBuilder(maxLength * 2);
                int _counter = counter;
                //10进制转换成26进制
                while (_counter >= fullCharLength) {
                    //获得低位
                    buider.insert(0, fullCharSource[_counter % fullCharLength]);
                    _counter = _counter / fullCharLength;
                    //处理进制体系中只有10没有01的问题，在穷举里面是可以存在01的
                    _counter--;
                }
                //最高位
                buider.insert(0, fullCharSource[_counter]);
                counter++;

                pw.write(buider.toString() + "\n");
                System.out.println(buider.toString());
            }
        }

        public static void main(String[] args) throws FileNotFoundException, UnsupportedEncodingException {
            System.out.print("请输入你需要生成的字典位数：");
            Scanner sc = new Scanner(System.in);
            int x = sc.nextInt();

            generate(x);

        }










//    private static final RestTemplate REST_TEMPLATE = new RestTemplate();
//
//
//    private static final org.apache.logging.log4j.Logger log = org.apache.logging.log4j.LogManager.getLogger(HttpUtils.class);
//    private static final Integer DAY_TIME_IN_MILLIS = 86400000; // 1000 * 24 * 60 * 60
//
//    public static Long getRemainDay(String date) {
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
//        Date now = new Date();
//        String year = sdf.format(now);
//        String dateStr = year + "-" + date;
//        SimpleDateFormat parseSdf = new SimpleDateFormat("yyyy-MM-dd");
//        Date time = null;
//        try {
//            time = parseSdf.parse(dateStr);
//        } catch (Exception e) {
//            log.error(e);
//            return null;
//        }
//        long diff = time.getTime() - now.getTime();
//        long day = diff / DAY_TIME_IN_MILLIS;
//        if (day < 0) {
//            Calendar calendar = Calendar.getInstance();
//            calendar.setTime(time);
//            calendar.add(Calendar.YEAR, 1);
//            diff = calendar.getTimeInMillis() - now.getTime();
//            day = diff / DAY_TIME_IN_MILLIS;
//        }
//        return day;
//
//    }


//
//    public static void main(String[] args) throws IOException {
//
//                BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("D://dictionaries.txt"),"utf-8"));
//                String[] str = {"a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z"};
//
//                for (int i = 0;i < str.length;++i)
//                {
//                    for (int j = 0;j < str.length;++j)
//                    {
//                        String tmp = ""; //算法部分
//                        out.write(tmp + "\n");
//                    }
//                }
//                for (int i = str.length - 1;i > 0;i--)
//                {
//                    for (int j = str.length - 1;j > 0;j--)
//                    {                String tmp = "";
//                        //算法部分
//                        out.write(tmp + "\n");
//                    }
//                }        /*不规范写法，临时需求*/
//                out.close();
//                System.out.println("密码本生成完毕！！！");


//        System.out.println(getRemainDay("08-18"));

//        try {
//            String url = "http://api.tianapi.com/tianqi/index?key=" + "1374d9fdb0f1391710e8ca4fe7712c3e" + "&city=" + "东莞市";
//            ResponseEntity<Object> forEntity = REST_TEMPLATE.getForEntity(url, Object.class);
//            JSONObject jsonObject = JSON.parseObject(JSON.toJSONString(forEntity.getBody()));
//            JSONArray forecasts = jsonObject.getJSONArray("newslist");
//            System.out.println(forecasts.getJSONObject(1));
//        } catch (Exception e) {
//            System.out.println(e);
//        }
}


//    // 配置HttpClients连接池管理器，可以自动释放连接池资源
//    static PoolingHttpClientConnectionManager manager;
//
//    static {
//        // 为了方便我直接丢到静态代码块中初始化
//        manager = new PoolingHttpClientConnectionManager();
//        manager.setMaxTotal(100);
//        manager.setDefaultMaxPerRoute(10);
//    }
//
//
//    public static String doGetHtml(String url) {
//        // 创建httpclient对象用于发起请求
//        CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(manager).build();
//        // 创建get请求，指定uri
//        HttpGet httpGet = new HttpGet(url);
//        // 设置连接的一些参数，可以不设置
//        httpGet.setConfig(getConfig());
//        // 设置user-agent来欺骗网站是浏览器访问
//        httpGet.setHeader("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/86.0.4240.75 Safari/537.36");
//
//        CloseableHttpResponse response = null;
//        try {
//            response = httpClient.execute(httpGet);
//            if (response.getStatusLine().getStatusCode() == 200) {
//                // 如果返回状态码为200代表成功，则将响应数据返回
//                return EntityUtils.toString(response.getEntity(), "utf8");
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            if (response != null) {
//                try {
//                    // 这里只需要释放response，httpclient由连接池管理器释放
//                    response.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//        return "";
//    }
//
//    private static RequestConfig getConfig() {
//        RequestConfig config = RequestConfig.custom().setConnectionRequestTimeout(2000)
//                .setConnectTimeout(3000)
//                .setSocketTimeout(10 * 1000)
//                .build();
//        return config;
//    }
//
//
//    //    private String getbiying() {
////
////        String json = HttpUtils.doGetHtml("https://www.bing.com/HPImageArchive.aspx?format=js&idx=0&n=1&nc=1603114218081&pid=hp&mkt=zh-CN");
////        ObjectMapper mapper = new ObjectMapper();
////        JsonNode jsonNode = null;
////        try {
////            jsonNode = mapper.readTree(json);
////        } catch (JsonProcessingException e) {
////            throw new RuntimeException(e);
////        }
////        String url  = jsonNode.get("images").get(0).get("url").asText();
////        return url;
////    }

