package com.example.weichat5_2_1;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.weichat5_2_1.fragment.ChatMainTabFragment;
import com.example.weichat5_2_1.fragment.ContactMainTabFragment;
import com.example.weichat5_2_1.fragment.FriendMainTabFragment;
import com.jauker.widget.BadgeView;

/**
 * 高仿微信5.2.1主界面及消息提醒
 *
 * 开源框架：https://github.com/qstumn/BadgeView
 */
public class MainActivity extends FragmentActivity {

	private ViewPager mViewPager;
	private FragmentPagerAdapter mAdapter;//ViewPager中使用Fragment的适配器
	private List<Fragment> mDatas; //在ViewPager中存放的三个Fragment

	// 导航栏的聊天、朋友、通讯录
	private TextView mChatTextView;
	private TextView mFriendTextView;
	private TextView mContactTextView;

	private LinearLayout mChatLinearLayout;
	private BadgeView mBadgeView;

	private ImageView mTabline;
	private int mScreen1_3;// 屏幕宽度的三分之一

	private int mCurrentPageIndex;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);

		initTabLine();
		initView();
	}

	private void initTabLine() {
		mTabline = (ImageView) findViewById(R.id.id_iv_tabline); // 导航栏底部的指示条

		// 获得手机屏幕的宽度
		Display display = getWindow().getWindowManager().getDefaultDisplay();
		DisplayMetrics outMetrics = new DisplayMetrics(); // outMetrics携带屏幕的宽度、高度等
		display.getMetrics(outMetrics);
		mScreen1_3 = outMetrics.widthPixels / 3;

		// 获得指示条的布局属性
		LayoutParams lp = mTabline.getLayoutParams();
		lp.width = mScreen1_3;
		mTabline.setLayoutParams(lp);
	}

	/**
	 * 初始化方法，实现一系列findViewById
	 *
	 * mDatas中数值的添加
	 */
	private void initView() {
		mViewPager = (ViewPager) findViewById(R.id.id_viewpager);
		mChatTextView = (TextView) findViewById(R.id.id_tv_chat);
		mFriendTextView = (TextView) findViewById(R.id.id_tv_friend);
		mContactTextView = (TextView) findViewById(R.id.id_tv_contact);
		mChatLinearLayout = (LinearLayout) findViewById(R.id.id_ll_chat);

		mDatas = new ArrayList<Fragment>();

		ChatMainTabFragment tab01 = new ChatMainTabFragment();
		FriendMainTabFragment tab02 = new FriendMainTabFragment();
		ContactMainTabFragment tab03 = new ContactMainTabFragment();

		// 将Fragment加入存放Fragment的list
		mDatas.add(tab01);
		mDatas.add(tab02);
		mDatas.add(tab03);

		/*
		 * 适配器的使用
		 */
		mAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
			@Override
			public int getCount() {
				return mDatas.size();
			}

			@Override
			public Fragment getItem(int position) {
				return mDatas.get(position);
			}
		};

		// 为ViewPager设置适配器
		mViewPager.setAdapter(mAdapter);

		// 为ViewPager设置页面改变事件
		mViewPager.addOnPageChangeListener(new OnPageChangeListener() {

			/**
			 * 当页面切换的时候改变顶部的字体颜色
			 */
			@Override
			public void onPageSelected(int position) {
				// 当从第二个页面进入第一个页面的时候清除第一个页面的字体等
				resetTextView();

				switch (position) {
					case 0:// 进入第1个页面

						// 为聊天界面设置浮动的红色字体
						// 《！--这里使用了github上的开源框架BadgeView--》
						if (mBadgeView != null) { // 如果已经有了则清除
							mChatLinearLayout.removeView(mBadgeView);
						}

						mBadgeView = new BadgeView(MainActivity.this);
						mBadgeView.setBadgeCount(7); // 设置数字
						mChatLinearLayout.addView(mBadgeView);// 把mBadgeView加入到聊天的框中

						// 设置字体为绿色
						mChatTextView.setTextColor(Color.parseColor("#008000"));
						break;
					case 1:// 进入第2个页面
						mFriendTextView.setTextColor(Color.parseColor("#008000"));
						break;
					case 2:// 进入第3个页面
						mContactTextView.setTextColor(Color.parseColor("#008000"));
						break;
				}

				mCurrentPageIndex = position;
			}

			/**
			 * 当页面改变的时候，让导航栏底部的绿色指示条随着滑动
			 */
			@Override
			public void onPageScrolled(int position, float positionOffset,int positionOffsetPx) {

				// 打印日志，判断 position, positionOffset, positionOffsetPx的变化特点
				Log.e("TAG", position + " , " + positionOffset + " , "+ positionOffsetPx);

				LinearLayout.LayoutParams lp = (android.widget.LinearLayout.LayoutParams) mTabline.getLayoutParams();

				// 0->1：第0页到第1页
				if (mCurrentPageIndex == 0 && position == 0) {
					lp.leftMargin = (int) (positionOffset * mScreen1_3 + mCurrentPageIndex * mScreen1_3);
				// 1->0：第1页到第0页
				} else if (mCurrentPageIndex == 1 && position == 0) {
					lp.leftMargin = (int) (mCurrentPageIndex * mScreen1_3 + (positionOffset - 1) * mScreen1_3);
				// 1->2：第1页到第2页
				} else if (mCurrentPageIndex == 1 && position == 1) {
					lp.leftMargin = (int) (mCurrentPageIndex * mScreen1_3 + positionOffset * mScreen1_3);
				// 2->1：第2页到第1页
				} else if (mCurrentPageIndex == 2 && position == 1) {
					lp.leftMargin = (int) (mCurrentPageIndex * mScreen1_3 + (positionOffset - 1) * mScreen1_3);
				}

				mTabline.setLayoutParams(lp);
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {

			}

		});

	}

	/**
	 * 初始化导航栏中的字体颜色为黑色
	 */
	protected void resetTextView() {
		mChatTextView.setTextColor(Color.BLACK);
		mFriendTextView.setTextColor(Color.BLACK);
		mContactTextView.setTextColor(Color.BLACK);
	}

}
