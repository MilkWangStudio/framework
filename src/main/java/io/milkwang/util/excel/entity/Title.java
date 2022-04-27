package io.milkwang.util.excel.entity;

import java.util.List;

/**
 * Excel第一行，表头行
 *
 * @author nethunder
 */
public class Title {
    private List<String> names;

    public List<String> getNames() {
        return names;
    }

    public void setNames(List<String> names) {
        this.names = names;
    }
}
