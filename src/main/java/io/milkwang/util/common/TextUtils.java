package io.milkwang.util.common;

import com.google.common.base.Splitter;
import org.apache.commons.lang3.RegExUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class TextUtils {

    public static final String BASE_RANDOM_STRING = "abcdefghijklmnopqrstuvwxyz0123456789";
    public static final String BASE_RANDOM_NUMBER = "0123456789";
    public static final String BASE_RANDOM_NO_SPECIAL = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    public static final String BASE_RANDOM_SPECIAL = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz!#%^&_+{}?,";


    /**
     * 对在占位符进行替换，content中的占位符需要用大括号括起来，例如：{key}
     *
     * @param content  你好,{name} \n 欢迎报名{lessonPackageName}!
     * @param replacer {name:'张三',lessonPackageName:'圈外商学院'}
     * @return 你好, 张三 \n 欢迎报名圈外商学院!
     */
    public static String placeholderReplace(String content, Map<String, String> replacer) {
        if (StringUtils.isNotEmpty(content) && replacer != null) {
            for (Map.Entry<String, String> entry : replacer.entrySet()) {
                content = StringUtils.replace(content, "{" + entry.getKey() + "}", entry.getValue());
            }
        }
        return content;
    }

    /**
     * 生成随机字符串，数字+字母(小写)
     *
     * @param length 指定长度
     * @return 随机字符串
     */
    public static String randomString(int length) {
        return random(BASE_RANDOM_STRING, length);
    }

    /**
     * 生成随机字符串，纯数字
     *
     * @param length 指定长度
     * @return 随机字符串
     */
    public static String randomNumber(int length) {
        return random(BASE_RANDOM_NUMBER, length);
    }

    /**
     * 生成随机字符串，没有特殊字符，有大小写字母、数字
     *
     * @param length 指定长度
     * @return 随机字符串
     */
    public static String randomNoSpecial(int length) {

        return random(BASE_RANDOM_NO_SPECIAL, length);
    }

    /**
     * 生成随机字符串， 有特殊字符、大小写字母、数字
     *
     * @param length 指定长度
     * @return 随机字符串
     */
    public static String randomSpecial(int length) {
        return random(BASE_RANDOM_SPECIAL, length);
    }

    /**
     * 生成随机字符串，根据指定的文本
     *
     * @param base   指定文本字符
     * @param length 指定长度
     * @return 随机字符串
     */
    public static String random(String base, int length) {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

    /**
     * 去掉文本中的style标签
     *
     * @param content html文本
     * @return 去掉style标签后的文本
     */
    public static String removeStyle(String content) {
        if (content == null) {
            return null;
        }
        // 正则表达式
        String regEx = " style=\"(.*?)\"";
        String regEx2 = " style='(.*?)'";
        Pattern p = Pattern.compile(regEx);
        Pattern p2 = Pattern.compile(regEx2);
        Matcher m = p.matcher(content);
        String okContent = null;
        if (m.find()) {
            okContent = m.replaceAll("");
        } else {
            okContent = content;
        }
        Matcher m2 = p2.matcher(okContent);
        String result = null;
        if (m2.find()) {
            result = m2.replaceAll("");
        } else {
            result = okContent;
        }
        return result;
    }

    /**
     * 去掉所有HTML标签
     *
     * @param html html文本
     * @return 去掉html标签后的纯文本
     */
    public static String removeHTMLTag(String html) {
        if (html == null) {
            return "";
        }
        return RegExUtils.removePattern(html, "<[^>]*>");
    }

    /**
     * 检查是否是emoji字符
     *
     * @param codePoint 字符
     * @return 是否是emoji字符
     */
    public static boolean isEmojiCharacter(char codePoint) {
        return (codePoint == 0x0) ||
                (codePoint == 0x9) ||
                (codePoint == 0xA) ||
                (codePoint == 0xD) ||
                ((codePoint >= 0x20) && (codePoint <= 0xD7FF)) ||
                ((codePoint >= 0xE000) && (codePoint <= 0xFFFD)) ||
                ((codePoint >= 0x10000) && (codePoint <= 0x10FFFF));
    }

    /**
     * 过滤emoji 或者 其他非文字类型的字符
     */
    public static String filterEmoji(String source) {
        if (source == null) {
            return source;
        }
        //到这里铁定包含
        StringBuilder buf = null;
        int len = source.length();
        for (int i = 0; i < len; i++) {
            char codePoint = source.charAt(i);
            if (isEmojiCharacter(codePoint)) {
                if (buf == null) {
                    buf = new StringBuilder(source.length());
                }
                buf.append(codePoint);
            }
        }
        if (buf == null) {
            return source;//如果没有找到 emoji表情，则返回源字符串
        } else {
            if (buf.length() == len) {//这里的意义在于尽可能少的toString，因为会重新生成字符串
                return source;
            } else {
                return buf.toString();
            }
        }
    }

    /**
     * 将一段文本，按照每段多少字，分割为多段
     *
     * @param str 文本值
     * @param len 每段多少个字
     * @return 分割后的数组
     */
    public static String[] splitStrByLineLength(String str, int len) {
        int splitSum = (str.length() - 1) / len;

        String[] splitStr = new String[splitSum + 1];

        for (int i = 0; i < splitSum; i++) {
            splitStr[i] = str.substring(len * i, len * (i + 1));
        }
        splitStr[splitSum] = str.substring(len * splitSum);

        return splitStr;
    }

    /**
     * 计算一段中英文混杂的文本中，字符长度为limit时，String的index是多少<p/>
     * 例如：你好lilyth,我是小明<br/>
     * limit为5,则index = 3<br/>
     * 再调用, "你好lilyth,我是小明".substring(0,index)即可拿到适配长度的文本
     *
     * @param str   例如: 你好lilyth,我是小明
     * @param limit 例如: 5
     * @return 例如: 你好l
     */
    public static Integer splitIndex(String str, Integer limit) {
        int count = 0;

        Integer index = 0;

        if (str == null) {
            return -1;
        }
        for (char c : str.toCharArray()) {
            index++;
            if (c >= 0x4E00 && c <= 0x9FA5) {
                count = count + 2;
            } else {
                count = count + 1;
            }
            if (count >= limit) {
                return index;
            }
        }
        return index;
    }

    /**
     * 将所有\n ，换行符替换为p标签
     *
     * @param content 普通文本
     * @return 替换p标签后的文本
     */
    public static String addPTag(String content) {
        if (content == null) {
            return null;
        }
        return content.replaceAll("\n", "<p/>");
    }

    /**
     * 判断字符串内是否有中文
     *
     * @param str 文本字符串
     * @return 是否有中文
     */
    public static boolean hasChinese(String str) {
        if (str == null) {
            return false;
        }
        for (char c : str.toCharArray()) {
            if (c >= 0x4E00 && c <= 0x9FA5) {
                return true;
            }
        }
        return false;
    }


    /**
     * 将字符串按逗号分隔后，再转换为整型数组
     *
     * @param splitStr 逗号拼接的字符串
     * @return 整型数组
     */
    public static List<Integer> spiltCommaToListInt(String splitStr) {
        if (StringUtils.isBlank(splitStr)) {
            return new ArrayList<>();
        }
        Iterable<String> iterable = Splitter.on(',')
                .trimResults()
                .omitEmptyStrings()
                .split(splitStr);
        return StreamSupport.stream(iterable.spliterator(), false).map(Integer::valueOf)
                .collect(Collectors.toList());
    }

    /**
     * 将字符串按照分隔符进行分割
     *
     * @param splitStr 分隔符
     * @return 字符数组
     */
    public static List<String> spiltCommaToList(String splitStr) {
        if (StringUtils.isBlank(splitStr)) {
            return new ArrayList<>();
        }
        Iterable<String> iterable = Splitter.on(',')
                .trimResults()
                .omitEmptyStrings()
                .split(splitStr);
        return StreamSupport.stream(iterable.spliterator(), false)
                .collect(Collectors.toList());
    }

    /**
     * 将字符串按逗号分隔后，再转换为整型Set
     *
     * @param splitStr 逗号拼接的字符串
     * @return 整型Set
     */
    public static Set<Integer> spiltCommaToSet(String splitStr) {
        if (StringUtils.isBlank(splitStr)) {
            return new HashSet<>();
        }
        Iterable<String> iterable = Splitter.on(',')
                .trimResults()
                .omitEmptyStrings()
                .split(splitStr);
        return StreamSupport.stream(iterable.spliterator(), false).map(id -> Integer.valueOf(id))
                .collect(Collectors.toSet());
    }

    /**
     * 将字符串按照分隔符分隔，并返回迭代器
     *
     * @param splitStr 分隔符
     * @return 迭代器
     */
    public static Iterable<String> spiltComma(String splitStr) {
        if (StringUtils.isBlank(splitStr)) {
            return new ArrayList<>();
        }
        return Splitter.on(',')
                .trimResults()
                .omitEmptyStrings()
                .split(splitStr);
    }


    /**
     * 对Map数据排序，对key做字母序升序
     *
     * @param params:签名字符串
     * @return 排序后的Map
     */
    public static List<Map.Entry<String, String>> sortMap(Map<String, String> params) {
        List<Map.Entry<String, String>> infos = new ArrayList<>(params.entrySet());
        infos.sort(Map.Entry.comparingByKey());
        return infos;
    }

    /**
     * 将排序后的参数与其对应值，组合成“参数=参数值”的格式，并且把这些参数用&字符连接起来
     *
     * @param params 参数数组
     * @return 签名
     */
    public static String sortMapString(Map<String, String> params) {
        //转译
        List<Map.Entry<String, String>> listEncode = sortMap(params);
        StringBuilder strBuilder = new StringBuilder();
        //拼接参数字符串
        Iterator<Map.Entry<String, String>> iterator = listEncode.iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, String> next = iterator.next();
            strBuilder.append(next.getKey())
                    .append("=")
                    .append(next.getValue());
            if (iterator.hasNext()) {
                strBuilder.append("&");
            }
        }
        return strBuilder.toString();
    }
}
