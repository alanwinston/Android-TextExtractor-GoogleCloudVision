package com.example.awd;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;


public class itemCache {

    public static List<String> loadItems(Context context){
        SharedPreferences pref = context.getSharedPreferences(Constants.SHARED_PERFERENCES_CONTAINER, Context.MODE_PRIVATE);
        String json = pref.getString(Constants.SHARED_KEY,null);
        if(json == null){
            return  new ArrayList<>();
        }
        Gson gson = new Gson();
        return gson.fromJson(json,new TypeToken<ArrayList<String>>() {}.getType());


    }

    public static void saveItems(Context context, List<String> items){
        if(items==null){

            itemCache.saveItems(context,new ArrayList<String>());
            return;
        }
        Gson gson = new Gson() ;
        String json = gson.toJson(items);
        SharedPreferences pref= context.getSharedPreferences(Constants.SHARED_PERFERENCES_CONTAINER, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(Constants.SHARED_KEY,json);
        editor.apply();
    }
}
