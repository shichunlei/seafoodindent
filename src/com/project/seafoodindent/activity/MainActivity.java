package com.project.seafoodindent.activity;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.project.seafoodindent.R;
import com.project.seafoodindent.customview.MainViewPager;
import com.project.seafoodindent.fragment.MineFragment;
import com.project.seafoodindent.fragment.NearbyFragment;

/**
 * 
 * 主界面
 * 
 * @author 师春雷
 * 
 */
public class MainActivity extends FragmentActivity {

	private MainViewPager viewPager;

	private List<Fragment> fragmentsList;

	private FragmentManager fm;
	/** Title：附近订单 */
	private TextView Nearby;
	/** Title：我的订单 */
	private TextView Mine;
	/** 碎片：附近订单 */
	private NearbyFragment nearbyFragment;
	/** 碎片：我的订单 */
	private MineFragment mineFragment;

	private TextView line1;

	private TextView line2;

	private long mExitTime;
	
	private SharedPreferences sp;
	
	private String token;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		init();
		initPager();
		titleClick();
	}

	/** 按钮点击事件 */
	private void titleClick() {
		Nearby.setOnClickListener(new MyOnClickListener(0));
		Mine.setOnClickListener(new MyOnClickListener(1));
	}

	/** 页面滑动 */
	private void initPager() {
		viewPager.setOffscreenPageLimit(2);
		viewPager.setOnPageChangeListener(new MyOnPageChangeListener());
		viewPager.setAdapter(new MyPagerAdapter());
	}

	/** 初始化方法 */
	private void init() {
		fm = getSupportFragmentManager();
		viewPager = (MainViewPager) findViewById(R.id.viewPager_main);

		Nearby = (TextView) findViewById(R.id.nearbyText);
		Mine = (TextView) findViewById(R.id.mineText);

		Nearby.setTextColor(getResources().getColor(R.color.myBlue));
		Mine.setTextColor(getResources().getColor(R.color.black));

		line1 = (TextView) findViewById(R.id.line1);
		line2 = (TextView) findViewById(R.id.line2);

		line1.setBackgroundResource(R.drawable.slide);
		line2.setBackgroundColor(getResources().getColor(R.color.white));
		
		sp = getSharedPreferences("Info",Context.MODE_PRIVATE);

		fragmentsList = new ArrayList<Fragment>();

		mineFragment = new MineFragment();
		nearbyFragment = new NearbyFragment();

		fragmentsList.add(nearbyFragment);
		fragmentsList.add(mineFragment);
		
		token = sp.getString("token", "");
	}

	/**
	 * 重写返回键
	 */
	@Override
	public boolean dispatchKeyEvent(KeyEvent event) {
		if (event.getAction() == KeyEvent.ACTION_DOWN
				&& event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
			// 判断两次点击的时间间隔（默认设置为2秒）
			if ((System.currentTimeMillis() - mExitTime) > 2000) {
				Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
				mExitTime = System.currentTimeMillis();
			} else {
				finish();
			}
			return true;
		}
		return super.dispatchKeyEvent(event);
	}

	/** 私有OnClickListener接口实现类 */
	private class MyOnClickListener implements OnClickListener {
		int pos;

		public MyOnClickListener(int pos) {
			this.pos = pos;
		}

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.nearbyText:
				Nearby.setTextColor(getResources().getColor(R.color.myBlue));
				Mine.setTextColor(getResources().getColor(R.color.black));
				line1.setBackgroundResource(R.drawable.slide);
				line2.setBackgroundColor(getResources().getColor(R.color.white));
				break;
			case R.id.mineText:
				Nearby.setTextColor(getResources().getColor(R.color.black));
				Mine.setTextColor(getResources().getColor(R.color.myBlue));
				line1.setBackgroundColor(getResources().getColor(R.color.white));
				line2.setBackgroundResource(R.drawable.slide);
				
				mineFragment.getOrderList(token);
				break;
			}
			viewPager.setCurrentItem(pos);
		}
	}

	/** 私有OnPageChangeListener接口实现类 */
	private class MyOnPageChangeListener implements OnPageChangeListener {

		@Override
		public void onPageSelected(int pos) {
			switch (pos) {
			case 0:
				Nearby.setTextColor(getResources().getColor(R.color.myBlue));
				Mine.setTextColor(getResources().getColor(R.color.black));
				line1.setBackgroundResource(R.drawable.slide);
				line2.setBackgroundColor(getResources().getColor(R.color.white));
				break;
			case 1:
				Nearby.setTextColor(getResources().getColor(R.color.black));
				Mine.setTextColor(getResources().getColor(R.color.myBlue));
				line1.setBackgroundColor(getResources().getColor(R.color.white));
				line2.setBackgroundResource(R.drawable.slide);
				
				mineFragment.getOrderList(token);
				break;
			}
		}

		@Override
		public void onPageScrollStateChanged(int pos) {

		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}
	}

	/** ViewPager适配器 */
	private class MyPagerAdapter extends PagerAdapter {
		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

		@Override
		public int getCount() {
			return fragmentsList.size();
		}

		@Override
		public void destroyItem(View container, int position, Object object) {
			((ViewPager) container).removeView(fragmentsList.get(position)
					.getView());
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			Fragment fragment = fragmentsList.get(position);
			if (!fragment.isAdded()) { // 如果fragment还没有added
				FragmentTransaction ft = fm.beginTransaction();
				ft.add(fragment, fragment.getClass().getSimpleName());
				ft.commit();
				/**
				 * 在用FragmentTransaction.commit()方法提交FragmentTransaction对象后
				 * 会在进程的主线程中,用异步的方式来执行。 如果想要立即执行这个等待中的操作,就要调用这个方法(只能在主线程中调用)。
				 * 要注意的是,所有的回调和相关的行为都会在这个调用中被执行完成,因此要仔细确认这个方法的调用位置。
				 */
				fm.executePendingTransactions();
			}
			if (fragment.getView().getParent() == null) {
				container.addView(fragment.getView()); // 为viewpager增加布局
			}
			return fragment.getView();
		}
	};
}