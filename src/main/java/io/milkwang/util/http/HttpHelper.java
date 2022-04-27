package io.milkwang.util.http;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * 圈外Http请求辅助工具
 */
public class HttpHelper {
    private static final Logger logger = LoggerFactory.getLogger(HttpHelper.class);
    private static final int CONNECTION_TIMEOUT = 1;
    private static final int READ_TIMEOUT = 60;
    private static final OkHttpClient CLIENT = new OkHttpClient.Builder()
            .connectTimeout(CONNECTION_TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
            .build();
    /**
     * 用户名
     */
    public static final String IQW_USER_NAME = "Iqw-Username";
    /**
     * 每一次请求会生成一个的RequestId
     */
    public final static String IQW_REQUEST_ID_HEADER = "Iqw-Request-id";
    /**
     * 用户唯一的Code,登陆后写到设备上作为指纹码存在
     */
    public final static String IQW_CODE_HEADER = "Iqw-Code";
    /**
     * 请求时的Referer地址
     */
    public final static String REFERER_HEADER = "Referer";

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    public static final MediaType XML = MediaType.parse("text/xml; charset=utf-8");
    public static final String FROM = "application/x-www-form-urlencoded;charset=UTF-8";

    /**
     * 请求Builder类
     */
    private final Request.Builder builder;
    private String url;
    private Response response;
    private Throwable throwable;
    private Consumer<HttpHelper> beforeExecute;

    /**
     * 构建辅助类
     *
     * @param builder 请求Builder
     */
    private HttpHelper(Request.Builder builder) {
        this.builder = builder;
    }


    /**
     * 创建辅助类
     *
     * @param url 请求Url
     * @return 辅助类
     */
    public static HttpHelper create(String url) {
        HttpHelper helper = new HttpHelper(new Request.Builder().url(url));
        helper.url = url;
        return helper;
    }

    public HttpHelper setBeforeExecute(Consumer<HttpHelper> beforeExecute) {
        this.beforeExecute = beforeExecute;
        return this;
    }

    private static String getValueEncoded(String value) {
        if (value == null) return "null";
        String newValue = value.replace("\n", "");
        for (int i = 0, length = newValue.length(); i < length; i++) {
            char c = newValue.charAt(i);
            if (c > '\u001f' && c < '\u007f') {
                continue;
            }
            try {
                return URLEncoder.encode(newValue, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                logger.error(e.getLocalizedMessage(), e);
            }
        }
        return newValue;
    }

    /**
     * 添加请求头
     *
     * @param key   HeaderKey
     * @param value HeaderValue
     * @return 辅助类
     */
    public HttpHelper addHeader(String key, String value) {
        this.builder.addHeader(key, getValueEncoded(value));
        return this;
    }

    /**
     * 移除Header
     *
     * @param key HeaderKey
     * @return 辅助类
     */
    public HttpHelper removeHeader(String key) {
        this.builder.removeHeader(key);
        return this;
    }

    /**
     * 执行Get请求
     *
     * @return 执行结果
     */
    public HttpHelper methodGet() {
        this.builder.get();
        return this;
    }

    public static RequestBody createBody(MediaType mediaType, String content) {
        return RequestBody.create(JSON, content);
    }

    public HttpHelper method(String method, RequestBody body) {
        this.builder.method(method, body);
        return this;
    }


    /**
     * 执行Post请求，参数是JSON形式的RequestBody
     *
     * @param json json形式参数，序列化工具建议用FastJson
     * @return 执行结果
     */
    public HttpHelper methodPostWithBody(String json) {
        this.builder.post(RequestBody.create(JSON, json));
        return this;
    }

    /**
     * 执行Post请求，参数是Form表单形式的RequestBody
     *
     * @param params Form表单
     * @return 执行结果
     */
    public HttpHelper methodPostWithForm(Map<String, String> params) {
        FormBody.Builder fromBuilder = new FormBody.Builder();
        params.keySet().forEach(key -> fromBuilder.add(key, params.get(key)));
        this.builder.addHeader("Content-Type", FROM);
        this.builder.post(fromBuilder.build());
        return this;
    }


    /**
     * 执行Put请求，参数是JSON形式的RequestBody
     *
     * @param json json形式参数，序列化工具建议用FastJson
     * @return 执行结果
     */
    public HttpHelper methodPut(String json) {
        this.builder.put(RequestBody.create(JSON, json));
        return this;
    }


    /**
     * 构建请求信息
     *
     * @return 请求信息
     */
    public HttpHelper execute() {
        if (this.beforeExecute != null) {
            this.beforeExecute.accept(this);
        }
        Request request = this.builder.build();
        try {
            this.response = CLIENT.newCall(request).execute();
        } catch (Exception e) {
            this.throwable = e;
        }
        return this;
    }


    /**
     * 获取原始的响应体
     *
     * @return 返回值信息
     * @throws Throwable 请求异常
     */
    public Response getResponse() throws Throwable {
        if (response == null && throwable == null) {
            this.execute();
        }
        if (throwable != null) {
            throw throwable;
        }
        return response;
    }

    /**
     * 获取原始响应体，并不会抛出异常，就算请求失败，返回值也只是null
     *
     * @return 返回值信息
     */
    public Response getResponseNoThrow() {
        try {
            return getResponse();
        } catch (Throwable e) {
            logger.error(e.getLocalizedMessage(), e);
        }
        return response;
    }

    /**
     * 根据圈外的接口规则，将返回值进行封装
     *
     * @return Http响应体
     */
    public String result() throws Throwable {
        if (throwable == null && response == null) {
            this.execute();
        }
        if (throwable != null) {
            throw throwable;
        }
        String result;
        try {
            result = response.body().string();
        } catch (IOException e) {
            logger.error("[[function=execute]] exception e={}", throwable.getMessage());
            return null;
        }
        return result;
    }


    /**
     * 根据圈外的接口规则，将返回值进行封装
     *
     * @return Http响应体
     */
    public String resultNoThrow() {
        try {
            return result();
        } catch (Throwable e) {
            logger.error("[[function=execute]] exception e={}", throwable.getMessage());
            return null;
        }
    }

    public <T> T getData(Class<T> clazz) {
        String result = resultNoThrow();
        return JSONObject.parseObject(result, clazz);
    }

    public <T> List<T> getList(Class<T> clazz) {
        String result = resultNoThrow();
        return JSONArray.parseArray(result, clazz);
    }

    public JSONObject getData() {
        String result = resultNoThrow();
        return JSONObject.parseObject(result);
    }

    public JSONArray getList() {
        String result = resultNoThrow();
        return JSONArray.parseArray(result);
    }
}
