package io.milkwang.framework.tracer;

import io.milkwang.util.common.IpUtils;
import io.milkwang.util.http.HttpHelper;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.net.URI;
import java.net.URLDecoder;

/**
 * 利用日志的MDC功能，提供统一线程内统一的日志前缀，方便追踪排障
 */
public class TracerUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(TracerUtils.class);
    public static final String MDC_KEY_MILK_CODE = "milkCode";
    public static final String MDC_KEY_REQUEST_ID = "milkRequestId";
    public static final String MDC_KEY_REFERER = "referer";
    public static final String MDC_KEY_IP = "ip";
    public static final String MDC_KEY_USERNAME = "username";

    /**
     * 设置MDC信息, 可以在每条日志中应用变量, 包含以下参数：<br/>
     * <pre>
     *     ip: 发起请求的ip地址, 用户真实ip
     *     referer: 发起请求的页面链接
     *     milkCode: 全局唯一码，从前端贯穿到整个请求链路，方便排障
     * </pre>
     *
     * @param servletRequest 请求
     */
    public static void putMDCInfoFromRequest(ServletRequest servletRequest) {
        try {
            HttpServletRequest request = (HttpServletRequest) servletRequest;
            String ip = IpUtils.getIpAddr(request);
            String page = "";
            String referer = request.getHeader(HttpHelper.REFERER_HEADER);
            String milkCode = request.getHeader(HttpHelper.MILK_CODE_HEADER);
            String requestId = request.getHeader(HttpHelper.MILK_REQUEST_ID_HEADER);
            String username = request.getHeader(HttpHelper.MILK_USER_NAME);
            if (requestId == null || requestId.length() == 0) {
                requestId = TraderCodeGenerator.generateRequestId();
            }
            if (referer != null) {
                URI url = URI.create(referer);
                page = url.getScheme() + "://" + url.getHost() + url.getPath();
            }
            // 添加MDC参数
            MDC.put(MDC_KEY_IP, ip);
            MDC.put(MDC_KEY_REFERER, page);
            MDC.put(MDC_KEY_MILK_CODE, milkCode);
            MDC.put(MDC_KEY_REQUEST_ID, requestId);
            if (StringUtils.isNotEmpty(username)) {
                MDC.put(MDC_KEY_USERNAME, URLDecoder.decode(username, "utf-8"));
            }
        } catch (Exception e) {
            LOGGER.error(e.getLocalizedMessage(), e);
        }
    }

    public static void clear() {
        try {
            MDC.clear();
        } catch (Exception e) {
            LOGGER.error(e.getLocalizedMessage(), e);
        }
    }

    public static String getMilkCode() {
        return MDC.get(MDC_KEY_MILK_CODE);
    }

    public static String getRequestId() {
        return MDC.get(MDC_KEY_REQUEST_ID);
    }

    public static String getMilkUsername() {
        return MDC.get(MDC_KEY_USERNAME);
    }
}
