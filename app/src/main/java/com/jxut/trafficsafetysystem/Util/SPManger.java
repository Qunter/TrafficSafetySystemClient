package com.jxut.trafficsafetysystem.Util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by luyufa on 2016/10/17.
 */
public class SPManger {

    private Context context;
    private SharedPreferences.Editor editor;

    @SuppressLint("CommitPrefEdits")
    public SPManger(Context context, String filename){
        this.context = context;
        SharedPreferences sharedPreferences = context.getSharedPreferences(filename, Context.MODE_APPEND);
        editor = sharedPreferences.edit();
    }

    public Object get(String filename,String key){
        SharedPreferences share = context.getSharedPreferences(filename, Context.MODE_WORLD_READABLE);
        Object value=share.getString("str",null);
        return value;
    }

    public void put(String key,String value){
        editor.putString(key,value);
        editor.commit();
    }
    public void put(String key,Integer value){
        editor.putInt(key,value);
        editor.commit();
    }
    public void put(String key,Long value){
        editor.putLong(key,value);
        editor.commit();
    }
    public void put(String key,Boolean value){
        editor.putBoolean(key,value);
        editor.commit();
    }
    public void put(String key,Float value){
        editor.putFloat(key,value);
        editor.commit();
    }
}
