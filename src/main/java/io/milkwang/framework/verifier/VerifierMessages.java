package io.milkwang.framework.verifier;

/**
 * The interface Verifier messages.
 */
public interface VerifierMessages {

    String POSTFIX_NO_NULLS_ALLOWED = " can not be null.";

    String POSTFIX_CHINESE_NAME_NOT_ALLOWED = " character need in [A-Za-z0-9_].";

    String POSTFIX_EMPTY_NOT_ALLOWED = " can not be null or empty.";

    String POSTFIX_ALL_NULLS = " can not be null and empty.";

    String POSTFIX_NON_NEGATIVE = " can not be negative.";

    String POSTFIX_POSITIVE = " can not be zero or negative.";

    String POSTFIX_GREATER = " can not greater than %d.";

    String POSTFIX_INVALID = " is illegal.";

    String POSTFIX_NOT_JSON = " is not json data";

    String IP_INVALID = " IP format error";

    String INVALID_RANGE=" out of the range";

    String NOT_CORRECT=" not correct";

//    String FIELD_NOT_NULL = "%s不能为空";
//    String FORMAT_ERROR = "%s格式错误";
//    String GENERAL_ERROR = "%s";

    static String fieldNotEmpty(String field){
        return String.format("%s不能为空", field);
    }

    static String formatError(String param){
        return String.format("%s格式错误", param);
    }

    static String generalError(String param){
        return String.format("%s",param);
    }
}
