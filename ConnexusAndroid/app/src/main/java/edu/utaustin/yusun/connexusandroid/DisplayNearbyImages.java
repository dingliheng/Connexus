package edu.utaustin.yusun.connexusandroid;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Liheng on 2015/10/19.
 */
public class DisplayNearbyImages extends AppCompatActivity {
//    Context context = this;
//    private String TAG = "Display Nearby Images";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get the message from the intent
        Intent intent = getIntent();
//        String message = intent.getStringExtra(ViewStreamsActivity.EXTRA_MESSAGE);

        // Create the text view
//        TextView textView = new TextView(this);
//        textView.setTextSize(40);
//        textView.setText(message);

        // Set the text view as the activity layout
//        setContentView(textView);
//        final String request_url = "http://localhost:8080/";
//        AsyncHttpClient httpClient = new AsyncHttpClient();
//        httpClient.get(request_url, new AsyncHttpResponseHandler() {
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
//                String content;
//                try {
//                    JSONObject jObject = new JSONObject(new String(responseBody));
//                    content = jObject.getString("");
//                    textView.setText(content);
//                }
//                catch(JSONException j){
//                    System.out.println("JSON Error");
//                }
//            }
//
//            @Override
//            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable e) {
//                Log.e(TAG, "There was a problem in retrieving the url : " + e.toString());
//            }
//        });

    }
}
