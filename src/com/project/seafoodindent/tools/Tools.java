package com.project.seafoodindent.tools;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.annotation.SuppressLint;

/**
 * 工具类，一些全局公用的工具类
 * 
 * @author 师春雷
 * 
 */
public class Tools {

	/**
	 * 邮箱格式验证
	 * 
	 * @param email
	 * @return
	 */
	public static boolean isEmail(String email) {
		String str = "^([a-zA-Z0-9]*[-_]?[a-zA-Z0-9]+)*@([a-zA-Z0-9]*[-_]?[a-zA-Z0-9]+)+[\\.][A-Za-z]{2,3}([\\.][A-Za-z]{2})?$";
		Pattern p = Pattern.compile(str);
		Matcher m = p.matcher(email);
		return m.matches();
	}

	/**
	 * 判空
	 * 
	 * @param msg
	 * @return
	 */
	public static boolean isEmpty(String msg) {
		if (null == msg.trim() || "".equals(msg))
			return true;
		else
			return false;
	}

	/**
	 * 获取刷新更新时间
	 * 
	 * MM-dd HH:mm 格式
	 * 
	 * @return
	 */
	@SuppressLint("SimpleDateFormat")
	public static String updateTime() {
		SimpleDateFormat formatter = new SimpleDateFormat("MM-dd HH:mm");
		Date curDate = new Date(System.currentTimeMillis());
		String updateTime = formatter.format(curDate);
		return updateTime;
	}
}
