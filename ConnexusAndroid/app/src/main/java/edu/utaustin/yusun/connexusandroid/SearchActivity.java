package edu.utaustin.yusun.connexusandroid;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
/**
 * Created by yusun on 15/10/18.
 */
public class SearchActivity extends AppCompatActivity implements
        View.OnClickListener{
    Context context = this;
    private String TAG  = "Search Streams";
    public final static String NAME = "name";
    public final static String KEYWORDS = "keywords";
    public final static String TIMES = "times";
    private String[] mPlanetTitles;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private String keywords;
    private int times;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null)
            getSupportActionBar().setTitle(" ");
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

        findViewById(R.id.search_button).setOnClickListener(this);
        findViewById(R.id.more_search_results).setOnClickListener(this);

        Intent intent = getIntent();
        keywords = intent.getStringExtra(ViewStreamsActivity.KEYWORDS);
        times = Integer.parseInt(intent.getStringExtra(ViewStreamsActivity.TIMES));
        keywords = intent.getStringExtra(SearchActivity.KEYWORDS);
        times = Integer.parseInt(intent.getStringExtra(SearchActivity.TIMES));

        final String SearchStreams_url = "http://connexus-1104.appspot.com/android_search?keywords="+keywords+"&times="+times;
        AsyncHttpClient httpClient = new AsyncHttpClient();
        httpClient.get(SearchStreams_url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                final ArrayList<String> cover_URLs = new ArrayList<String>();
                final ArrayList<String> names = new ArrayList<String>();
                try {
                    JSONObject jObject = new JSONObject(new String(responseBody));
                    JSONArray cover_URLs_json = jObject.getJSONArray("cover_urls");
                    JSONArray names_json = jObject.getJSONArray("names");
                    times = jObject.getInt("times");
                    int num_results = jObject.getInt("num_results");
                    TextView keywords_text = (TextView) findViewById(R.id.keywords_text);
                    keywords_text.setText(num_results + " results for " + keywords + "\n click on an image to view stream");

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
                                                            Intent viewastream = new Intent(SearchActivity.this, ViewAStreamActivity.class);
                                                            viewastream.putExtra(NAME, names.get(position).toString());
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
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.search_button:
                onSearchClicked();
                break;
            case R.id.more_search_results:
                onMoreSearchReultsClick();
                break;

        }
    }

    //When click on the nearby button
    private void onMoreSearchReultsClick() {
        Intent k = new Intent(SearchActivity.this, SearchActivity.class);
        k.putExtra(KEYWORDS, keywords);
        k.putExtra(TIMES,Integer.toString(times));
        startActivity(k);
    }

    //When click on the search button
    private void onSearchClicked() {
        Intent k = new Intent(SearchActivity.this, SearchActivity.class);
        EditText find_streams = (EditText) findViewById(R.id.keywords_edit);
        keywords = find_streams.getText().toString();
        k.putExtra(KEYWORDS, keywords);
        k.putExtra(TIMES, "1");
        startActivity(k);
    }

    //When click on the view subscibed stream button

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
                Intent k1 = new Intent(SearchActivity.this, ViewAStreamActivity.class);
                startActivity(k1);
                break;
            case 2:
                Intent k2 = new Intent(SearchActivity.this, LoginActivity.class);
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
