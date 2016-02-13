package com.project.seafoodindent.activity;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.project.seafoodindent.R;
import com.project.seafoodindent.customview.ProgressDialog;
import com.project.seafoodindent.model.Result;
import com.project.seafoodindent.tools.HttpUtils;
import com.project.seafoodindent.tools.JsonParse;
import com.project.seafoodindent.tools.Tools;

/**
 * 登录界面
 * 
 * @author 师春雷
 * 
 */
public class LoginActivity extends Activity {

	private final static String TAG = "LoginActivity";

	/** 用户名输入框 */
	private EditText userName;
	/** 密码输入框 */
	private EditText userPassword;
	/** 登录 */
	private Button login;
	/** 用户名 */
	private String name;
	/** 密码 */
	private String password;

	ProgressDialog progressDialog;

	private SharedPreferences sp;

	private Editor edit;

	private String token;

	private Result loginInfo;

	private Intent intent;

	private Context context;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		context = LoginActivity.this;

		init();

		setListener();
	}

	/**
	 * 初始化方法
	 */
	private void init() {
		sp = getSharedPreferences("Info", Context.MODE_PRIVATE);
		edit = sp.edit();

		userName = (EditText) findViewById(R.id.et_username);
		userPassword = (EditText) findViewById(R.id.et_password);

		login = (Button) findViewById(R.id.login);

		name = sp.getString("username", "");

		if (name.length() > 0) {
			userName.setText(name);
		}

		progressDialog = ProgressDialog.show(this, "正在登录...");

		intent = new Intent();
	}

	/** 按钮点击事件 */
	private void setListener() {
		login.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				name = userName.getText().toString();// 获取输入的账号
				password = userPassword.getText().toString();// 获取输入的密码

				if (Tools.isEmpty(name)) {
					Toast.makeText(context, "账户不能为空！", Toast.LENGTH_SHORT)
							.show();
				} else if (!Tools.isEmail(name)) {
					Toast.makeText(context, "邮箱格式错误！", Toast.LENGTH_SHORT)
							.show();
				} else if (Tools.isEmpty(password)) {
					Toast.makeText(context, "密码不能为空！", Toast.LENGTH_SHORT)
							.show();
				} else {
					progressDialog.show();
					Login(name, password);
				}
			}
		});
	}

	/**
	 * 登陆请求
	 * 
	 * @param userName
	 *            用户名
	 * @param password
	 *            密码
	 */
	private void Login(final String userName, String password) {
		AjaxParams params = new AjaxParams();
		params.put("email", userName);
		params.put("password", password);

		FinalHttp fh = new FinalHttp();
		fh.configTimeout(10000);
		fh.post(HttpUtils.ROOT_URL + HttpUtils.LOGIN_URL, params,
				new AjaxCallBack<Object>() {

					@Override
					public void onLoading(long count, long current) {
						super.onLoading(count, current);
					}

					@Override
					public void onSuccess(Object t) {
						super.onSuccess(t);
						String str = t.toString();
						if (str != null) {
							Log.i(TAG, str);
							loginInfo = JsonParse.ParseLoginInfo(str);

							if (loginInfo != null) {

								Log.i(TAG, loginInfo.toString());

								if (loginInfo.result.equals("200")) {
									token = loginInfo.courier.auth_token;
									edit.putString("token", token);
									edit.putString("username", userName);
									edit.commit();

									Toast.makeText(context, "登录成功！",
											Toast.LENGTH_SHORT).show();

									progressDialog.dismiss();
									intent.setClass(getApplicationContext(),
											MainActivity.class);
									startActivity(intent);
									finish();
								} else if (loginInfo.result.equals("400")) {
									if (loginInfo.error
											.equals("password_error")) {
										Toast.makeText(context, "密码错误！",
												Toast.LENGTH_SHORT).show();
									} else if (loginInfo.error
											.equals("email_error")) {
										Toast.makeText(context, "邮箱错误！",
												Toast.LENGTH_SHORT).show();
									}

									progressDialog.dismiss();
								} else {
									Toast.makeText(context, "网络异常，请稍候再试！",
											Toast.LENGTH_SHORT).show();
									progressDialog.dismiss();
								}
							}
						}
					}

					@Override
					public void onFailure(Throwable t, int erroNo, String strMsg) {
						super.onFailure(t, erroNo, strMsg);
						Toast.makeText(context, "网络错误，请稍候再试！",
								Toast.LENGTH_SHORT).show();
						progressDialog.dismiss();
						if (t != null) {
							Log.i(TAG, t.toString());
						}
						if (strMsg != null) {
							Log.i(TAG, strMsg);
						}
					}
				});
	}

}