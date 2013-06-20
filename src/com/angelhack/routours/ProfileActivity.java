package com.angelhack.routours;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;


public class ProfileActivity extends Activity implements OnClickListener{

	private Button routes, createRoute;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile);

		routes = (Button) findViewById(R.id.routes);
		createRoute = (Button) findViewById(R.id.createRoute);
		routes.setOnClickListener(this);
		createRoute.setOnClickListener(this);
		
		

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.profile, menu);
		return true;
	}

	@Override
	public void onClick(View arg0) {
		Intent intent = null;
		
		switch (arg0.getId()) {
		
		case R.id.routes:
		intent = new Intent(ProfileActivity.this,MyRouteList.class);
		startActivity(intent);
			break;
			
		case R.id.createRoute:
			intent = new Intent(ProfileActivity.this,RoutesActivity.class);
			startActivity(intent);
			break;
			
		default:
			break;
		}
	}

}
