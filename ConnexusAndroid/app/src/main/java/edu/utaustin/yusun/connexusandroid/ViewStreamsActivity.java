package edu.utaustin.yusun.connexusandroid;


import android.content.Context;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

import java.util.ArrayList;

public class ViewStreamsActivity extends AppCompatActivity {
//    public final static String EXTRA_MESSAGE = "com.mycompany.myfirstapp.MESSAGE";
    Context context = this;
    private String TAG  = "Display Streams";
    final String ViewAllStreams_url = "http://connexus-1104.appspot.com/android_viewall";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_streams);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        AsyncHttpClient httpClient = new AsyncHttpClient();
        httpClient.get(ViewAllStreams_url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                final ArrayList<String> cover_URLs = new ArrayList<String>();
                try {
                    System.out.println(new String(responseBody));
                    JSONObject jObject = new JSONObject(new String(responseBody));
                    JSONArray cover_URLs_json = jObject.getJSONArray("cover_urls");

                    for (int i = 0; i < cover_URLs_json.length(); i++) {
                        cover_URLs.add(cover_URLs_json.getString(i));
                        System.out.println(cover_URLs_json.getString(i));
                    }
                    ExpandableHeightGridView gridView = (ExpandableHeightGridView) findViewById(R.id.gridView);
                    gridView.setAdapter(new ImageAdapter(context, cover_URLs));
                    gridView.setExpanded(true);
                    gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                                        public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                                                            Toast.makeText(getApplicationContext(),
                                                                    ((TextView) v).getText(), Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                    );
                } catch (JSONException j) {
                    System.out.println("JSON Error");
                }
            }
            @Override
            public void onFailure(int statusCode, Header[] headers,byte[] responseBody, Throwable error) {

            }
        });
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        });
    }

    // [START on_click]
//    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.nearby:
                onNearbyClicked();
                break;
            case R.id.search:
                onSearchClicked();
                break;
            case R.id.view_subscribed_streams:
                onViewSubscribedClicked();
                break;

        }
    }

    //When click on the nearby button
    private void onNearbyClicked() {
        Intent k = new Intent(ViewStreamsActivity.this, ViewNearbyActivity.class);
        startActivity(k);
    }

    //When click on the search button
    private void onSearchClicked() {
    }

    //When click on the view subscibed stream button
    private  void onViewSubscribedClicked() {
    }

}
