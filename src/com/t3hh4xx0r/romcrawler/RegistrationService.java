package com.t3hh4xx0r.romcrawler;

import com.t3hh4xx0r.romcrawler.activities.MainActivity;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;


	public class RegistrationService extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			 final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);

			 if (!prefs.getBoolean("isReg", false)) {
	             SharedPreferences.Editor editor = prefs.edit();
	             editor.putBoolean("isReg", true);
	             editor.commit();
	             
				 int icon = R.drawable.ic_launcher;
				 CharSequence tickerText = "Registered!";
				 long when = System.currentTimeMillis();
				 CharSequence contentTitle = "License Found"; 
				 CharSequence contentText = "Click to unlock premium features."; 
				 
				 Intent notificationIntent = new Intent(context, MainActivity.class);
	
				 PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);
	
				 Notification notification = new Notification(icon, tickerText, when);
		   	     notification.defaults = Notification.DEFAULT_VIBRATE;
		   	     notification.flags = Notification.FLAG_AUTO_CANCEL;
				 notification.setLatestEventInfo(context, contentTitle, contentText, contentIntent);
				 final int HELLO_ID = 1;
	
				 NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(
			                Context.NOTIFICATION_SERVICE);	
				 mNotificationManager.notify(HELLO_ID, notification);
			 }
		}
}