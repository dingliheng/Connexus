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
import android.widget.TextView;
import android.widget.Toast;

public class ViewAStreamActivity extends AppCompatActivity {
    Context context = this;
    private String TAG  = "Display Pictures";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_astream);

        Intent intent = getIntent();
        String sream_name = intent.getStringExtra(ViewStreamsActivity.EXTRA_MESSAGE);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final String view_astream_url = "http://connexus-1104.appspot.com/android_viewsingle?stream_name="+sream_name;
        AsyncHttpClient httpClient = new AsyncHttpClient();
        httpClient.get(view_astream_url, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        final ArrayList<String> picture_URLs = new ArrayList<String>();
                        try {
                            JSONObject jObject = new JSONObject(new String(responseBody));
                            JSONArray picture_URLs_json = jObject.getJSONArray("picture_urls");

                            for (int i = 0; i < picture_URLs_json.length(); i++) {
                                picture_URLs.add(picture_URLs_json.getString(i));
                                System.out.println(picture_URLs_json.getString(i));
                            }
                            ExpandableHeightGridView gridView = (ExpandableHeightGridView) findViewById(R.id.gridView2);
                            gridView.setAdapter(new ImageAdapter(context, picture_URLs));
                            gridView.setExpanded(true);
                            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
//                                    Toast.makeText(getApplicationContext(),
//                                            ((TextView) v).getText(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        } catch (JSONException j) {
                            System.out.println("JSON Error");
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                    }
                });
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

}
