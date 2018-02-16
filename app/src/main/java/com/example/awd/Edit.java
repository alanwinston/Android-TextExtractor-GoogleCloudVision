package com.example.awd;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class Edit extends AppCompatActivity {

    public  static EditText cloud_response_data;
    Boolean flag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar1);
        cloud_response_data = (EditText) findViewById(R.id.cloudresponsedata1);


        String extra = getIntent().getStringExtra(Constants.LANDING_EDIT);
        cloud_response_data.setText(extra);

        setSupportActionBar(toolbar);


        FloatingActionButton fab_share = (FloatingActionButton) findViewById(R.id.fab_share1);
        fab_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textparse();
                if (flag){
                    sharing();
                }

            }
        });
        FloatingActionButton fab_save = (FloatingActionButton) findViewById(R.id.fab_save1);
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
    }
    private void saveItem() {
        Intent intent = new Intent();
        String item = cloud_response_data.getText().toString();
        intent.putExtra(Constants.EDIT_INTENT, item);
        setResult(RESULT_OK, intent);
        finish();

    }
    @Override
    public void onBackPressed()
    {
        finish();

        super.onBackPressed();
    }

    public void sharing(){
        String sharetext = cloud_response_data.getText().toString();
        Intent shareintent = new Intent(Intent.ACTION_SEND);
        shareintent.setType("text/plain");
        shareintent.putExtra(Intent.EXTRA_TEXT, sharetext);
        startActivity(Intent.createChooser(shareintent, "share via"));
    }
    public void textparse(){
        if (cloud_response_data.length()==0){
            Toast.makeText(getApplicationContext(),R.string.no_data, Toast.LENGTH_SHORT).show();
            finish();
        } else {
            flag = true;
        }
    }

}
