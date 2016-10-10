package com.jxut.trafficsafetysystem.UI;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Administrator on 2016/10/10.
 */

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initVariables();
        initViews();
        loadData();
    }
    public void initVariables(){
        //接收页面之间的数据传递
    }

    public void initViews(){
        //初始化控件
    }

    public void loadData(){
        //访问API数据
    }
    protected void toNewActivity(Class classes){
        Intent intent = new Intent();
        intent.setClass(getApplicationContext(), classes);
        startActivity(intent);
    }
    protected void SPManger(String filename,String key,String value){
        SharedPreferences sharedPreferences = getSharedPreferences(filename,MODE_APPEND);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key,value);
        editor.commit();
    }
    protected void SPManger(String filename,String key,Integer value){
        SharedPreferences sharedPreferences = getSharedPreferences(filename,MODE_APPEND);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key,value);
        editor.commit();
    }
    protected void SPManger(String filename,String key,Long value){
        SharedPreferences sharedPreferences = getSharedPreferences(filename,MODE_APPEND);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(key,value);
        editor.commit();
    }
    protected void SPManger(String filename,String key,Boolean value){
        SharedPreferences sharedPreferences = getSharedPreferences(filename,MODE_APPEND);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key,value);
        editor.commit();
    }
    protected void SPManger(String filename,String key,Float value){
        SharedPreferences sharedPreferences = getSharedPreferences(filename,MODE_APPEND);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putFloat(key,value);
        editor.commit();
    }
}
