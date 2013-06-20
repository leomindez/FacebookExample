package com.angelhack.routours;

import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;


public class MyRouteList extends ListActivity {

	private ArrayAdapter<String> adapter;
	private ArrayList<String> arrayList;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_route_list);
		arrayList = new ArrayList<String>();
		adapter = new ArrayAdapter<String>(MyRouteList.this,
				android.R.layout.simple_list_item_1, arrayList);
		setListAdapter(adapter);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.my_route_list, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case R.id.add_place:
			addPlaceDialog(MyRouteList.this, arrayList, adapter);
			break;

		default:
			break;
		}
		return true;
	}

	private void addPlaceDialog(final Context context,
			final ArrayList<String> arrayList,
			final ArrayAdapter<String> adapter) {

		LayoutInflater inflater = getLayoutInflater();
		View view = inflater.inflate(R.layout.layout_dialog, null);
		final EditText editText = (EditText) view.findViewById(R.id.add_place);

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setView(view);
		builder.setTitle("Add place");

		builder.setMessage("Do you want to add a place?");

		builder.setPositiveButton("Add", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {

				String place = editText.getText().toString();
				if (place.equals("")) {
					Toast.makeText(context, "Los campos deben de estar llenos",
							Toast.LENGTH_SHORT).show();
				} else {
					arrayList.add(place);
					adapter.notifyDataSetChanged();
				}                                                                                                                                                                       

			}
		});

		builder.show();

	}

}
