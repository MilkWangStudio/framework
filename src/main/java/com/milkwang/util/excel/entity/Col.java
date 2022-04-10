package com.milkwang.util.excel.entity;


import io.gitee.tonggong.util.excel.helper.AtomType;

/**
 * 每一列
 *
 * @author nethunder
 */
public class Col {
    private AtomType type;
    private Object value;

    public AtomType getType() {
        return type;
    }

    public void setType(AtomType type) {
        this.type = type;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
