package com.viewscenes.common.dto;

import java.io.Serializable;

/**
 *  库存DTO对象
 * @author duyubo
 *
 */
public class StorageDTO implements Serializable {

	private static final long serialVersionUID = 1324118706008241907L;
	
	private Integer id;
	
	private String commodityCode;
	
	private String name;
	
	private Integer count;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCommodityCode() {
		return commodityCode;
	}

	public void setCommodityCode(String commodityCode) {
		this.commodityCode = commodityCode;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getCount() {
		return count;
	}

	public void setCount(Integer count) {
		this.count = count;
	}
	
}
