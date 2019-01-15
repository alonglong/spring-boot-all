package com.along.common.export;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

/**
 * @Description: 复杂表头excel(二级)
 * @Author along
 * @Date 2019/1/14 15:26
 */
public class CreateThreeMapExcel {
    private static Logger logger = LoggerFactory.getLogger(CreateThreeMapExcel.class);

    /**
     * @param
     * 		datas  	数据
     * @param
     * 		props	参数设置
     * @param
     * 		fps		合并单元格坐标值
     * @param
     * 		sheetName	单元格名称
     *
     * */
    public static SXSSFWorkbook create(List<Map<String, Object>> datas,
                                       List<PropSetter> props, List<FourPoint> fps, String sheetName) {

        long startTime = System.currentTimeMillis();
        SXSSFWorkbook workbook = new SXSSFWorkbook();
        Sheet sheet = workbook.createSheet();
        workbook.setSheetName(0, sheetName);

        Font titleFont = workbook.createFont();
        titleFont.setFontName("微软雅黑");
        titleFont.setFontHeightInPoints((short) 10); // 字体大小
        titleFont.setBoldweight(Font.BOLDWEIGHT_BOLD);// 加粗

        Font contentFont = workbook.createFont();
        contentFont.setFontName("微软雅黑");
        contentFont.setFontHeightInPoints((short) 9);

        CellStyle titleStyle = workbook.createCellStyle();
        titleStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直
        titleStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 水平
        titleStyle.setBorderBottom(CellStyle.BORDER_THIN);
        titleStyle.setBorderLeft(CellStyle.BORDER_THIN);
        titleStyle.setBorderRight(CellStyle.BORDER_THIN);
        titleStyle.setBorderTop(CellStyle.BORDER_THIN);
        titleStyle.setFillForegroundColor(HSSFColor.LIGHT_GREEN.index);// 填暗红色
        titleStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
        titleStyle.setFont(titleFont);
        titleStyle.setWrapText(true);

        CellStyle stringStyle = workbook.createCellStyle();
        stringStyle.setAlignment(CellStyle.ALIGN_LEFT);
        stringStyle.setBorderBottom(CellStyle.BORDER_THIN);
        stringStyle.setBorderLeft(CellStyle.BORDER_THIN);
        stringStyle.setBorderRight(CellStyle.BORDER_THIN);
        stringStyle.setBorderTop(CellStyle.BORDER_THIN);
        stringStyle.setFont(contentFont);
        stringStyle.setWrapText(true);

        Row rowOne = sheet.createRow(0);
        rowOne.setHeight((short) 350);
        Row rowTwo = sheet.createRow(1);
        rowTwo.setHeightInPoints((2*sheet.getDefaultRowHeightInPoints()));
        Row rowThree = sheet.createRow(2);
        rowThree.setHeightInPoints((2*sheet.getDefaultRowHeightInPoints()));
        Cell cell;
        for(int i=0; i<props.size(); i++) {
            cell = rowOne.createCell(i); // 一级标题的设置
            cell.setCellStyle(titleStyle);
            cell.setCellValue(props.get(i).getrOne());
            cell = rowTwo.createCell(i); // 二级标题的设置
            cell.setCellStyle(titleStyle);
            cell.setCellValue(props.get(i).getrTwo());
            cell = rowThree.createCell(i); // 三级标题的设置
            cell.setCellStyle(titleStyle);
            cell.setCellValue(props.get(i).getrThree());
            sheet.setColumnWidth(i, props.get(i).getWidth()); // 宽度
        }
        for(FourPoint fp : fps) {//	合并单元格
            sheet.addMergedRegion(new CellRangeAddress(fp.getRowStart(), fp.getRowEnd(),
                    fp.getColStart(), fp.getColEnd()));
        }
        // 内容设置
        Row row;
        if (datas.size() != 0) {
            for (int m = 0; m < datas.size(); m++) {
                row = sheet.createRow(m + 3);
                row.setHeight((short) 310);
                for (int i = 0; i < props.size(); i++) {
                    Cell cont = row.createCell(i);
                    Object value = datas.get(m).get(props.get(i).getProp());
                    if (value == null) {
                        cont.setCellValue("");
                        cont.setCellStyle(stringStyle);
                    } else if (props.get(i).getRender() != null) {
                        cont.setCellType(HSSFCell.CELL_TYPE_STRING);
                        cont.setCellStyle(stringStyle);
                        cont.setCellValue(props.get(i).getRender().view(value));
                    } else {
                        cont.setCellType(HSSFCell.CELL_TYPE_STRING);
                        cont.setCellValue(String.valueOf(value));
                        cont.setCellStyle(stringStyle);
                    }
                }
            }
        }
        logger.info("导出总计耗时: {}", System.currentTimeMillis() - startTime + "毫秒!");
        return workbook;
    }
}
