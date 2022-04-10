package com.milkwang.util.excel.entity;


import java.util.ArrayList;
import java.util.List;

/**
 * Excel主体
 *
 * @author nethunder
 */
public class Excel {
    private String name;
    private List<ExcelSheet> excelSheets;

    public Excel() {
        this.excelSheets = new ArrayList<>();
    }

    public Boolean isEmpty() {
        return excelSheets == null || excelSheets.size() == 0;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ExcelSheet> getExcelSheets() {
        return excelSheets;
    }

    public void setExcelSheets(List<ExcelSheet> excelSheets) {
        this.excelSheets = excelSheets;
    }
}
