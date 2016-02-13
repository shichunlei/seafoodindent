package com.project.seafoodindent;

/**
 * 
 * 说明类
 * 
 * @author LiuZhenYu
 * 
 */
public class Info {

	/**
	 * 1、登录
	 * 
	 * 传入参数：email、password（post方式）
	 * 
	 * 
	 * 登陆成功返回数据：
	 * 
	 * {result: 200, user: {id: 2 , mail: 12@1.com, name: "李四"， mobile:34566 }}
	 * 
	 * 登录失败返回数据：
	 * 
	 * 密码错误：{result: 400, error: "password_error"}
	 * 
	 * 邮箱错误：{result:400, error: "email_error"}
	 */

	/**
	 * 2、我的订单：
	 * 
	 * 传入参数：快递员的token
	 * 
	 * 返回Json列表 [{id: 1,title: "abc", price: 123,name: "客户名字"，mobile: "客户电话"，
	 * mer_address: "派发起始地址"， address: "目标地址"}]
	 */

	/**
	 * 3、推送附近订单列表：
	 * 
	 * [["order",{"id":null,"channel":null,"user_id":null,"data":{"order":[{"id"
	 * :2,"title":"成都小吃","price":12,"name":"22","mer_mobile":"443534345",
	 * "mer_address": "中关村" ,"cus_mobile":"13240939168","cus_address":"北京海淀"}]
	 * },"success":null,"result":null,"token":null,"server_token":null}]]
	 */

	/**
	 * 4、接单：
	 * 
	 * 传入参数：订单的id、快递员的token
	 * 
	 * 接单返回数据:
	 * 
	 * 抢单失败: {result: false}
	 * 
	 * 抢单成功: {"result":true}
	 */
}