package com.t3hh4xx0r.romcrawler.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;

import com.t3hh4xx0r.romcrawler.Constants;
import com.t3hh4xx0r.romcrawler.R;
import com.t3hh4xx0r.romcrawler.adapters.DBAdapter;
import com.t3hh4xx0r.romcrawler.rootzwiki.RWDeviceGeneral;

public class MyDevice extends PreferenceActivity {
	Preference rw;
	Preference xda;
	String url;
	String rwl;
	String xdal;

	/** Called when the activity is first created. */
    @Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.layout.my_device);
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        rw = (Preference) findPreference("rootzwiki");
        xda = (Preference) findPreference("xda");
                
    	DBAdapter db = new DBAdapter(this);
   		db.open();
   		if (db.isDeviceSet()) {
   			Cursor c = db.getDevice();
   			rwl = c.getString(1);
   			xdal = c.getString(2);
   			Log.d("RW", rwl);
   			Log.d("XDA", xdal);
   		
   			if (prefs.getBoolean("isReg", false)) {
   	  			if (!xdal.equals("") && xdal != null) {
   	   				if (!xdal.startsWith("http://")) {
   	   					xdal = new String("http://"+url);
   	   				} 
   	   				xda.setEnabled(true);
   	   			}
   			} 		
    	
   			if (!rwl.equals("") && rwl != null) {
   				if (!rwl.startsWith("http://")) {
   					rwl = new String("http://"+url);
   				} 
   				rw.setEnabled(true);
   			}
   	        c.close();
   		} else {
			LayoutInflater li = LayoutInflater.from(this);
			View v = li.inflate(R.layout.device_help, null);

			AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
					this);

			alertDialogBuilder.setView(v);

			alertDialogBuilder
				.setTitle("No device currently set!")
				.setMessage("Long click on a device to set it as \"My Device\"")
				.setCancelable(false)
				.setPositiveButton("OK",
				  new DialogInterface.OnClickListener() {
				    public void onClick(DialogInterface dialog,int id) {
				    	finish();
				    }
				  });
			AlertDialog alertDialog = alertDialogBuilder.create();
			alertDialog.show();
   		}
        db.close();
   
    }
    
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
       	Intent intent = null;
        Bundle b = new Bundle();

   		if(preference == xda){    
   	    	b.putString("url", xdal);
        } else if (preference == rw) {
        	intent = new Intent(MyDevice.this, RWDeviceGeneral.class);
        	b.putString("url", rwl);
        }

        intent.putExtras(b);
        startActivity(intent);  
        
        return true;
   }
    
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	        case android.R.id.home:
	            Intent intent = new Intent(this, MainActivity.class);
	            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	            startActivity(intent);
	            return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}
}
