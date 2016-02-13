package com.project.seafoodindent.tools;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.project.seafoodindent.model.Data;
import com.project.seafoodindent.model.GrabIndent;
import com.project.seafoodindent.model.JsonData;
import com.project.seafoodindent.model.Result;
import com.project.seafoodindent.model.Order;

public class JsonParse {

	private final static String TAG = "JsonParse";

	private static Gson gson = new Gson();

	/**
	 * 解析登录信息
	 * 
	 * @param result
	 * @return
	 */
	public static Result ParseLoginInfo(String string) {

		Result result = gson.fromJson(string, Result.class);

		return result;
	}

	/**
	 * 解析订单列表
	 * 
	 * @param result
	 * @return
	 */
	public static Data ParseOrderList(String result) {
		Data data = gson.fromJson(result, Data.class);

		return data;
	}

	/**
	 * 解析我的订单列表
	 * 
	 * @param result
	 * @return
	 */
	public static List<Order> ParseMineOrderList(String result) {
		List<Order> list = null;

		list = gson.fromJson(result, new TypeToken<List<Order>>() {
		}.getType());

		return list;
	}

	/**
	 * 解析抢单
	 * 
	 * @param result
	 * @return
	 */
	public static GrabIndent ParseGrabIndent(String result) {
		try {
			JSONArray obj = new JSONArray("[" + result + "]");
			JSONObject o = obj.getJSONObject(0);
			GrabIndent grab = new GrabIndent();

			grab.result = o.getString("result");

			return grab;
		} catch (JSONException e) {
			e.printStackTrace();
			Log.e(TAG, "ParseGrabIndent error");
			return null;
		}
	}

	/**
	 * 解析推送订单信息
	 * 
	 * @param str
	 * @return
	 */
	public static JsonData ParserJsonData(String str) {
		try {

			Gson gson = new Gson();
			JsonData ps = gson.fromJson(str, JsonData.class);
			return ps;
		} catch (Exception e) {
			Log.e("ParserJsonData", "error");
			e.printStackTrace();
		}
		return null;
	}
}