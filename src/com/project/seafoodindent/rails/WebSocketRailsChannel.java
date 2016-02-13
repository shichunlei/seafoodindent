package com.project.seafoodindent.rails;

import java.util.ArrayList;
import java.util.HashMap;

import com.project.seafoodindent.model.Data;
import com.project.seafoodindent.model.JsonData;

public class WebSocketRailsChannel {

	private boolean isPrivate;
	private String eventNameString;
	private String channelName;
	private WebSocketRailsDispatcher dispatcher;
	private HashMap<String, ArrayList<EventCompleteCallBack>> callbacks;
	
	

	public WebSocketRailsChannel(String channelName,WebSocketRailsDispatcher disPatcher,boolean isPrivate){
		
		String eventName = null;
		if(isPrivate){
			eventName = "websocket_rails.subscribe_private";
		}else{
			eventName = "websocket_rails.subscribe";
		}
		
		this.channelName = channelName;
		this.dispatcher = disPatcher;
		
		ArrayList<Object> data = new ArrayList<Object>();
		data.add("\""+eventName+"\"");
		JsonData msgData = new JsonData();
		Data dataInfo = new Data();
		dataInfo.connection_id = disPatcher.getConnectionId().equals("0") ? null : disPatcher.getConnectionId();
		dataInfo.channel=channelName;
		msgData.data = dataInfo;
		data.add(msgData);
		
		WebSocketRailsEvent event = new WebSocketRailsEvent(data, null, null);
		disPatcher.triggerEvent(event);
		callbacks = new HashMap<String, ArrayList<EventCompleteCallBack>>();
	}
	
	public void bind(String eventName, EventCompleteCallBack callBack){
		if (!callbacks.containsKey(eventName)) {
			ArrayList<EventCompleteCallBack> calls = new ArrayList<EventCompleteCallBack>();
			callbacks.put(eventName, calls);
		}
		
		callbacks.get(eventName).add(callBack);
	}
	
	public void dispatch(String eventName,Data data){
		if (!callbacks.containsKey(eventName)) {
			return;
		}
		
		for (EventCompleteCallBack callback : callbacks.get(eventName)) {
			callback.receiveData(data);
		}
	}
	
	public void destroy(){
		String eventName = "websocket_rails.unsubscribe";
		
		ArrayList<Object> data = new ArrayList<Object>();
		data.add(eventName);
		JsonData msgData = new JsonData();
		msgData.channel = channelName;
		Data dataInfo = new Data();
		dataInfo.connection_id = dispatcher.getConnectionId();
		msgData.data = dataInfo;
		data.add(msgData);
		
		WebSocketRailsEvent event = new WebSocketRailsEvent(data);
		dispatcher.triggerEvent(event);
		callbacks.clear();
	}
	
	
	public String getEventNameString() {
		return eventNameString;
	}

	public void setEventNameString(String eventNameString) {
		this.eventNameString = eventNameString;
	}

	public String getChannelName() {
		return channelName;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
	}

	public WebSocketRailsDispatcher getDispatcher() {
		return dispatcher;
	}

	public void setDispatcher(WebSocketRailsDispatcher dispatcher) {
		this.dispatcher = dispatcher;
	}

	public HashMap<String, ArrayList<EventCompleteCallBack>> getCallbacks() {
		return callbacks;
	}

	public void setCallbacks(HashMap<String, ArrayList<EventCompleteCallBack>> callbacks) {
		this.callbacks = callbacks;
	}

	public boolean isPrivate() {
		return isPrivate;
	}

	public void setPrivate(boolean isPrivate) {
		this.isPrivate = isPrivate;
	}
}
