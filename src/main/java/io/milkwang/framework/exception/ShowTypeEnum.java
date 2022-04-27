package io.milkwang.framework.exception;

public enum ShowTypeEnum {
    /**
     * 静默, 不提示
     */
    silent(0),
    /**
     * 告警消息, 不预期的危险程度较低异常
     */
    warn(1),
    /**
     * 错误消息, 不预期的严重异常
     */
    error(2),
    /**
     * 提醒, 主要用在一些业务异常, 主动弹窗上
     */
    notification(4),
    /**
     * 跳转页面
     */
    page(9),
    ;

    private final int type;

    ShowTypeEnum(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }
}
