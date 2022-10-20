package com.huishao.config;

import java.util.ArrayList;
import java.util.List;


public class EmojiConfig {
    public static final List<String> Emoji_LIST = new ArrayList<>();

    /**
     * 把太大的颜文字拆分多个发送
     */
    public static List<String> splitExpression(String str) {
        int max = 200;
        String[] split = str.split("\\\n");
        List<String> list = new ArrayList<>();
        StringBuilder first = new StringBuilder();
        for (int i = 0; i < split.length; i++) {
            String s = split[i];
            if (first.length() + s.length() > max) {
                list.add(first + "\n");
                first = new StringBuilder(s).append("\n");
            } else {
                first.append(s).append("\n");
                if (i== split.length-1){
                    list.add(first.toString());
                }
            }
        }
        return list;
    }

    public static final String s1=
            "く__,.ヘヽ.　　　　/　,ー､ 〉\n" +
            "　　　＼ , !-─‐-i　/　/´\n" +
            "　　　 　 ／｀ｰ　　　 L/／｀ヽ､\n" +
            "　　 　 /　 ／,　 /|　 ,　 ,　　    ,\n" +
            "　　　ｲ 　/ /-‐/　ｉ　L_ ﾊ ヽ!　 i\n" +
            "　　　 ﾚ ﾍ 7ｲ｀ﾄ　 ﾚ-ﾄ､!ハ|　 |\n" +
            "　　　　 !,/7 ✪　　 ´i✪ﾊiソ| 　 |　　　\n" +
            "　　　　 |.从　　_　　 ,,,, / |./ 　 |\n" +
            "　　　　 ﾚ| i＞.､,,__　_,.イ / 　.i 　|\n" +
            "　　　 ﾚ| | / k_７_/ﾚヽ,　ﾊ.　|\n" +
            "　　　 | |/i 〈|/　 i　,.ﾍ |　i　|\n" +
            "　　　.|/ /　ｉ： 　 ﾍ!　　＼　|\n" +
            "　　　 　 　 kヽ､ﾊ 　 _,.ﾍ､ 　 /､!\n" +
            "　　　 !〈//｀Ｔ´, ＼ ｀7ｰr\n" +
            "　　　 ﾚヽL__|___i,___,ンﾚ|ノ\n" +
            "　　　 　　　ﾄ-,/　|___./\n" +
            "　　　 　　　ｰ　　!_,.:";

    public final static String s2=
            "/ ￣￣￣＼　 \n" +
            "|　ー　ー \\　 \n" +
            "|　 ◉　◉ |   / ￣￣￣￣￣￣＼\n" +
            "\\ 　 ▱　 / ∠ 今天早上，        \\ \n" +
            "  ＼       イ   \\   好好好呀      /\n" +
            " ／　   　＼    \\______________/\n" +
            "/　|　　  \\ \\　 \n" +
            "|　|　　　 | |";

    public final static String s3=
            "好看的人已经醒来~~~ \n" +
            "　　　∩∩\n" +
            "　　（´･ω･）\n" +
            "　  ＿|　⊃／(＿＿_\n" +
            "　／ └-(＿＿＿_／\n" +
            "　￣￣￣￣￣￣￣";

    public final static String s4=
            "┌─────────────────┐\n" +
            "　│通知：　       │\n" +
            "　│今天超级爱你    |\n" +
            "　│记得爱我!      │\n" +
            "　  (ﾖ─-∧＿∧─-E)\n" +
            "　  ＼（* ´∀｀）／\n" +
            "　 　 Y 　　　 Y";

    public final static String s5=
            "你是最胖的！！！！！！\n" +
            "　☆  *　.  　☆\n" +
            "　　. ∧＿∧　∩　* ☆\n" +
            "*  ☆ ( ・∀・)/ .\n" +
            "　.  ⊂　　 ノ* ☆\n" +
            "☆ * (つ ノ  .☆\n" +
            "　　 (ノ";

    public final static String s6=
            "　 睡醒了吗！　   |\n" +
            "＼/\n" +
            "　￣￣￣￣∨￣￣\n" +
            "  。\n" +
            "　　　∧ ∧　.・　\n" +
            "|￣￣( ´Д｀)￣|\n" +
            "|＼⌒⌒⌒⌒⌒⌒＼\n" +
            "|　 ＼⌒⌒⌒⌒⌒⌒＼\n" +
            "＼　｜⌒⌒⌒⌒⌒⌒⌒|\n" +
            "　 ＼|＿＿＿＿＿＿＿_|";

