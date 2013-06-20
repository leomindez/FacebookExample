package com.angelhack.routours;

import android.os.Bundle;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.lionlab.routours.fragment.LoginFragment;

public class MainActivity extends SherlockFragmentActivity {

	private LoginFragment fragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (savedInstanceState == null) {
			fragment = new LoginFragment();
			getSupportFragmentManager().beginTransaction()
					.add(android.R.id.content, fragment).commit();
		} else {
			fragment = (LoginFragment) getSupportFragmentManager()
					.findFragmentById(android.R.id.content);
		}
	}

}