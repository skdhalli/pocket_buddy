package com.app.pocket_buddy;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.os.AsyncTask;
import android.text.format.DateFormat;
import android.text.format.Time;

public class LocationAnalysis extends AsyncTask <Void, Void, String> {
	
	private DBHelper dbhelper;
	private String lastdate_summarized = "";
	
	public LocationAnalysis(DBHelper _dbhelper)
	{
		this.dbhelper = _dbhelper;
	}
	
	@Override
	protected String doInBackground(Void... arg0) {
		
		Calendar c = Calendar.getInstance();
		SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
		if(lastdate_summarized == df.format(c.getTime()))
		{
			//do nothing
		}
		else
		{
			summarize();
		}
		return lastdate_summarized;
	}
	
	protected void onPostExecute(String results)
	{
		
	}
	
	void summarize()
	{
		dbhelper.GenerateSummary();
	}

}
