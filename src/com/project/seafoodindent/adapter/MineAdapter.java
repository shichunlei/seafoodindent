package com.project.seafoodindent.adapter;

import java.util.ArrayList;
import java.util.List;

import com.project.seafoodindent.R;
import com.project.seafoodindent.model.Order;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

/**
 * 
 * 我的订单的数据适配器
 * 
 * @author 师春雷
 * 
 */
public class MineAdapter extends BaseAdapter {

	private final static String TAG = "MineAdapter";

	private Context context;

	private List<Order> list = new ArrayList<Order>();

	private LayoutInflater inflater;

	/** 商家电话 */
	private String MerchantNumber;
	/** 顾客电话 */
	private String ClientNumber;

	private Intent intent;

	public MineAdapter(Context context, List<Order> list) {
		this.context = context;
		this.list = list;
		inflater = LayoutInflater.from(context);
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

			convertView = inflater.inflate(R.layout.mine_orders_item, null);

			holder.name = (TextView) convertView
					.findViewById(R.id.mineorders_shop_name);// 商铺名称

			holder.startLocation = (TextView) convertView
					.findViewById(R.id.mineorder_start_location);// 订单起始位置

			holder.endLocation = (TextView) convertView
					.findViewById(R.id.mineorder_stop_location);// 订单目的位置

			holder.price = (TextView) convertView
					.findViewById(R.id.mineorder_price);// 订单价格

			holder.call = (Button) convertView
					.findViewById(R.id.mineorder_call);// 打电话

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

		Log.i(TAG, "订单名称：" + _name + "\n商铺位置：" + mer_address + "\n顾客位置："
				+ cus_address + "\n价格：" + _price);

		holder.call.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				MerchantNumber = list.get(position).mer_mobile;

				if (null == MerchantNumber || MerchantNumber.equals("")) {
					MerchantNumber = "暂无商家电话";
				}

				ClientNumber = list.get(position).cus_mobile;

				if (null == ClientNumber || ClientNumber.equals("")) {
					ClientNumber = "暂无顾客电话";
				}

				new AlertDialog.Builder(context)
						.setTitle("选择号码")
						.setItems(
								new String[] { "商家:" + MerchantNumber,
										"顾客:" + ClientNumber },
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										switch (which) {
										case 0:
											if (!MerchantNumber
													.equals("暂无商家电话")) {
												intent = new Intent(
														Intent.ACTION_CALL,
														Uri.parse("tel:"
																+ MerchantNumber));
												context.startActivity(intent);
											}
											break;
										case 1:
											if (!ClientNumber.equals("暂无顾客电话")) {
												intent = new Intent(
														Intent.ACTION_CALL,
														Uri.parse("tel:"
																+ ClientNumber));
												context.startActivity(intent);
											}
											break;
										}
									}
								}).create().show();
			}
		});

		return convertView;
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
		/** 拨打电话按钮 */
		private Button call;
	}
}
