package com.along.common.export;

import java.util.LinkedList;
import java.util.List;

/**
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @Author along
 * @Date 2019/1/14 18:18
 */
public class MoreSetter {

    private List<String> rowTitles; // 行标题名集合

    private String prop;// 字段值

    private String type;// 数据类型(缺省)

    private int width;// 单元格宽度

    private boolean color;// 单元格颜色(资源核查用到)

    private String format; // 格式化

    private ExcelColRnderer render;


    public List<String> getRowTitles() {
        return rowTitles;
    }

    public void setRowTitles(LinkedList<String> rowTitles) {
        this.rowTitles = rowTitles;
    }

    public String getProp() {
        return prop;
    }

    public void setProp(String prop) {
        this.prop = prop;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public boolean isColor() {
        return color;
    }

    public void setColor(boolean color) {
        this.color = color;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public ExcelColRnderer getRender() {
        return render;
    }

    public void setRender(ExcelColRnderer render) {
        this.render = render;
    }

    public MoreSetter(List<String> rowTitles, String prop, int width) {
        super();
        this.rowTitles = rowTitles;
        this.prop = prop;
        this.width = width;
    }

    public MoreSetter(List<String> rowTitles, String prop, int width,
                      boolean color) {
        super();
        this.rowTitles = rowTitles;
        this.prop = prop;
        this.width = width;
        this.color = color;
    }

    public MoreSetter(List<String> rowTitles, String prop, String type,
                      int width) {
        super();
        this.rowTitles = rowTitles;
        this.prop = prop;
        this.type = type;
        this.width = width;
    }

    public MoreSetter(List<String> rowTitles, String prop, String type, String format,
                      int width) {
        super();
        this.rowTitles = rowTitles;
        this.prop = prop;
        this.type = type;
        this.format = format;
        this.width = width;
    }

    public MoreSetter(List<String> rowTitles, String prop, String type, String format,
                      int width, ExcelColRnderer render) {
        super();
        this.rowTitles = rowTitles;
        this.prop = prop;
        this.type = type;
        this.format = format;
        this.width = width;
        this.render = render;
    }

    public MoreSetter() {
        super();
    }

}
