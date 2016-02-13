package com.project.seafoodindent.adapter;

import java.util.ArrayList;
import java.util.List;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import com.project.seafoodindent.R;
import com.project.seafoodindent.model.GrabIndent;
import com.project.seafoodindent.model.Order;
import com.project.seafoodindent.tools.HttpUtils;
import com.project.seafoodindent.tools.JsonParse;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 
 * 附近的订单的数据适配器
 * 
 * @author 师春雷
 * 
 */
public class NearbyAdapter extends BaseAdapter {

	private final static String TAG = "NearbyAdapter";

	private Context context;

	ProgressDialog dialog;

	private List<Order> list = new ArrayList<Order>();

	private LayoutInflater inflater;

	GrabIndent grab = new GrabIndent();

	private SharedPreferences sp;

	public NearbyAdapter(Context context, List<Order> list) {
		this.context = context;
		this.list = list;
		inflater = LayoutInflater.from(context);

		sp = this.context.getSharedPreferences("Info", Context.MODE_PRIVATE);

		dialog = new ProgressDialog(context);
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();

			convertView = inflater.inflate(R.layout.nearby_orders_item, null);

			holder.name = (TextView) convertView
					.findViewById(R.id.nearbyorders_shop_name);// 商铺名称
			holder.startLocation = (TextView) convertView
					.findViewById(R.id.nearbyorder_start_location);// 订单起始位置
			holder.endLocation = (TextView) convertView
					.findViewById(R.id.nearbyorder_stop_location);// 订单目的位置
			holder.price = (TextView) convertView
					.findViewById(R.id.nearbyorders_price);// 订单价格
			holder.submit = (Button) convertView
					.findViewById(R.id.nearbyorders_submit);// 提交订单

			holder.submit.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View arg0) {
					String indentID = list.get(position).id;
					String token = sp.getString("token", "");

					dialog.setMessage("请稍候...");
					dialog.show();
					OrderIndent(indentID, token);
				}
			});

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		String _name = list.get(position).order_name;
		String mer_address = list.get(position).mer_address;
		String cus_address = list.get(position).cus_address;
		String _price = list.get(position).price;

		holder.name.setText(_name);
		holder.startLocation.setText(mer_address);
		holder.endLocation.setText(cus_address);
		holder.price.setText(_price + " 元");

		return convertView;
	}

	/**
	 * 发送接单网络请求的方法
	 * 
	 * @param indentID
	 *            订单ID
	 */
	private void OrderIndent(String indentID, String token) {
		FinalHttp fh = new FinalHttp();
		AjaxParams params = new AjaxParams();

		params.put("id", indentID);
		params.put("auth_token", token);

		fh.post(HttpUtils.ROOT_URL + HttpUtils.ORDER_INDENT, params,
				new AjaxCallBack<Object>() {
					@Override
					public void onSuccess(Object t) {
						super.onSuccess(t);

						String result = t.toString();

						Log.i(TAG, result);

						grab = JsonParse.ParseGrabIndent(result);

						if (grab.result.equals("true")) {
							Toast.makeText(context, "抢单成功！", Toast.LENGTH_SHORT)
									.show();
						} else if (grab.result.equals("false")) {
							Toast.makeText(context, "抢单失败！", Toast.LENGTH_SHORT)
									.show();
						} else {
							Toast.makeText(context, "网络异常，请检查网络后重试！",
									Toast.LENGTH_SHORT).show();
						}

						dialog.dismiss();
					}

					@Override
					public void onFailure(Throwable t, int erroNo, String strMsg) {
						super.onFailure(t, erroNo, strMsg);
						Toast.makeText(context, "网络异常，请检查网络后重试！",
								Toast.LENGTH_SHORT).show();
						dialog.dismiss();
						if (t != null) {
							Log.i(TAG, t.toString());
						}
						if (strMsg != null) {
							Log.i(TAG, strMsg);
						}
					}
				});
	}

	private class ViewHolder {
		/** 订单名称 */
		private TextView name;
		/** 订单起始位置 */
		private TextView startLocation;
		/** 订单目的位置 */
		private TextView endLocation;
		/** 订单价格 */
		private TextView price;
		/** 接单按钮 */
		private Button submit;
	}
}