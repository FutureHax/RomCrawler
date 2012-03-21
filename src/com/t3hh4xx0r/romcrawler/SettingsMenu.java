package com.t3hh4xx0r.romcrawler;

import com.t3hh4xx0r.romcrawler.activities.MainActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;
import android.view.MenuItem;


public class SettingsMenu extends PreferenceActivity implements OnPreferenceChangeListener {

	private ListPreference mRefreshTime;
    private int refreshValue;
    private CheckBoxPreference mUpdates;

	/** Called when the activity is first created. */
    @Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.layout.settings_menu);
		
		
		mUpdates = (CheckBoxPreference) findPreference("auto_update");
        mRefreshTime = (ListPreference) findPreference("refresh_time");
   	    refreshValue = (Constants.REFRESH_TIME);
   	    mRefreshTime.setValue(String.valueOf(refreshValue));
   	    mRefreshTime.setOnPreferenceChangeListener(this);
        mUpdates.setChecked(Constants.AUTOMATICALLY_UPDATE);
        
        //if (!Constants.isReg) {
        	mUpdates.setEnabled(false);
        //}
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

	public boolean onPreferenceChange(Preference preference, Object objValue) {
        if (preference == mRefreshTime) {
             refreshValue = Integer.valueOf((String) objValue);
             Constants.REFRESH_TIME = Integer.valueOf((String) objValue);
             return true;
	   }
	   return false;
	}
	
	public void checkService() {
        if (Constants.AUTOMATICALLY_UPDATE) {
      		Intent intent = new Intent(getBaseContext(), Receiver.class);
    		PendingIntent pendingIntent = PendingIntent.getBroadcast(
    				getBaseContext().getApplicationContext(), 234324243, intent, 0);
    		AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), Constants.REFRESH_TIME*1000, pendingIntent);
        } else {
            Constants.AUTOMATICALLY_UPDATE = false;
            Intent myIntent = new Intent(getBaseContext(), Receiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(getBaseContext(), 234324243, myIntent, 0);
            AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);
            alarmManager.cancel(pendingIntent);
        }
	}

	    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
		    boolean value;
	       if(preference == mUpdates){
	            value = mUpdates.isChecked();
	            if(value) {
	                Constants.AUTOMATICALLY_UPDATE  = true;
	                checkService();
	            } else {
	                Constants.AUTOMATICALLY_UPDATE = false;
	                checkService();
	            }
	        }
		return true;
	   }
}
