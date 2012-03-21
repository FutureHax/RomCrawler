package com.t3hh4xx0r.romcrawler;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class Utils {
    public static void CopyStream(InputStream is, OutputStream os)
    {
        final int buffer_size=1024;
        try
        {
            byte[] bytes=new byte[buffer_size];
            for(;;)
            {
              int count=is.read(bytes, 0, buffer_size);
              if(count==-1)
                  break;
              os.write(bytes, 0, count);
            }
        }
        catch(Exception ex){}
    }
    
    public String Paste() {


		return null;
    	
    }

    public static Bitmap loadBitmap(String fileName) throws IOException {
        Bitmap result = null;

        FileInputStream fileInput = null;
        BufferedInputStream bufInput = null;
        try {
           fileInput = new FileInputStream(fileName);
           bufInput = new BufferedInputStream(fileInput);
           result = BitmapFactory.decodeStream(bufInput);
        } finally {
           if (fileInput != null) {
              try {
                 fileInput.close();
              } catch (IOException e) {
              }
           }
           if (bufInput != null) {
              try {
                 bufInput.close();
              } catch (IOException e) {
              }
           }
        }
        return result;
     }
}