package com.milkwang.util.excel.entity;

import java.util.List;

/**
 * Excel每一个Sheet
 *
 * @author nethunder
 */
public class ExcelSheet {
    private String name;
    private List<String> titles;
    private List<Row> rows;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getTitles() {
        return titles;
    }

    public void setTitles(List<String> titles) {
        this.titles = titles;
    }

    public List<Row> getRows() {
        return rows;
    }

    public void setRows(List<Row> rows) {
        this.rows = rows;
    }
}
