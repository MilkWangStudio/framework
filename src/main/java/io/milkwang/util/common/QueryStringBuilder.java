package io.milkwang.util.common;

import com.google.common.collect.Lists;
import io.milkwang.util.http.URLEncodedUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 对HTTP请求的链接进行处理，方便构建url
 */
public class QueryStringBuilder {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final String uri;

    private final List<NameValuePair> params;

    /**
     * 构建请求信息<br/>
     * https://www.baidu.com/path/test?key1=fwefe&key2=1232 <br/>
     * 会拆分成uri: https://www.baidu.com/path/test <br/>
     * params: key1=fwefe&key2=1232
     *
     * @param url 请求链接
     */
    public static QueryStringBuilder create(String url) {
        return new QueryStringBuilder(url);
    }

    /**
     * 构建请求信息<br/>
     * https://www.baidu.com/path/test?key1=fwefe&key2=1232 <br/>
     * 会拆分成uri: https://www.baidu.com/path/test <br/>
     * params: key1=fwefe&key2=1232
     *
     * @param url 请求链接
     */
    private QueryStringBuilder(String url) {
        List<NameValuePair> params = Lists.newArrayList();
        String uri;
        if (url.contains("?")) {
            uri = url.substring(0, url.indexOf("?"));
            String queryString = url.substring(url.indexOf("?") + 1);
            String[] paramPairs = queryString.split("&");
            for (String pair : paramPairs) {
                String[] keyValue = pair.split("=");
                if (keyValue.length != 2) {
                    continue;
                }
                params.add(new BasicNameValuePair(keyValue[0], keyValue[1]));
            }
        } else {
            uri = url;
        }
        this.uri = uri;
        this.params = params;
    }

    /**
     * 添加queryString参数
     *
     * @param key   queryStringKey
     * @param value queryStringValue
     * @return QueryStringBuilder
     */
    public QueryStringBuilder append(String key, Object value) {
        if (value instanceof Iterable) {
            Iterable<?> list = (Iterable<?>) value;
            for (Object item : list) {
                String itemValue;
                if (item instanceof String) {
                    itemValue = (String) item;
                } else {
                    itemValue = String.valueOf(item);
                }
                params.add(new BasicNameValuePair(key, itemValue));
            }
            return this;
        }
        params.add(new BasicNameValuePair(key, String.valueOf(value)));
        return this;
    }

    /**
     * 拼接参数
     *
     * @param params {key:value}
     * @return QueryStringBuilder
     */
    public QueryStringBuilder append(Map<String, String> params) {
        List<Map.Entry<String, String>> listEncode = TextUtils.sortMap(params);
        //拼接参数字符串
        for (Map.Entry<String, String> next : listEncode) {
            String key = next.getKey();
            String value = next.getValue();
            if (key == null || value == null) {
                logger.error(String.format("[[function=append]] can not appendParam, key or value is null, key = %s , value = %s", key, value));
                continue;
            }
            this.append(key, value);
        }
        return this;
    }

    /**
     * 去掉某个参数名
     *
     * @param key queryStringKey
     * @return QueryStringBuilder
     */
    public QueryStringBuilder remove(String key) {
        this.params.removeIf(pair -> Objects.equals(pair.getName(), key));
        return this;
    }

    /**
     * 输出成正常url
     *
     * @return 链接
     */
    @Override
    public String toString() {
        try {
            if (CollectionUtils.isEmpty(params)) {
                return uri;
            }
            return uri + "?" + URLEncodedUtils.format(params, "utf-8");
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
            return null;
        }
    }


}
