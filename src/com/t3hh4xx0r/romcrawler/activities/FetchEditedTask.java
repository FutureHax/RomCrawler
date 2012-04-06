package com.t3hh4xx0r.romcrawler.activities;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.jsoup.select.Elements;

import android.content.Context;
import android.util.Log;

import com.t3hh4xx0r.romcrawler.adapters.DBAdapter;

public class FetchEditedTask {

	public static void populate(final Context ctx, final String url, final String ident) {
    	new Thread(new Runnable() {
            public void run() {
            	final DBAdapter db = new DBAdapter(ctx);
            	db.open();
            	String URL = new String(url);
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
				        db.addEdited(ident, edit.text());
			        }
			    }
			    db.close();
       	    }
      }).start();   
    }
}
