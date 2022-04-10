package com.milkwang.framework.exception;

/**
 * 基础异常
 */
public enum BaseErrorCode implements IErrorCode {
    /**
     * 参数异常
     */
    ILLEGAL_ARGUMENT_ERROR("40001", "参数异常", ShowTypeEnum.error),
    /**
     * JSON格式异常
     */
    JSON_FORMAT_ERROR("40002", "参数格式异常", ShowTypeEnum.error),
    /**
     * 系统异常
     */
    SYSTEM_THROWABLE("500001", "服务器伐开心,我们正在想办法", ShowTypeEnum.error),
    /**
     * 未登录
     */
    NOT_LOGIN("700", "未登录", ShowTypeEnum.silent);

    private final String code;
    private final String message;
    private final ShowTypeEnum showType;

    BaseErrorCode(String code, String message, ShowTypeEnum showType) {
        this.code = code;
        this.message = message;
        this.showType = showType;
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
