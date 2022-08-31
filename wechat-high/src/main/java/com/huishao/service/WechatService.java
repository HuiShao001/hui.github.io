package com.huishao.service;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.huishao.connector.WechatInterface;
import com.huishao.connector.PersonalInfo;
import com.huishao.connector.WechatConfigProperties;
import com.huishao.config.EmojiConfig;
import lombok.extern.slf4j.Slf4j;
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

import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@Slf4j
public class WechatService {

    private final static String DEFAULT_KEY = "default";

    private static final List<String> COLOR_LIST = new ArrayList<>();
    private static final RestTemplate REST_TEMPLATE = new RestTemplate();

    private static final String HAPPY_STATE = "欢快、喜上眉梢、喜气洋洋、欢天喜地、乐不思蜀、喜笑颜开、兴高采烈、兴致大发、笑容满面、精神抖擞、满面春风、笑逐颜开、心花怒放、情绪高涨、欢欣雀跃、风光无限、春心荡漾、巧笑嫣然、上蹿下跳、怦然心动、激动不已、激情四射、情绪高涨、乐开怀、红红火火、笑容可掬、乐开怀、高高兴兴、意气风发、" +
            "欢喜、喜悦、夷愉、愉快、开心、快乐、欢乐、乐意、首肯、愿意、忻悦、欣忭、欣喜、快活、夷悦、怡悦、雀跃、兴奋、欢腾、欢跃、欢欣、欢畅、欢娱、得意、痛快、康乐、安乐、得志、畅快、舒畅、称心、满足、满意欢娱、欢天喜地、乐不可支、兴趣盎然、兴致勃勃、喜形于色" +
            "乐悠悠、甜滋滋、乐滋滋、乐悠悠、笑嘻嘻、笑眯眯、笑哈哈、喜洋洋、喜滋滋、兴冲冲、乐融融、乐陶陶、乐呵呵";

    static {
        COLOR_LIST.add("#00ff00");
        COLOR_LIST.add("#00ffff");
        COLOR_LIST.add("#ff0000");
        COLOR_LIST.add("#980000");
        COLOR_LIST.add("#ff9900");
        COLOR_LIST.add("#4a86e8");
        COLOR_LIST.add("#0000ff");
        COLOR_LIST.add("#9900ff");
        COLOR_LIST.add("#ff00ff");
        COLOR_LIST.add("#434343");
        COLOR_LIST.add("#660000");
        COLOR_LIST.add("#cc0000");
        COLOR_LIST.add("#783f04");
        COLOR_LIST.add("#4c1130");
        COLOR_LIST.add("#1c4587");
        COLOR_LIST.add("#3c78d8");
        COLOR_LIST.add("#674ea7");

    }

