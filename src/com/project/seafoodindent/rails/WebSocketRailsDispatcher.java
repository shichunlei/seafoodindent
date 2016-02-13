package com.project.seafoodindent.rails;

import java.util.ArrayList;
import java.util.HashMap;

import com.project.seafoodindent.model.Data;
import com.project.seafoodindent.model.JsonData;

public class WebSocketRailsDispatcher {

	private String state;
	private String url;
	private HashMap<String, WebSocketRailsChannel> channels;
	private String connectionId;
	private HashMap<String, WebSocketRailsEvent> queue;
	private HashMap<String, ArrayList<EventCompleteCallBack>> callbacks;
	private WebSocketRailsConnection connection;

	public WebSocketRailsDispatcher(String url) {
		this.url = url;
		this.state = "connecting";
		this.channels = new HashMap<String, WebSocketRailsChannel>();
		this.queue = new HashMap<String, WebSocketRailsEvent>();
		this.callbacks = new HashMap<String, ArrayList<EventCompleteCallBack>>();
		this.connection = new WebSocketRailsConnection(url, this);
		this.connectionId = "0";

	}

	public void dispatch(WebSocketRailsEvent event) {
		if (!callbacks.containsKey(event.getName())) {
			return;
		}

		for (EventCompleteCallBack callback : callbacks.get(event.getName())) {
			callback.receiveData(event.getData());
		}
	}

	public void dispatchChannel(WebSocketRailsEvent event) {
		if (!channels.containsKey(event.getChannel())) {
			return;
		}

		channels.get(event.getChannel()).dispatch(event.getName(),
				event.getData());
	}

	public void newMessage(ArrayList<Object> data) {
		
			
			WebSocketRailsEvent event = new WebSocketRailsEvent(data);

			if (event.isResult()) {
				if (this.queue.containsKey(event.getId())) {
					this.queue.get(event.getId()).runCallBacks(
							event.isSuccess(), event.getData());
					this.queue.remove(event.getId());
				}
			} else if (event.isChannel()) {
				dispatchChannel(event);
			} else if (event.isPing()) {
				pong();
			} else {
				dispatch(event);
			}

			if (this.state.equals("connecting")
					&& event.getName().equals("client_connected")) {
				connectionEstablished(event.getData());
			}
		
	}

	public void connectionEstablished(Data data) {
		this.state = "connected";
		
		this.connectionId = data.connection_id == null ? "0" : data.connection_id;
		this.connection.flushQueue(this.connectionId);
	}

	public void pong() {
		ArrayList<Object> data = new ArrayList<Object>();
		data.add("\"websocket_rails.pong\"");
		JsonData dataMsg = new JsonData();
		dataMsg.id = String.valueOf((int)(Math.random() * 100000));
		data.add(dataMsg);
		WebSocketRailsEvent pong = new WebSocketRailsEvent(data);
		connection.trigger(pong);
	}

	public void bind(String eventName, EventCompleteCallBack callback) {
		if (!this.callbacks.containsKey(eventName)) {
			ArrayList<EventCompleteCallBack> calls = new ArrayList<EventCompleteCallBack>();
			callbacks.put(eventName, calls);
		}

		callbacks.get(eventName).add(callback);
	}

	public void trigger(String eventName, JsonData data,
			EventCompleteCallBack success, EventCompleteCallBack failure) {
		ArrayList<Object> newData = new ArrayList<Object>();
		newData.add(eventName);
		newData.add(data);
		WebSocketRailsEvent event = new WebSocketRailsEvent(newData, success,
				failure);

		queue.put(event.getId(), event);
	}

	public void triggerEvent(WebSocketRailsEvent event) {
		if (queue.containsKey(event.getId())
				&& queue.get(event.getId()).equals(event)) {
			return;
		}

		queue.put(event.getId(), event);
		connection.trigger(event);
	}

	public WebSocketRailsChannel subcribe(String channelName) {
		if (channels.containsKey(channelName)) {
			return channels.get(channelName);
		}
		WebSocketRailsChannel channel = new WebSocketRailsChannel(channelName,
				this, false);
		channels.put(channelName, channel);
		return channel;
	}

	public void unsubscribe(String channelName) {
		if (!channels.containsKey(channelName)) {
			return;
		}

		channels.get(channelName).destroy();
		channels.remove(channelName);
	}

	public void disconnect() {
		connection.disconnect();
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public HashMap<String, WebSocketRailsChannel> getChannels() {
		return channels;
	}

	public void setChannels(HashMap<String, WebSocketRailsChannel> channels) {
		this.channels = channels;
	}

	public String getConnectionId() {
		return connectionId;
	}

	public void setConnectionId(String connectionId) {
		this.connectionId = connectionId;
	}

	public HashMap<String, WebSocketRailsEvent> getQueue() {
		return queue;
	}

	public void setQueue(HashMap<String, WebSocketRailsEvent> queue) {
		this.queue = queue;
	}

	public HashMap<String, ArrayList<EventCompleteCallBack>> getCallbacks() {
		return callbacks;
	}

	public void setCallbacks(
			HashMap<String, ArrayList<EventCompleteCallBack>> callbacks) {
		this.callbacks = callbacks;
	}

	public WebSocketRailsConnection getConnection() {
		return connection;
	}

	public void setConnection(WebSocketRailsConnection connection) {
		this.connection = connection;
	}

}
