package io.milkwang.util.encryption;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * 对java8提供的Base64内部类：Encode和Decoder 的简单封装
 */
public class Base64Utils {
    private static final Charset charset = StandardCharsets.UTF_8;
    private static final Base64.Encoder encoder = Base64.getEncoder();
    private static final Base64.Decoder decoder = Base64.getDecoder();
    private static final Base64.Encoder urlEncoder = Base64.getUrlEncoder();
    private static final Base64.Decoder urlDecoder = Base64.getUrlDecoder();


    /**
     * 进行base64编码
     *
     * @param data 待编码byte数组
     * @return 编码后的byte数组
     */
    public static byte[] encode(byte[] data) {
        return encoder.encode(data);
    }

    /**
     * 默认按照UTF-8字符集进行编码，得到byte数组
     *
     * @param data 待编码字符串
     * @return 编码后的byte数组
     */
    public static byte[] encode(String data) {
        return encoder.encode(data.getBytes(charset));
    }

    /**
     * 按照指定字符集读取字符串byte数组，然后进行base64编码
     *
     * @param data    待编码字符串
     * @param charset 读取字符串的指定字符集
     * @return 编码后的byte数组
     */
    public static byte[] encode(String data, Charset charset) {
        return encoder.encode(data.getBytes(charset));
    }

    /**
     * 将byte数组进行base64编码
     *
     * @param data 待编码byte数组
     * @return 编码后的字符串
     */
    public static String encodeToString(byte[] data) {
        return encoder.encodeToString(data);
    }

    /**
     * 默认按照UTF-8的字符集读取字符串byte数组，然后进行编码
     *
     * @param data 待编码字符串
     * @return 编码后的字符串
     */
    public static String encodeToString(String data) {
        return encoder.encodeToString(data.getBytes(charset));
    }

    /**
     * 按照指定字符集读取byte数组，然后进行编码
     *
     * @param data    待编码字符串
     * @param charset 按照这个字符集读取字符串的byte数组
     * @return 编码后的字符串
     */
    public static String encodeToString(String data, Charset charset) {
        return encoder.encodeToString(data.getBytes(charset));
    }

    /**
     * 解码
     *
     * @param data 编码后字符串
     * @return 解码后的byte数组
     */
    public static byte[] decode(String data) {
        return decoder.decode(data);
    }

    /**
     * 解码后，按照指定字符集生成字符串
     *
     * @param data    编码后字符串
     * @param charset 字符集
     * @return 解码后字符串
     */
    public static String decodeToString(String data, Charset charset) {
        return new String(decode(data), charset);
    }

    /**
     * 将编码后字符串进行解码
     *
     * @param data 编码后的base64字符串
     * @return 解码后的原字符串
     */
    public static String decodeToString(String data) {
        return new String(decode(data), charset);
    }

    /**
     * 按照Url的特殊字符转换来编码base64
     *
     * @param data 编码前的字符串
     * @return 编码后的字符串
     */
    public static String encodeToStringUrl(String data) {
        return urlEncoder.encodeToString(data.getBytes(charset));
    }

    /**
     * 按照Url的特殊字符转换来解码base64
     *
     * @param data 编码后的字符串
     * @return 解码后的字符串
     */
    public static String decodeUrl(String data) {
        return new String(urlDecoder.decode(data), charset);
    }

}
