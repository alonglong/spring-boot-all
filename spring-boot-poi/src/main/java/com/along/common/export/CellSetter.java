package com.along.common.export;

public class CellSetter {
	/**
	 * 标题
	 */
	private String title;

	/**
	 * 属性
	 */
	private String property;

	/**
	 * 类型
	 */
	private String type;

	/**
	 * 数据格式
	 */
	private String format;

	/**
	 * 列宽
	 */
	private int cellColumnWidth;

	/**
	 *
	 */
	private ExeclColRnderer render;


	public int getCellColumnWidth() {
		return cellColumnWidth;
	}

	public void setCellColumnWidth(int cellColumnWidth) {
		this.cellColumnWidth = cellColumnWidth;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getProperty() {
		return property;
	}

	public void setProperty(String property) {
		this.property = property;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public ExeclColRnderer getRender() {
		return render;
	}

	public void setRender(ExeclColRnderer render) {
		this.render = render;
	}

	public CellSetter(String title, String property, String type, String format, int cellColumnWidth) {
		super();
		this.title = title;
		this.property = property;
		this.type = type;
		this.format = format;
		this.cellColumnWidth = cellColumnWidth;
	}
	

	public CellSetter(String title, String property, String type, String format, int cellColumnWidth, ExeclColRnderer render) {
		super();
		this.title = title;
		this.property = property;
		this.type = type;
		this.format = format;
		this.cellColumnWidth = cellColumnWidth;
		this.render = render;
	}

	public CellSetter() {

	}

}

interface ExeclColRnderer{
	String view(Object o);
}