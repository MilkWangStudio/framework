package io.milkwang.framework.rpc;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.milkwang.framework.exception.ShowTypeEnum;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.Writer;

/**
 *
 */
public class WebUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(WebUtils.class);


    /**
     * 将异常封装成正常的响应<br/>
     * <pre>
     * http status: 200
     * {
     *      code: xxxx,
     *      message: "err message"
     * }
     * </pre>
     *
     * @param servletResponse Response对象
     * @param code            业务Code
     * @param message         业务结果
     */
    public static void responseErrorCode(HttpServletResponse servletResponse, String code, String message, ShowTypeEnum showType) {
        WebUtils.responseJson(servletResponse, Result.error(code, message, showType));
    }

    /**
     * 返回业务信息 <br/>
     * <pre>
     * http status: 200
     * {
     *      code: 0,
     *      message: "success",
     *      result: data
     * }
     * </pre>
     *
     * @param response Response对象
     * @param data     返回值
     */
    public static void responseResult(HttpServletResponse response, Object data) {
        WebUtils.responseJson(response, Result.create(data));
    }

    /**
     * 进行重定向处理
     *
     * @param response Response
     * @param url      url
     */
    public static void redirect(HttpServletResponse response, String url) {
        try {
            response.sendRedirect(url);
        } catch (Exception e) {
            LOGGER.error(e.getLocalizedMessage(), e);
        }
    }

    /**
     * 输出json类型返回值
     *
     * @param response Response
     * @param json     Object
     */
    public static void responseJson(HttpServletResponse response, Object json) {
        Writer writer = null;
        try {
            response.setCharacterEncoding("utf-8");
            response.setContentType("application/json; charset=utf-8");
            writer = response.getWriter();
            writer.write(JSON.toJSONString(json));
        } catch (Exception e) {
            LOGGER.error(e.getLocalizedMessage(), e);
        } finally {
            if (writer != null) {
                IOUtils.closeQuietly(writer);
            }
        }
    }

    public static JSONObject parseRequestBody(HttpServletRequest request) {
        String reqBody = null;
        try {
            BufferedReader reader = request.getReader();
            StringBuilder builder = new StringBuilder();
            String line = reader.readLine();
            while (line != null) {
                builder.append(line);
                line = reader.readLine();
            }
            reader.close();
            reqBody = builder.toString();
            return JSONObject.parseObject(reqBody);
        } catch (Exception e) {
            LOGGER.error(e.getLocalizedMessage() + "reqBody:" + reqBody, e);
        }
        return null;
    }

    public static void responseText(HttpServletResponse response, String result) {
        PrintWriter writer = null;

        try {
            response.setCharacterEncoding("utf-8");
            response.setContentType("text/plain;charset=UTF-8");
            writer = response.getWriter();
            writer.write(result);
        } catch (Exception var7) {
            LOGGER.error(var7.getLocalizedMessage(), var7);
        } finally {
            if (writer != null) {
                IOUtils.closeQuietly(writer);
            }
        }
    }
}