    public final static String s7=
            "＿＿＿＿＿\n" +
            "　　 ｜早上好  ｜\n" +
            "　　　￣￣∨￣￣\n" +
            "　　　　∧＿∧　\n" +
            "　　　 (´・ω⊂ヽ゛\n" +
            "　　　 /　　 _ノ⌒⌒ヽ\n" +
            "、(￣⊂人　 ノシ⌒　　ノ\n" +
            "⊂ニニニニニニニニニニニ⊃";

    public final static String s8=
            "   ＼　　／ \n" +
            "┏━━━━＼／━━━━━┓\n" +
            "┃┏━━━━━━━━━━┓ ┃\n" +
            "┃┃今天可以睡迟┃ ┃\n" +
            "┃┃一会会~~~~┃ ┃\n" +
            "┃┗━━━━━━━━━━┛ ┃\n" +
            "┗━━∪━━━━∪━━━";

    public final static String s9=
            "呼呼呼呼呼\n" +
            "好困啊...\n" +
            "　　　∧_∧__\n" +
            "　 ／(*´o｀)／＼\n" +
            "／|￣∪∪￣|＼／\n" +
            "　|＿＿ ＿|／";

    public final static String s10=
            "｡o◇☆οo｡\n" +
            "　　　 ｡◎∧_∧☆∂o\n" +
            "　　　｡○(*ﾟーﾟ)◇☆\n" +
            "　　　◎|￣∪∪￣￣|\n" +
            "　　／☆|超级开心！|\n" +
            "　▼　 ﾟoο◇☆＿＿|\n" +
            "∠▲――-☆∂o◎∂ﾟ";

    public final static String s11=
            "   ∧_∧　啪啪啪啪~起床起床\n" +
            "（ ・ω・)=つ≡つ\n" +
            "（っ ≡つ=つ\n" +
            "`/　　)\n" +
            "(ノΠＵ";

    public final static String s12=
            "再睡一会~再睡一会~\n" +
            "￣￣￣￣￣＼／￣￣￣￣ \n" +
            "　　　　∧＿∧　　　　 \n" +
            "　　;;（´・ω・） 　 \n" +
            "　＿旦_(っ(,,■)＿＿  \n" +
            "　|l￣l||￣しﾞしﾞ￣|i";

    public final static String s13=
            "✨  ︵ \n" +
            "(\"\\(●-●)\n" +
            " \\ /     0\\ \\\n" +
            "  (          )\"\n" +
            "  \\__T__/";

    public final static String s14=
            "。　Ｏ　　　ｏ\n" +
            "　 ｏ　　　。\n" +
            "○。\n" +
            "　　　早上好呀呀　 Ｏ\n" +
            "。　 　 ＿\n" +
            "　　 ,.'´　　｀゛、　ｏ\n" +
            "  　（ ＿´ ∀ ｀ _  ）";

    public final static String s15=
            "        ＿＿＿＿\n" +
            "　　 　 ‖　　　　|\n" +
            "　　 　 ‖起床起床|\n" +
            "　　 　 ‖　　　　|\n" +
            "　　 　 ‖￣￣￣￣\n" +
            "　 ∧＿∧\n" +
            "　(`･ω･‖\n" +
            "　丶　つ０\n" +
            "　 しーＪ";

    public final static String s16=
            "巴拉巴拉巴拉，把你变成猪！\n" +
            "　 ∧＿∧\n" +
            " （｡･ω･｡)つ━☆・*。\n" +
            "  ⊂　　 ノ 　　　・゜+.\n" +
            "　しーＪ　　　°。+ *´¨)\n" +
            "　　　       　　.· ´¸.·*´¨) ¸.·*¨)\n" +
            "     　(¸.·´ (¸.·’*";

    public final static String s17=
            "　 ﾍ^ヽ､　 /⌒､　　_,_\n" +
            "　 |　　￣7　 (⌒r⌒7/\n" +
            "　 レ　　　＼_/￣＼_｣\n" +
            "＿/　　　　　　　　 {\n" +
            "_ﾌ　●　　　　　　　ゝ\n" +
            "_人　　　ο　　●　 ナ\n" +
            "　 `ト､＿　　　　　メ\n" +
            "　　　 /　 ￣ ーィﾞ\n" +
            "　　 〈ﾟ･｡｡｡･ﾟ 　丶";

