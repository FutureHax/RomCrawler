package com.t3hh4xx0r.romcrawler.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.t3hh4xx0r.romcrawler.R;
import com.t3hh4xx0r.romcrawler.xda.XDADeviceGeneral;

public class XDAGeneralAdapter extends BaseAdapter {
	ArrayList<TitleResults> threadArrayList;
	static ArrayList<String> favs;
	String title;
	String url;
	String summary;
	
	 private LayoutInflater mInflater;
	 Context ctx;

	 public XDAGeneralAdapter(Context context, ArrayList<TitleResults> results, String site) {
	  threadArrayList = new ArrayList<TitleResults>(results);
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
		  convertView = mInflater.inflate(R.layout.xda_list_item, null);

		  holder = new ViewHolder();
		  holder.itemName = (TextView) convertView.findViewById(R.id.itemName);
		  holder.itemSummary = (TextView) convertView.findViewById(R.id.itemSummary);
	
		  convertView.setTag(holder);   	   	  
		  } else {
		   	  holder = (ViewHolder) convertView.getTag();
		  }
	
	  holder.itemName.setText(threadArrayList.get(position).getItemName());
	  holder.itemSummary.setText(threadArrayList.get(position).getSummary());

	  return convertView;
	 }

	 static class ViewHolder {
	  TextView itemName;
	  TextView itemSummary;
	 }
}