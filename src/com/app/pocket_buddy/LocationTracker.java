package com.app.pocket_buddy;

import java.util.Calendar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

public class LocationTracker extends Activity implements LocationListener {

	LocationManager locationManager;
	DBHelper dbhelper;
	SQLiteDatabase database;
	LocationAnalysis locAnalysis;
	
	public LocationTracker(Object locationService, Context appContext)
	{
		locAnalysis = new LocationAnalysis(dbhelper);
		locationManager = (LocationManager) locationService;
		dbhelper = new DBHelper(appContext);
		database = dbhelper.getWritableDatabase();
		dbhelper.onCreate(database);
	}
	
	public void Start()
	{
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,30*1000, 0, this);
	}
	
	public void Stop()
	{
		locationManager.removeUpdates(this);
	}
	
	@Override
	public void onLocationChanged(Location location) {
		//LocationAnalysis.Summarize();
		int hour = (Calendar.getInstance()).get(Calendar.HOUR_OF_DAY);
		//summarize location updates after 11 pm
		if(hour > 23)
		{
			locAnalysis.execute();
		}
		dbhelper.AddLocation(location);
		broadcast_location(location);
	}
	
	private void broadcast_location(Location location)
	{
		Intent intent = new Intent("location_changed");
		String insert_sql = "Insert into LocationUpdates (TimeStamp, Latitude, Longitude) Values (datetime(), "+Double.toString(location.getLatitude())+", "+Double.toString(location.getLongitude())+");";
	  intent.putExtra("location_insert", insert_sql);
	  LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub
		Log.d("Provider Status", "Disabled : "+provider);
	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub
		Log.d("Provider Status", "Enabled : "+provider);
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub
		Log.d("Provider Status", "Status Changed : "+provider+ " Status:"+Integer.toString(status));
	}

}
