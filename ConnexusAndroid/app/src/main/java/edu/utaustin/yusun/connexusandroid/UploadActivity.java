package edu.utaustin.yusun.connexusandroid;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class UploadActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        findViewById(R.id.take_picture_button).setOnClickListener(this);
        findViewById(R.id.use_picture_button).setOnClickListener(this);
        findViewById(R.id.view_all_button).setOnClickListener(this);



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.take_picture_button:
                Intent k1 = new Intent(UploadActivity.this, ViewNearbyActivity.class);
                startActivity(k1);
                break;
            case R.id.use_picture_button:
                Intent k2 = new Intent(UploadActivity.this, ViewNearbyActivity.class);
                startActivity(k2);
                break;
            case R.id.view_all_button:
                Intent k3 = new Intent(UploadActivity.this, ViewNearbyActivity.class);
                startActivity(k3);
                break;

        }
    }
}
