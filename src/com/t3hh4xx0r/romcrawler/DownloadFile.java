package com.t3hh4xx0r.romcrawler;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

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
    private int progress = 10;
	public DownloadFile(String url, String out, String zip, Context ctx) {        
		c = ctx;
		zipfile = zip;
		outfile = out;
		
        notificationManager = (NotificationManager) c.getSystemService(
                c.NOTIFICATION_SERVICE);
        
		new Download().execute(url);
	}

	private class Download extends AsyncTask<String, Integer, String>{
	    @Override
	    protected String doInBackground(String... urls) {
	        try {
	            URL url = new URL(urls[0]);
	            HttpURLConnection c = (HttpURLConnection) url.openConnection();
//	            c.setRequestMethod("GET");
//	            c.setDoOutput(true);
	            c.connect();

	            String PATH = Environment.getExternalStorageDirectory()
	                    + "/t3hh4xx0r/romCrawler/";
	            Log.v("log_tag", "PATH: " + PATH);
	            File file = new File(PATH);
	            file.mkdirs();
	            File outputFile = new File(file, zipfile);
	            FileOutputStream fos = new FileOutputStream(outputFile);

	            InputStream is = c.getInputStream();

	            byte[] buffer = new byte[1024];
	            int len1 = 0;
	            while ((len1 = is.read(buffer)) != -1) {
	                fos.write(buffer, 0, len1);
	            }
	            fos.close();
	            is.close();
	        } catch (Exception e) {
	        	e.printStackTrace();
	        }
	        return zipfile;
	    }

		protected void onPreExecute() {						
	        Intent intent = new Intent(c, DownloadFile.class);
	        final PendingIntent pendingIntent = PendingIntent.getActivity(c, 0, intent, 0);

	        notification = new Notification(R.drawable.ic_launcher, "simulating a download", System
	                .currentTimeMillis());
	        notification.flags = notification.flags | Notification.FLAG_ONGOING_EVENT;
	        notification.contentView = new RemoteViews(c.getPackageName(), R.layout.download_progress);
	        notification.contentIntent = pendingIntent;
	        notification.contentView.setImageViewResource(R.id.status_icon, R.drawable.ic_launcher);
	        notification.contentView.setTextViewText(R.id.status_text, "simulation in progress");
	        notification.contentView.setProgressBar(R.id.status_progress, 100, progress, false);

	        //notificationManager.notify(42, notification);
		}	    
		
		protected void onProgressUpdate(Integer... args){
		    notification.contentView.setProgressBar(R.id.status_progress, 100, args[0], false);		    
		}
		   
 	    protected void onPostExecute(String f) {
 	    	Toast.makeText(c, "DONE WITH "+f, 9965454).show();
            notificationManager.cancel(42);
 	    }
	}
}
