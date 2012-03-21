package com.t3hh4xx0r.romcrawler;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.t3hh4xx0r.romcrawler.activities.MainActivity;
import com.t3hh4xx0r.romcrawler.activities.ThreadFragment;
import com.t3hh4xx0r.romcrawler.adapters.TitleAdapter;
import com.t3hh4xx0r.romcrawler.adapters.TitleResults;

import eu.erikw.PullToRefreshListView;
import eu.erikw.PullToRefreshListView.OnRefreshListener;

public class XDAActivity extends Activity {
	public static ArrayList<String> threadArray;
    static ArrayList<String> titlesArray;
    static ArrayList<String> authorArray;
    public static ArrayList<String> identList;
    String message;
    String ident;
    String threadTitle = null;    
    ProgressBar pB;
    PullToRefreshListView listView;    
    Context ctx;
    
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.main);
        ctx = XDAActivity.this;
        
        listView = (PullToRefreshListView) findViewById(android.R.id.list);
        listView.setOnRefreshListener(new OnRefreshListener() {
			public void onRefresh() {
				getDevice();
	        	new CreateArrayListTask().execute(Constants.FORUM);
	            listView.onRefreshComplete();
			}
        });
        pB = (ProgressBar) findViewById(R.id.progressBar1);

        threadArray = new ArrayList<String>();
        titlesArray = new ArrayList<String>();
        authorArray = new ArrayList<String>();
        identList = new ArrayList<String>();

        final Vibrator vibe = (Vibrator) getSystemService(VIBRATOR_SERVICE) ;

        getDevice();
        
        listView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> a, View v, int position, long id) { 
          		 Object o = listView.getItemAtPosition(position);
                 TitleResults fullObject = (TitleResults)o;
                 threadTitle = fullObject.getItemName();
                 String author = fullObject.getAuthorDate();
                 String URL = fullObject.getUrl();
                 String ident = fullObject.getIdent();
                 Intent intent = new Intent(XDAActivity.this, ThreadFragment.class);
                 Bundle b = new Bundle();
                 b.putString("title", threadTitle);
                 b.putString("url", URL);
                 b.putString("type", "xda");
                 b.putString("author", author);
                 b.putString("ident", ident);             
                 intent.putExtras(b);
                 startActivity(intent);    	  	
            }  
           });
        listView.setOnItemLongClickListener(new OnItemLongClickListener() {
        	@Override
        	public boolean onItemLongClick(AdapterView<?> a, View v, int position, long id) {
        	vibe.vibrate(50); // 50 is time in ms
            String url = threadArray.get(position);
        	Intent iW = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
    		startActivity(Intent.createChooser(iW, getResources().getString(R.string.browser_view)));
        	return false;	
        	}
        });
        
        if(Constants.DEVICE != null){
        	new CreateArrayListTask().execute(Constants.FORUM);
            }
        }
    
    public void getDevice() {
    	String device = android.os.Build.DEVICE.toUpperCase();
    	try {
    		DeviceTypeXDA type = Enum.valueOf(DeviceTypeXDA.class, device);
    		Constants.DEVICE = type.name();
    		Constants.FORUM = type.getForumUrl();
    	} catch (IllegalArgumentException e) {
    		message = "Device not found/supported!";
        	makeToast(message);        	
    	}
    }
    
    
    private class CreateArrayListTask extends AsyncTask<String, Void, ArrayList<TitleResults>> {       
        final ArrayList<TitleResults> results = new ArrayList<TitleResults>();
        
		@Override
		protected ArrayList<TitleResults> doInBackground(String... params) {
	        TitleResults titleArray =  new TitleResults();
			StringBuilder whole = new StringBuilder();

				try {
					URL url = new URL(
							Constants.FORUM);
					HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
					urlConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2");
					try {
						BufferedReader in = new BufferedReader(
							new InputStreamReader(new BufferedInputStream(urlConnection.getInputStream())));
						String inputLine;
						while ((inputLine = in.readLine()) != null)
							whole.append(inputLine);
						in.close();
					} catch (IOException e) {}
					finally {
						urlConnection.disconnect();
					}
				} catch (Exception e) {}
				String clean = whole.toString().replaceAll("<html dir?.*?<!-- show threads -->", "");
				String authorString = "Originally Posted By:";

			    while (clean.contains(authorString)){
			    	String author = clean.split(authorString)[1];
			    	String[] tmp = author.split("<");
			    	author =  tmp[0];
			    	authorArray.add(author.replace(" ", ""));
			    	clean = new String(clean.replace(authorString + author, ""));
				}	
			        
				Document doc = Parser.parse(clean, Constants.FORUM);
				Elements threads = doc.select("a[id]");

	       		for (Element thread : threads) {
	       			titleArray =  new TitleResults();

	       			try{titleArray.setAuthorDate(authorArray.get(0));authorArray.remove(0);}catch(Exception e){}
	       				       			
	       			//Thread title
	       			threadTitle = thread.text();
	       			titleArray.setItemName(threadTitle);
	       			titlesArray.add(threadTitle);
	       		
	       			//Thread link
	       			String threadStr = thread.attr("abs:href");
	       			threadArray.add(threadStr);
	       			String[] bits = threadStr.split("=");
	       			ident = bits[bits.length-1];
	       			identList.add(ident);
	       			if (!(threadTitle.length()<2)) {
	       				results.add(titleArray);
	       			}
	       		}
	 			return results;
			}

			protected void onPreExecute(){
				results.clear();
	        	pB.setVisibility(View.VISIBLE);
	        }
		
	        @Override
	        protected void onPostExecute(ArrayList<TitleResults> results) {
	        	pB.setVisibility(View.GONE);        	
	            listView.setAdapter(new TitleAdapter(XDAActivity.this, results, "xda")); 
	            
	     		Intent intent = new Intent(ctx, Receiver.class);
	    		PendingIntent pendingIntent = PendingIntent.getBroadcast(
	    				ctx.getApplicationContext(), 234324243, intent, 0);
	    		AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
	    		alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()
	    				+ 0, pendingIntent);
	        }
	    }

	public void makeToast(String message) {
		Toast.makeText(this, message, Toast.LENGTH_LONG).show();
	}
	
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater menuinflate = new MenuInflater(this);
		menuinflate.inflate(R.menu.forum_menu, menu);
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
	        case R.id.restart:
				getDevice();
	        	new CreateArrayListTask().execute(Constants.FORUM);
	            listView.onRefreshComplete();
	        break;
	        case R.id.settings:
	            Intent si = new Intent(this, SettingsMenu.class);
	            si.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	            startActivity(si);
	        break;
	    default:
	            return super.onOptionsItemSelected(item);
	    }
		return false;
	}
}
