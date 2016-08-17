package com.work.admin.schema;

import java.io.Serializable;
import java.util.List;

/**
 * м╪пнап╠М
 * 
 * @author zhangwentao
 * 
 */
public class SerieSchema implements Serializable {

	private static final long serialVersionUID = 201033331L;

	private String name;

	private float[] data;
	private String[] categories;
	private List<String> day;
	private List<Integer> line;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


	public float[] getData() {
		return data;
	}

	public void setData(float[] data) {
		this.data = data;
	}

	public String[] getCategories() {
		return categories;
	}

	public void setCategories(String[] categories) {
		this.categories = categories;
	}

	public static long getSerialVersionUID() {
		return serialVersionUID;
	}

	public List<String> getDay() {
		return day;
	}

	public void setDay(List<String> day) {
		this.day = day;
	}

	public List<Integer> getLine() {
		return line;
	}

	public void setLine(List<Integer> line) {
		this.line = line;
	}


}
