package com.t3hh4xx0r.romcrawler.activities;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.Gallery.LayoutParams;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher.ViewFactory;

import com.t3hh4xx0r.romcrawler.R;
import com.t3hh4xx0r.romcrawler.adapters.LazyAdapter;

public class SSActivity extends Activity implements ViewFactory {
    LazyAdapter adapter;
    ArrayList<String> linkArray;
    Elements links;
    String message;
    String threadUrl = null;
    ListView list;
    ImageSwitcher selected;
    ImageView il;
    ImageView im;
    ImageView ir;
    TextView tV;
    String listItemValue;
    int listValue = 0;
    ProgressBar pB;
    Drawable i0;
    Drawable i1;
    Drawable i2;
    Gallery gallery;
    BitmapDrawable selectedBitmap;
    ArrayList<Drawable> bArray;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle); 
        setContentView(R.layout.ss_layout);

		bArray = new ArrayList<Drawable>();      

        listItemValue = "0";
        pB = (ProgressBar) findViewById(R.id.progressBar1);
        
        gallery = (Gallery) findViewById(R.id.gallery);
		selected = (ImageSwitcher)findViewById(R.id.selected);
		selected.setFactory(this);
		selected.setInAnimation(AnimationUtils.loadAnimation(this,
                android.R.anim.fade_in));
		selected.setOutAnimation(AnimationUtils.loadAnimation(this,
                android.R.anim.fade_out));
		
        Bundle extras = getIntent().getExtras();
        threadUrl = extras.getString("url");
        
        gallery.setOnItemClickListener(new AdapterView.OnItemClickListener() {
           public void onItemClick(AdapterView<?> adapterView,
                                   View parent, int position, long id) {
               selected.setImageDrawable(bArray.get(position));
            }
         });
  		new CreateArrayListTask().execute();

    }
    
    private Drawable ImageOperations(Context ctx, String url, String saveFilename) {
		try {
			InputStream is = (InputStream) this.fetch(url);
			Drawable d = Drawable.createFromStream(is, "src");
			return d;
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
    
	public Object fetch(String address) throws MalformedURLException,IOException {
		URL url = new URL(address);
		Object content = url.getContent();
		return content;
	}
	
	   private class CreateArrayListTask extends AsyncTask<String, String, ArrayList<String>> {               
	 		@Override
	 		protected ArrayList<String> doInBackground(String... params) {
				Document doc;
				try {
			        linkArray = new ArrayList<String>();
					if (threadUrl.startsWith("http://")) {							
							doc = Jsoup.connect(threadUrl).get();
					} else {
							doc = Jsoup.connect("http://" + threadUrl).get();
					}
		
					Elements images = doc.select(".bbc_img");
    				for (Element image : images) {
    					if (image.attr("abs:src").endsWith(".png")) { 
    				       	publishProgress(image.attr("abs:src"));
    					}
    				}
				} catch (Exception e) {}
		       	return linkArray;
	 		}
	 		
	 	protected void onPostExecute(ArrayList<String> linkArray) {
	       	gallery.setAdapter(new ImageAdapter(getApplication()));
	       	if (bArray.size()>0) {
	       		selected.setImageDrawable(bArray.get(0));
	       	} else {
	       		makeToast("No screenshots found.");
	       	}
	 	    pB.setVisibility(View.GONE);  
	 	}
	 	
		protected void onProgressUpdate(String... image) {
        	Drawable i = ImageOperations(getBaseContext(),image[0],image[0]+"-image.jpg");
			bArray.add(i);
	       	gallery.setAdapter(new ImageAdapter(getApplication()));
	 	}
	}
    
	public void makeToast(String message) {
		Toast.makeText(this, message, Toast.LENGTH_LONG).show();
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

    public View makeView() 
    {
        ImageView imageView = new ImageView(this);
        imageView.setBackgroundColor(android.R.color.black);
        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        imageView.setLayoutParams(new 
                ImageSwitcher.LayoutParams(
                        LayoutParams.FILL_PARENT,
                        LayoutParams.FILL_PARENT));
        return imageView;
    }
    
	 public class ImageAdapter extends BaseAdapter 
	    {
	        private Context context;
	        private int itemBackground;
	 
	        public ImageAdapter(Context c) 
	        {
	            context = c;
	 
	            //---setting the style---                
	            TypedArray a = obtainStyledAttributes(R.styleable.myGallery);
	            itemBackground = a.getResourceId(
	                    R.styleable.myGallery_android_galleryItemBackground, 0);
	            a.recycle();                                                    
	        }
	 
	        //---returns the number of images---
	        public int getCount() 
	        {
	            return bArray.size();
	        }
	 
	        //---returns the ID of an item--- 
	        public Object getItem(int position) 
	        {
	            return position;
	        }
	 
	        public long getItemId(int position) 
	        {
	            return position;
	        }
	 
	        //---returns an ImageView view---
	        public View getView(int position, View convertView, ViewGroup parent)
	        {
	            ImageView imageView = new ImageView(context);
	            imageView.setImageDrawable(bArray.get(position));
	            imageView.setBackgroundResource(itemBackground);
	            return imageView;
	        }
	   }    	
    
}