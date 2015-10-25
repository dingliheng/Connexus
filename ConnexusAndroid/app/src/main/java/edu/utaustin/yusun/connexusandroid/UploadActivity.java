package edu.utaustin.yusun.connexusandroid;

import android.content.Context;
import android.content.Intent;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.FrameLayout;

import java.io.File;


/**
 * Created by yusun on 15/10/18.
 */
public class UploadActivity extends AppCompatActivity implements View.OnClickListener{
    private Camera mCamera;
    private CameraPreview mPreview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        findViewById(R.id.take_picture_button).setOnClickListener(this);
        findViewById(R.id.use_picture_button).setOnClickListener(this);
        findViewById(R.id.view_all_button).setOnClickListener(this);

        //Create an instance of Camera
        mCamera = getCameraInstance();
        //Create out Preview view and set it as the content of the activity
        mPreview = new CameraPreview(this, mCamera);
        FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
        preview.addView(mPreview);



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    public static  Camera getCameraInstance() {
        Camera c = null;
        try {
            c = Camera.open(0);
        } catch (Exception e) {
            Log.w("getCameraInstance ", "We have Caremas  num" + Camera.getNumberOfCameras());
            Log.w("getCameraInstance ", "Error setting camera preview: " + e.getMessage());
        }
        if(c == null)
            Log.w("Null", "open failed!!!!!!");
        return c;
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
