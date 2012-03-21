package com.t3hh4xx0r.romcrawler;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;


	public class RegistrationService extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			Constants.isReg = true;
			Toast.makeText(context, "DFGDFSGWRDSGVDFZGBWSV", 100000000);
		}
	}