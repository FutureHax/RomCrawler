package com.t3hh4xx0r.romcrawler.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.t3hh4xx0r.romcrawler.Constants;
import com.t3hh4xx0r.romcrawler.R;
import com.t3hh4xx0r.romcrawler.rootzwiki.RWDeviceGeneral;

public class RWGeneralAdapter extends BaseAdapter {
	ArrayList<TitleResults> threadArrayList;
	static ArrayList<String> favs;
	String title;
	String url;
	String author;
	String type;
	String msite;
	String ident;
	
	 private LayoutInflater mInflater;
	 Context ctx;

	 public RWGeneralAdapter(Context context, ArrayList<TitleResults> results, String site) {
	  threadArrayList = new ArrayList<TitleResults>(results);
	  mInflater = LayoutInflater.from(context);
	  ctx = context;	  
	  favs = new ArrayList<String>();
	  msite = site;	  
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
		  convertView = mInflater.inflate(R.layout.list_item, null);

		  holder = new ViewHolder();
		  holder.authorDate = (TextView) convertView.findViewById(R.id.authorDate);
		  holder.itemName = (TextView) convertView.findViewById(R.id.itemName);
		  holder.itemName.setSelected(true);
		  holder.fb = (ImageView) convertView.findViewById(R.id.fav_button);
		  
	    	if (Constants.isReg) {
	    		reFav();
        		holder.fb.setVisibility(View.VISIBLE);
	    	} else {
	    		if (Constants.isAdFree) {
	        		holder.fb.setEnabled(true);
		    		reFav();
	    		}
	    	}
	
		  convertView.setTag(holder);   	   	  
		  } else {
		   	  holder = (ViewHolder) convertView.getTag();
		  }
	
		  holder.itemName.setText(threadArrayList.get(position).getItemName());
		  holder.authorDate.setText(threadArrayList.get(position).getAuthorDate());
			  if (favs.contains(RWDeviceGeneral.identList.get(position))) {
				  holder.fb.setBackgroundResource(R.drawable.fav_yes);
			  } else {
				  holder.fb.setBackgroundResource(R.drawable.fav_no);
			  }
	      holder.fb.setOnClickListener(new OnClickListener() {
	  		public void onClick(View v) {
	  			DBAdapter db = new DBAdapter(ctx);
	  			title = holder.itemName.getText().toString();
	  			author = holder.authorDate.getText().toString();
  		  	  	url = RWDeviceGeneral.threadArray.get(position);
  		  	  	ident = RWDeviceGeneral.identList.get(position);
  		  	  	String type = RWDeviceGeneral.typeList.get(position);
	  			url = new String(url.replaceAll("http://", ""));
	  			db.open();
	  			Cursor c = db.getByIdent(ident);
	  		    if (c.getCount()>0) {
		    		db.deleteFav(Integer.parseInt(c.getString(0)));
		    		holder.fb.setBackgroundResource(R.drawable.fav_no);	
		    		reFav();
	  		    } else {
	  	    		db.insertFav(url, title, ident, msite, author, type, "");	  		    	
	  			    holder.fb.setBackgroundResource(R.drawable.fav_yes);
	  		    }
	  			c.close();
	  			db.close();
	  		}
	      });
	  return convertView;
	 }

	 static class ViewHolder {
	  TextView itemName;
	  TextView authorDate;
	  ImageView fb;
	 }
	 
	 void reFav() {
		 favs.clear();
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
	 }
}