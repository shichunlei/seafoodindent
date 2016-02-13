package com.project.seafoodindent.rails;

import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.http.message.BasicNameValuePair;

import android.util.Log;

import com.project.seafoodindent.model.JsonData;
import com.project.seafoodindent.tools.JsonParse;
import com.project.seafoodindent.websocket.WebSocketClient;
import com.project.seafoodindent.websocket.WebSocketClient.Listener;

public class WebSocketRailsConnection {

	private String url;
	private WebSocketRailsDispatcher dispatcher;
	private ArrayList<WebSocketRailsEvent> messageQueue;
	private WebSocketClient client;
	private final static String TAG = "WebSocketRailsConnection";

	public WebSocketRailsConnection(String url,
			final WebSocketRailsDispatcher dispatcher) {

		this.url = url;
		this.dispatcher = dispatcher;
		this.messageQueue = new ArrayList<WebSocketRailsEvent>();

		List<BasicNameValuePair> extraHeaders = Arrays
				.asList(new BasicNameValuePair("Cookie", "session=14cells"));

		URI uri = URI.create(url);

		Listener listener = new WebSocketClient.Listener() {

			@Override
			public void onMessage(byte[] data) {
				// TODO Auto-generated method stub
			}

			@Override
			public void onMessage(final String message) {
				Log.i(TAG, "receive msg is ****" + message + "******");

				ArrayList<Object> data = jsonToArray(message);
				dispatcher.newMessage(data);
			}

			@Override
			public void onError(Exception error) {
				// TODO Auto-generated method stub
				error.printStackTrace();
				Log.i(TAG, "error is : " + error.toString());
				ArrayList<Object> data = new ArrayList<Object>();
				data.add("connection_error");
				JsonData msgData = new JsonData();
				data.add(msgData);
				WebSocketRailsEvent closeEvent = new WebSocketRailsEvent(data);
				dispatcher.setState("disconnected");
				dispatcher.dispatch(closeEvent);
			}

			@Override
			public void onDisconnect(int code, String reason) {
				// TODO Auto-generated method stub
				Log.i(TAG, "reason is : " + reason);
				Log.i(TAG, "code is : " + code);
				ArrayList<Object> data = new ArrayList<Object>();
				data.add("connection_closed");
				JsonData msgData = new JsonData();
				data.add(msgData);
				WebSocketRailsEvent closeEvent = new WebSocketRailsEvent(data);
				dispatcher.setState("disconnected");
				dispatcher.dispatch(closeEvent);
			}

			@Override
			public void onConnect() {
			}
		};
		this.client = new WebSocketClient(uri, listener, extraHeaders);
		this.client.connect();
	}

	public void trigger(WebSocketRailsEvent event) {
		if (!this.dispatcher.getState().equals("connected")) {
			this.messageQueue.add(event);
		} else {
			client.send(event.serialize());
		}
	}

	// TODO
	public void flushQueue(String id) {

		for (WebSocketRailsEvent event : messageQueue) {
			String eventString = event.serialize();
			client.send(eventString);
		}
	}

	public void disconnect() {
		client.disconnect();
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public ArrayList<Object> jsonToArray(String message) {
		ArrayList<Object> data = new ArrayList<Object>();
		// 找到第一个 “,” 的位置
		int index = message.indexOf(",");
		// 截取第三位到第一个“,” 之前的字符串
		String string = message.substring(3, index - 1);
		data.add(string);

		int length = message.length();

		String message0 = message.substring(index + 1, length - 2);

		JsonData messageData = JsonParse.ParserJsonData(message0);
		data.add(messageData);

		return data;
	}

}
