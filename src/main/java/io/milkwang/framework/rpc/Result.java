package io.milkwang.framework.rpc;


import com.google.common.collect.Lists;
import io.milkwang.framework.exception.IErrorCode;
import io.milkwang.framework.exception.ShowTypeEnum;
import io.milkwang.framework.tracer.TracerUtils;

import java.util.List;

/**
 * 封装Controller的返回值
 * <p/>
 * <pre>
 * HttpStatus 200
 * ResponseBody {
 *     showType: 0,
 *     errorCode: 0,
 *     errorMessage: "ok",
 *     data: data
 * }
 * </pre>
 * <b><i>Note:</i></b>
 * <ul>
 *     <li>由于Result继承了LinkedHashMap, 因此可以在data同级再增加附加参数返回</li>
 *     <li>errorCode/errorMessage/data 这三个属于保留参数，绝对不能通过put来添加！</li>
 *     <li>一旦对errorCode/errorMessage/data进行了修改，只保证在MVC配置了使用FastJSON来输出的情况下可以符合预期，输出修改后的值。</li>
 * </ul>
 *
 * @author nethunder
 */
public class Result<T> {
    /**
     * if request is success
     */
    private Boolean success;
    /**
     * 业务码,code for errorType
     */
    private String errorCode;
    /**
     * 文字信息, message display to user
     */
    private String errorMessage;
    /**
     * error display type： 0 silent; 1 message.warn; 2 message.error; 4 notification; 9 page
     */
    private Integer showType;
    /**
     * Convenient for back-end Troubleshooting: unique request ID
     */
    private String traceId;
    /**
     * onvenient for backend Troubleshooting: host of current access server
     */
    private String host;
    /**
     * 业务响应结果
     */
    private T data;
    /**
     * 参数错误信息列表
     */
    private List<String> argumentsErrors;

    public static <T> Result<T> create() {
        return new Result<>();
    }

    public static <T> Result<T> create(T data) {
        return new Result<T>().setData(data);
    }

    public static <T> Result<T> error(IErrorCode e) {
        Result<T> result = new Result<T>();
        result.setErrorCode(e.getErrorCode());
        result.setErrorMessage(e.getErrorMessage());
        return result;
    }

    public static <T> Result<T> error(String code, String message, ShowTypeEnum showType) {
        return error(code, message, showType, Lists.newArrayList());
    }

    public static <T> Result<T> error(String code, String message, ShowTypeEnum showType, List<String> argumentsErrors) {
        Result<T> result = new Result<T>();
        result.setErrorCode(code);
        result.setErrorMessage(message);
        result.setShowType(showType == null ? 0 : showType.getType());
        result.setTraceId(TracerUtils.getRequestId());
        result.setArgumentsErrors(argumentsErrors);
        return result;
    }

    public Result() {
        this("0", "Success");
    }

    public Result(String code, String message) {
        this.setErrorCode(code);
        this.setErrorMessage(message);
        this.setShowType(0);
    }

    public Result<T> setErrorCode(String code) {
        this.errorCode = code;
        this.setSuccess("0".equals(code));
        return this;
    }

    public Result<T> setErrorMessage(String message) {
        this.errorMessage = message;
        return this;
    }

    public Result<T> setData(T attributeValue) {
        this.data = attributeValue;
        return this;
    }

    public Boolean getSuccess() {
        return success;
    }

    public Result<T> setSuccess(Boolean success) {
        this.success = success;
        return this;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public Integer getShowType() {
        return showType;
    }

    public Result<T> setShowType(Integer showType) {
        this.showType = showType;
        return this;
    }

    public String getTraceId() {
        return traceId;
    }

    public Result<T> setTraceId(String traceId) {
        this.traceId = traceId;
        return this;
    }

    public String getHost() {
        return host;
    }

    public Result<T> setHost(String host) {
        this.host = host;
        return this;
    }

    public T getData() {
        return data;
    }

    public List<String> getArgumentsErrors() {
        return argumentsErrors;
    }

    public void setArgumentsErrors(List<String> argumentsErrors) {
        this.argumentsErrors = argumentsErrors;
    }

    @Override
    public String toString() {
        return "Result" + super.toString();
    }
}
