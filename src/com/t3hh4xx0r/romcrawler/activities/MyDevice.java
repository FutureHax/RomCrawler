package com.t3hh4xx0r.romcrawler.activities;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;
import android.util.Log;
import android.view.MenuItem;

import com.t3hh4xx0r.romcrawler.Constants;
import com.t3hh4xx0r.romcrawler.R;
import com.t3hh4xx0r.romcrawler.adapters.DBAdapter;
import com.t3hh4xx0r.romcrawler.rootzwiki.RWSubForum;

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

        rw = (Preference) findPreference("rootzwiki");
        xda = (Preference) findPreference("xda");
                
    	DBAdapter db = new DBAdapter(this);
   		db.open();
   		Cursor c = db.getDevice();
   		if (Constants.deviceIsSet) {
   			rwl = c.getString(1);
   			xdal = c.getString(2);
   		
   			if (Constants.isReg) {
   				xda.setEnabled(true);
   			} else {
   				xda.setEnabled(false);
   			}   		
    	
   			if (!xdal.equals("")) {
   				if (!xdal.startsWith("http://")) {
   					xdal = new String("http://"+url);
   				} 
   			} else {
   				xda.setEnabled(false);
   			}
   			if (!rwl.equals("")) {
   				if (!rwl.startsWith("http://")) {
   					rwl = new String("http://"+url);
   				} 
   			} else {
   				rw.setEnabled(false);
   			}
   		} else {
   			//popup
   		}
        c.close();
        db.close();
   
    }
    
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
       	Intent intent = null;
        Bundle b = new Bundle();

   		if(preference == xda){    
   	    	b.putString("url", xdal);
        } else if (preference == rw) {
        	intent = new Intent(MyDevice.this, RWSubForum.class);
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
