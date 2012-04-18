package com.t3hh4xx0r.romcrawler.activities;

import java.util.ArrayList;

import android.app.DownloadManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.t3hh4xx0r.romcrawler.Constants;
import com.t3hh4xx0r.romcrawler.R;
import com.t3hh4xx0r.romcrawler.adapters.DBAdapter;
import com.viewpagerindicator.TitlePageIndicator;
import com.viewpagerindicator.TitleProvider;

public class ListFragmentViewPagerActivity extends FragmentActivity {
	ArrayList<String> URLS;
	ArrayList<String> IDENTS;
	ArrayList<String> TITLES;
	ArrayList<String> EDITS;
    BroadcastReceiver receiver;
    String threadTitle = null;
    public static String threadUrl = null;
    String type = null;
    String threadAuthor = null;
    String ident = null;
    boolean isFav = false;
    String author = null;
    String edited = null;
    int countage;
    boolean f;
    SharedPreferences prefs;
    
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.thread_view);
		prefs = PreferenceManager.getDefaultSharedPreferences(this);

        Bundle extras = getIntent().getExtras();
        try {
        	f = extras.getBoolean("f");
        } catch (Exception e) {
        	f = false;
        }
        type = extras.getString("type");
        ident = extras.getString("ident");
        author = extras.getString("author");
        edited = extras.getString("edited");
        if (edited == null) {
        	edited = "never";
        }
        URLS = extras.getStringArrayList("urls");
        IDENTS = extras.getStringArrayList("idents");
        TITLES = extras.getStringArrayList("titles");
        countage = extras.getInt("c");
        threadUrl = URLS.get(0);
        
        ViewPager pager = (ViewPager) findViewById(android.R.id.list);
        pager.setAdapter(new ExamplePagerAdapter(getSupportFragmentManager()));
        if (countage != URLS.size() && !f) {	
        	countage = countage-1;        	
        }
        pager.setCurrentItem(countage);
        TitlePageIndicator indicator = (TitlePageIndicator)findViewById(R.id.indicator);
        indicator.setViewPager(pager, countage);
        indicator.setOnPageChangeListener(new OnPageChangeListener() {
        	 @Override
        	 public void onPageSelected(int position) {
        		 threadTitle = TITLES.get(position);
        		 Log.d("TITLE", threadTitle);
        		 threadUrl = URLS.get(position);
        		 ident = IDENTS.get(position);
        		 if (prefs.getBoolean("isReg", false) || prefs.getBoolean("isAdFree", false)) {
	                 final DBAdapter db = new DBAdapter(getBaseContext());
	        	     	db.open();
	        	 	    Cursor c = db.getAllFavs();
	        	 	    if (c.getCount()>0) {
	        	 	    	if (c.getString(c.getColumnIndex("ident")).equals(ident)) {
	        	 	    		isFav = true;
	        	 	    	}
	        	 	    		try {
	        	 	    			while (c.moveToNext()) {
	        	 	    	 	    	if (c.getString(c.getColumnIndex("ident")).equals(ident)) {
	        	 	    		    		isFav = true;
	        	 	    		    	}
	        	 	    		    }
	        	 	    		} catch (Exception ep) {
	        	 	    			ep.printStackTrace();
	        	 	    		}
	        	 	    }
	        	 	    c.close();
	        	 		db.close();
	        	 		if (android.os.Build.VERSION.SDK_INT > 10) {
	        	 			invalidateOptionsMenu();
	        	 		}
	        	 		isFav = false;
        		 	}
        		 }

			@Override
			public void onPageScrollStateChanged(int arg0) {				
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {				
			}
        });
        
	    receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(action)) {
                    
        		 	String ns = Context.NOTIFICATION_SERVICE;
        		 	NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(ns);

        		 	int icon = R.drawable.ic_launcher;        // icon from resources
        		 	CharSequence tickerText = "Download ready!";              // ticker-text
        		 	long when = System.currentTimeMillis();         // notification time
        		 	CharSequence contentTitle = "OMG";  // expanded message title
        		 	CharSequence contentText = "Your download is finished!";      // expanded message text

        		 	Intent notificationIntent = new Intent(context, ThreadFragment.class);

        		 	PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);
        		 	
        			Notification notification = new Notification(icon, tickerText, when);
        			notification.setLatestEventInfo(context, contentTitle, contentText, contentIntent);
        			notification.defaults |= Notification.DEFAULT_VIBRATE;
        			notification.flags |= Notification.FLAG_AUTO_CANCEL;
        			final int HELLO_ID = 1;
        			 
        			mNotificationManager.notify(HELLO_ID, notification);
                }
            }
    };
    
    registerReceiver(receiver, new IntentFilter(
            DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }
    
    public class ExamplePagerAdapter extends FragmentPagerAdapter implements TitleProvider{

	    public ExamplePagerAdapter(FragmentManager fm) {
	        super(fm);
	    }
	
	    @Override
	    public int getCount() {
	        return URLS.size();
	    }
	
	    @Override
	    public Fragment getItem(int position) {
	        Fragment fragment = new ThreadFragment();
	
	        Bundle args = new Bundle();
	        args.putString("url", URLS.get(position));
	        args.putString("ident", IDENTS.get(position));
	        fragment.setArguments(args);
	
	        return fragment;
	    }

		@Override
		public String getTitle(int pos) {
			return TITLES.get(pos);
		}
		
    }
    
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
		if (type.equals("xda") || !prefs.getBoolean("isReg", false)) {
			menu.removeItem(R.id.ss_view);
		}
		if (prefs.getBoolean("isReg", false) || prefs.getBoolean("isAdFree", false)) {
			if (isFav) {
				menu.getItem(0).setIcon(R.drawable.fav_ab);
			}
		} else {
			menu.removeItem(R.id.fav_ab);
		}
    return super.onPrepareOptionsMenu(menu);
    }
    
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater menuinflate = new MenuInflater(this);
		menuinflate.inflate(R.menu.thread_menu, menu);
 		if (android.os.Build.VERSION.SDK_INT < 11) {
			menu.removeItem(R.id.fav_ab); 			
 		}
		if (type.equals("xda") || !prefs.getBoolean("isReg", false)) {
			menu.removeItem(R.id.ss_view);
		}
		if (prefs.getBoolean("isReg", false) || prefs.getBoolean("isAdFree", false)) {
			if (isFav) {
				menu.getItem(0).setIcon(R.drawable.fav_ab);
			}
		} else {
			menu.removeItem(R.id.fav_ab);
		}
		return true;		
	}	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
	    switch (item.getItemId()) {
	        case android.R.id.home:
	            Intent intent = new Intent(this, MainActivity.class);
	            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	            startActivity(intent);
	        break;
	        case R.id.ss_view:
	            Intent ssi = new Intent(this, SSActivity.class);
	            ssi.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	            Bundle b = new Bundle();
	            b.putString("title", threadTitle);
	            b.putString("url", threadUrl);
	            ssi.putExtras(b);
	            startActivity(ssi);
	        break;
	        case R.id.fav_ab:
	        	threadUrl = new String(threadUrl.replaceAll("http://", ""));
  	            DBAdapter fdb = new DBAdapter(this);
            	fdb.open();	        	
            	if (isFav) {
	  	            Cursor c = fdb.getAllUrls();
	      	          while (c.moveToNext()) {
	      	        	  String id = c.getString(c.getColumnIndex("_id"));
	      	        	  int rowId = Integer.parseInt(id);
	          	          if(c.getString(c.getColumnIndex("url")).equals(threadUrl)) {
	          	        	  if (fdb.deleteUrl(rowId)) {
	          	        		  Log.d("THREAD", "SUCCESS");
	          	        	  } else {
	          	        		  Log.d("THREAD", "FAILED");
	          	        	  }
	          	          }
	          	    }
	  	            c.close();
	  	            item.setIcon(R.drawable.fav_ab_off);
	  	            isFav = false;
	  	        } else {
	  	        	try {
	  	        		fdb.insertFav(threadUrl, threadTitle, ident, type, author, "thread", edited);
	  	        	} catch (Exception e) {}
	  	        	item.setIcon(R.drawable.fav_ab);
	  	            isFav = true;
	  	        }
  	            fdb.close();
	        default:
	            return super.onOptionsItemSelected(item);
	    }
		return false;
	}
	
	@Override
	public void onStop() {
		super.onStop();
		unregisterReceiver(receiver);		
	}
	
	@Override
	public void onStart() {
		super.onStart();
		registerReceiver(receiver, new IntentFilter(
	            DownloadManager.ACTION_DOWNLOAD_COMPLETE));
	}

}