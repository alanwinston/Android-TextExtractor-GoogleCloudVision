package com.example.awd;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

public class LandingPage extends AppCompatActivity {

    private MarshMallowPermission marshMallowPermission;
    private CoordinatorLayout coordinatorLayout;
    private StringAdapter mAdapter;
    private int removepostion;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_page);


        List<String> items = itemCache.loadItems(this);
        mAdapter = new StringAdapter(this, R.id.list_item_textview);
        mAdapter.setItem(items);
        ListView listView = (ListView) findViewById(R.id.activity_main_listview);
        listView.setAdapter(mAdapter);
        registerForContextMenu(listView);


        marshMallowPermission = new MarshMallowPermission(LandingPage.this);
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorlout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        FloatingActionButton fab_camera = (FloatingActionButton) findViewById(R.id.fab_camera);
        fab_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                camerapermission();
            }
        });


        FloatingActionButton fab_gallery = (FloatingActionButton) findViewById(R.id.fab_gallery);
        fab_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gallerypermission();
            }
        });


        String value = getIntent().getStringExtra(Constants.SAVE_DATA);
        if(value!=null){
            mAdapter.addItem(value);
            saveItem();
        }
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                removepostion = i;
                String extra = mAdapter.getItemdata(removepostion);
                Intent intent = new Intent(LandingPage.this,Edit.class);
                intent.putExtra(Constants.LANDING_EDIT,extra);
                startActivityForResult(intent,Constants.EDITING);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
            if (resultCode == RESULT_OK && requestCode == Constants.EDITING){
                mAdapter.removeItem(removepostion);
                String item = data.getStringExtra(Constants.EDIT_INTENT);
                mAdapter.addItem(item);
                saveItem();
            }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo)
    {
        super.onCreateContextMenu(menu, v, menuInfo);

        if (v.getId() == R.id.activity_main_listview)
        {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.menu_main, menu);
            saveItem();
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item)
    {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        int position = info.position;
        mAdapter.removeItem(position);
        saveItem();
        return true;
    }

    private void saveItem(){
        java.util.List<String > items= mAdapter.Save();
        itemCache.saveItems(this,items);
    }

    public void galleryactivity(){
        Intent intent = new Intent(this,Fetch_Activity.class);
        intent.putExtra(Constants.VISION_ACTIVITY, Constants.LANDING_GALERY);
        startActivity(intent);
    }
    public void cameraactivity(){
        Intent intent = new Intent(this,Fetch_Activity.class);
        intent.putExtra(Constants.VISION_ACTIVITY, Constants.LANDING_CAMERA);
        startActivity(intent);
    }

    public void camerapermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if (!marshMallowPermission.checkPermission(Constants.CAMERA_PERMISSION)) {
                Snackbar.make(coordinatorLayout, R.string.allow_permission, Snackbar.LENGTH_SHORT)
                        .setAction("Action", null).show();
                marshMallowPermission.requestPermission(Constants.CAMERA_PERMISSION, Constants.CAMERA_PERMISSION_CODE);
            }else {
                cameraactivity();
            }
        }else {
            cameraactivity();
        }
    }

    public void gallerypermission(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!marshMallowPermission.checkPermission(Constants.GALLERY_PERMISSION)) {
                Snackbar.make(coordinatorLayout, R.string.allow_permission, Snackbar.LENGTH_SHORT)
                        .setAction("Action", null).show();
                marshMallowPermission.requestPermission(Constants.GALLERY_PERMISSION, Constants.GALLERY_PERMISSION_CODE);
            }else {
                galleryactivity();
            }
        } else {
            galleryactivity();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case Constants.CAMERA_PERMISSION_CODE:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    cameraactivity();
                } else {
                    Snackbar.make(coordinatorLayout, R.string.permission_denied, Snackbar.LENGTH_INDEFINITE)
                            .setAction("Action", null).show();
                }
                break;
            case Constants.GALLERY_PERMISSION_CODE:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    galleryactivity();
                } else {
                    Snackbar.make(coordinatorLayout, R.string.permission_denied, Snackbar.LENGTH_INDEFINITE)
                            .setAction("Action", null).show();
                }
        }
    }

}
