package io.milkwang.util.excel.helper;


import com.google.common.collect.Lists;
import io.milkwang.util.excel.entity.Col;
import io.milkwang.util.excel.entity.Excel;
import io.milkwang.util.excel.entity.ExcelSheet;
import io.milkwang.util.excel.entity.Row;
import org.apache.commons.collections4.CollectionUtils;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Excel工具类
 *
 * @author nethunder
 */
public class ExcelBuilder {
    private Excel excel;

    public ExcelBuilder() {
        this.excel = new Excel();
    }

    public static ExcelBuilder init() {
        return new ExcelBuilder();
    }

    /**
     * 设置excel名字
     *
     * @param name excel名字
     */
    public ExcelBuilder name(String name) {
        this.excel.setName(name);
        return this;
    }

    /**
     * 添加sheet
     *
     * @param name      sheet名字
     * @param sheetData sheet数据
     */
    @SuppressWarnings("unchecked")
    public ExcelBuilder addSheet(String name, List<?> sheetData) {
        ExcelSheet sheet = new ExcelSheet();
        sheet.setName(name);
        if (CollectionUtils.isEmpty(sheetData)) {
            sheet.setTitles(Lists.newArrayList());
            sheet.setRows(Lists.newArrayList());
            return this;
        }
        // 设置title
        List<String> titles;
        Object data = sheetData.get(0);
        Boolean isJxlAtom = Lists.newArrayList(data.getClass().getDeclaredFields()).stream()
                .anyMatch(field -> field.isAnnotationPresent(JxlAtom.class));
        if (isJxlAtom) {
            titles = Lists.newArrayList(data.getClass().getDeclaredFields())
                    .stream()
                    .filter(item -> item.getDeclaredAnnotation(JxlAtom.class) != null)
                    .sorted((o1, o2) -> {
                        int o1Seq = o1.getDeclaredAnnotation(JxlAtom.class).order();
                        int o2Seq = o2.getDeclaredAnnotation(JxlAtom.class).order();
                        return o1Seq - o2Seq;
                    }).map(item -> item.getDeclaredAnnotation(JxlAtom.class).name())
                    .collect(Collectors.toList());
        } else {
            LinkedHashMap<String, Object> firstMap = (LinkedHashMap<String, Object>) data;
            titles = Lists.newArrayList(firstMap.keySet());
        }
        // 填充数据 rows
        List<Row> rows;
        if (isJxlAtom) {
            // 注解形式
            rows = sheetData.stream()
                    .map(row -> {
                        List<Col> cols = Lists.newArrayList(row.getClass().getDeclaredFields())
                                .stream()
                                .filter(item -> item.getDeclaredAnnotation(JxlAtom.class) != null)
                                .sorted((o1, o2) -> {
                                    int o1Seq = o1.getDeclaredAnnotation(JxlAtom.class).order();
                                    int o2Seq = o2.getDeclaredAnnotation(JxlAtom.class).order();
                                    return o1Seq - o2Seq;
                                })
                                .map(item -> {
                                    try {
                                        Col col = new Col();
                                        item.setAccessible(true);
                                        JxlAtom atom = item.getDeclaredAnnotation(JxlAtom.class);
                                        col.setType(atom.type());
                                        col.setValue(item.get(row));
                                        return col;
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        return null;
                                    }
                                })
                                .filter(Objects::nonNull)
                                .collect(Collectors.toList());
                        Row tempRow = new Row();
                        tempRow.setCols(cols);
                        return tempRow;
                    })
                    .collect(Collectors.toList());
        } else {
            // map形式
            rows = sheetData.stream()
                    .map(row -> {
                        List<Col> cols = ((LinkedHashMap<String, Object>) row)
                                .values()
                                .stream()
                                .map(this::generateCol)
                                .collect(Collectors.toList());
                        Row tempRow = new Row();
                        tempRow.setCols(cols);
                        return tempRow;
                    })
                    .collect(Collectors.toList());
        }
        sheet.setRows(rows);
        sheet.setTitles(titles);
        sheet.setName(name);
        this.excel.getExcelSheets().add(sheet);
        return this;
    }

    /**
     * 返回生成的excel
     */
    public Excel build() {
        return this.excel;
    }


    private Col generateCol(Object value) {
        Col col = new Col();
        col.setValue(value);
        if (value instanceof String) {
            col.setType(AtomType.STRING);
        } else if (value instanceof Double || value instanceof Integer || value instanceof Long || value instanceof Float) {
            col.setType(AtomType.NUMBER);
        } else if (value instanceof Date) {
            col.setType(AtomType.DATETIME);
        } else {
            // 默认设置为字符串
            col.setType(AtomType.STRING);
        }
        return col;
    }


}