    public final static String s18=
            "　　　 ＿＿＿\n" +
            "　　 ／　　　▲\n" +
            "／￣　 ヽ　■■\n" +
            "●　　　　　■■\n" +
            "ヽ＿＿＿　　■■\n" +
            "　　　　）＝｜\n" +
            "　　　／　｜｜\n" +
            "　∩∩＿＿とﾉ\n" +
            "　しし———┘\n";

    public final static String s19=
            "起来摸鱼~起来摸鱼了~\n" +
            "￣￣￣￣￣＼／￣￣￣￣\n" +
            "　　　 　∧＿∧　　　　\n" +
            "　　;;（´・ω・） 　\n" +
            "　＿旦_(っ(,,■)＿＿\n" +
            "　|l￣l||￣しﾞしﾞ￣|i";
    public final static String s20=
            "::|＿_\n" +
            "::|　 ＼\n" +
            "::| 、＿ ＼\n" +
            "::|　(●) ＼\n" +
            "::|人_)⌒:: ｜\n" +
            "::|⌒´　 ／\n" +
            "(⌒ー―′ )\n" +
            "::| (起来了吗？)";
    public final static String s21=
            "\n" +
            "┳┻|\n" +
            "┻┳|\n" +
            "┳┻|__∧\n" +
            "┻┳|･ω･) ~起来了吗\n" +
            "┳┻|⊂ﾉ\n" +
            "┻┳|Ｊ";

    public final static String s22=
            "~~~~起床发呆中~~~~\n"+
            "╭﹌☆﹌﹌﹌☆﹌╮　\n" +
            "∣　　　　　　　∣\n" +
            "∣　●　　　●  ∣　\n" +
            "∣　　　▽　　　∣\n" +
            "╰————--—╯　\n" +
            "　∣　﹏　﹏　∣　　\n" +
            "　╰∪———∪╯";
    public final static String s23=
            "~~~~起床发呆中~~~~\n"+
            "◥◤~~~~◥◤　　\n" +
            "┃　　　　┃\n" +
            "≡━ ﹏ ━≡　　\n" +
            "┗━━┳∞┳━━┛\n" +
            "　┏┫　┣┓";

    public final static String s24=
            "{\\__/}\n" +
            "( • - •)\n" +
            "/つ\uD83D\uDDA4早上好";


    public final static String s25=
            "~~瞄 ／l、\n" +
            "    （ﾟ､ 。 ７\n" +
            "　   l、 ~ヽ\n" +
            "　   じしf_, )ノ";

    public final static String s26=
            "／ヽ　/ /⌒＼\n" +
            "／ ／ヽヽ|/⌒＼ii|＼\n" +
            "|／／ヾゞ//／＼＼| ＼\n" +
            "|／　　 |;;;;;;| 　 ＼|\n" +
            "　 　 　 |;;;;;;|\n" +
            "　 　 　 |;;;;;;|ﾍ⌒ヽﾌ\n" +
            "　 　 　 |;;;;;（　・ω・）\n" +
            "　 　 　 |;;;;⊂　　　｝\n" +
            "　 　 　 |;;;;⊂,____,ノｅ\n" +
            "　 　 　 |;;;;;;|\n" +
            "　 　 　 |;;;;;;|\n" +
            "　 　 　 |;;;;;;|  该爬树了，你起床了咩";


    public final static String s27=
            "|　 早安 　 |\n" +
            "＼　　　　　　　　/\n" +
            "　￣￣￣￣∨￣￣\n" +
            "　　　　　　　。\n" +
            "　　　∧ ∧　.・　\n" +
            "|￣￣( ´Д｀)￣|\n" +
            "|＼⌒⌒⌒⌒⌒⌒＼\n" +
            "|　 ＼⌒⌒⌒⌒⌒⌒＼\n" +
            "＼　｜⌒⌒⌒⌒⌒⌒⌒|\n" +
            "　 ＼|＿＿＿＿＿＿＿_|";

