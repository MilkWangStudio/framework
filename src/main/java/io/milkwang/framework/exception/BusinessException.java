package io.milkwang.framework.exception;


public class BusinessException extends BaseException {
    public BusinessException(String message) {
        super("500", message, ShowTypeEnum.warn);
    }

    public BusinessException(String code, String message) {
        super(code, message, ShowTypeEnum.warn);
    }

    public BusinessException(String code, String message, ShowTypeEnum showType) {
        super(code, message, showType);
    }

    public BusinessException(IErrorCode errorCode) {
        super(errorCode);
    }

    public BusinessException(IErrorCode errorCode, Object... args) {
        super(errorCode, args);
    }

    public BusinessException(Throwable cause, IErrorCode errorCode) {
        super(cause, errorCode);
    }
}
