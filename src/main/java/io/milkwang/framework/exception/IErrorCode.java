package io.milkwang.framework.exception;

/**
 * 异常Code码
 */
public interface IErrorCode {
    /**
     * 内部Code
     */
    String getErrorCode();

    /**
     * 内部Message
     */
    String getErrorMessage();

    ShowTypeEnum getShowType();
}