    // 如果彩虹屁获取失败则输出以下
    String CHP = "1、这满目星河也抵不过你对我展眉一笑。早安。\n" +
            "2、不管你信不信，有个人正在等待，在每个太阳正常升起的早上，对你说早安! \n" +
            "3、每一天的太阳都愿照在你的身上，让你一天都保持好心情，早安!\n" +
            "4、喜爱一切美好的事物：阳光，秋千，树叶，花瓣……还有你。早安!\n" +
            "5、昨晚你说睡了，你知道我说了多少次你的名字才睡着我吗，在数到1313次我醒了，在1314次对你说“早安”，说不定就是一辈子了。\n" +
            "6、喜欢你是什么感觉呢，有时候委屈得要哭鼻子好久好久，有时候又因为你开心的飞起来，喜欢你，也太难了吧。早安!\n" +
            "7、跟你说早安，是想让你知道，睁开眼睛第一个想到的，是你。\n" +
            "8、想对你说，时光兜兜转转，我感谢与你的这次遇见。余生，请多赐教。早安!\n" +
            "9、你的微笑，是我这辈子见过最美的景色，我想收藏这样的风景一辈子。早安!\n" +
            "10、在青山绿水之间，我想牵着你的手，走过这座桥，桥上是绿叶红花，桥下是流水人家，桥的那头是青丝，桥的这头是白发，而此刻的你，早上，要安好。\n" +
            "11、在对你说出晚安后我睡着了了，今天对你说出早安后，我又开始期盼晚安了。\n" +
            "12、我嘴角向上扬起，每一次都是因为你，早安。\n" +
            "13、总想把世界上最好的都给你，却发现世界最好的就是你。早安!\n" +
            "14、我爱这世间，但这世间不如你。早安!\n" +
            "15、最近牙痛，因为常常想你，感觉太甜蜜，长蛀牙了。早安~\n" +
            "16、如果可以和你在一起，我宁愿让天空所有的星光全部损落，因为你的眼睛，是我生命里最亮的光芒。早安!\n" +
            "17、如果黑夜太难熬，我陪你日夜颠倒。早安!\n" +
            "18、若非青春苦短，谁会想来日方长。早安!\n" +
            "19、花开花落千年，我对你的爱永生不变。早安!\n" +
            "20、我爱你，永远为期，死亡为界，这辈子从没想过要放弃你。早安!\n" +
            "21、生活就是一幅美丽的风景画，而你就是这画中人，让人忘记昼夜更替忘掉四季轮回，只愿沉醉其中。早安~\n" +
            "22、我相信，总会有不期而遇的温暖，和生生不息的希望，就像你奇迹般出现在我的生命里，照亮了我的生活，早安!\n" +
            "23、你是我永远的春天，是我美丽的风景，一生与你不离不弃，每天准时给你一句早安。\n" +
            "24、我想牵你的手，从心动到古稀。早安!\n" +
            "25、若有幸到白头，绝不负你此生温柔。早安!\n" +
            "26、我想未来我一定会天天陪你上市场。早安!\n" +
            "27、我小小的世界，装不下太多，只能装下在乎我和我在乎的你。早安~\n" +
            "28、有错过，才会有新的遇见。缘分就是，遇到你的那一刻，不早不晚，刚刚好。早安~\n" +
            "29、我要收一个可爱到爆炸的小朋友，听说你最合适，以后你的早安问候都由我来说了，早安，我家小宝贝。\n" +
            "30、我要牵着你的手，从心动到古稀。早安!\n" +
            "31、世上最幸福的工作就是做你的专职爱人。早安!\n" +
            "32、我给你一片阳光，就是希望你灿烂。早安!\n" +
            "33、走在路上，满脑子都是你，发现风都是甜的。早安~\n" +
            "34、遇见你，是我生命的幸运;爱上你，是我生命的愉悦;失去你，是我生命的遗憾;没有你，无法感受心灵的震撼，此生惟愿爱你!早安!\n" +
            "35、心中自从有了期待的人，见到你的每一天都是崭新的一天。早安!\n" +
            "36、走过千山万水，才懂得家最温馨;历经风风雨雨，才感悟平淡最真;错过许许多多，才知道珍惜拥有;路过熙熙攘攘，才明白你最珍贵。早安!\n" +
            "37、在千千万万种可能里，我只会选择你。早安!\n" +
            "38、你的身上有一股魔力，让我不自觉地想要靠近你，就连早安我也只想对你一个人说。\n" +
            "39、狭路相逢勇者胜，温柔只给意中人，早安!\n" +
            "40、从未想过要其它，只想和你有个家。早安!\n" +
            "41、你喜欢我这件事，我允许了。早安!\n" +
            "42、每个人的早餐不是豆浆油条就是馒头包子，而我不一样，我只希望我的早餐是你，看到你我就心满意足了，早安!\n" +
            "43、没有谁的缘分是天注定的，我不想错过，所以我就来了，出现在你的每个清晨，对你说出早上好!\n" +
            "44、遇见你，是我所有美好故事的开始。早安!\n" +
            "45、喜欢你，想和你在一起。早安!\n" +
            "46、多想一不小心，就和你白头到老。早安!\n" +
            "47、放下你手的一切，接住这个满心满眼都是你的男孩子吧，我相信他一定能做好的。早安\n" +
            "48、错过的从前用将来补全，陪着你到静止的地方。早安!\n" +
            "49、别追公交车了，追我吧。早安!\n" +
            "50、要陪在值得的人身边一年又一年。早安!\n" +
            "51、早安，你是我平淡白开水的生活里加的一颗糖，让我的生活也跟着甜蜜起来。\n" +
            "52、爱你的理由只有一个，你就是理由。早安!\n" +
            "53、情书是我抄的，但喜欢你是真的。早安!\n" +
            "54、我想马上牵着你的手，去感受什么才是永远。早安!\n" +
            "55、自从遇见你，一切都和以前不同了。早安!\n" +
            "56、世界上唯一的你，是可爱的是迷人的，却也是脆弱的，让人不得不去照顾你保护你。早安!\n" +
            "57、在这个晴空暖阳下，陪你看花开倾城，陪你听潮起潮落，陪你欢喜，陪你微笑，就这样生生世世，尘埃落定。\n" +
            "58、我苦苦追寻的星辰，在你的笑眼里。早安!\n" +
            "59、只要你还在，我就不怕孤独。孤独是一个可怕的命题，往往是相爱的人不会孤独，孤独的人更加孤独。一个人独处的时光，怎么也比不上和你共处的片刻。亲爱的，早上好。\n" +
            "60、遇见你以后，理想不再是骑马喝酒去天涯，而是再晚我也要回家。早安!\n" +
            "61、希望有个如你一般的人，如山间清爽的风，如古城温暖的光，从清晨到夜晚，由山野到书房只要最后是你，就好。早安!\n" +
            "62、遇见你之后，我才知道过去都孤独。早安!\n" +
            "63、我的脑海里，总有一种声音在呼唤我，后来我才知道原来是你的名字。早安!\n" +
            "65、不知道为什么，你的脸，总有一种让我拥抱你的幻想。可能是我太喜欢你了吧，不知不觉就想要吻你，早安~\n" +
            "66、一个人走或许可以走得很快，但两个人走却可以走很远。早安!\n" +
            "67、你一定是皮卡丘，为什么我一见到你就有一种心脏麻痹的感觉，早安!\n" +
            "68、清晨的第一缕阳光是那么美好且绚烂，就如同你一般，早安!\n" +
            "69、就算全世界都否定，我也要跟你在一起。早安!\n" +
            "70、和你在一起，我不想给任何人机会!早安!";

