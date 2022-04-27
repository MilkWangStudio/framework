package io.milkwang.framework.exception;

import java.text.MessageFormat;

/**
 * 基础业务异常, 各项目从该类继承出特定模块异常, 可在继承时增加特殊字段<br/>
 * 例如：<br/>
 * OAuthCodeNotFoundException extend BaseException<br/>
 * PaymentException extend BaseException
 */
public class BaseException extends RuntimeException implements IErrorCode {

    /**
     * 业务结果码
     */
    private final String code;
    /**
     * 异常信息
     */
    private final String message;
    /**
     * 显示类型
     */
    private final ShowTypeEnum showType;

    /**
     * 直接定义code和message，抛出异常，这种一般用在UI层统一封装用
     *
     * @param code    业务结果码
     * @param message 结果码对应的信息
     */
    public BaseException(String code, String message) {
        super(message);
        this.code = code;
        this.message = message;
        this.showType = ShowTypeEnum.silent;
    }

    /**
     * 直接定义code和message，抛出异常，这种一般用在UI层统一封装用
     *
     * @param code     业务结果码
     * @param message  结果码对应的信息
     * @param showType 展示类型
     */
    public BaseException(String code, String message, ShowTypeEnum showType) {
        super(message);
        this.code = code;
        this.message = message;
        this.showType = showType;
    }

    /**
     * 通过结果码枚举抛出异常
     *
     * @param errorCode 业务结果码
     */
    public BaseException(IErrorCode errorCode) {
        super(errorCode.getErrorMessage());
        this.code = errorCode.getErrorCode();
        this.message = errorCode.getErrorMessage();
        this.showType = errorCode.getShowType();
    }

    /**
     * IErrorCode.message 中有占位符，可以进行占位符替换, 注意不要有{} 这种，会报错
     *
     * @param errorCode 异常Code
     * @param args      实际参数
     */
    public BaseException(IErrorCode errorCode, Object... args) {
        super(MessageFormat.format(errorCode.getErrorMessage(), args));
        this.code = errorCode.getErrorCode();
        this.message = String.format(errorCode.getErrorMessage(), args);
        this.showType = errorCode.getShowType();
    }

    /**
     * 对代码中抛出的异常进行封装
     *
     * @param cause     异常
     * @param errorCode 业务结果码
     */
    public BaseException(Throwable cause, IErrorCode errorCode) {
        super(cause);
        this.code = errorCode.getErrorCode();
        this.message = errorCode.getErrorMessage();
        this.showType = errorCode.getShowType();
    }

    @Override
    public String getErrorCode() {
        return code;
    }

    @Override
    public String getErrorMessage() {
        return message;
    }

    @Override
    public ShowTypeEnum getShowType() {
        return showType;
    }
}
