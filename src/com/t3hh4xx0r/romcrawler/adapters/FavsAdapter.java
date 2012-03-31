package com.t3hh4xx0r.romcrawler.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.t3hh4xx0r.romcrawler.R;
import com.t3hh4xx0r.romcrawler.activities.FavoritesActivity;

public class FavsAdapter extends BaseAdapter {
	ArrayList<TitleResults> threadArrayList;
	static ArrayList<String> favs;
	String title;
	String url;
	String ident;
	 
	 private LayoutInflater mInflater;
	 Context ctx;
	 FavoritesActivity fav;

	 

	 public FavsAdapter(Context context, ArrayList<TitleResults> results) {
	  threadArrayList = results;
	  mInflater = LayoutInflater.from(context);
	  ctx = context;	  
	  favs = new ArrayList<String>();
	 }
	 
	 public int getCount() {
	  return threadArrayList.size();
	 }

	 public Object getItem(int position) {
	  return threadArrayList.get(position);
	 }

	 public long getItemId(int position) {
	  return position;
	 }

	 public View getView(final int position, View convertView, ViewGroup parent) {
	  final ViewHolder holder;
   	  
	  if (convertView == null) {
	      DBAdapter db = new DBAdapter(ctx);
    	  db.open();
		  Cursor c = db.getAllFavs();
		    if (c.getCount()>0) {
		    	favs.add(c.getString(3));
		    		try {
		    			while (c.moveToNext()) {
		    		    	favs.add(c.getString(3));
		    	    	}
		    		} catch (Exception ep) {
		    			ep.printStackTrace();
		    		}
		    }
	    c.close();
		db.close();

	  convertView = mInflater.inflate(R.layout.list_item2, null);
	  holder = new ViewHolder();
	  holder.authorDate = (TextView) convertView.findViewById(R.id.authorDate);
	  holder.itemName = (TextView) convertView.findViewById(R.id.itemName);
	  holder.itemName.setSelected(true);
	  holder.site = (ImageView) convertView.findViewById(R.id.site_button);

	  convertView.setTag(holder);   	   	  
	  } else {
	   	  holder = (ViewHolder) convertView.getTag();
	  }

	  holder.itemName.setText(threadArrayList.get(position).getItemName());
	  holder.authorDate.setText(threadArrayList.get(position).getAuthorDate());
	  if (FavoritesActivity.sites.get(position).equals("xda")) {
		  holder.site.setBackgroundResource(R.drawable.xda);
	  } else { 
		  holder.site.setBackgroundResource(R.drawable.rootz);
	  }
	  return convertView;
	 }

	 static class ViewHolder {
	  TextView itemName;
	  TextView authorDate;
	  ImageView site;
	 }
	}