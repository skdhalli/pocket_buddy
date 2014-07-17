package com.app.pocket_buddy;

import java.util.ArrayList;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.ToggleButton;

public class MainActivity extends Activity implements OnClickListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		locationTracker =new LocationTracker(this.getSystemService(Context.LOCATION_SERVICE), this.getApplicationContext());
		
		//gather references to all UI elements here
		loc_tracking = (ToggleButton)findViewById(R.id.loc_tracking);
		copy = (Button)findViewById(R.id.copyLog);
		
		logslist = (ListView)findViewById(R.id.logslist);
		
		//handle interactions from UI elements
		loc_tracking.setOnClickListener(this);
		copy.setOnClickListener(this);
	
		adapter=new ArrayAdapter<String>(this,
				R.layout.list_view_root_item,
				R.id.textViewItem,
	            listItems);
	    logslist.setAdapter(adapter);
		
		// Register to receive messages.
		  // We are registering an observer (location_changed_reciever) to receive Intents
		  // with actions named "location_changed".
		LocalBroadcastManager.getInstance(this).registerReceiver(location_changed_reciever,
			      new IntentFilter("location_changed"));
	}
	
	//LIST OF ARRAY STRINGS WHICH WILL SERVE AS LIST ITEMS
    ArrayList<String> listItems=new ArrayList<String>();

    //DEFINING A STRING ADAPTER WHICH WILL HANDLE THE DATA OF THE LISTVIEW
    ArrayAdapter<String> adapter;
    
	LocationTracker locationTracker;
	ToggleButton loc_tracking;
	Button copy;
	ListView logslist;
	
	void startTrackingLocationUpdates()
	{
		locationTracker.Start();
		loc_tracking.setChecked(true);
	}
	
	void stopTrackingLocationUpdates()
	{
		locationTracker.Stop();
		loc_tracking.setChecked(false);
	}

	@Override
	public void onClick(View view) {
		
		switch(((Button)view).getTag().toString())
		{
			case "loc_tracking":
			if(loc_tracking.isChecked())
			{
				this.startTrackingLocationUpdates();
				Log.d("Tracking Status", "Starting location updates");
			}
			else
			{
				this.stopTrackingLocationUpdates();
				Log.d("Tracking Status", "Stop location updates");
			}
			break;
			case "copy":
			ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE); 
			String clipData = "";
			for(int i =0; i<listItems.size(); i++)
			{
				clipData += listItems.get(i);
			}
			ClipData clip = ClipData.newPlainText("label", clipData);
			clipboard.setPrimaryClip(clip);
			Toast.makeText(getApplicationContext(), "Copied to clipboard ..", 3).show();;
			break;
		}
	}
	
	// Our handler for received Intents. This will be called whenever an Intent
	// with an action named "custom-event-name" is broadcasted.
	private BroadcastReceiver location_changed_reciever = new BroadcastReceiver() {
	  @Override
	  public void onReceive(Context context, Intent intent) {
	    // Get extra data included in the Intent
		  String sql = intent.getStringExtra("location_insert");
		  listItems.add(sql);
		  adapter.notifyDataSetChanged();
	  }
	};

	@Override
	protected void onDestroy() {
	  // Unregister since the activity is about to be closed.
	  LocalBroadcastManager.getInstance(this).unregisterReceiver(location_changed_reciever);
	  super.onDestroy();
	}
}
