package com.project.seafoodindent.model;

import java.io.Serializable;

/**
 * 订单实体类
 * 
 * @author 师春雷
 * 
 */
public class Order implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4729064393992094932L;

	/** 订单ID */
	public String id;
	/** 订单名称 */
	public String order_name;
	/** 订单价格 */
	public String price;
	/** 客户姓名 */
	public String cus_name;
	/** 商户电话 */
	public String mer_mobile;
	/** 商户地址 */
	public String mer_address;
	/** 顾客电话 */
	public String cus_mobile;
	/** 顾客地址 */
	public String cus_address;

	@Override
	public String toString() {
		return "{id=" + id + ", order_name=" + order_name + ", price=" + price
				+ ", cus_name=" + cus_name + ", mer_mobile=" + mer_mobile
				+ ", mer_address=" + mer_address + ", cus_mobile=" + cus_mobile
				+ ", cus_address=" + cus_address + "}";
	}

}
