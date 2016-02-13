package com.project.seafoodindent.customview;

import com.project.seafoodindent.R;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

/**
 * 进度条对话框
 * 
 * @author 师春雷
 * 
 */
public class ProgressDialog extends Dialog {

	private static ProgressDialog myProgressDialog;

	private ProgressDialog(Context context, int theme) {
		super(context, theme);
	}

	/**
	 * 
	 * @param context
	 *            需要显示的文字 ；传入null或者""则不显示文字
	 * @param message
	 * @return
	 */
	public static ProgressDialog show(Context context, String title,
			String message, boolean cancelable) {
		myProgressDialog = new ProgressDialog(context,
				R.style.CustomProgressDialog);
		myProgressDialog.setContentView(R.layout.progress_dialog);
		myProgressDialog.setCancelable(cancelable);
		myProgressDialog.getWindow().getAttributes().gravity = Gravity.CENTER;

		TextView tv_message = (TextView) myProgressDialog
				.findViewById(R.id.tv_message);
		if (null == message || "".equals(message))
			tv_message.setVisibility(View.GONE);
		else
			tv_message.setText(message);
		return myProgressDialog;
	}

	public static ProgressDialog show(Context context, String title,
			String message) {
		return show(context, title, message, false);
	}

	public static ProgressDialog show(Context context, String message) {
		return show(context, "", message, false);
	}

	public static ProgressDialog show(Context context) {
		return show(context, "");
	}

	@Override
	public void cancel() {
		super.cancel();
		myProgressDialog = null;
	}

	@Override
	public void dismiss() {
		super.dismiss();
		myProgressDialog = null;
	}

}