package com.along.common.export;

/**
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @Author along
 * @Date 2019/1/14 15:28
 */
public class PropSetter {
    private String rOne;// 第一行标题

    private String rTwo;// 第二行标题

    private String rThree;// 第三行标题

    private String prop;// 字段值

    private String type;// 数据类型(缺省)

    private int width;// 单元格宽度

    private boolean color;// 单元格颜色(资源核查用到)

    private String format; // 格式化

    private ExcelColRnderer render;

    public String getrOne() {
        return rOne;
    }

    public void setrOne(String rOne) {
        this.rOne = rOne;
    }

    public String getrTwo() {
        return rTwo;
    }

    public void setrTwo(String rTwo) {
        this.rTwo = rTwo;
    }

    public String getrThree() {
        return rThree;
    }

    public void setrThree(String rThree) {
        this.rThree = rThree;
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

    public PropSetter(String rOne, String rTwo, String prop, int width) {
        super();
        this.rOne = rOne;
        this.rTwo = rTwo;
        this.prop = prop;
        this.width = width;
    }

    public PropSetter(String rOne, String rTwo, String prop, int width,
                      boolean color) {
        super();
        this.rOne = rOne;
        this.rTwo = rTwo;
        this.prop = prop;
        this.width = width;
        this.color = color;
    }

    public PropSetter(String rOne, String prop, int width) {
        super();
        this.rOne = rOne;
        this.prop = prop;
        this.width = width;
    }

    public PropSetter(String rOne, String rTwo, String prop, String type,
                      int width) {
        super();
        this.rOne = rOne;
        this.rTwo = rTwo;
        this.prop = prop;
        this.type = type;
        this.width = width;
    }

    public PropSetter(String rOne, String rTwo, String prop, String type, String format,
                      int width) {
        super();
        this.rOne = rOne;
        this.rTwo = rTwo;
        this.prop = prop;
        this.type = type;
        this.format = format;
        this.width = width;
    }

    public PropSetter(String rOne, String rTwo, String prop, String type, String format,
                      int width, ExcelColRnderer render) {
        super();
        this.rOne = rOne;
        this.rTwo = rTwo;
        this.prop = prop;
        this.type = type;
        this.format = format;
        this.width = width;
        this.render = render;
    }

    public PropSetter(String rOne, String rTwo, String rThree, String prop, String type, String format,
                      int width) {
        super();
        this.rOne = rOne;
        this.rTwo = rTwo;
        this.rThree = rThree;
        this.prop = prop;
        this.type = type;
        this.format = format;
        this.width = width;
    }

    public PropSetter(String rOne, String rTwo,String rThree, String prop, String type, String format,
                      int width, ExcelColRnderer render) {
        super();
        this.rOne = rOne;
        this.rTwo = rTwo;
        this.rThree = rThree;
        this.prop = prop;
        this.type = type;
        this.format = format;
        this.width = width;
        this.render = render;
    }

    public PropSetter() {
        super();
    }

    @Override
    public String toString() {
        return "PropSetter{" +
                "rOne='" + rOne + '\'' +
                ", rTwo='" + rTwo + '\'' +
                ", rThree='" + rThree + '\'' +
                ", prop='" + prop + '\'' +
                ", type='" + type + '\'' +
                ", width=" + width +
                ", color=" + color +
                ", format='" + format + '\'' +
                ", render=" + render +
                '}';
    }
}

