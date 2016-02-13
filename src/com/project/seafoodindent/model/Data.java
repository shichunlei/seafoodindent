package com.project.seafoodindent.model;

import java.io.Serializable;
import java.util.ArrayList;

public class Data implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1872359433077473259L;

	public ArrayList<Order> order;
	public String connection_id;
	public String channel;

	@Override
	public String toString() {
		return "Data [order=" + order + ", connection_id=" + connection_id
				+ ", channel=" + channel + "]";
	}

}
