package com.project.seafoodindent.fragment;

import java.util.ArrayList;
import java.util.List;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import com.project.seafoodindent.R;
import com.project.seafoodindent.adapter.MineAdapter;
import com.project.seafoodindent.customview.PullToRefreshView;
import com.project.seafoodindent.customview.PullToRefreshView.OnFooterRefreshListener;
import com.project.seafoodindent.customview.PullToRefreshView.OnHeaderRefreshListener;
import com.project.seafoodindent.model.Order;
import com.project.seafoodindent.tools.HttpUtils;
import com.project.seafoodindent.tools.JsonParse;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

/***
 * 我的订单
 * 
 * @author 师春雷
 * 
 */
public class MineFragment extends Fragment implements OnHeaderRefreshListener,
		OnFooterRefreshListener {

	private final static String TAG = "MineFragment";
	private ListView listView;
	private MineAdapter adapter;

	private SharedPreferences sp;

	private String token;

	private PullToRefreshView mPullToRefreshView;

	private List<Order> list;
	private List<Order> orderList = new ArrayList<Order>();

	private TextView noOrder;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.fragment_mine, null);

		init(view);

		return view;
	}

	/**
	 * 获取我的订单信息
	 * 
	 * @param token
	 */
	public void getOrderList(String token) {
		FinalHttp fh = new FinalHttp();
		AjaxParams params = new AjaxParams();
		params.put("auth_token", token);
		fh.configTimeout(10000);
		fh.get(HttpUtils.ROOT_URL + HttpUtils.MY_INDENT, params,
				new AjaxCallBack<Object>() {
					@Override
					public void onSuccess(Object t) {
						super.onSuccess(t);

						String result = t.toString();

						Log.i(TAG, result);

						list = JsonParse.ParseMineOrderList(result);

						if (list.size() > 0) {
							Log.i(TAG, list.size() + "");
							noOrder.setVisibility(View.GONE);
							setAdapter();
						} else {
							noOrder.setVisibility(View.VISIBLE);
						}
					}

					@Override
					public void onFailure(Throwable t, int erroNo, String strMsg) {
						super.onFailure(t, erroNo, strMsg);
					}
				});
	}

	protected void setAdapter() {
		if (orderList != null) {
			orderList.clear();
		}

		orderList.addAll(list);
		list.clear();

		adapter = new MineAdapter(getActivity(), orderList);
		listView.setAdapter(adapter);
	}

	private void init(View view) {
		listView = (ListView) view.findViewById(R.id.mine_list);

		noOrder = (TextView) view.findViewById(R.id.no_mine_order);

		sp = getActivity().getSharedPreferences("Info", Context.MODE_PRIVATE);
		token = sp.getString("token", "");

		mPullToRefreshView = (PullToRefreshView) view
				.findViewById(R.id.pull_refresh);
		mPullToRefreshView.setOnHeaderRefreshListener(this);
		mPullToRefreshView.setOnFooterRefreshListener(this);
	}

	@Override
	public void onFooterRefresh(PullToRefreshView view) {
		mPullToRefreshView.postDelayed(new Runnable() {

			@Override
			public void run() {
				mPullToRefreshView.onFooterRefreshComplete();
			}
		}, 2000);
	}

	@Override
	public void onHeaderRefresh(PullToRefreshView view) {
		mPullToRefreshView.postDelayed(new Runnable() {

			@Override
			public void run() {
				getOrderList(token);

				mPullToRefreshView.onHeaderRefreshComplete();
			}
		}, 2000);
	}

}
