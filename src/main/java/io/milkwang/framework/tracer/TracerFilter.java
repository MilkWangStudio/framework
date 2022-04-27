package io.milkwang.framework.tracer;

import javax.servlet.*;
import java.io.IOException;

/**
 * 全局追踪的Filter，从Request中拿到code，存到MDC里，后续从这里拿到进行打点
 */
public class TracerFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        /*
        1. 采集MDC变量
        2. 执行filterChain后续操作
        3. final 清理MDC的ThreadLocal
         */
        TracerUtils.putMDCInfoFromRequest(servletRequest);
        try {
            filterChain.doFilter(servletRequest, servletResponse);
        } finally {
            // 这是最后一层Filter，因此这里的MDC.clear一定可以清掉所有的数据
            TracerUtils.clear();
        }
    }

    @Override
    public void destroy() {
        TracerUtils.clear();
    }
}
