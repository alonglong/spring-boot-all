package com.along.common.export;

import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.List;

public class CreateExcel<Record> {

    private Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 构建excel
     *
     * @param cellSetters 表头属性描述对象
     * @param records     数据
     * @param sheetName   工作表名
     * @return
     */
    public SXSSFWorkbook create(List<CellSetter> cellSetters, List<Record> records, String sheetName) {

        long xl = System.currentTimeMillis();
        CellSetter cellSetter;
        Cell cell;
        SXSSFWorkbook workbook = new SXSSFWorkbook();
        DataFormat format = workbook.createDataFormat();
        Method getMethod = null;
        try {
            int cols = cellSetters.size();
            int rows = records.size();


            //标题字体设置
            Font titleFont = workbook.createFont();
            titleFont.setFontName("微软雅黑");
            titleFont.setFontHeightInPoints((short) 10); // 字体大小
            titleFont.setBoldweight(Font.BOLDWEIGHT_BOLD); // 加粗

            //内容字体设置
            Font contentFont = workbook.createFont();
            contentFont.setFontName("微软雅黑");
            contentFont.setFontHeightInPoints((short) 8);

            CellStyle titleStyle = workbook.createCellStyle();
            CellStyle longStyle = workbook.createCellStyle();
            CellStyle stringStyle = workbook.createCellStyle();
            CellStyle dateStyle = workbook.createCellStyle();
            CellStyle booleanStyle = workbook.createCellStyle();
            CellStyle doubleStyle = workbook.createCellStyle();

            titleStyle.setAlignment(CellStyle.ALIGN_CENTER);
            titleStyle.setBorderBottom(CellStyle.BORDER_THIN);
            titleStyle.setBorderLeft(CellStyle.BORDER_THIN);
            titleStyle.setBorderRight(CellStyle.BORDER_THIN);
            titleStyle.setBorderTop(CellStyle.BORDER_THIN);
            titleStyle.setFillForegroundColor(HSSFColor.LIGHT_GREEN.index); // 填暗红色
            titleStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
            titleStyle.setFont(titleFont);
            titleStyle.setWrapText(true);

            longStyle.setAlignment(CellStyle.ALIGN_LEFT);
            longStyle.setBorderBottom(CellStyle.BORDER_THIN);
            longStyle.setBorderLeft(CellStyle.BORDER_THIN);
            longStyle.setBorderRight(CellStyle.BORDER_THIN);
            longStyle.setBorderTop(CellStyle.BORDER_THIN);
            longStyle.setFont(contentFont);
            longStyle.setWrapText(true);

            doubleStyle.setAlignment(CellStyle.ALIGN_LEFT);
            doubleStyle.setBorderBottom(CellStyle.BORDER_THIN);
            doubleStyle.setBorderLeft(CellStyle.BORDER_THIN);
            doubleStyle.setBorderRight(CellStyle.BORDER_THIN);
            doubleStyle.setBorderTop(CellStyle.BORDER_THIN);
            doubleStyle.setFont(contentFont);
            doubleStyle.setWrapText(true);

            stringStyle.setAlignment(CellStyle.ALIGN_LEFT);
            stringStyle.setBorderBottom(CellStyle.BORDER_THIN);
            stringStyle.setBorderLeft(CellStyle.BORDER_THIN);
            stringStyle.setBorderRight(CellStyle.BORDER_THIN);
            stringStyle.setBorderTop(CellStyle.BORDER_THIN);
            stringStyle.setFont(contentFont);
            stringStyle.setWrapText(true);

            dateStyle.setAlignment(CellStyle.ALIGN_LEFT);
            dateStyle.setBorderBottom(CellStyle.BORDER_THIN);
            dateStyle.setBorderLeft(CellStyle.BORDER_THIN);
            dateStyle.setBorderRight(CellStyle.BORDER_THIN);
            dateStyle.setBorderTop(CellStyle.BORDER_THIN);
            dateStyle.setFont(contentFont);
            dateStyle.setWrapText(true);

            booleanStyle.setAlignment(CellStyle.ALIGN_LEFT);
            booleanStyle.setBorderBottom(CellStyle.BORDER_THIN);
            booleanStyle.setBorderLeft(CellStyle.BORDER_THIN);
            booleanStyle.setBorderRight(CellStyle.BORDER_THIN);
            booleanStyle.setBorderTop(CellStyle.BORDER_THIN);
            booleanStyle.setFont(contentFont);
            booleanStyle.setWrapText(true);

            Sheet sheet = workbook.createSheet(); // 创建工作表
            workbook.setSheetName(0, sheetName);
            Row row = sheet.createRow(0);
//			Drawing draw = sheet.createDrawingPatriarch();
            Class<Record> clz = null;

            Method[] methods = new Method[cols];
            if (!CollectionUtils.isEmpty(records)) {
                clz = (Class<Record>) records.get(0).getClass();
            }

            // 设置标题
            for (int i = 0; i < cols; i++) {
                cell = row.createCell(i);
                cellSetter = cellSetters.get(i);
                cell.setCellValue(cellSetter.getTitle().trim());
                cell.setCellStyle(titleStyle);
//				if (cellSetters.get(i) != null) {
//					Comment comment = draw
//							.createCellComment(new XSSFClientAnchor(0, 0, 0, 0,
//									cell.getColumnIndex(), row.getRowNum(),
//									cell.getColumnIndex() + 1,
//									row.getRowNum() + 2));
//					comment.setString(new XSSFRichTextString(cellSetter
//							.getTitle()));
//					cell.setCellComment(comment);
//				}
                if (clz != null) {
                    PropertyDescriptor pd = new PropertyDescriptor(
                            cellSetter.getProperty(), clz);
                    methods[i] = pd.getReadMethod();
                }
            }

            // 这是内容
            for (int i = 0; i < rows; i++) {

                row = sheet.createRow(i + 1);
                row.setHeight((short) 300);
                if ((i + 1) % 10000 == 0)
                    logger.info("存了一万条记录  {}");
                for (int j = 0; j < cols; j++) {
                    cellSetter = cellSetters.get(j);
                    cell = row.createCell(j);
                    Record record = records.get(i);

                    getMethod = methods[j];
                    if (getMethod.invoke(record) == null) {
                        cell.setCellStyle(stringStyle);
                        continue;
                    }
                    if (cellSetter.getRender() != null) {
                        cell.setCellType(Cell.CELL_TYPE_STRING);
                        cell.setCellStyle(stringStyle);
                        cell.setCellValue(cellSetter.getRender().view(
                                getMethod.invoke(record)));
//						Comment comment = draw
//								.createCellComment(new XSSFClientAnchor(0, 0,
//										0, 0, cell.getColumnIndex(), row
//												.getRowNum(), cell
//												.getColumnIndex() + 1, row
//												.getRowNum() + 2));
//						comment.setString(new XSSFRichTextString(cellSetter
//								.getRender().view(getMethod.invoke(record))));
//						cell.setCellComment(comment);
                        continue;
                    }
                    if (cellSetter.getType().trim().toLowerCase().equals("long")
                            || cellSetter.getType().trim().toLowerCase().equals("int")
                            || cellSetter.getType().trim().toLowerCase().equals("byte")
                            || cellSetter.getType().trim().toLowerCase().equals("short")) {
                        cell.setCellType(Cell.CELL_TYPE_NUMERIC);
                        longStyle.setDataFormat(format.getFormat(cellSetter.getFormat()));
                        cell.setCellValue(Long.parseLong(getMethod.invoke(record).toString()));
//						Comment comment = draw
//								.createCellComment(new XSSFClientAnchor(0, 0,
//										0, 0, cell.getColumnIndex(), row
//												.getRowNum(), cell
//												.getColumnIndex() + 1, row
//												.getRowNum() + 2));
//						comment.setString(new XSSFRichTextString(getMethod
//								.invoke(record).toString()));
//						cell.setCellComment(comment);
                        cell.setCellStyle(longStyle);
                        continue;
                    }
                    if (cellSetter.getType().trim().toLowerCase().equals("double")
                            || cellSetter.getType().trim().toLowerCase().equals("float")) {
                        cell.setCellType(Cell.CELL_TYPE_NUMERIC);
                        doubleStyle.setDataFormat(format.getFormat(cellSetter.getFormat()));
                        cell.setCellValue(Double.parseDouble(getMethod.invoke(record).toString()));
//						Comment comment = draw
//								.createCellComment(new XSSFClientAnchor(0, 0,
//										0, 0, cell.getColumnIndex(), row
//												.getRowNum(), cell
//												.getColumnIndex() + 1, row
//												.getRowNum() + 2));
//						comment.setString(new XSSFRichTextString(getMethod
//								.invoke(record).toString()));
//						cell.setCellComment(comment);
                        cell.setCellStyle(doubleStyle);
                        continue;
                    }
                    if (cellSetter.getType().trim().toLowerCase().equals("date")) {
                        cell.setCellType(Cell.CELL_TYPE_NUMERIC);
                        dateStyle.setDataFormat(format.getFormat(cellSetter.getFormat()));
                        cell.setCellValue((Date) getMethod.invoke(record));
                        cell.setCellStyle(dateStyle);
//						Comment comment = draw
//								.createCellComment(new XSSFClientAnchor(0, 0,
//										0, 0, cell.getColumnIndex(), row
//												.getRowNum(), cell
//												.getColumnIndex() + 1, row
//												.getRowNum() + 2));
//						comment.setString(new XSSFRichTextString(getMethod
//								.invoke(record).toString()));
//						cell.setCellComment(comment);
                        continue;
                    }
                    if (cellSetter.getType().trim().toLowerCase().equals("string")
                            || cellSetter.getType().trim().toLowerCase().equals("char")) {
                        cell.setCellType(Cell.CELL_TYPE_STRING);
                        cell.setCellStyle(stringStyle);
                        cell.setCellValue(getMethod.invoke(record).toString());
						/*try {2015.0925修改 tablemiao 
							cell.setCellValue(Integer.parseInt(getMethod
									.invoke(record).toString()));
							cell.setCellStyle(longStyle);
						} catch (Exception e) {
							try {
								cell.setCellValue(Double.parseDouble(getMethod
										.invoke(record).toString()));
								cell.setCellStyle(doubleStyle);
							} catch (Exception e1) {
								cell.setCellStyle(stringStyle);
								cell.setCellValue(getMethod.invoke(record)
										.toString());
							}
						}
						*/
//						Comment comment = draw
//								.createCellComment(new XSSFClientAnchor(0, 0,
//										0, 0, cell.getColumnIndex(), row
//												.getRowNum(), cell
//												.getColumnIndex() + 1, row
//												.getRowNum() + 2));
//						comment.setString(new XSSFRichTextString(getMethod
//								.invoke(record).toString()));
//						cell.setCellComment(comment);
                        continue;
                    }
                    if (cellSetter.getType().trim().toLowerCase().equals("boolean")) {
                        cell.setCellType(Cell.CELL_TYPE_BOOLEAN);
                        booleanStyle.setDataFormat(format.getFormat(cellSetter.getFormat()));
                        cell.setCellStyle(booleanStyle);
                        cell.setCellValue((Boolean) getMethod.invoke(record));
//						Comment comment = draw
//								.createCellComment(new XSSFClientAnchor(0, 0,
//										0, 0, cell.getColumnIndex(), row
//												.getRowNum(), cell
//												.getColumnIndex() + 1, row
//												.getRowNum() + 2));
//						comment.setString(new XSSFRichTextString(getMethod
//								.invoke(record).toString()));
//						cell.setCellComment(comment);
                        continue;
                    }
                }
            }
            // 给每行设置单元格长度
            for (int i = 0; i < cellSetters.size(); i++) {
                cellSetter = cellSetters.get(i);
                sheet.setColumnWidth(i, cellSetter.getCellColumnWidth());
            }

            logger.info("总耗时 {} ", System.currentTimeMillis() - xl);
            return workbook;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
