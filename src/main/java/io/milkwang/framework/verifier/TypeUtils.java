package io.milkwang.framework.verifier;

import org.apache.commons.lang3.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TypeUtils {
    /**
     * 是否是正整数
     *
     * @param number
     * @return
     */
    public static boolean isPositiveNumber(Number number) {
        return (number != null) && (number.intValue() > 0);
    }

    /**
     * Is not negative.
     *
     * @param number the number
     * @return the verifier
     */
    public static boolean isNotNegative(Number number) {
        return (number != null) && (number.intValue() >= 0);
    }

    /**
     * 手机正则表达式
     */
    private static final Pattern MOBILE_PTN = Pattern.compile("^1[3456789]\\d{9}$");

    /**
     * The constant IP_PTN.
     */
    private static final Pattern IP_PTN = Pattern.compile("^(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|[1-9])\\."
            + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
            + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
            + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)$");

    private static final Pattern IP_MASK_PTN = Pattern.compile("^(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|[1-9])\\."
            + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
            + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
            + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\/([0-9]|3[012]|[12][0-9])$");

    /**
     * Is mobile format.
     *
     * @param mobileString the mobile string
     * @return the verifier
     */
    public static boolean isMobile(String mobileString) {
        return (mobileString != null) && (MOBILE_PTN.matcher(mobileString).find());
    }

    /**
     * 护照 format
     */
    private static String PASSPORT_REGEX = "(?:^|[^A-Za-z0-9_.])(1[45]\\d{7}|[DGS]\\d{8}|[PS]\\.\\d{7})(?!\\w)";

    private static final Pattern PASSPORT_PIN = Pattern.compile(PASSPORT_REGEX);

    /**
     * 判断是不是护照
     *
     * @param passportStr
     * @return
     */
    public static boolean isPassport(String passportStr) {
        return (StringUtils.isNotBlank(passportStr) && (PASSPORT_PIN.matcher(passportStr).find()));
    }

    /**
     * Is IPv4 format.
     *
     * @param ip the ip
     * @return the verifier
     */
    public static boolean isIPV4(String ip) {
        return (ip != null) && (IP_PTN.matcher(ip).find());
    }


    public static String convertIPV4ToV6(String ip) {
        String[] split = ip.split("\\.");
        StringBuilder ipv6 = new StringBuilder(":");

        for (int i = 0; i < 4; i += 2) {
            int re = (Integer.valueOf(split[i]) << 8) + (Integer.valueOf(split[i + 1]));

            ipv6.append(":");
            ipv6.append(Integer.toHexString(re));
        }

        return ipv6.toString();
    }

    /**
     * Is IPv4 format.
     *
     * @param ip the ip
     * @return the verifier
     */
    public static boolean isIPNetwork(String ip) {
        return ((ip != null) && (IP_PTN.matcher(ip).find())) || ((ip != null) && (IP_MASK_PTN.matcher(ip).find()));
    }

    /**
     * email 正则表达式
     */
    private static final Pattern EMAIL_PTN = Pattern
            .compile("^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$");

    /**
     * Is mail format
     *
     * @param mailString the mail string
     * @return verifier
     */
    public static boolean isMail(String mailString) {
        return (mailString != null) && (EMAIL_PTN.matcher(mailString).find());
    }

    /**
     * 非中文正则表达式
     */
    private static final Pattern NOT_CHINA_NAME = Pattern
            .compile("^[a-zA-Z0-9_\\-\\.]*$");

    /**
     * not china name
     *
     * @param name
     * @return
     */
    public static boolean isNotChinaName(String name) {
        return (StringUtils.isNotBlank(name) && (NOT_CHINA_NAME).matcher(name).find());
    }

    /**
     * 判断是否为 int
     *
     * @param value
     * @return
     */
    public static boolean isInt(String value) {
        try {
            Integer.decode(value);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 校验银行卡卡号
     *
     * @param cardId
     * @return
     */
    public static boolean checkBankCard(String cardId) {
        char bit = getBankCardCheckCode(cardId.substring(0, cardId.length() - 1));
        if (bit == 'N') {
            return false;
        }
        return cardId.charAt(cardId.length() - 1) == bit;
    }

    /**
     * 从不含校验位的银行卡卡号采用 Luhm 校验算法获得校验位
     *
     * @param nonCheckCodeCardId
     * @return
     */
    public static char getBankCardCheckCode(String nonCheckCodeCardId) {
        if (nonCheckCodeCardId == null || nonCheckCodeCardId.trim().length() == 0
                || !nonCheckCodeCardId.matches("\\d+")) {
            //如果传的不是数据返回N
            return 'N';
        }
        char[] chs = nonCheckCodeCardId.trim().toCharArray();
        int luhmSum = 0;
        for (int i = chs.length - 1, j = 0; i >= 0; i--, j++) {
            int k = chs[i] - '0';
            if (j % 2 == 0) {
                k *= 2;
                k = k / 10 + k % 10;
            }
            luhmSum += k;
        }
        return (luhmSum % 10 == 0) ? '0' : (char) ((10 - luhmSum % 10) + '0');
    }

    private static int[] factors = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};
    private static char[] checkBits = {'1', '0', 'X', '9', '8', '7', '6', '5', '4', '3', '2'};
    private static String[] aCity = new String[]{null, null, null, null, null, null,
            null, null, null, null, null, "北京", "天津", "河北", "山西", "内蒙古", null,
            null, null, null, null, "辽宁", "吉林", "黑龙江", null, null, null, null,
            null, null, null, "上海", "江苏", "浙江", "安微", "福建", "江西", "山东", null,
            null, null, "河南", "湖北", "湖南", "广东", "广西", "海南", null, null, null,
            "重庆", "四川", "贵州", "云南", "西藏", null, null, null, null, null, null,
            "陕西", "甘肃", "青海", "宁夏", "新疆", null, null, null, null, null, "台湾",
            null, null, null, null, null, null, null, null, null, "香港", "澳门",
            null, null, null, null, null, null, null, null, "国外"};

    public static boolean CheckIdCardFormat(String idCardNo) {
        if (idCardNo.length() == 15) {
            idCardNo = per15To18(idCardNo);
        }
        if (idCardNo.length() != 18) {
            return false;
        }

        Pattern p = Pattern.compile("^\\d{17}(\\d|x|X)$");
        Matcher m = p.matcher(idCardNo);
        if (!m.matches()) {
            // return "格式不正确!";
            return false;
        }

        idCardNo = idCardNo.toUpperCase();
        if (aCity[Integer.parseInt(idCardNo.substring(0, 2))] == null) {
            // return "非法地区";
            return false;
        }
        try {
            SimpleDateFormat sFormat = new SimpleDateFormat("yyyy-MM-dd");
            sFormat.parse(idCardNo.substring(6, 10) + '-' + idCardNo.substring(10, 12) + '-' + idCardNo.substring(12, 14));
        } catch (ParseException e) {
            // return "非法生日";
            return false;
        }

        return checkLastBit(idCardNo);
    }

    private static boolean checkLastBit(String idCardNo) {
        if (idCardNo.length() != 18) {
            return false;
        }
        int sum = 0;
        for (int i = 0; i < 17; i++) {
            sum += charToInt(idCardNo.charAt(i)) * factors[i];
        }

        char mod = (char) (sum % 11);
        if (idCardNo.charAt(17) == checkBits[(int) mod]) {
            return true;
        }

        return false;
    }

    private static int charToInt(char c) {
        return c - 48;
    }

    private static String per15To18(String perIDSrc) {
        Integer iS = 0;
        // 新身份证号
        String perIDNew;
        perIDNew = perIDSrc.substring(0, 6);
        // 填在第6位及第7位上填上‘1’，‘9’两个数字
        perIDNew += "19";
        String sub = perIDSrc.substring(6, perIDSrc.length());
        perIDNew += sub;
        // 进行加权求和
        for (int i = 0; i < 17; i++) {
            iS += charToInt(perIDNew.charAt(i)) * factors[i];
        }
        // 取模运算，得到模值
        int iY = iS % 11;
        // 从checkBits中取得以模为索引号的值，加到身份证的最后一位，即为新身份证号。
        perIDNew += checkBits[iY];
        return perIDNew;
    }

    public static boolean checkRightUrl(String url) {
        //转换为小写
        url = url.toLowerCase();
        String regex = "^((https|http){1}://){1}"
                + "(([0-9a-z_!~*'().&=+$%-]+: )?[0-9a-z_!~*'().&=+$%-]+@)?"
                + "(([0-9]{1,3}\\.){3}[0-9]{1,3}"
                + "|"
                + "([0-9a-z_!~*'()-]+\\.)*"
                + "([0-9a-z][0-9a-z-]{0,61})?[0-9a-z]\\."
                + "[a-z]{2,6})"
                + "(:[0-9]{1,5})?"
                + "((/?)|"
                + "(/[0-9a-z_!~*'().;?:@&=+$,%#-]+)+/?)$";
        return url.matches(regex);
    }


}
