package com.milkwang.util.select;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

/**
 * 下拉框组合数据
 *
 * @author nethunder
 * @date 2019/12/23
 */
public class SelectGroup<T, R> {
    /**
     * 用来做Key-Value映射，方便查询
     */
    private HashMap<R, SelectItem<R>> itemMap = new HashMap<>();
    /**
     * 所有下拉框值的集合
     */
    private List<SelectItem<R>> selectList = new ArrayList<>();
    /**
     * 根目录集合
     */
    private List<SelectItem<R>> rootList = new ArrayList<>();

    /**
     * 创建下拉框数据组合
     *
     * @param originList             原始数据列表
     * @param getNodeValueFunc       获取节点值的方法
     * @param getNodeLabelFunc       获取节点文本的方法
     * @param getNodeParentValueFunc 获取父节点值的方法
     * @param isEmptyFunc            判断节点是否为空
     */
    public SelectGroup(List<T> originList,
                       Function<T, R> getNodeValueFunc,
                       Function<T, String> getNodeLabelFunc,
                       Function<T, R> getNodeParentValueFunc,
                       Function<R, Boolean> isEmptyFunc) {
        if (originList == null || originList.size() == 0) {
            return;
        }
        for (T item : originList) {
            SelectItem<R> select = new SelectItem<>();
            select.setLabel(getNodeLabelFunc.apply(item));
            select.setValue(getNodeValueFunc.apply(item));
            select.setParentValue(getNodeParentValueFunc.apply(item));
            selectList.add(select);
            itemMap.put(select.getValue(), select);
        }
        for (SelectItem<R> item : selectList) {
            if (isEmptyFunc.apply(item.getParentValue())) {
                rootList.add(item);
                continue;
            }
            R parentValue = item.getParentValue();
            if (parentValue == null) {
                continue;
            }
            SelectItem<R> parent = itemMap.get(parentValue);
            item.setParent(parent);
            parent.addChild(item);
        }
    }


    /**
     * 创建下拉框数据组合，判断节点是否空时，默认使用0、""、null<br/><br/>
     * (R value) -> value == null || "".equals(value) || Objects.equals(0, value))
     *
     * @param originList             原始数据列表
     * @param getNodeValueFunc       获取节点值的方法
     * @param getNodeLabelFunc       获取节点文本的方法
     * @param getNodeParentValueFunc 获取父节点值的方法
     */
    public SelectGroup(List<T> originList,
                       Function<T, R> getNodeValueFunc,
                       Function<T, String> getNodeLabelFunc,
                       Function<T, R> getNodeParentValueFunc) {
        this(originList, getNodeValueFunc, getNodeLabelFunc, getNodeParentValueFunc,
                (R value) -> value == null || "".equals(value) || Objects.equals(0, value));
    }

    /**
     * 创建下拉框数据组合，判断节点是否空时，默认使用0、""、null<br/><br/>
     * (R value) -> value == null || "".equals(value) || Objects.equals(0, value))
     *
     * @param originList       原始数据列表
     * @param getNodeValueFunc 获取节点值的方法
     * @param getNodeLabelFunc 获取节点文本的方法
     */
    public SelectGroup(List<T> originList,
                       Function<T, R> getNodeValueFunc,
                       Function<T, String> getNodeLabelFunc) {
        this(originList, getNodeValueFunc, getNodeLabelFunc, (config) -> null);
    }


    public List<SelectItem<R>> getSelectList() {
        return selectList;
    }

    public List<SelectItem<R>> getRootList() {
        return rootList;
    }

    public SelectItem<R> getSelectItem(R value) {
        return this.itemMap.get(value);
    }

    @Override
    public String toString() {
        return "SelectGroup{" +
                "itemMap=" + itemMap +
                ", selectList=" + selectList +
                ", rootList=" + rootList +
                '}';
    }
}
