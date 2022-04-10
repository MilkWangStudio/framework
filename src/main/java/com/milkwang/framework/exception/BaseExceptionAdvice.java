package com.milkwang.framework.exception;

import com.alibaba.fastjson.JSONException;
import io.gitee.tonggong.framework.rpc.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 基础的异常拦截，只能拦截到Controller这一层, 项目中可以继承这个Advice
 * <p/>
 * <b><i>Note:</i></b>
 * <ul>
 *     <li>useOutCode：如果为true，则返回code、message默认会使用外部值，否则会返回内部值</li>
 *     <li>响应使用{@link WebUtils#responseErrorCode}, 响应体为: ResponseBody{code: xxx, message: 'xxx'}</li>
 * </ul>
 */
public class BaseExceptionAdvice {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    public BaseExceptionAdvice() {

    }

    /**
     * 输出异常Code信息，子类可以调用这个方法来返回异常信息给调用方<br/>
     * {code:xxx,message:"xxxx"}
     *
     * @param res Response
     * @param e   异常参数
     */
    protected void printErrorCode(HttpServletResponse res, IErrorCode e) {
        logger.error("[[function=printErrorCode]] errorCode = {}, errorMessage = {}",
                e.getErrorCode(), e.getErrorMessage());
        WebUtils.responseErrorCode(res, e.getErrorCode(), e.getErrorMessage(), e.getShowType());
    }

    /**
     * 输出异常Code信息，子类可以调用这个方法来返回异常信息给调用方<br/>
     * {code:xxx,message:"xxxx"}
     *
     * @param res     Response
     * @param code    code
     * @param message message
     */
    protected void printErrorCode(HttpServletResponse res, String code, String message, ShowTypeEnum showType) {
        logger.error("[[function=printErrorCode]] errorCode = {}, errorMessage = {}, useOutCode = customCode", code, message);
        WebUtils.responseErrorCode(res, code, message, showType);
    }

    /**
     * 处理基础异常，项目中可以继承修改默认处理，或者定义子类异常的Handler区分处理异常
     *
     * @param req Request
     * @param res Response
     * @param e   基础异常
     */
    @ExceptionHandler(BaseException.class)
    public void handleBaseException(HttpServletRequest req, HttpServletResponse res, BaseException e) {
        printErrorCode(res, e);
    }

    /**
     * 处理参数异常
     *
     * @param req Request
     * @param res Response
     * @param e   参数异常
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public void handleIllegalArgumentException(HttpServletRequest req, HttpServletResponse res, IllegalArgumentException e) {
        logger.error(e.getLocalizedMessage(), e);
        printErrorCode(res, BaseErrorCode.ILLEGAL_ARGUMENT_ERROR.getErrorCode(), e.getLocalizedMessage(), BaseErrorCode.ILLEGAL_ARGUMENT_ERROR.getShowType());
    }

    /**
     * 处理JSON格式异常
     *
     * @param req Request
     * @param res Response
     * @param e   JSON格式异常
     */
    @ExceptionHandler(JSONException.class)
    public void handleJSONException(HttpServletRequest req, HttpServletResponse res, JSONException e) {
        logger.error(e.getLocalizedMessage(), e);
        printErrorCode(res, BaseErrorCode.JSON_FORMAT_ERROR);
    }

    /**
     * 处理Throwable异常
     *
     * @param req Request
     * @param res Response
     * @param e   Throwable异常
     */
    @ExceptionHandler(Throwable.class)
    public void handleThrowable(HttpServletRequest req, HttpServletResponse res, Throwable e) {
        logger.error(e.getLocalizedMessage(), e);
        printErrorCode(res, BaseErrorCode.SYSTEM_THROWABLE);
    }


}
