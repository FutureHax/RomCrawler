package com.t3hh4xx0r.romcrawler;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;

import com.t3hh4xx0r.romcrawler.activities.FavoritesActivity;
import com.t3hh4xx0r.romcrawler.adapters.DBAdapter;
import com.t3hh4xx0r.romcrawler.rootzwiki.DeviceTypeRW;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;

import android.util.Log;
//FIX ME
public class Receiver extends BroadcastReceiver {
	 ArrayList<String> rwtitlesArray;
	 ArrayList<String> rwidentList;
	 ArrayList<String> xtitlesArray;
	 ArrayList<String> xidentList;
	 String ident;
	 Context ctx;

	 
     String threadTitle = null;  

	public void onReceive(Context ctx, Intent intent) {
		populate(ctx);
	}
	
	public void populate(final Context ctx) {
		new Thread(new Runnable() {
            public void run() {
            	DBAdapter db = new DBAdapter(ctx);
            	db.open();
            	Cursor c = db.getAllFavs();
        	    if (c.getCount()>0) {
        	    	if (c.getString(4).equals("rw")) {
	        	    	if (c.getString(6).equals("thread")) {
	        	    		String URL = new String(c.getString(1));
	        			    if (!URL.startsWith("http://") ) {
	        			    	URL = "http://"+URL;
	        			    }
	
	        	    		StringBuilder whole = new StringBuilder();
	    	    			URL url = null;
							try {
								url = new URL(URL);
							} catch (MalformedURLException e1) {
								e1.printStackTrace();
							}
	        	    		try {
	        	    			HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
	        	    			urlConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2");
	        	    			try {
	        	    				BufferedReader in = new BufferedReader(new InputStreamReader(new BufferedInputStream(urlConnection.getInputStream())));
	        	    				String inputLine;
	        	    				while ((inputLine = in.readLine()) != null)
	        	    					whole.append(inputLine);
	    								in.close();
	        	    			} catch (IOException e) {
	        	    				//
	        	    			} finally {
	        	    				urlConnection.disconnect();
	        	    			}
	        	    		} catch (Exception e) {
	        	    			//
	        	    		}
							Document doc = Parser.parse(whole.toString(), URL);
							Elements edits = doc.select(".edit");
							boolean isFirst = true;
					       	for (Element edit : edits) {		
					       		if (isFirst) {
				           			isFirst = false;
				           			if (!c.getString(c.getColumnIndex("edited")).equals(edit.text())) {
				           				if (db.updateUrl(ident, edit.text())) {
				           					Log.d("RECEIVER", "UPDATE SUCCESS");
				           				} else {
				           					Log.d("RECEIVER", "UPDATE FAIL");
				           				}
							       	alert(ctx, c.getString(c.getColumnIndex("title")));
				           			}  
					       		}
					       	}
					       		
	        	    	}
        	    	}
            	    try {
             	       	while (c.moveToNext()) {
             	  	    	if (c.getString(4).equals("rw")) {
        	        	    	if (c.getString(6).equals("thread")) {
        	        	    		String URL = new String(c.getString(1));
        	        			    if (!URL.startsWith("http://") ) {
        	        			    	URL = "http://"+URL;
        	        			    }
        	
        	        	    		StringBuilder whole = new StringBuilder();
        	    	    			URL url = null;
        							try {
        								url = new URL(URL);
        							} catch (MalformedURLException e1) {
        								e1.printStackTrace();
        							}
        	        	    		try {
        	        	    			HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        	        	    			urlConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2");
        	        	    			try {
        	        	    				BufferedReader in = new BufferedReader(new InputStreamReader(new BufferedInputStream(urlConnection.getInputStream())));
        	        	    				String inputLine;
        	        	    				while ((inputLine = in.readLine()) != null)
        	        	    					whole.append(inputLine);
        	    								in.close();
        	        	    			} catch (IOException e) {
        	        	    				//
        	        	    			} finally {
        	        	    				urlConnection.disconnect();
        	        	    			}
        	        	    		} catch (Exception e) {
        	        	    			//
        	        	    		}
        							Document doc = Parser.parse(whole.toString(), URL);
        							Elements edits = doc.select(".edit");
        							boolean isFirst = true;
        					       	for (Element edit : edits) {		
        					       		if (isFirst) {
        				           			isFirst = false;
        				           			if (!c.getString(c.getColumnIndex("edited")).equals(edit.text())) {
        				           				if (db.updateUrl(ident, edit.text())) {
        				           					Log.d("RECEIVER", "UPDATE SUCCESS");
        				           				} else {
        				           					Log.d("RECEIVER", "UPDATE FAIL");
        				           				}
        							       	alert(ctx, c.getString(c.getColumnIndex("title")));
        				           			}  
        					       		}
        					       	}
        	        	    	}
                	    	}
             	       	}
            	    } catch (Exception ep) {
            	       	ep.printStackTrace();
            	    }  		
        	    }
        	c.close();
            db.close();
            }
      }).start();       		
    }
	
	public void alert(Context ctx, String title) {
		 
		 String ns = Context.NOTIFICATION_SERVICE;
		 NotificationManager mNotificationManager = (NotificationManager) ctx.getSystemService(ns);

		 int icon = R.drawable.ic_launcher;
		 CharSequence tickerText = "Favorite updated!";
		 long when = System.currentTimeMillis();
		 CharSequence contentTitle = "OMG"; 
		 CharSequence contentText = title + " was updated!"; 
		 
		 Intent notificationIntent = new Intent(ctx, FavoritesActivity.class);

		 PendingIntent contentIntent = PendingIntent.getActivity(ctx, 0, notificationIntent, 0);

		 Notification notification = new Notification(icon, tickerText, when);
		 notification.setLatestEventInfo(ctx, contentTitle, contentText, contentIntent);
   	     notification.defaults = Notification.DEFAULT_VIBRATE;
   	     notification.flags = Notification.FLAG_AUTO_CANCEL;
   	     final int HELLO_ID = 1;

		 mNotificationManager.notify(HELLO_ID, notification);		
	}
	
}