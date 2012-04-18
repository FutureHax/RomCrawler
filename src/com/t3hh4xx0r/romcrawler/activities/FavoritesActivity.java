package com.t3hh4xx0r.romcrawler.activities;

import java.util.ArrayList;
import java.util.LinkedHashSet;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.t3hh4xx0r.romcrawler.Constants;
import com.t3hh4xx0r.romcrawler.R;
import com.t3hh4xx0r.romcrawler.adapters.DBAdapter;
import com.t3hh4xx0r.romcrawler.adapters.FavsAdapter;
import com.t3hh4xx0r.romcrawler.adapters.TitleResults;
import com.t3hh4xx0r.romcrawler.rootzwiki.RWSubForum;
import com.t3hh4xx0r.romcrawler.ui.BetterPopupWindow;

public class FavoritesActivity extends ListActivity {
	ArrayList<String> urls;
	static ArrayList<String> names;
	public static ArrayList<String> types;
	public static ArrayList<String> sites;
	public static ArrayList<String> identList;
	static ArrayList<String> authors;
    ArrayAdapter<String> adapter;
    ListView lV;
    Context ctx;

	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.favorites);
		final Vibrator vibe = (Vibrator) getSystemService(VIBRATOR_SERVICE) ;
		types = new ArrayList<String>();
		sites = new ArrayList<String>();
		urls = new ArrayList<String>();
		names = new ArrayList<String>();
		authors = new ArrayList<String>();
        identList = new ArrayList<String>();
	    lV = (ListView) findViewById(android.R.id.list);
	    ctx = (FavoritesActivity.this);    
	    populate();

	    lV.setOnItemClickListener(new OnItemClickListener() {
	    	public void onItemClick(AdapterView<?> a, View v, int position, long id) {
	            Intent intent = null;
	            Bundle b = new Bundle();
	            b.putStringArrayList("titles", names);
	            if (types.get(position).equals("thread")) {
		            b.putStringArrayList("urls", urls);
	            	intent = new Intent(FavoritesActivity.this, ListFragmentViewPagerActivity.class);
	            } else {
	            	intent = new Intent(FavoritesActivity.this, RWSubForum.class);
	            }
	            b.putString("title", names.get(position));
	            if (!urls.get(position).startsWith("http://")) {
	            	b.putString("url", "http://"+urls.get(position));
	            } else {
	            	b.putString("url", urls.get(position));
	            }
                b.putInt("c", position);
                b.putBoolean("f", true);
                b.putStringArrayList("idents", identList);
	            b.putString("type", types.get(position));
	            intent.putExtras(b);
	            startActivity(intent);    	           	    		
	    	}
        });
	    
	    lV.setOnItemLongClickListener(new OnItemLongClickListener() {
	    	@Override
	    	public boolean onItemLongClick(AdapterView<?> a, View v, int position, long id) {
	    		String u;
	    		if (!urls.get(position).startsWith("http://")) {
	            	u = "http://"+urls.get(position);
	            } else {
	            	u = urls.get(position);
	            }
	        	vibe.vibrate(50);
	        	Constants.sel = false;
	        	BetterPopupWindow dw = new BetterPopupWindow.DemoPopupWindow(v, position, u, null, null);
				dw.showLikeQuickAction(0, 30);
				return false;	
	    	}
	    });
	}
	    
    public  void populate() {
        final DBAdapter db = new DBAdapter(this);
        	db.open();
    	    Cursor c = db.getAllFavs();
    	    if (c.getCount()>0) {
        		urls.add(c.getString(1));
        		names.add(c.getString(2));
        		authors.add(c.getString(5));
        		identList.add(c.getString(3));
        		sites.add(c.getString(4));
        		types.add(c.getString(6));
    	    		try {
    	    			while (c.moveToNext()) {
    	    		   		urls.add(c.getString(1));
    	    	    		names.add(c.getString(2));
    	            		authors.add(c.getString(5));
    	            		sites.add(c.getString(4));
    	            		types.add(c.getString(6));
    	            		identList.add(c.getString(3));
    	    	    	}
    	    		} catch (Exception ep) {
    	    			ep.printStackTrace();
    	    		}
    	    }
    	    c.close();
    		db.close();
		LinkedHashSet<String> lhashSet = new LinkedHashSet<String>(names);
 	    names = new ArrayList<String>(lhashSet);
 	    createArrayList();
    }

	public void createArrayList() {       
        final ArrayList<TitleResults> results = new ArrayList<TitleResults>();
        TitleResults titleArray =  new TitleResults();
        
        for (int i=0;i<names.size();i++) {
   			titleArray =  new TitleResults();
			titleArray.setItemName(names.get(i));
			titleArray.setAuthorDate(authors.get(i));
	        results.add(titleArray);
        }
   
        lV.setAdapter(new FavsAdapter(FavoritesActivity.this, results));
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