    String huise ="1、超级粉\n" + "2、勃艮第红\n" +
            "3、红釉色\n" + "4、绯红色\n" + "5、赭红色\n" + "6、酒红色\n" + "7、妃红色\n" + "8、绯红色\n" + "9、绛红色\n" + "10、猩红色\n" + "11、枣红色\n" + "12、砖红色\n" + "13、圣女果红\n" +
            "14、金陵红\n" + "15、淮安红\n" + "16、雁来红\n" + "17、釉里红\n" + "18、提香红\n" + "19、云锦红\n" + "20、暮霭红\n" + "21、落霞红\n" +
            "22、赤赭红\n" + "23、火焰红\n" + "24、唐菖蒲红\n" + "25、鹅冠红\n" +
            "26、锦葵红\n" + "27、莲瓣红\n" + "28、炼瓦红\n" + "29、玳瑁红\n" + "30、深藕红\n" + "31、碧玺红\n" + "32、郎窑红\n" + "33、沙漠红\n" + "34、胡桃红\n" + "35、赤焰红\n" + "36、赭红色\n" +
            "37、酒红色\n" + "38、嫣红色\n" + "39、浆果红\n"+ "40、枫叶红\n" + "41、樱桃红\n" + "42、杜鹃红\n" + "43、海棠红\n" + "44、高粱红\n" + "45、石榴红\n" + "46、西柚红\n" + "47、焦糖红\n" + "48、桑葚红\n" +
            "49、玫瑰红\n" + "50、树莓红\n" + "51、月季红\n"+ "52、米绿黄\n" + "53、米色淡黄\n" + "54、沙黄色\n" + "55、信号黄 \n" + "56、金黄色\n" + "57、蜜黄色\n" + "58、玉米黄\n" + "59、灰黄色 \n" + "60、米褐色\n" +
            "61、柠檬黄\n" + "62、浅灰\n" + "63、象牙色 \n" + "64、亮象牙色\n" + "65、硫磺色\n" + "66、深黄色\n" + "67、绿黄色 \n" + "68、米灰色\n" + "69、橄榄黄\n" + "70、油菜黄\n" + "71、交通黄 \n" +
            "72、赭黄色\n" + "73、咖喱色\n" + "74、浅橙黄\n" + "75、金雀花黄 \n" + "76、大丽花黄\n" + "77、粉黄色\n" + "78、黄橙色\n" + "79、橘红 \n" + "80、朱红\n" + "81、淡橙\n" + "82、淡橙\n" + "83、浅红橙 \n" +
            "84、交通橙\n" + "85、信号橙\n" + "86、深橙色\n" +
            "87、鲑鱼橙 \n" + "88、火焰红\n" + "89、信号红\n" + "90、胭脂红\n" + "91、宝石红 \n" + "92、紫红色\n" + "93、葡萄酒红\n" + "94、氧化红 \n" + "95、红玄武土色\n" + "96、米红色\n" + "97、番茄红\n" +
            "98、古粉红色\n" + "99、淡粉红色\n" + "100、珊瑚红色\n" +
            "101、玫瑰色\n" + "102、草莓红 \n" + "103、交通红\n" + "104、鲑鱼粉红色\n" + "105、悬钩子红色\n" + "106、戈亚红色 \n" + "107、丁香红\n" +
            "108、紫红色\n" + "109、石南紫\n" + "110、酒红紫 \n" + "111、丁香蓝\n" + "112、交通紫\n" + "113、紫红蓝色\n" + "114、信号紫罗兰\n" + "115、崧蓝紫色\n" + "116、电视品红色\n" +
            "117、紫蓝色\n" + "118、蓝绿色\n" + "119、群青蓝\n" + "120、蓝宝石蓝\n" + "121、蓝黑色\n" +
            "122、信号蓝\n" + "123、亮蓝色\n" + "124、灰蓝色\n" + "125、天青蓝\n" + "126、龙胆蓝色\n"+"127、钢蓝色\n" + "128、淡蓝色\n" + "129、钴蓝色\n" + "130、鸽蓝色 \n" + "131、天蓝色\n" + "132、交通蓝\n" + "133、绿松石蓝\n" +
            "134、卡布里蓝色 \n" + "135、海蓝色\n" + "136、不来梅蓝色\n" +
            "137、夜蓝色\n" + "138、冷蓝色 \n" + "139、崧蓝蓝色";

//            "超级粉\n" + "勃艮第红\n" +
//            "红釉色\n" + "绯红色\n" + "赭红色\n" + "酒红色\n" + "妃红色\n" + "绯红色\n" + "绛红色\n" + "猩红色\n" + "枣红色\n" + "砖红色\n" + "圣女果红\n" +
//            "金陵红\n" + "淮安红\n" + "雁来红\n" + "釉里红\n" + "提香红\n" + "云锦红\n" + "暮霭红\n" + "落霞红\n" +
//            "赤赭红\n" + "火焰红\n" + "唐菖蒲红\n" + "鹅冠红\n" +
//            "锦葵红\n" + "莲瓣红\n" + "炼瓦红\n" + "玳瑁红\n" + "深藕红\n" + "碧玺红\n" + "郎窑红\n" + "沙漠红\n" + "胡桃红\n" + "赤焰红\n" + "赭红色\n" +
//            "酒红色\n" + "嫣红色\n" + "浆果红\n"+ "枫叶红\n" + "樱桃红\n" + "杜鹃红\n" + "海棠红\n" + "高粱红\n" + "石榴红\n" + "西柚红\n" + "焦糖红\n" + "桑葚红\n" +
//            "玫瑰红\n" + "树莓红\n" + "月季红\n"+ "米绿黄\n" + "米色淡黄\n" + "沙黄色\n" + "信号黄 \n" + "金黄色\n" + "蜜黄色\n" + "玉米黄\n" + "灰黄色 \n" + "米褐色\n" +
//            "柠檬黄\n" + "浅灰\n" + "象牙色 \n" + "亮象牙色\n" + "硫磺色\n" + "深黄色\n" + "绿黄色 \n" + "米灰色\n" + "橄榄黄\n" + "油菜黄\n" + "交通黄 \n" +
//            "赭黄色\n" + "咖喱色\n" + "浅橙黄\n" + "金雀花黄 \n" + "大丽花黄\n" + "粉黄色\n" + "黄橙色\n" + "橘红 \n" + "朱红\n" + "淡橙\n" + "淡橙\n" + "浅红橙 \n" +
//            "交通橙\n" + "信号橙\n" + "深橙色\n" +
//            "鲑鱼橙 \n" + "火焰红\n" + "信号红\n" + "胭脂红\n" + "宝石红 \n" + "紫红色\n" + "葡萄酒红\n" + "氧化红 \n" + "红玄武土色\n" + "米红色\n" + "番茄红\n" +
//            "古粉红色 \n" + "淡粉红色\n" + "珊瑚红色\n" +
//            "玫瑰色\n" + "草莓红 \n" + "交通红\n" + "鲑鱼粉红色\n" + "悬钩子红色\n" + "戈亚红色 \n" + "丁香红\n" +
//            "紫红色\n" + "石南紫\n" + "酒红紫 \n" + "丁香蓝\n" + "交通紫\n" + "紫红蓝色\n" + "信号紫罗兰\n" + "崧蓝紫色\n" + "电视品红色\n" +
//            "紫蓝色\n" + "蓝绿色\n" + "群青蓝\n" + "蓝宝石蓝\n" + "蓝黑色\n" +
//            "信号蓝\n" + "亮蓝色\n" + "灰蓝色\n" + "天青蓝\n" + "龙胆蓝色\n"+"钢蓝色\n" + "淡蓝色\n" + "钴蓝色\n" + "鸽蓝色 \n" + "天蓝色\n" + "交通蓝\n" + "绿松石蓝\n" +
//            "卡布里蓝色 \n" + "海蓝色\n" + "不来梅蓝色\n" +
//            "夜蓝色\n" + "冷蓝色 \n" + "崧蓝蓝色\n" + "铜锈绿色\n" + "翡翠绿色\n" + "叶绿色 \n" + "橄榄绿\n" + "蓝绿色\n" + "苔藓绿\n" + "橄榄灰绿 \n" + "瓶绿\n" + "褐绿\n" +
//            "冷杉绿\n" + "草绿色 \n" + "淡橄榄绿\n" +
//            "墨绿色\n" + "芦苇绿\n" + "橄榄黄 \n" + "黑齐墩果色\n" + "绿松石绿色\n" + "五月绿\n" + "黄绿色 \n" + "崧蓝绿色\n" + "铭绿色\n" + "浅绿色\n" + "橄榄土褐色 \n" +
//            "交通绿\n" + "蕨绿色\n" + "蛋白石绿色\n" + "浅绿色 \n" + "松绿色\n" + "薄荷绿\n" + "信号绿\n" + "薄荷绿蓝色 \n" +
//            "崧蓝绿松石色\n" + "松鼠灰\n" + "银灰色\n" + "橄榄灰绿色 \n" + "苔藓绿\n" + "信号灰\n" + "鼠灰色\n" + "米灰色 \n" + "土黄灰色\n" + "绿灰色\n" + "油布灰\n" +
//            "铁灰色 \n" + "玄武石灰\n" +
//            "褐灰色\n" + "浅橄榄灰\n" + "煤灰 \n" + "黑灰\n" + "暗灰\n" + "混凝土灰\n" + "石墨灰 \n" + "花岗灰\n" + "石灰色\n" + "蓝灰色\n" + "卵石灰 \n" + "水泥灰\n" +
//            "黄灰色\n" + "浅灰色\n" +
//            "铂灰色 \n" + "土灰色\n" + "玛瑙灰\n" + "石英灰\n";


