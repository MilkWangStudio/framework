package com.milkwang.util.excel.entity;

import java.util.List;

/**
 * Excel每一行
 *
 * @author nethunder
 */
public class Row {
    private List<Col> cols;

    public List<Col> getCols() {
        return cols;
    }

    public void setCols(List<Col> cols) {
        this.cols = cols;
    }
}
