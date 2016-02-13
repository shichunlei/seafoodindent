package com.project.seafoodindent.fragment;

import java.util.ArrayList;
import java.util.List;

import com.project.seafoodindent.R;
import com.project.seafoodindent.adapter.NearbyAdapter;
import com.project.seafoodindent.model.Data;
import com.project.seafoodindent.model.Order;
import com.project.seafoodindent.rails.EventCompleteCallBack;
import com.project.seafoodindent.rails.WebSocketRailsChannel;
import com.project.seafoodindent.rails.WebSocketRailsDispatcher;
import com.project.seafoodindent.websocket.WebSocketClient;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

/***
 * 附近订单
 * 
 * @author 师春雷
 * 
 */
public class NearbyFragment extends Fragment {

	private final static String TAG = "NearbyFragment";

	private ListView listView;

	private TextView no_order;

	WebSocketClient client;

	private NearbyAdapter adapter;
	private List<Order> orderList = new ArrayList<Order>();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_nearby, null);

		listView = (ListView) view.findViewById(R.id.nearby_list);

		no_order = (TextView) view.findViewById(R.id.no_nearby_order);

		websocketMsg();

		return view;
	}

	/**
	 * 推送
	 */
	private void websocketMsg() {

		String uri = "ws://192.168.51.145:3000/websocket";

		WebSocketRailsDispatcher dispatcher = new WebSocketRailsDispatcher(uri);

		dispatcher.bind("order", new EventCompleteCallBack() {

			@Override
			public void receiveData(final Data data) {

				getActivity().runOnUiThread(new Runnable() {

					@Override
					public void run() {
						if (data != null) {
							setAdapter(data);
						}
					}
				});
			}
		});

		WebSocketRailsChannel orderChannel = dispatcher.subcribe("order");

		orderChannel.bind("order", new EventCompleteCallBack() {

			@Override
			public void receiveData(final Data data) {

				getActivity().runOnUiThread(new Runnable() {

					@Override
					public void run() {
						
						if (data != null) {
							setAdapter(data);
						}
					}
				});
			}
		});

	}

	private void setAdapter(Data data) {
		
		if (data.order.size() > 0) {
			no_order.setVisibility(View.GONE);

			orderList.clear();
			orderList.addAll(data.order);

			Log.i(TAG, "orderList is : " + orderList.toString());

			adapter = new NearbyAdapter(getActivity(), orderList);
			adapter.notifyDataSetChanged();
			listView.setAdapter(adapter);
		} else {
			no_order.setVisibility(View.VISIBLE);
		}
	}
}