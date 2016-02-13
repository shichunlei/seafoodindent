package com.project.seafoodindent.model;

import java.io.Serializable;

public class JsonData implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8596315910778484225L;
	
	public String id;
	public String channel;
	public String user_id;
	public Data data;
	public String success;
	public String result;
	public String token;
	public String server_token;

	@Override
	public String toString() {
		return "JsonData [id=" + id + ", channel=" + channel + ", user_id="
				+ user_id + ", data=" + data + ", success=" + success
				+ ", result=" + result + ", token=" + token + ", server_token="
				+ server_token + "]";
	}

}
