package edu.utaustin.yusun.connexusandroid;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import cz.msebera.android.httpclient.Header;

public class UploadPictureActivity extends AppCompatActivity {
    Context context = this;
    public final String NAME = "name";
    private String user_email;
    private Bitmap bitmap;
    private String stream_name;
    private String upload_url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_picture);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Intent intent = getIntent();
        user_email = intent.getStringExtra(ViewAStreamActivity.USEREMAIL);
        bitmap = (Bitmap) intent.getParcelableExtra(ViewAStreamActivity.BITMAPIMAGE);
        stream_name = intent.getStringExtra(ViewAStreamActivity.STREAMNAME);
        user_email = intent.getStringExtra(ViewAStreamActivity.USEREMAIL);
        bitmap = (Bitmap) intent.getParcelableExtra(ViewAStreamActivity.BITMAPIMAGE);
        stream_name = intent.getStringExtra(ViewAStreamActivity.STREAMNAME);
        upload_url=getUploadURL();

        ImageView imageView = (ImageView) findViewById(R.id.imageView);
        imageView.setImageBitmap(bitmap);
    }

    public void upload_image(View view) {
        EditText editText = (EditText) findViewById(R.id.pictureTags);
        String tags = editText.getText().toString();
        System.out.println(tags);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        byte[] b = baos.toByteArray();
        byte[] encodedImage = Base64.encode(b, Base64.DEFAULT);
        String encodedImageStr = encodedImage.toString();
        System.out.println(upload_url);
        Double latitude = ViewNearbyActivity.lastLocation.getLatitude();
        Double longitude = ViewNearbyActivity.lastLocation.getLongitude();
        RequestParams params = new RequestParams();
        params.put("latitude", latitude);
        params.put("longitude", longitude);
        params.put("file",new ByteArrayInputStream(b));
        params.put("tags", tags);
        params.put("stream_name", stream_name);
        AsyncHttpClient client = new AsyncHttpClient();
        client.post(upload_url, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                Log.w("async", "success!!!!");
                Toast.makeText(context, "Upload Successful", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable e) {
                Log.e("Posting_to_blob","There was a problem in retrieving the url : " + e.toString());
            }
        });
        Intent viewsingle = new Intent(UploadPictureActivity.this,ViewAStreamActivity.class);
        viewsingle.putExtra(NAME,stream_name);
        startActivity(viewsingle);
    }

    public String getUploadURL(){
        AsyncHttpClient httpClient = new AsyncHttpClient();
        String request_url="http://connexuselvis.appspot.com/android_getUploadURL";
        System.out.println(request_url);
        httpClient.get(request_url, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] response) {

                try {
                    JSONObject jObject = new JSONObject(new String(response));
                    upload_url = jObject.getString("upload_url");
//                    postToServer(encodedImage, photoCaption, upload_url);

                } catch (JSONException j) {
                    System.out.println("JSON Error");
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] errorResponse, Throwable e) {
                Log.e("Get_serving_url", "There was a problem in retrieving the url : " + e.toString());
            }
        });
        return upload_url;
    }
}
