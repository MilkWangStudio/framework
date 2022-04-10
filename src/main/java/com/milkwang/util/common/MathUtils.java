package com.milkwang.util.common;

import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Objects;

public class MathUtils {
    public static DecimalFormat priceFormat;

    static {
        priceFormat = new DecimalFormat("0.00");
        priceFormat.setRoundingMode(RoundingMode.HALF_UP);
    }

    /**
     * 减法, a - b后保留两位小数
     */
    public static Double substract(Double a, Double b) {
        if (a == null || b == null) {
            return null;
        }

        return new BigDecimal(a).subtract(new BigDecimal(b)).
                setScale(2, BigDecimal.ROUND_HALF_DOWN).doubleValue();
    }

    /**
     * 将金额格式化成0.00的文本
     *
     * @param fee 单位元
     * @return 格式化后的文本
     */
    public static String formatPrice(Double fee) {
        return priceFormat.format(fee);
    }

    /**
     * 加法 保留两位小数
     */
    public static Double add(Double a, Double b) {
        if (a == null) {
            a = 0d;
        }
        if (b == null) {
            b = 0d;
        }
        return new BigDecimal(a).add(new BigDecimal(b)).
                setScale(2, BigDecimal.ROUND_HALF_DOWN).doubleValue();
    }


    /**
     * 数字number是否在两者之间，闭区间
     *
     * @param number 待检查数字
     * @param lower  较低的值
     * @param upper  较高的值
     * @return 检查结果
     */
    public static boolean isBetween(double number, double lower, double upper) {
        return lower <= number && number <= upper;
    }

    /**
     * 格式化为百分数形式, 最多精确到0.01，即1%
     *
     * @param percent 例:0.3123
     * @return 31%
     */
    public static String toPercent(Double percent) {
        return toPercent(percent, "0%");
    }

    /**
     * 按照格式输出浮点型
     *
     * @param percent 浮点型
     * @param format  文本格式, 例如: 0%、0.00%
     * @return 格式化文本
     */
    public static String toPercent(Double percent, String format) {
        DecimalFormat df = new DecimalFormat(format);
        return df.format(percent);
    }

    /**
     * 将浮点数向上取整，输出为整型
     *
     * @param number 浮点数
     * @return 整型
     */
    public static Integer ceilToInt(Double number) {
        return Double.valueOf(Math.ceil(number)).intValue();
    }

    /**
     * 在区间内进行随机,大于等于最小值，小于最大值<br/>
     * 保留x小数，后面的小数直接去掉
     *
     * @param min   最小值
     * @param max   最大值
     * @param scale 保留几位小数
     * @return 随机值
     */
    public static Double randomScale(Double min, Double max, Integer scale) {
        return new BigDecimal(Math.random() * (max - min) + min).setScale(scale, BigDecimal.ROUND_DOWN).doubleValue();
    }

    /**
     * 在区间内进行随机,大于等于最小值，小于最大值<br/>
     * 保留两位小数，后面的小数直接去掉
     *
     * @param min 最小值
     * @param max 最大值
     * @return 随机值
     */
    public static Double random(Double min, Double max) {
        return randomScale(min, max, 2);
    }

    /**
     * 升序排列，如果希望降序排列: -sortedAsc(o1,o2)
     *
     * @param o1 数字A
     * @param o2 数字B
     * @return -1不换位置，0不换位置，1换位置
     */
    public static Integer sortedAsc(Number o1, Number o2) {
        if (Objects.equals(o1, o2)) {
            return 0;
        }
        if (o1 == null) {
            // o1是null则不换位置
            return -1;
        }
        if (o2 == null) {
            return 1;
        }
        if (o1.doubleValue() > o2.doubleValue()) {
            // o1大于o2则换位置
            return 1;
        } else {
            // o1不大于o2则不换位置
            return -1;
        }
    }

    /**
     * 判断是否正整数，正整数不包含0
     *
     * @param num 数字文本
     * @return 是否正整数
     */
    public static Boolean isPositiveInteger(String num) {
        if (StringUtils.isEmpty(num)) {
            return false;
        }
        for (int i = 0; i < num.length(); i++) {
            char c = num.charAt(i);
            if (c < '0' || c > '9') {
                return false;
            }
        }
        return true;
    }
}
