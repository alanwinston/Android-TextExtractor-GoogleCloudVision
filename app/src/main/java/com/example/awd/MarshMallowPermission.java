package com.example.awd;


import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;


public class MarshMallowPermission {
    Activity activity;
    Context mContext;

    public MarshMallowPermission(Activity activity) {
        this.activity = activity;
        this.mContext = activity;
    }

    public boolean checkPermission(String permissionname){
        if (ContextCompat.checkSelfPermission(activity, permissionname) == PackageManager.PERMISSION_GRANTED){
            return true;
        } else {
            return false;
        }
    }

    public void requestPermission(String permissionname,int requestCode){

            ActivityCompat.requestPermissions(activity,new String[]{permissionname},requestCode);
    }
}

