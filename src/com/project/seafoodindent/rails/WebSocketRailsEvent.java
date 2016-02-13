package com.project.seafoodindent.rails;

import java.util.ArrayList;
import com.google.gson.Gson;
import com.project.seafoodindent.model.Data;
import com.project.seafoodindent.model.JsonData;

public class WebSocketRailsEvent {

	private String name;
	private JsonData attr;
	private String id;
	private String channel;
	private Data data;
	private String connectionId;
	private boolean success;
	private boolean result;
	private EventCompleteCallBack successCallBack;
	private EventCompleteCallBack failureCallBack;

	public WebSocketRailsEvent(ArrayList<Object> data) {

		name = (String) data.get(0);
		attr = (JsonData) data.get(1);

		if (attr != null) {
			if (null != attr.id ) {
				this.id = attr.id;
			} else {
				this.id =  String.valueOf((int)(Math.random() * 100000));
			}

			if (attr.channel != null) {
				this.channel = attr.channel;
			}

			if (attr.data != null) {
				this.data = attr.data;
				
				if (attr.data.connection_id != null) {
					this.connectionId = attr.data.connection_id;
				} 
			}	
		}

		this.successCallBack = null;
		this.failureCallBack = null;
	}

	public WebSocketRailsEvent(ArrayList<Object> data,
			EventCompleteCallBack success, EventCompleteCallBack failure) {
		name = (String) data.get(0);
		attr = (JsonData) data.get(1);

		if (attr != null) {
			if (null != attr.id ) {
				this.id = attr.id;
			} else {
				this.id =  String.valueOf((int)(Math.random() * 100000));
			}

			if (attr.channel != null) {
				this.channel = attr.channel;
			}

			if (attr.data != null) {
				this.data = attr.data;
				
				if (attr.data.connection_id != null) {
					this.connectionId = attr.data.connection_id;
				} 
			}	
		}
		this.successCallBack = success;
		this.failureCallBack = failure;
	}

	public boolean isChannel() {
		return (channel!=null);
	}

	public boolean isResult() {
		return result;
	}

	public boolean isPing() {
		return name.equals("websocket_rails.ping");
	}

	public String serialize() {
		StringBuffer serializeString = new StringBuffer();
		serializeString.append("[");
		serializeString.append(this.name);
		serializeString.append(",");
		JsonData msgData = getAttr();
		msgData.id = String.valueOf((int)(Math.random() * 100000));
		msgData.channel = this.channel;
		Gson gson = new Gson();
		String jsonString = gson.toJson(msgData);
		serializeString.append(jsonString);
		serializeString.append("]");
		
		System.out.println("send string is ==="+serializeString.toString());
		return serializeString.toString();
	}

	/**
	 * 把HashMap转换成json字符串
	 */
	/*
	public static String map2Json(HashMap<String, Object> msgData) {

		if (msgData.isEmpty())
			return "{}";

		Iterator<Entry<String, Object>> msgs = msgData.entrySet().iterator();
		StringBuffer json = new StringBuffer();
		json.append("{");
		while (msgs.hasNext()) {
			Entry<String, Object> info = msgs.next();
			String key = info.getKey();
			String value = info.getValue().toString();
			json.append("\"" + key + "\"");
			json.append(":");
			json.append("\"" + value + "\"");
			json.append(",");

		}
		json.deleteCharAt(json.length() - 1);
		json.append("}");

		return json.toString();
	}
	*/

	public void runCallBacks(boolean success, Data eventData) {
		if (success && successCallBack != null) {
			successCallBack.receiveData(eventData);
		} 
		else {
			if (failureCallBack != null) {
				failureCallBack.receiveData(eventData);
			}
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 获取各个属性
	 * 
	 * @return
	 */
	public JsonData getAttr() {	
		return attr;
	}

	public void setAttr(JsonData attr) {
		this.attr = attr;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public Data getData() {
		return data;
	}

	public void setData(Data data) {
		this.data = data;
	}

	public String getConnectionId() {
		return connectionId;
	}

	public void setConnectionId(String connectionId) {
		this.connectionId = connectionId;
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public void setResult(boolean result) {
		this.result = result;
	}

	public EventCompleteCallBack getSuccessCallBack() {
		return successCallBack;
	}

	public void setSuccessCallBack(EventCompleteCallBack successCallBack) {
		this.successCallBack = successCallBack;
	}

	public EventCompleteCallBack getFailureCallBack() {
		return failureCallBack;
	}

	public void setFailureCallBack(EventCompleteCallBack failureCallBack) {
		this.failureCallBack = failureCallBack;
	}

}
