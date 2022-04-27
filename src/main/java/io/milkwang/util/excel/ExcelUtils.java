package io.milkwang.util.excel;


import io.milkwang.util.common.DateUtils;
import io.milkwang.util.common.TextUtils;
import io.milkwang.util.excel.entity.Col;
import io.milkwang.util.excel.entity.Excel;
import io.milkwang.util.excel.entity.ExcelSheet;
import io.milkwang.util.excel.entity.Row;
import io.milkwang.util.excel.helper.AtomType;
import io.milkwang.util.excel.helper.ExcelBuilder;
import jxl.Workbook;
import jxl.write.*;
import jxl.write.Number;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class ExcelUtils {
    private static Logger logger = LoggerFactory.getLogger(ExcelUtils.class);

    public static ExcelBuilder builder() {
        return ExcelBuilder.init();
    }

    public static File saveExcelFile(Excel excel) {
        String excelName = excel.getName();
        List<ExcelSheet> excelSheets = excel.getExcelSheets();


        String fileName;
        if (excelName != null) {
            fileName = excelName;
        } else {
            fileName = TextUtils.randomString(8);
        }
        File file = new File("/data/temp/" + fileName + ".xls");
        // 创建缓存文件夹
        File parentFile = file.getParentFile();
        if (!parentFile.exists()) {
            boolean tempDirCreated = parentFile.mkdirs();
            if (!tempDirCreated) {
                logger.error("创建缓存文件夹失败:{}", parentFile.getAbsolutePath());
            } else {
                logger.info("创建缓存文件夹:{}", parentFile.getAbsolutePath());
            }
        }

        WritableWorkbook book = null;

        try {
            book = Workbook.createWorkbook(file);
            excelSheets.sort(((o1, o2) -> o1.getName().compareToIgnoreCase(o2.getName())));
            for (ExcelSheet sheet : excelSheets) {
                // 渲染sheet
                String sheetName = sheet.getName();
                List<Row> rows = sheet.getRows();
                List<String> titles = sheet.getTitles();

                WritableSheet excelSheet = book.createSheet(sheetName, book.getNumberOfSheets());
                int rowSize = 0;
                // 输出表头
                for (int i = 0; i < titles.size(); i++) {
                    String title = titles.get(i);
                    excelSheet.addCell(new Label(i, rowSize, title));
                }
                rowSize++;
                // 输出内容
                for (Row row : rows) {
                    List<Col> cols = row.getCols();
                    for (int i = 0; i < cols.size(); i++) {
                        Col col = cols.get(i);
                        AtomType type = col.getType();
                        Object value = col.getValue();
                        if (value != null) {
                            switch (type) {
                                case DATETIME: {
                                    Date date = (Date) value;
                                    excelSheet.addCell(new Label(i, rowSize, DateUtils.parseDateTimeToString(date)));
                                    break;
                                }
                                case DATE: {
                                    Date date = (Date) value;
                                    excelSheet.addCell(new Label(i, rowSize, DateUtils.parseDateToString(date)));
                                    break;
                                }
                                case NUMBER: {
                                    double number = Double.parseDouble(value.toString());
                                    if (Double.isInfinite(number) || Double.isNaN(number)) {
                                        excelSheet.addCell(new Label(i, rowSize, "error"));
                                    } else {
                                        excelSheet.addCell(new Number(i, rowSize, new BigDecimal(number).setScale(4, BigDecimal.ROUND_UP).doubleValue()));
                                    }

                                    break;
                                }
                                case STRING: {
                                    excelSheet.addCell(new Label(i, rowSize, value.toString()));
                                    break;
                                }
                                default: {
                                    excelSheet.addCell(new Label(i, rowSize, value.toString()));
                                }
                            }
                        }
                    }
                    rowSize++;
                }
            }
            book.write();

        } catch (IOException | WriteException e) {
            logger.error(e.getLocalizedMessage(), e);
        } finally {
            if (book != null) {
                try {
                    book.close();
                } catch (Exception e) {
                    logger.error(e.getLocalizedMessage(), e);
                }
            }
        }
        return file;
    }
}
