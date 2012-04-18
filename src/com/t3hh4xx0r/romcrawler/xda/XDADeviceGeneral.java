package com.t3hh4xx0r.romcrawler.xda;

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
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.t3hh4xx0r.romcrawler.R;
import com.t3hh4xx0r.romcrawler.Receiver;
import com.t3hh4xx0r.romcrawler.SettingsMenu;
import com.t3hh4xx0r.romcrawler.activities.ListFragmentViewPagerActivity;
import com.t3hh4xx0r.romcrawler.activities.MainActivity;
import com.t3hh4xx0r.romcrawler.adapters.RWGeneralAdapter;
import com.t3hh4xx0r.romcrawler.adapters.TitleResults;
import com.t3hh4xx0r.romcrawler.adapters.XDAGeneralAdapter;

import eu.erikw.PullToRefreshListView;
import eu.erikw.PullToRefreshListView.OnRefreshListener;

public class XDADeviceGeneral extends Activity {

    public static ArrayList<String> threadArray;
    static ArrayList<String> titlesArray;
    String message;
    String url;
    String threadTitle = null;  
    ProgressBar pB;
    ListView listView;
    Context ctx;
    int subCount;

    
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.main);
        ctx = (XDADeviceGeneral.this);
        threadArray = new ArrayList<String>();
        titlesArray = new ArrayList<String>();
        Bundle extras = getIntent().getExtras();
        url = extras.getString("url");
        
        
        listView = (ListView) findViewById(android.R.id.list);
        pB = (ProgressBar) findViewById(R.id.progressBar1);
		
        final Vibrator vibe = (Vibrator) getSystemService(VIBRATOR_SERVICE) ;

        listView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> a, View v, int position, long id) { 
       		 Object o = listView.getItemAtPosition(position);
             TitleResults fullObject = (TitleResults)o;
             String URL = fullObject.getUrl();
             Intent intent = new Intent(XDADeviceGeneral.this, XDADeviceSpecific.class);
             Bundle b = new Bundle();
             b.putString("url", URL);
             intent.putExtras(b);
             startActivity(intent);     	
            }  
           });
        listView.setOnItemLongClickListener(new OnItemLongClickListener() {
        	@Override
        	public boolean onItemLongClick(AdapterView<?> a, View v, int position, long id) {
    		vibe.vibrate(50); // 50 is time in ms
      		Object o = listView.getItemAtPosition(position);
            TitleResults fullObject = (TitleResults)o;
            threadTitle = fullObject.getItemName();
            String URL = fullObject.getUrl();
            Intent iW = new Intent(Intent.ACTION_VIEW, Uri.parse(URL));
    		startActivity(Intent.createChooser(iW, getResources().getString(R.string.browser_view)));
        	return false;	
        	}
        });
        new CreateArrayListTask().execute(url);
    }
    
    private class CreateArrayListTask extends AsyncTask<String, Void, ArrayList<TitleResults>> {       
        final ArrayList<TitleResults> results = new ArrayList<TitleResults>();
        
		@Override
		protected ArrayList<TitleResults> doInBackground(String... urls) {
	        TitleResults titleArray =  new TitleResults();
			StringBuilder whole = new StringBuilder();
	        subCount = 0;

			try {
				URL url = new URL(urls[0]);
				HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
				urlConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Macintosh; U; Intel Mac OS X 10.4; en-US; rv:1.9.2.2) Gecko/20100316 Firefox/3.6.2");
				try {
					BufferedReader in = new BufferedReader(
						new InputStreamReader(new BufferedInputStream(urlConnection.getInputStream())));
					String inputLine;
					while ((inputLine = in.readLine()) != null)
						whole.append(inputLine);
					in.close();
				} catch (IOException e) {
	       			e.printStackTrace();
				}
				finally {
					urlConnection.disconnect();
				}
			} catch (Exception e) {
       			e.printStackTrace();
			}
			Document doc = Parser.parse(whole.toString(), urls[0]);
			Elements threads = doc.select(".forumtitle");
       		for (Element thread : threads) {
       			titleArray =  new TitleResults();
       			
       			//Thread title
       			threadTitle = thread.text();
       			titleArray.setItemName(threadTitle);
       			titlesArray.add(threadTitle);
       			//Thread link
				String u = "http://forum.xda-developers.com/forumdisplay.php?f="+thread.toString().split("=")[4].split("\"")[0];
       			titleArray.setUrl(u);
       			//Summary
       			String s = thread.toString().split("<p>")[1].replace("</p></h3>", "").replaceAll("amp;", "");
       			threadArray.add(u);
       			titleArray.setSummary(s);
       			results.add(titleArray);
       		}
 			return results;
		}
			
		protected void onPreExecute(){
					results.clear();
		        	pB.setVisibility(View.VISIBLE);
				}
		
        @Override
        protected void onPostExecute(ArrayList<TitleResults> results) {
        	if (results.size() == 0) {
        		makeToast("no results", XDADeviceGeneral.this);
        	}
        	pB.setVisibility(View.GONE); 
        	listView.setAdapter(new XDAGeneralAdapter(XDADeviceGeneral.this, results, "xda"));
            
     		Intent intent = new Intent(ctx, Receiver.class);
    		PendingIntent pendingIntent = PendingIntent.getBroadcast(
    				ctx.getApplicationContext(), 234324243, intent, 0);
    		AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
    		alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()
    				+ 0, pendingIntent);
        }
    }
    
	public void makeToast(String message, Context ctx) {
		Toast.makeText(ctx, message, Toast.LENGTH_LONG).show();
	}
	
 
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater menuinflate = new MenuInflater(this);
		menuinflate.inflate(R.menu.forum_menu, menu);
		menu.removeItem(R.id.restart);
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

