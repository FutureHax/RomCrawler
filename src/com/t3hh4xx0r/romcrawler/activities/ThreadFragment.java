package com.t3hh4xx0r.romcrawler.activities;

import java.io.File;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.t3hh4xx0r.romcrawler.Constants;
import com.t3hh4xx0r.romcrawler.R;

public class ThreadFragment extends ListFragment {
	  String URL;
	  Context ctx;
	  ArrayList<String> linkArray;
	  String[] urls;
	  ArrayList<String> URLS;
	  ArrayList<String> TITLES;
	  
	   @Override
	    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		    ctx = container.getContext();
		    URL = getArguments().getString("url");
		    if (!URL.startsWith("http://") ) {
		    	URL = "http://"+URL;
		    }
		    URLS = getArguments().getStringArrayList("urls");
		    TITLES = getArguments().getStringArrayList("titles");
	        return super.onCreateView(inflater, container, savedInstanceState);
	    }
	  
	    @Override
	    public void onStart() {
	        super.onStart();
	        new CreateArrayListTask().execute(URL);
	    }
	  
	    @Override
		public void onListItemClick(ListView l, View v, int position, long id) {
	    	super.onListItemClick(l, v, position, id);
	        String linkURL = linkArray.get(position);
	        String[] bits = linkURL.split("/");
			String zipName = bits[bits.length-1];
			if (zipName.startsWith("download.php?=")) {
				zipName.replace("download.php?=", "");
	    	}
	        File dir = new File(Constants.extSD + "/" + "t3hh4xx0r/romCrawler/" + zipName);
	        if (!dir.exists()) {
	        	dir.mkdirs();
	        }
	        File f = new File(Constants.extSD + "/" + "t3hh4xx0r/romCrawler/" + zipName);

	        if (linkURL.startsWith("http://")) {	        	
	        	if (linkURL.contains("goo-inside") || linkURL.contains("goo.me")) {
	  	            Intent iW = new Intent(Intent.ACTION_VIEW, Uri.parse(linkURL));
	  	            v.getContext().startActivity(Intent.createChooser(iW, v.getContext().getResources().getString(R.string.browser_view)));
	        	} else {
	        		DownloadManager dManager = (DownloadManager)ctx.getSystemService(Context.DOWNLOAD_SERVICE);
	        		Uri down = Uri.parse(linkURL);
	        		DownloadManager.Request req = new DownloadManager.Request(down);
	        		req.setShowRunningNotification(true);
	        		req.setVisibleInDownloadsUi(true);
	        		req.setDestinationUri(Uri.fromFile(f));
	        		dManager.enqueue(req);
	        	}
	        }
	 		
	    }
	    
	    private class CreateArrayListTask extends AsyncTask<String, Void, ArrayList<String>> {               
	 		@Override
	 		protected ArrayList<String> doInBackground(String... params) {
		        linkArray = new ArrayList<String>();
	 			try {
	 					URL url = new URL(URL);
	 					URLConnection con = url.openConnection();
	 					Pattern p = Pattern.compile("text/html;\\s+charset=([^\\s]+)\\s*");
	 					Matcher m = p.matcher(con.getContentType());
	 					String charset = m.matches() ? m.group(1) : "ISO-8859-1";
	 					Reader r = new InputStreamReader(con.getInputStream(), charset);
	 					StringBuilder buf = new StringBuilder();
	 					while (true) {
	 						int ch = r.read();
	 						if (ch < 0)
	 							break;
	 						buf.append((char) ch);
	 					}
	 					String strMessy = buf.toString();
	 					String strClean = new String (strMessy.replaceAll("\\<.*?>",""));
	 					while (strClean.contains(".zip")){
	 						String trimmed = strClean.substring(0, strClean.lastIndexOf(".zip"));
	 						String[] parts = trimmed.split("\n");
	 						String lastWord = parts[parts.length - 1] + ".zip";
	 						strClean = new String(strClean.replace(lastWord, ""));
	 						if (!lastWord.contains(" ")) {
	 							linkArray.add(lastWord);
	 						}
	 					}
	 					Document doc = Jsoup.connect(url.toString()).get();
	    				Elements links = doc.select("a[href]");
	     				for (Element link : links) {
	     					for (Iterator<String> c = linkArray.iterator(); c.hasNext();) {
	     						String newName = c.next();
	 	       					if (link.attr("abs:href").contains(newName)) {		        				
	 	       						c.remove();
	 	       					}
	     					}
	     					if (link.attr("abs:href").contains(".zip")) {	
	    							linkArray.add(link.attr("abs:href"));
	    						}
	    					}
	     				if (linkArray.isEmpty()) {
	     					String warning = "No direct links detected. Please have your rom dev switch to dev-host.org for their hosting.\nIts free and provides direct links to all downloads.";
	     					linkArray.add(warning);
	     				}
	 				} catch (Exception e) {
	 					e.printStackTrace();
	 				}
	 			return linkArray;
	 		}
	 		
	 	    protected void onPostExecute(ArrayList<String> linkArray) {
	 	    	try {
	 	    		ArrayAdapter<String> a = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, linkArray);
	 	    		setListAdapter(a);
	 	    	} catch (Exception e) {}
	 	    }
	    }
}