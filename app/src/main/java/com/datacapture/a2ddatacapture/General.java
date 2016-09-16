package com.datacapture.a2ddatacapture;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Environment;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by Control & Inst. LAB on 16-Sep-16.
 */
public class General {
    public boolean isWrite = false, isAvail = false;
    public Context context;
    boolean checking = false;

    public General(Context context){
        this.context = context;
    }

    public void SaveImage(ImageView iv, String filename){

        String check_state = checkSDCard();
        String check_memory = checkSDCARDSIZEFORIMAGE();

        if(!check_state.contentEquals("allow")){
            Toast.makeText(context,"NO SDCARD", Toast.LENGTH_LONG).show();
        }else if(!check_memory.contentEquals("memory_available")){
            Toast.makeText(context,"SDCARD MEMORY LOW", Toast.LENGTH_LONG).show();
        }else{
            File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES+ "/db-images/");
            File file = new File(path, filename);
            path.mkdirs();

            OutputStream os;

            try {
                os = new FileOutputStream(file);
                BitmapDrawable bmd = (BitmapDrawable) iv.getDrawable();
                Bitmap bitmap = bmd.getBitmap();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
                os.close();
                Toast.makeText(context,"Details Saved",Toast.LENGTH_LONG).show();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String checkSDCARDSIZEFORIMAGE(){

        long size = 0;
        String getSize = "";

        size = Environment.getExternalStorageDirectory().getFreeSpace();
        if(size < 200000){
            getSize = "memory_low";
        }else {
            getSize = "memory_available";
        }

        return getSize;
    }

    public String checkSDCard(){
        String setState = "";

        String state = Environment.getExternalStorageState();
        if(Environment.MEDIA_MOUNTED.contains(state)){
            isAvail = true;
            isWrite = true;
            setState = "allow";
        }else if(Environment.MEDIA_MOUNTED_READ_ONLY.contains(state)){
            isAvail = true;
            isWrite = false;
            setState = "read";
        }else {
            isWrite = false;
            isAvail = false;
            setState = "disallow";
        }
        return setState;
    }

    public String[] ListImages(){
        File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES+ "/db-images/");
        File[] images = path.listFiles();
        String[] image_names = new String[images.length];

        for (int i = 0; i < images.length; i++) {
            image_names[i] = images[i].getAbsolutePath();
        }

        return image_names;
    }
}
