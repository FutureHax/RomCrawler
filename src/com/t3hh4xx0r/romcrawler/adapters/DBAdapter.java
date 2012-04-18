package com.t3hh4xx0r.romcrawler.adapters;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBAdapter 
{
    public static final String KEY_ROWID = "_id";
    public static final String KEY_URL = "url";
    public static final String KEY_XDA = "xda";
    public static final String KEY_RW = "rw";
    public static final String KEY_TITLE = "title";
    public static final String KEY_IDENT = "ident";
    public static final String KEY_TYPE = "type";
    public static final String KEY_AUTHOR = "author";
    public static final String KEY_EDITED = "edited";
    public static final String KEY_SITE = "site";
    private static final String TAG = "DBAdapter";
    
    private static final String DATABASE_NAME = "crawler.db";
    private static final String DATABASE_TABLE = "favs";
    private static final String DATABASE_TABLE2 = "device";
    private static final int DATABASE_VERSION = 10;

    private static final String DATABASE_CREATE =
            "create table favs (_id integer primary key autoincrement, "
            + "url text not null, title text not null, ident text not null, site text not null, author text not null, type text not null, edited text not null );";
 
    private static final String DATABASE_CREATE2 =
            "create table device (_id integer primary key autoincrement, "
            + "rw text not null, xda text not null );";
 
    private final Context context; 
    
    private DatabaseHelper DBHelper;
    public SQLiteDatabase db;

    public DBAdapter(Context ctx) 
    {
        this.context = ctx;
        DBHelper = new DatabaseHelper(context);
    }
        
    private static class DatabaseHelper extends SQLiteOpenHelper 
    {
        DatabaseHelper(Context context) 
        {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) 
        {
            db.execSQL(DATABASE_CREATE);
            db.execSQL(DATABASE_CREATE2);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, 
        int newVersion) 
        {
            Log.w(TAG, "Upgrading database from version " + oldVersion 
                    + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS favs");
            db.execSQL("DROP TABLE IF EXISTS device");
            onCreate(db);
        }
    }    
    
    //---opens the database---
    public DBAdapter open() throws SQLException 
    {
        db = DBHelper.getWritableDatabase();
        return this;
    }

    //---closes the database---    
    public void close() 
    {
    	DBHelper.close();
    }
    
    //---insert a title into the database---
    public long insertFav(String url, String title, String ident, String site, String author, String type, String edited) 
    {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_URL, url);
        initialValues.put(KEY_TITLE, title);
        initialValues.put(KEY_IDENT, ident);
        initialValues.put(KEY_SITE, site);
        initialValues.put(KEY_AUTHOR, author);
        initialValues.put(KEY_TYPE, type);
        initialValues.put(KEY_EDITED, edited);
        
        return db.insert(DATABASE_TABLE, null, initialValues);
    }

    //---deletes a particular title---
    public boolean deleteFav(long rowId) 
    {
        return db.delete(DATABASE_TABLE, KEY_ROWID + 
        		"=" + rowId, null) > 0;        		
    }
    

    //---retrieves all the titles---
    public Cursor getAllFavs() 
    {
    	Cursor mCursor = db.query(DATABASE_TABLE, new String[] {
        		KEY_ROWID, 
                KEY_URL,
                KEY_TITLE,
                KEY_IDENT,
                KEY_SITE,
                KEY_AUTHOR,
                KEY_TYPE,
                KEY_EDITED}, 
                null, 
                null, 
                null, 
                null, 
                null);

    	if (mCursor != null) {
            mCursor.moveToFirst();
        }
	
		return mCursor;
    }

    public Cursor getAllUrls() 
    {
        return db.query(DATABASE_TABLE, new String[] {
        		KEY_ROWID, 
                KEY_URL}, 
                null, 
                null, 
                null, 
                null, 
                null);
    }
    
    public Cursor getId(String url) throws SQLException {
        Cursor mCursor = db.query(true, DATABASE_TABLE, new String[] {
        		KEY_ROWID,
        		}, 
        		KEY_URL + "=" + url, 
        		null,
        		null, 
        		null, 
        		null, 
        		null);

        if (mCursor != null) {
            mCursor.moveToFirst();
        }

		return mCursor;
    	
    }


    public Cursor getByIdent(String ident) throws SQLException {
        Cursor mCursor = db.query(true, DATABASE_TABLE, new String[] {
        		KEY_ROWID,
        		KEY_TITLE,
        		}, 
        		KEY_IDENT + "=" + ident, 
        		null,
        		null, 
        		null, 
        		null, 
        		null);

        if (mCursor != null) {
            mCursor.moveToFirst();
        }

		return mCursor;
    	
    }
    
    public Cursor getUrl(String title) throws SQLException {
        Cursor mCursor = db.query(true, DATABASE_TABLE, new String[] {
        		"url",
        		}, 
        		title + "=" + title, 
        		null,
        		null, 
        		null, 
        		null, 
        		null);

        if (mCursor != null) {
            mCursor.moveToFirst();
        }

		return mCursor;
    	
    }
    
    //---deletes a particular title---
    public boolean deleteUrl(long rowId) 
    {
        return db.delete(DATABASE_TABLE, KEY_ROWID + 
        		"=" + rowId, null) > 0;        		
    }
    
    //---retrieves a particular title---
    public Cursor getUrl(long rowId) throws SQLException 
    {
        Cursor mCursor =
                db.query(true, DATABASE_TABLE, new String[] {
                		KEY_ROWID,
                		KEY_URL
                		}, 
                		KEY_ROWID + "=" + rowId, 
                		null,
                		null, 
                		null, 
                		null, 
                		null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

	public Cursor byTitle(String[] title) throws SQLException {
		  Cursor mCursor = db.query(true, DATABASE_TABLE, new String[] {
                  KEY_URL
                  }, 
                  "title=?", 
                  title,
                  null, 
                  null, 
                  null, 
                  null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
   }
	
	   public void addEdited(String ident, String edited) 
	    {
		   Log.d("UPDATING", ident +" : "+edited);
	        ContentValues args = new ContentValues();
	        args.put(KEY_EDITED, edited);
	        this.db.update(DATABASE_TABLE, args,"ident = ?", new String[] {ident});
	    }
	   
	    public void updateDevice(String rw, String xda) 
	    {	    	
			if (isDeviceSet()) { 
		    	if (xda.equals("")) {
		    		Cursor c = getDevice();
		   			xda = c.getString(2);
		   			c.close();
		   		}
		    	if (rw.equals("")) {
		    		Cursor c = getDevice();
		   			rw = c.getString(1);
		   			c.close();
		   		}
		    	
		    	ContentValues initialValues = new ContentValues();
				initialValues.put(KEY_XDA, xda);
				initialValues.put(KEY_RW, rw);
				db.update(DATABASE_TABLE2, initialValues, "_id=1", null);
		    } else {
				ContentValues initialValues = new ContentValues();
				initialValues.put(KEY_XDA, xda);
				initialValues.put(KEY_RW, rw);
		        db.insert(DATABASE_TABLE2, null, initialValues);				
			}
	  	}

		public Cursor getDevice() {
		   	Cursor mCursor = db.query(DATABASE_TABLE2, new String[] {
	        		KEY_ROWID, 
	                KEY_RW,
	                KEY_XDA}, 
	                null, 
	                null, 
	                null, 
	                null, 
	                null);

	    	if (mCursor != null) {
	            mCursor.moveToFirst();
	        }
		
			return mCursor;
		}

		public boolean isDeviceSet() {
			Cursor cur = db.rawQuery("SELECT COUNT(*) FROM DEVICE", null);
			if (cur != null) {
			    cur.moveToFirst();
			    if (cur.getInt (0) == 0) {
			    	return false;
			    } else{
			    	return true;
			    }
			} else {
				return false;
			}			
		}
}