    public final static String s28=
            "　　 。*◇☆*。\n" +
            "　 ・*★◎○＠。*・\n" +
            "。◇＠★◎◇★。*\n" +
            "。☆◎。*☆◎。*・゜゜\n" +
            "　　 ＼　￣￣　／\n" +
            "　　　 ＼　　／\n" +
            "　 ∧_∧＼／\n" +
            "　( ・∀・)∞ (我话超多，起床后麻烦请找我)\n" +
            "　/ つ つ△\n" +
            "~(　 ノ\n" +
            "　ＵＵ";
    public final static String s29=
            "　　 ∧_∧\n" +
            "　　 ( ˘ω˘ )　　起床啦！\n" +
            "　 　|　⊃ ⊃\n" +
            "　　└-⊃～⊃\n" +
            "　　　\n" +
            "　　　｜｜　　　　　　　　\n" +
            "　 ＿ _　　／(＿＿_\n" +
            "／　 (＿＿＿_／　／";

    public final static String s30=
            "　　　／　　　 ／ |\n" +
            "　　 Γ￣￣￣￣ |　|\n" +
            "　　 |[]::　　　 |　|\n" +
            "　　 |＿＿_＿＿|　|\n" +
            "　　 |[]::　　　 |　|\n" +
            "　　 |＿＿_＿＿|　|\n" +
            "盯！ |＿＿_＿＿|　|\n" +
            " ！／(´･ω･)　／|　|\n" +
            "　Γ￣￣￣￣ |　|／\n" +
            "　Ｌ＿＿＿＿|／";

    public final static String s31=
            "//  程序出Bug了？     \n" +
            "// 　　　∩∩          \n" +
            "//   （´･ω･）       \n" +
            "//   ＿|　⊃／(＿＿_  \n" +
            "// 　／ └-(＿＿＿／  \n" +
            "// 　￣￣￣￣￣￣￣   \n" +
            "// 算了反正不是我写的  \n" +
            "// 　 ⊂⌒／ヽ-、＿   \n" +
            "//  ／⊂_/＿＿＿＿ ／\n" +
            "// 　￣￣￣￣￣￣￣  \n" +
            "// 万一是我写的呢   \n" +
            "//   　∩∩      \n" +
            "//   （´･ω･）   \n" +
            "//   ＿|　⊃／(＿＿_  \n" +
            "//  ／ └-(＿＿＿／ \n" +
            "//  ￣￣￣￣￣￣￣  \n" +
            "// 算了反正改了一个又出三个 \n" +
            "//    ⊂⌒／ヽ-、＿  \n" +
            "//  ／⊂_/＿＿＿＿ ／\n" +
            "//  ￣￣￣￣￣￣￣  ";


    public final static String s33=
            "♥*•♫.•*♥♥*•♫.•*♥♥*•♫.•*♥  \n" +
            "                          \n" +
            "   Happy Happy Happy      \n" +
            "                          \n" +
            "♥*•♫.•*♥♥*•♫.•*♥♥*•♫.•*♥  ";

    public final static String s34=
            "|￣￣￣￣￣￣￣￣￣￣￣|  \n" +
            "    能不能找我聊下天     \n" +
            "|＿＿＿＿＿＿＿＿＿＿＿|  \n" +
            "     \\ (•◡•) /         \n" +
            "      \\     /          \n" +
            "        ---            \n" +
            "       |   | ";


    static {

        Emoji_LIST.add(s1);
        Emoji_LIST.add(s2);
        Emoji_LIST.add(s3);
        Emoji_LIST.add(s4);
        Emoji_LIST.add(s5);
        Emoji_LIST.add(s6);
        Emoji_LIST.add(s7);
        Emoji_LIST.add(s8);
        Emoji_LIST.add(s9);
        Emoji_LIST.add(s10);
        Emoji_LIST.add(s11);
        Emoji_LIST.add(s12);
        Emoji_LIST.add(s13);
        Emoji_LIST.add(s14);
        Emoji_LIST.add(s15);
        Emoji_LIST.add(s16);
        Emoji_LIST.add(s17);
        Emoji_LIST.add(s18);
        Emoji_LIST.add(s19);
        Emoji_LIST.add(s20);
        Emoji_LIST.add(s21);
        Emoji_LIST.add(s22);
        Emoji_LIST.add(s23);
        Emoji_LIST.add(s24);
        Emoji_LIST.add(s25);
        Emoji_LIST.add(s26);
        Emoji_LIST.add(s27);
        Emoji_LIST.add(s28);
        Emoji_LIST.add(s29);
        Emoji_LIST.add(s30);
//        Emoji_LIST.add(s31);
        Emoji_LIST.add(s33);
        Emoji_LIST.add(s34);
    }
}
