package com.iwanghang.drmplayer;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.astuetz.PagerSlidingTabStrip;
import com.astuetz.viewpager.extensions.sample.QuickContactFragment;
import com.iwanghang.drmplayer.utils.MediaUtils;
import com.iwanghang.drmplayer.vo.Mp3Info;

import java.nio.charset.CoderMalfunctionError;
import java.util.ArrayList;


public class MainActivity extends BaseActivity {

//	private final Handler handler = new Handler();

	private PagerSlidingTabStrip tabs;
	private ViewPager pager;
	private MyPagerAdapter adapter;



//	private Drawable oldBackground = null;
//	private int currentColor = 0xFFC74B46;

	private MyMusicListFragment myMusicListFragment;
	private NetMusicListFragment netMusicListFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
		pager = (ViewPager) findViewById(R.id.pager);
		adapter = new MyPagerAdapter(getSupportFragmentManager());

		pager.setAdapter(adapter);

		final int pageMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources()
				.getDisplayMetrics());
		pager.setPageMargin(pageMargin);

		tabs.setViewPager(pager);

		//修改主界面颜色,稍后修复功能,暂时使用默认颜色
		//changeColor(currentColor);

		//绑定服务
		//服务在加载SplashActivity(欢迎页面)的时候,已经启动
		//bindPlayService();
		//这里,我在MyMusicListFragment里面绑定,而没有在MainActivity里绑定

    }

	@Override
	public void publish(int progress) {
		//更新进度条
	}

	@Override
	public void change(int position) {
		//更新歌曲位置.按钮的状态等信息
		/**
		 * 本地音乐的播放UI实际上在MyMusicListFragment中,所以要
		 * 先在MyMusicListFragmen中,写入public void changeUIStatus(int position){}
		 * 然后,传参过去
		 */
		if (pager.getCurrentItem()==0){//如果页面等于0,则说明选中的是第一个页面,我的音乐页面
			myMusicListFragment.loadData();//初始化数据
			myMusicListFragment.changeUIStatusOnPlay(position);
		}else if (pager.getCurrentItem()==1){

		}
	}


//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		getMenuInflater().inflate(R.menu.main, menu);
//		return true;
//	}
//
//	@Override
//	public boolean onOptionsItemSelected(MenuItem item) {
//
//		switch (item.getItemId()) {
//
//		case R.id.action_contact:
//			QuickContactFragment dialog = new QuickContactFragment();
//			dialog.show(getSupportFragmentManager(), "QuickContactFragment");
//			return true;
//
//		}
//
//		return super.onOptionsItemSelected(item);
//	}

//	private void changeColor(int newColor) {
//
//		tabs.setIndicatorColor(newColor);
//
//		// change ActionBar color just if an ActionBar is available
//		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
//
//			Drawable colorDrawable = new ColorDrawable(newColor);
//			Drawable bottomDrawable = getResources().getDrawable(R.drawable.actionbar_bottom);
//			LayerDrawable ld = new LayerDrawable(new Drawable[] { colorDrawable, bottomDrawable });
//
//			if (oldBackground == null) {
//
//				if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
//					ld.setCallback(drawableCallback);
//				} else {
//					getActionBar().setBackgroundDrawable(ld);
//				}
//
//			} else {
//
//				TransitionDrawable td = new TransitionDrawable(new Drawable[] { oldBackground, ld });
//
//				// workaround for broken ActionBarContainer drawable handling on
//				// pre-API 17 builds
//				// https://github.com/android/platform_frameworks_base/commit/a7cc06d82e45918c37429a59b14545c6a57db4e4
//				if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
//					td.setCallback(drawableCallback);
//				} else {
//					getActionBar().setBackgroundDrawable(td);
//				}
//
//				td.startTransition(200);
//
//			}
//
//			oldBackground = ld;
//
//			// http://stackoverflow.com/questions/11002691/actionbar-setbackgrounddrawable-nulling-background-from-thread-handler
//			getActionBar().setDisplayShowTitleEnabled(false);
//			getActionBar().setDisplayShowTitleEnabled(true);
//
//		}
//
//		currentColor = newColor;
//
//	}

//	public void onColorClicked(View v) {
//
//		int color = Color.parseColor(v.getTag().toString());
//		changeColor(color);
//
//	}

//	@Override
//	protected void onSaveInstanceState(Bundle outState) {
//		super.onSaveInstanceState(outState);
//		outState.putInt("currentColor", currentColor);
//	}

//	@Override
//	protected void onRestoreInstanceState(Bundle savedInstanceState) {
//		super.onRestoreInstanceState(savedInstanceState);
//		currentColor = savedInstanceState.getInt("currentColor");
//		changeColor(currentColor);
//	}

//	private Drawable.Callback drawableCallback = new Drawable.Callback() {
//		@Override
//		public void invalidateDrawable(Drawable who) {
//			getActionBar().setBackgroundDrawable(who);
//		}
//
//		@Override
//		public void scheduleDrawable(Drawable who, Runnable what, long when) {
//			handler.postAtTime(what, when);
//		}
//
//		@Override
//		public void unscheduleDrawable(Drawable who, Runnable what) {
//			handler.removeCallbacks(what);
//		}
//	};



	public class MyPagerAdapter extends FragmentPagerAdapter {

		private final String[] TITLES = { "本地音乐", "网络音乐"};

		public MyPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return TITLES[position];
		}

		@Override
		public int getCount() {
			return TITLES.length;
		}

		@Override
		public Fragment getItem(int position) {
			//return SuperAwesomeCardFragment.newInstance(position);
			if(position==0){
				if(myMusicListFragment==null){
					myMusicListFragment = MyMusicListFragment.newInstance();
				}
				return myMusicListFragment;
			}else if (position == 1){
				if(netMusicListFragment==null){
					netMusicListFragment = NetMusicListFragment.newInstance();
				}
				return netMusicListFragment;
			}
			return null;
			//return MyMusicListFragment.newInstance();
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {//填充菜单
		getMenuInflater().inflate(R.menu.main_menu,menu);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item){//菜单中事件
		switch (item.getItemId()){
			case R.id.ilove:
				Intent intent = new Intent(this,MyLoveMusicActivity.class);
				startActivity(intent);
				break;
			case R.id.near_play:
				break;
		}
		return true;
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		//保存当前播放的一些状态值
		DRMPlayerApp app = (DRMPlayerApp) getApplication();
		SharedPreferences.Editor editor = app.sp.edit();
		//保存 当前正在播放的歌曲的位置
		editor.putInt("currentPosition",playService.getCurrentPosition());
		//保存 播放模式
		editor.putInt("play_mode",playService.getPlay_mode());
		//保存 提交
		editor.commit();

		//创建DRMPlayerApp继承Application,同时需要在把AndroidManiFest中的public换成DRMPlayerApp
		//在DRMPlayerApp的onCreate中 实例化 SharedPreferences
		//在MainActivity的onDestroy中 保存状态值
		//在PlayService的onCreate中 恢复状态值
	}
}


