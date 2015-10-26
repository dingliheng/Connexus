package edu.utaustin.yusun.connexusandroid;

import android.content.Intent;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;


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

    public Camera getCameraInstance() {
        Camera c = null;
        try {
            int cameraId = -1;
            // Search for the front facing camera
            int numberOfCameras = Camera.getNumberOfCameras();
            for (int i = 0; i < numberOfCameras; i++) {
                Camera.CameraInfo info = new Camera.CameraInfo();
                Camera.getCameraInfo(i, info);
                if (info.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                    Log.w("getCameraInstance" , "Camera found");
                    cameraId = i;
                    break;
                }
            }
            if (cameraId < 0) {
                Toast.makeText(this, "No camera found.",
                        Toast.LENGTH_LONG).show();
            } else {
                c = Camera.open(cameraId);
            }
            c = Camera.open(cameraId);
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
