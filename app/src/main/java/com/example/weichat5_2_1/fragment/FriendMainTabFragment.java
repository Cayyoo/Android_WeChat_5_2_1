package com.example.weichat5_2_1.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.weichat5_2_1.R;

public class FriendMainTabFragment extends Fragment {

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
		// 加载相应的布局layout
		return inflater.inflate(R.layout.tab02, container, false);
	}

}
