package io.milkwang.util.select;

import com.alibaba.fastjson.annotation.JSONField;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 下拉框单个值
 *
 * @author nethunder
 * @date 2019/12/23
 */
public class SelectItem<R> implements Serializable {
    private static final long serialVersionUID = 7623580035915618066L;

    /**
     * 父元素值
     */
    private R parentValue;
    /**
     * 当前节点值
     */
    private R value;
    /**
     * 当前节点文本
     */
    private String label;
    /**
     * 父节点
     */
    @JSONField(serialize = false)
    private SelectItem<R> parent;
    /**
     * 直属子节点
     */
    private List<SelectItem<R>> children = new ArrayList<>();

    public R getValue() {
        return value;
    }

    public void setValue(R value) {
        this.value = value;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public List<SelectItem<R>> getChildren() {
        return children;
    }

    public void setChildren(List<SelectItem<R>> children) {
        this.children = children;
    }

    public void addChild(SelectItem<R> item) {
        this.children.add(item);
    }


    public R getParentValue() {
        return parentValue;
    }

    public void setParentValue(R parentValue) {
        this.parentValue = parentValue;
    }

    public SelectItem<R> getParent() {
        return parent;
    }

    public void setParent(SelectItem<R> parent) {
        this.parent = parent;
    }

    @Override
    public String toString() {
        return "SelectItem{" +
                "parentValue=" + parentValue +
                ", value=" + value +
                ", label='" + label + '\'' +
                ", children=" + children +
                '}';
    }
}
