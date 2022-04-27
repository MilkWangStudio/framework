package io.milkwang.framework.tracer;


import io.milkwang.util.common.DateUtils;
import io.milkwang.util.common.TextUtils;

import java.util.Date;

/**
 * 生成追踪用的全局Code
 */
public class TraderCodeGenerator {
    /**
     * 生成请求requestId，一共32位，14(时间)+18(随机字符)
     */
    public static String generateRequestId() {
        return DateUtils.format(new Date(), DateUtils.formatterNoSplitDateTime) + TextUtils.randomString(18);
    }
}
