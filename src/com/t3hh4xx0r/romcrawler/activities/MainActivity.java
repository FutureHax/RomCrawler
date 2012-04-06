package com.t3hh4xx0r.romcrawler.activities;


import java.io.IOException;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.pontiflex.mobile.webview.sdk.AdManagerFactory;
import com.pontiflex.mobile.webview.sdk.IAdManager;
import com.t3hh4xx0r.romcrawler.Constants;
import com.t3hh4xx0r.romcrawler.Manifest;
import com.t3hh4xx0r.romcrawler.R;
import com.t3hh4xx0r.romcrawler.SettingsMenu;
import com.t3hh4xx0r.romcrawler.adapters.DBAdapter;


public class MainActivity extends PreferenceActivity {
private IAdManager adManager;
private Preference favs;
//private Preference xda;
public static SharedPreferences prefs;

	/** Called when the activity is first created. */
    @Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.layout.forums);
		prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = prefs.edit();

    	adManager = AdManagerFactory.createInstance(getApplication());																																																																																																																																											
    	editor.putBoolean("isAdFree", adManager.hasValidRegistrationData());
    	editor.commit();
        favs = (Preference) findPreference("favs");
//        xda = (Preference) findPreference("xda");
        DBAdapter fdb = new DBAdapter(this);
        
        try {
 			new SimpleEula(this).show();
 		} catch (IOException e1) {
 			e1.printStackTrace();
 		} 

        if (!prefs.getBoolean("lReg", false) && prefs.getBoolean("isReg", false)) {
            editor.putBoolean("lReg", true);
            editor.commit();
            
        	AlertDialog.Builder builder = new AlertDialog.Builder(this);
      		builder.setTitle("Thanks for purchasing!");
      		builder.setMessage("Enjoy the premium features.")
      		   .setCancelable(false)
      		   .setPositiveButton("Can\'t wait to check em out!", new DialogInterface.OnClickListener() {
      		       public void onClick(DialogInterface dialog, int id) {
      		    	   dialog.dismiss();
      		       }
      		   });
      		AlertDialog alert = builder.create();
      		alert.show();
        }
        if (!prefs.getBoolean("isReg", false)) {
    	   Intent intent = new Intent("com.t3hh4xx0r.romcrawler.REGISTER");
    	   this.sendBroadcast(intent, Manifest.permission.REGISTER);	
    	}
    	if (prefs.getBoolean("isReg", false)) {
    		favs.setEnabled(true);
    		//xda.setEnabled(true);
    		try {
    			fdb.open();
    			fdb.close();
    		} catch (Exception e) {
    			
    		}
    	} else {
    		if (!prefs.getBoolean("isAdFree", false)) {																																																																			
            	adManager.showAd();		
    		} else {
        		favs.setEnabled(true);
        		try {
        			fdb.open();
        		} catch (Exception e) {
        			
        		}
    		}
    	}
    }
    
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater menuinflate = new MenuInflater(this);
		menuinflate.inflate(R.menu.main_menu, menu);
		if (prefs.getBoolean("isReg", false)) {
			menu.removeItem(R.id.upgrade);
		}
		return true;
	}	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	        case R.id.settings:
	            Intent i = new Intent(this, SettingsMenu.class);
	            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	            startActivity(i);
	        break;
	        case R.id.upgrade:
          		AlertDialog.Builder builder = new AlertDialog.Builder(this);
          		builder.setTitle("Upgrade to premium");
          		builder.setMessage("Purchase the unlock key today!")
          		   .setCancelable(false)
          		   .setPositiveButton("Lets check it out", new DialogInterface.OnClickListener() {
          		       public void onClick(DialogInterface dialog, int id) {
          					Intent marketApp = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.t3hh4xx0r.romcrawlerpremium"));
          					marketApp.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
          		 			try{
          						startActivity(marketApp);
          					}catch(Exception e){
          						e.printStackTrace();
          					}        	 
          		       }
          		   });
          		AlertDialog alert = builder.create();
          		alert.show();
          		break;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
		return false;
	}	
}
