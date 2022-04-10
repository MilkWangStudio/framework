package com.milkwang.framework.exception;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import io.gitee.tonggong.framework.tracer.TracerUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

/**
 * 做最后兜底的filter, 一般来说只有UI层需要，避免用户看到Tomcat的报错页面
 *
 * @author nethunder
 * @date 2019/10/31
 */
public class ExceptionFilter implements Filter {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        try {
            filterChain.doFilter(servletRequest, servletResponse);
        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
            try {
                HttpServletResponse response = (HttpServletResponse) servletResponse;
                response.setContentType("application/json;charset=utf-8");
                PrintWriter writer = response.getWriter();
                Map<String, Object> json = Maps.newHashMap();
                json.put("errorCode", 500);
                json.put("errorMessage", "服务器伐开心,我们正在想办法");
                json.put("success", false);
                json.put("showType", ShowTypeEnum.error.getType());
                json.put("traceId", TracerUtils.getIqwRequestId());
                writer.write(JSON.toJSONString(json));
                writer.close();
                response.flushBuffer();
            } catch (Exception e1) {
                logger.error(e.getLocalizedMessage(), e1);
            }
        }
    }

    @Override
    public void destroy() {

    }
}
