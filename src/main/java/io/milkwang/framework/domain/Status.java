package io.milkwang.framework.domain;

/**
 * 状态, 增加时请按顺序, 否则后果自负
 */
public enum Status {
    /**
     * 对应数据库为0
     */
    ON(0),
    /**
     * 对应数据库为1
     */
    OFF(1);
    private final Integer value;


    Status(Integer i) {
        this.value = i;
    }

    public Integer getValue() {
        return value;
    }
}
