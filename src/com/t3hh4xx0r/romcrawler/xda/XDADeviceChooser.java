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
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.t3hh4xx0r.romcrawler.Constants;
import com.t3hh4xx0r.romcrawler.R;
import com.t3hh4xx0r.romcrawler.activities.MainActivity;
import com.t3hh4xx0r.romcrawler.adapters.TitleAdapter;
import com.t3hh4xx0r.romcrawler.adapters.TitleResults;
import com.t3hh4xx0r.romcrawler.ui.BetterPopupWindow;

import eu.erikw.PullToRefreshListView;
import eu.erikw.PullToRefreshListView.OnRefreshListener;

public class XDADeviceChooser extends Activity {
	Context ctx;
    ListView listView;
    ProgressBar pB;
    String url;
    String title;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.main);
        ctx = (XDADeviceChooser.this);
        final Vibrator vibe = (Vibrator) getSystemService(VIBRATOR_SERVICE) ;
        Bundle extras = getIntent().getExtras();
        url = extras.getString("url");
        title = extras.getString("title");
        
        listView = (ListView) findViewById(android.R.id.list);     
        listView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> a, View v, int position, long id) { 
    		 Object o = listView.getItemAtPosition(position);
             TitleResults fullObject = (TitleResults)o;
             String URL = fullObject.getUrl();
             Intent intent = new Intent(XDADeviceChooser.this, XDADeviceGeneral.class);
             Bundle b = new Bundle();
             b.putString("url", URL);
             intent.putExtras(b);
             startActivity(intent);    	
            }  
           });
        listView.setOnItemLongClickListener(new OnItemLongClickListener() {
        	@Override
        	public boolean onItemLongClick(AdapterView<?> a, View v, int position, long id) {
      		Object o = listView.getItemAtPosition(position);
            TitleResults fullObject = (TitleResults)o;
            String URL = fullObject.getUrl();
        	Constants.sel = true;
        	vibe.vibrate(50);
        	BetterPopupWindow dw = new BetterPopupWindow.DemoPopupWindow(v, position, URL, null, URL);
			dw.showLikeQuickAction(0, 30);
    		

        	return false;	
        	}
        });
        pB = (ProgressBar) findViewById(R.id.progressBar1);
        new CreateArrayListTask().execute(url);
    }
    
    private class CreateArrayListTask extends AsyncTask<String, Void, ArrayList<TitleResults>> {       
        final ArrayList<TitleResults> results = new ArrayList<TitleResults>();
        
		@Override
		protected ArrayList<TitleResults> doInBackground(String... urls) {
	        TitleResults titleArray =  new TitleResults();
			StringBuilder whole = new StringBuilder();

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
			Elements devices = doc.select(".forumbox-header");
      		for (Element device : devices) {
      			if (!device.text().startsWith("Welcome")) {
        			titleArray =  new TitleResults();
    				titleArray.setItemName(device.text());
    				String u = "http://forum.xda-developers.com/forumdisplay.php?f="+device.toString().split("=")[4].split("\"")[0];
    				titleArray.setUrl(u);
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
        	try {
        		pB.setVisibility(View.GONE); 
        	} catch (Exception e) {}
        	listView.setAdapter(new TitleAdapter(XDADeviceChooser.this, results, "shit"));
        }
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
	
}
