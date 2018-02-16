package com.example.awd;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.IOException;

public class Fetch_Activity extends AppCompatActivity {



    public  static EditText cloud_response_data;
    ProgressDialog progressDialog;
    public static CoordinatorLayout coordinatorLayout;
    public static ImageView selectedImage;
    public static Boolean flag = false;
    String LOG_TAG = "LOG";
    VisionCloud visionCloud;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fetch);

        cloud_response_data = (EditText) findViewById(R.id.cloudresponsedata);
        selectedImage = (ImageView) findViewById(R.id.selectedimage);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.colayout_gallery);

        String value = getIntent().getStringExtra(Constants.VISION_ACTIVITY);
        if(value!=null){
            if (value.equals(Constants.LANDING_GALERY)){
                Intent intent = new Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, Constants.GALLERY_INTENT);
            }
            else if (value.equals(Constants.LANDING_CAMERA)){
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, Constants.CAMERA_INTENT);
            }
        }




        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab_share = (FloatingActionButton) findViewById(R.id.fab_share);
        fab_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textparse();
                if (flag){
                    sharing();
                }

            }
        });
        FloatingActionButton fab_save = (FloatingActionButton) findViewById(R.id.fab_save);
        fab_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textparse();
                if (flag){
                    Toast.makeText(getApplicationContext(),R.string.saving, Toast.LENGTH_SHORT).show();
                saveItem();
                }
            }
        });

        visionCloud = new VisionCloud(this,progressDialog,coordinatorLayout,cloud_response_data);

    }

    public void sharing(){
        String sharetext = cloud_response_data.getText().toString();
        Intent  shareintent = new Intent(Intent.ACTION_SEND);
        shareintent.setType("text/plain");
        shareintent.putExtra(Intent.EXTRA_TEXT, sharetext);
        startActivity(Intent.createChooser(shareintent, "share via"));
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        try {
            if (requestCode == Constants.GALLERY_INTENT
                    && resultCode == RESULT_OK
                    && null != data) {
                Uri URI = data.getData();
                uploadImage(URI);

            }
            else if (requestCode == Constants.CAMERA_INTENT &&
                    resultCode == RESULT_OK
                    && null != data) {
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                selectedImage.setImageBitmap(resizeBitmap(bitmap));
                visionCloud.callCloudVision(bitmap);
                }
            else {
                finish();
            }
        } catch (Exception e) {
            Toast.makeText(this, R.string.fetchfail, Toast.LENGTH_LONG)
                    .show();
        }

    }
    public void uploadImage(Uri uri) {
        if (uri != null) {
            try {
                Bitmap bitmap = resizeBitmap(
                        MediaStore.Images.Media.getBitmap(getContentResolver(), uri));
                visionCloud.callCloudVision(bitmap);
                selectedImage.setImageBitmap(bitmap);
            } catch (IOException e) {
                Log.e(LOG_TAG, e.getMessage());
            }
        } else {
            Log.e(LOG_TAG, "Null image was returned.");
        }
    }
    public Bitmap resizeBitmap(Bitmap bitmap) {
        int maxDimension = 1024;
        int originalWidth = bitmap.getWidth();
        int originalHeight = bitmap.getHeight();
        int resizedWidth = maxDimension;
        int resizedHeight = maxDimension;

        if (originalHeight > originalWidth) {
            resizedHeight = maxDimension;
            resizedWidth = (int) (resizedHeight * (float) originalWidth / (float) originalHeight);
        } else if (originalWidth > originalHeight) {
            resizedWidth = maxDimension;
            resizedHeight = (int) (resizedWidth * (float) originalHeight / (float) originalWidth);
        } else if (originalHeight == originalWidth) {
            resizedHeight = maxDimension;
            resizedWidth = maxDimension;
        }
        return Bitmap.createScaledBitmap(bitmap, resizedWidth, resizedHeight, false);
    }


    public void textparse(){
        if (cloud_response_data.length()==0){
            Toast.makeText(getApplicationContext(),R.string.no_data, Toast.LENGTH_SHORT).show();
            finish();
        } else {
            flag = true;
        }
    }
    public void saveItem(){
        Intent intent = new Intent(Fetch_Activity.this,LandingPage.class);
        String item = cloud_response_data.getText().toString();
        intent.putExtra(Constants.SAVE_DATA,item);
        startActivity(intent);
    }

}
