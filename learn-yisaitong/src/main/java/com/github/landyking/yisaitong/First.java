package com.github.landyking.yisaitong;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.*;

import java.io.File;
import java.io.IOException;

/**
 * Description：TODO <br/>
 *
 * @author: Landy
 * @date: 2017/8/25 11:09
 * note:
 */
public class First {

    public static void main(String[] args) throws IOException, InvalidFormatException {
        Workbook workbook = WorkbookFactory.create(new File("E:\\XDJA\\codes\\workspace\\daydayup\\learn-yisaitong\\src\\main\\resources\\test.xls"));
        Sheet sheet = workbook.getSheetAt(0);
        Row row = sheet.getRow(0);
        Cell cell = row.getCell(0);
        System.out.println(cell.getStringCellValue());

    }
}
