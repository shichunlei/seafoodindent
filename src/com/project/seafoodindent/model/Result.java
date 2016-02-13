package com.project.seafoodindent.model;

/**
 * 登录返回结果模型类
 * 
 * @author 师春雷
 * 
 */
public class Result {
	/** 返回码 */
	public String result;
	/** 用户信息 */
	public Courier courier;
	/** 错误提示 */
	public String error;

	@Override
	public String toString() {
		return "Result [result=" + result + ", courier=" + courier + ", error="
				+ error + "]";
	}
}