    @Autowired
    private WechatConfigProperties wechatConfigProperties;

    // 天气
    @Value("${lbs.key}")
    private String lbsKey;

    // 星座
    @Value("${constellation.key}")
    private String constellationKey;

    // 生日倒计时
    @Value("${birthdayConf.birthdayDate}")
    private String birthdayDate;

    // 第一次见面计算天数
    @Value("${First.Firstmeeting}")
    private String firstmeeting;

    @Autowired
    private WxMpService wxMpService;

    @Autowired
    private PersonalInfo personalInfo;

    /**
     * 设置发送时间
     */
    @Scheduled(cron = "0 30 7 * * ?")
    public void sendMpWechatMessage() {
        try {
            WxMpUserList wxMpUserList = wxMpService.getUserService().userList(null);
            for (String openId : wxMpUserList.getOpenids()) {
                //早上问好
                sendWechatMessage(openId, "morningTemplate");
                //颜文字
                sendWechatMessage(openId, "emoticonsTemplate");
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
            Optional.ofNullable(wechatTemplate.getParameter())
                    .map(stringBaseTemplateParameterMap -> {
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
                                .toUser(openId)
                                .templateId(wechatTemplate.getTemplateId())
                                .build();
                        switch (template) {
                            case "morningTemplate":
                                boolean b = false;
                                try {
                                    b = morningTemplate(baseTemplateParameter, random, templateMessage);
                                } catch (ParseException e) {
                                    throw new RuntimeException(e);
                                }
                                if (!b) {
                                    return;
                                }
                                break;
                            case "emoticonsTemplate":
                                String color = COLOR_LIST.get(random.nextInt(COLOR_LIST.size()));
                                String s = EmojiConfig.EXPRESSION_LIST.get(random.nextInt(EmojiConfig.EXPRESSION_LIST.size()));
                                List<String> splitExpression = EmojiConfig.splitExpression(s);
                                for (String sp : splitExpression) {
                                    templateMessage.addData(new WxMpTemplateData("data1", sp, color));
                                    sendMessage(templateMessage);
                                }
                                return;
                            default:
                                return;
                        }
                        sendMessage(templateMessage);
                    });
        });
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
     * 获取星座
     */
    private JSONObject getConstellation(String name) {
        try {
            String url = "http://web.juhe.cn/constellation/getAll?consName=" + URLEncoder.encode(name, "UTF-8") + "&type=today&key=" + constellationKey;
            String s = HttpUtil.get(url);
            return JSON.parseObject(s);
        } catch (Exception e) {
            log.error("获取星座失败", e);
            return new JSONObject();
        }
    }

    /**
     * 获取天气
     */
    private JSONObject getWeather(String city) {
        try {
            String url = "https://restapi.amap.com/v3/weather/weatherInfo?key=" + lbsKey + "&city=" + city + "&extensions=all";
            // String url = "https://restapi.amap.com/v3/weather/weatherInfo?key=" + lbsKey + "&city=110000&extensions=all";
            // String url = "https://restapi.amap.com/v3/weather/weatherInfo?key=97837c233380be62a6eaf2713e2d1990&city=110000&extensions=all";
            ResponseEntity<Object> forEntity = REST_TEMPLATE.getForEntity(url, Object.class);
            JSONObject jsonObject = JSON.parseObject(JSON.toJSONString(forEntity.getBody()));
            JSONArray forecasts = jsonObject.getJSONArray("forecasts");
            return forecasts.getJSONObject(0).getJSONArray("casts").getJSONObject(0);
        } catch (Exception e) {
            log.error("获取天气失败", e);
            return new JSONObject();
        }
    }

    /**
     * 计算生日倒计时
     * @param birthdayDate
     * @return
     */
    private String getbirthdayDate(String birthdayDate) {
        SimpleDateFormat myFormatter = new SimpleDateFormat("yyyy-MM-dd");
        String clidate = birthdayDate;
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
        return days;
    }

    /**
     * 第一次见面时间计算
     *
     * @param firstmeeting
     * @return
     */
    private String  getfirstmeeting(String firstmeeting){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String meeting = firstmeeting;
        Date date = new Date();
        Date dates = null;
        try {
            dates = simpleDateFormat.parse(meeting);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        meeting = String.valueOf(Math.abs(dates.getTime() - date.getTime()) / (24*3600*1000));
        return meeting;
    }

    /**
     * 早安模板推送
     * @param baseTemplateParameter
     * @param random
     * @param templateMessage
     * @return
     * @throws ParseException
     */
    private boolean morningTemplate(Object baseTemplateParameter, Random random, WxMpTemplateMessage templateMessage) throws ParseException {
        WechatInterface wechatInterface = JSON.parseObject(JSON.toJSONString(baseTemplateParameter), WechatInterface.class);
        // 认识时间
        LocalDate meetDate = LocalDate.parse(wechatInterface.getMeetDate(),DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        // 天气
        JSONObject weather = getWeather(wechatInterface.getCityCode());
        // 彩虹屁
        String chp = getChp();
        // 颜色
        String huise = getHuise();
        // 毒鸡汤
        String du = getDu();
        // 心情
        String[] split = HAPPY_STATE.split("、");
        String state = split[random.nextInt(split.length)];

        // 速配星座
        // String friend = constellation.getOrDefault("QFriend", "魔羯座").toString();

        // 星座
        JSONObject constellation = getConstellation(wechatInterface.getConstellation());
        // 根据星座预测
        String summary = constellation.getOrDefault("summary", "幸运值有点爆满哦").toString();
        // 更新日期
        templateMessage.addData(new WxMpTemplateData("time", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy年MM月dd日 HH:mm")), COLOR_LIST.get(random.nextInt(COLOR_LIST.size()))));
        // 早上好title
        templateMessage.addData(new WxMpTemplateData("title", wechatInterface.getTitle(), COLOR_LIST.get(random.nextInt(COLOR_LIST.size()))));
        // 心情
        templateMessage.addData(new WxMpTemplateData("state", state, COLOR_LIST.get(random.nextInt(COLOR_LIST.size()))));
        // 认识多久了
        templateMessage.addData(new WxMpTemplateData("day", LocalDate.now().toEpochDay() - meetDate.toEpochDay() + 1 + "", COLOR_LIST.get(random.nextInt(COLOR_LIST.size()))));
        // 第一次见面时间
        templateMessage.addData(new WxMpTemplateData("Firstmeeting", getfirstmeeting(firstmeeting), COLOR_LIST.get(random.nextInt(COLOR_LIST.size()))));
        // 生日倒计时
        templateMessage.addData(new WxMpTemplateData("birthdayDate",getbirthdayDate(birthdayDate),COLOR_LIST.get(random.nextInt(COLOR_LIST.size()))));
        // 城市
        templateMessage.addData(new WxMpTemplateData("city", wechatInterface.getCity(), COLOR_LIST.get(random.nextInt(COLOR_LIST.size()))));
        // weather方法接受
        // 接收天气
        // templateMessage.addData(new WxMpTemplateData("dayWeather", weather.getOrDefault("dayweather", "晴转多云" ).toString(), COLOR_LIST.get(random.nextInt(COLOR_LIST.size()))));
        templateMessage.addData(new WxMpTemplateData("dayWeather", weather.getOrDefault("dayweather" , weather ).toString(), COLOR_LIST.get(random.nextInt(COLOR_LIST.size()))));
        // 接收温度
        templateMessage.addData(new WxMpTemplateData("daytemp", weather.getOrDefault("daytemp", "").toString() + "℃", COLOR_LIST.get(random.nextInt(COLOR_LIST.size()))));

        // 喜欢的颜色
        templateMessage.addData(new WxMpTemplateData("color", constellation.getOrDefault("color", huise).toString(), COLOR_LIST.get(random.nextInt(COLOR_LIST.size()))));

        // constellationName.DATA && love.DATA
        templateMessage.addData(new WxMpTemplateData("constellationName", constellation.getOrDefault("name", "来自啊辉星球").toString(), COLOR_LIST.get(random.nextInt(COLOR_LIST.size()))));
        // templateMessage.addData(new WxMpTemplateData("love", constellation.getOrDefault("love", random.nextInt(100)).toString(), COLOR_LIST.get(random.nextInt(COLOR_LIST.size()))));
        templateMessage.addData(new WxMpTemplateData("love", constellation.getOrDefault("love", "100%").toString(), COLOR_LIST.get(random.nextInt(COLOR_LIST.size()))));

        // 速配星座
        // templateMessage.addData(new WxMpTemplateData("friend", friend.equals(personalInfo.getConstellation()) ? friend : friend + "（竟然不是我", COLOR_LIST.get(random.nextInt(COLOR_LIST.size()))));

        // 接收今天的你：
        templateMessage.addData(new WxMpTemplateData("summary", summary, COLOR_LIST.get(random.nextInt(COLOR_LIST.size()))));

        // 拆分发送
        if (summary.length() + chp.length() + du.length() > 130 && StrUtil.isNotEmpty(wechatConfigProperties.getSingleTemplateId())) {
            templateMessage.addData(new WxMpTemplateData("chp", "⬇⬇", COLOR_LIST.get(random.nextInt(COLOR_LIST.size()))));
            templateMessage.addData(new WxMpTemplateData("du", "⬇⬇", COLOR_LIST.get(random.nextInt(COLOR_LIST.size()))));
            sendMessage(templateMessage);
            WxMpTemplateMessage wxMpTemplateMessage = WxMpTemplateMessage.builder()
                    .toUser(templateMessage.getToUser())
                    .templateId(wechatConfigProperties.getSingleTemplateId())
                    .build();
            wxMpTemplateMessage.addData(new WxMpTemplateData("data1", "今日情话：" + chp, COLOR_LIST.get(random.nextInt(COLOR_LIST.size()))));
            wxMpTemplateMessage.addData(new WxMpTemplateData("data2", "一个毒鸡汤：" + du, COLOR_LIST.get(random.nextInt(COLOR_LIST.size()))));
            sendMessage(wxMpTemplateMessage);
            return false;
        } else {
            templateMessage.addData(new WxMpTemplateData("chp", "今日情话：" + chp, COLOR_LIST.get(random.nextInt(COLOR_LIST.size()))));
            templateMessage.addData(new WxMpTemplateData("du", "一个毒鸡汤：" + du, COLOR_LIST.get(random.nextInt(COLOR_LIST.size()))));
            return true;
        }
    }


    /**
     * 随机获取颜色
     * @return
     */
    private String getHuise(){
        String[] sthui = huise.split("\\n");
        return sthui[new Random().nextInt(sthui.length)].split("、")[1];
    }


    /**
     * 获取彩虹屁
     * @return
     */
    private String getChp() {
        try {
            String url = "https://api.shadiao.pro/chp";
            ResponseEntity<Object> forEntity = REST_TEMPLATE.getForEntity(url, Object.class);
            JSONObject jsonObject = JSON.parseObject(JSON.toJSONString(forEntity.getBody()));
            return jsonObject.getJSONObject("data").getString("text");
        } catch (Exception e) {
            log.error("获取彩虹屁失败", e);
            String[] split = CHP.split("\\n");
            return split[new Random().nextInt(split.length)].split("、")[1];
        }
    }

    /**
     * 获取毒鸡汤
     * @return
     */
    public String getDu() {
        try {
            String url = "https://api.shadiao.pro/du";
            ResponseEntity<Object> forEntity = REST_TEMPLATE.getForEntity(url, Object.class);
            JSONObject jsonObject = JSON.parseObject(JSON.toJSONString(forEntity.getBody()));
            return jsonObject.getJSONObject("data").getString("text");
        } catch (Exception e) {
            log.error("获取毒鸡汤失败", e);
            return "加油！你是最胖的！！！";
        }
    }
}
