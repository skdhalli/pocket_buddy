package com.app.pocket_buddy;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.location.Location;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper {

	public DBHelper(Context applicationContext)
	{
		super(applicationContext, "pocket_buddy.db", null, 1);
	}
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		try
		{
			String query = "Create Table LocationUpdates(_id integer Primary Key AutoIncrement, TimeStamp DateTime, Latitude numeric, Longitude numeric)";
			db.execSQL(query);
			Log.d("DB access", query);
			query = "Create Table LocationUpdatesSummary(_id integer Primary Key AutoIncrement, Latitude numeric, Longitude numeric, Destination varchar(255), WaitTime numeric, Time DateTime)";
			db.execSQL(query);
			Log.d("DB access", query);
		}
		catch(Exception exc)
		{
			Log.d("Create Table: ", exc.getMessage());
		}
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
		//TODO.....
		
		/*if(arg2 > arg1)
		{
			String query = "DROP TABLE IF EXISTS LocationUpdates"; 
			db.execSQL(query);
			query = "DROP TABLE IF EXISTS LocationUpdatesSummary"; 
			db.execSQL(query);
			onCreate(db);
		}*/
	}
	
	public boolean AddLocation(Location location)
	{
		boolean retval = false;
		try
		{
			SQLiteDatabase database = this.getWritableDatabase();
			Log.d("Location Changed", Double.toString(location.getLatitude())+", "+Double.toString(location.getLongitude())+" Speed:"+Double.toString(location.getSpeed()));
			String insert_sql = "Insert into LocationUpdates (TimeStamp, Latitude, Longitude, WaitTime_mins) Values ('datetime()', "+Double.toString(location.getLatitude())+", "+Double.toString(location.getLongitude())+")";
			Log.d("DB access", insert_sql);
			database.execSQL(insert_sql);
		}
		catch(Exception exc)
		{
			Log.d("DB access", exc.getMessage());
		}
		return retval;
	}
	
	public void GenerateSummary()
	{
		//Move todays updates into the summary table
		
		//Delete the rows from location updates table
	}
}
