package io.milkwang.framework.exception;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import io.milkwang.framework.rpc.WebUtils;
import io.milkwang.framework.tracer.TracerUtils;
import org.apache.commons.lang3.StringUtils;
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
            HttpServletResponse response = (HttpServletResponse) servletResponse;
            if (BaseException.class.isAssignableFrom(e.getClass())) {
                BaseException baseException = (BaseException) e;
                WebUtils.responseErrorCode(response, baseException.getErrorCode(), baseException.getErrorMessage(), baseException.getShowType());
            } else {
                WebUtils.responseErrorCode(response,
                        "500",
                        StringUtils.isEmpty(e.getLocalizedMessage()) ? "系统异常，请联系客服" : e.getLocalizedMessage(),
                        ShowTypeEnum.error);
            }
        }
    }

    @Override
    public void destroy() {

    }
}
