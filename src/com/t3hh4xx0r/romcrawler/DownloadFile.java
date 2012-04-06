package com.t3hh4xx0r.romcrawler;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import com.t3hh4xx0r.romcrawler.activities.MainActivity;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.RemoteViews;
import android.widget.Toast;

public class DownloadFile {
	String outfile;
	String zipfile;
	Context c;
    Notification notification;
    NotificationManager notificationManager;
    ProgressBar progressBar;
    private int progress = 50;
	public DownloadFile(String url, String out, String zip, Context ctx) {        
		c = ctx;
		zipfile = zip;
		outfile = out;
		
        notificationManager = (NotificationManager) c.getSystemService(
                Context.NOTIFICATION_SERVICE);
        
		new Download().execute(url);
	}

	private class Download extends AsyncTask<String, String, String>{
	    @Override
	    protected String doInBackground(String... urls) {
	        int count;
	        try {
	        	
	            URL url = new URL(urls[0]);
	            URLConnection conexion = url.openConnection();
	            conexion.connect();
	            int lenghtOfFile = conexion.getContentLength();

	            InputStream input = new BufferedInputStream(url.openStream());
	            String PATH = Environment.getExternalStorageDirectory()
	                    + "/t3hh4xx0r/romCrawler/";
	            File file = new File(PATH);
	            file.mkdirs();
	            File outputFile = new File(file, zipfile);
	            new File(file, zipfile).delete();
	            FileOutputStream output = new FileOutputStream(outputFile);

	            byte data[] = new byte[1024];

	            long total = 0;

	            while ((count = input.read(data)) != -1) {
	                total += count;
	                publishProgress(Long.toString(total*100/lenghtOfFile));
	                output.write(data, 0, count);
	            }

	            output.flush();
	            output.close();
	            input.close();
	            	            
	        } catch (Exception e) {
	        	e.printStackTrace();
	        }
	        return zipfile;
	    }

		protected void onPreExecute() {						
	        Intent intent = new Intent(c, DownloadFile.class);
	        final PendingIntent pendingIntent = PendingIntent.getActivity(c, 0, intent, 0);

	        notification = new Notification(R.drawable.ic_launcher, "Download Started", System
	                .currentTimeMillis());
	        notification.flags = notification.flags | Notification.FLAG_ONGOING_EVENT;
	        notification.contentView = new RemoteViews(c.getPackageName(), R.layout.download_progress);
	        notification.contentIntent = pendingIntent;
	        notification.contentView.setImageViewResource(R.id.status_icon, R.drawable.ic_launcher);
	        notification.contentView.setTextViewText(R.id.status_text, "Downloading " + zipfile);
	        notification.contentView.setProgressBar(R.id.status_progress, 100, progress, false);

	        notificationManager.notify(42, notification);
		}	    
		
		protected void onProgressUpdate(String... args){
		    notification.contentView.setProgressBar(R.id.status_progress, 100, Integer.parseInt(args[0]), false);			            
		}
		   
 	    protected void onPostExecute(String f) {
			 int icon = R.drawable.ic_launcher;
			 CharSequence tickerText = "Finished!";
			 long when = System.currentTimeMillis();
			 CharSequence contentTitle = "Finished Downloading"; 
			 CharSequence contentText = "Finished with "+f; 			 
			 Intent notificationIntent = new Intent(c, MainActivity.class);
			 PendingIntent contentIntent = PendingIntent.getActivity(c, 0, notificationIntent, 0);
			 Notification n = new Notification(icon, tickerText, when);
	   	     n.defaults = Notification.DEFAULT_VIBRATE;
	   	     n.flags = Notification.FLAG_AUTO_CANCEL;
			 n.setLatestEventInfo(c, contentTitle, contentText, contentIntent);
			 final int HELLO_ID = 1;
			 notificationManager.notify(HELLO_ID, n);            
			 
			 notificationManager.cancel(42);
 	    }
	}
}
