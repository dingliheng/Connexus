package edu.utaustin.yusun.connexusandroid;


import android.content.Context;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
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
    public final static String EXTRA_MESSAGE = "name";
    private String[] mPlanetTitles;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_streams);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mPlanetTitles = getResources().getStringArray(R.array.planets_array);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        ObjectDrawerItem[] drawerItem = new ObjectDrawerItem[3];
        drawerItem[0] = new ObjectDrawerItem(R.drawable.drawer_home, "Home");
        drawerItem[1] = new ObjectDrawerItem(R.drawable.drawer_view_all, "View All Streams");
        drawerItem[2] = new ObjectDrawerItem(R.drawable.drawer_logout, "Log Out");
        // Set the adapter for the list view
        DrawerItemCustomAdapter adapter = new DrawerItemCustomAdapter(this, R.layout.drawer_list_item, drawerItem);
        mDrawerList.setAdapter(adapter);
        // Set the list's click listener
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        AsyncHttpClient httpClient = new AsyncHttpClient();
        httpClient.get(ViewAllStreams_url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                final ArrayList<String> cover_URLs = new ArrayList<String>();
                final ArrayList<String> names = new ArrayList<String>();
                try {
                    JSONObject jObject = new JSONObject(new String(responseBody));
                    JSONArray cover_URLs_json = jObject.getJSONArray("cover_urls");
                    JSONArray names_json = jObject.getJSONArray("names");

                    for (int i = 0; i < cover_URLs_json.length(); i++) {
                        cover_URLs.add(cover_URLs_json.getString(i));
                        names.add(names_json.getString(i));
                        System.out.println(cover_URLs_json.getString(i));
                    }
                    ExpandableHeightGridView gridView = (ExpandableHeightGridView) findViewById(R.id.gridView);
                    gridView.setAdapter(new ImageAdapter(context, cover_URLs));
                    gridView.setExpanded(true);
                    gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                                        @Override
                                                        public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                                                            System.out.println(names.get(position));
                                                            Intent viewastream = new Intent(ViewStreamsActivity.this, ViewAStreamActivity.class);
                                                            viewastream.putExtra(EXTRA_MESSAGE,names.get(position).toString());
                                                            startActivity(viewastream);
                                                        }
                                                    }
                    );
                } catch (JSONException j) {
                    System.out.println("JSON Error");
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

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

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView parent, View view, int position, long id) {
            selectItem(position);
        }
    }

    /** Swaps fragments in the main content view */
    private void selectItem(int position) {
        // Create a new fragment and specify the planet to show based on position
        switch (position) {
            case 0:
                break;
            case 1:
                Intent k1 = new Intent(ViewStreamsActivity.this, ViewAStreamActivity.class);
                startActivity(k1);
                break;
            case 2:
                Intent k2 = new Intent(ViewStreamsActivity.this, LoginActivity.class);
                startActivity(k2);
                break;
        }

        // Highlight the selected item, update the title, and close the drawer
        mDrawerList.setItemChecked(position, true);
        setTitle(mPlanetTitles[position]);
        mDrawerLayout.closeDrawer(mDrawerList);
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getSupportActionBar().setTitle(" ");
    }

}
