package edu.utaustin.yusun.connexusandroid;
/**
 * Created by yusun on 15/10/28.
 */
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class MySubscribedActivity extends AppCompatActivity implements
        View.OnClickListener{
    Context context = this;
    private String TAG  = "Display Streams";
    final String ViewAllStreams_url = "http://connexus-1104.appspot.com/android_viewall";
    public final static String NAME = "name";
    public final static String KEYWORDS = "keywords";
    public final static String TIMES = "times";
    private String[] mPlanetTitles;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_subscribed);
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
    }

    @Override
    public void onClick(View v) {

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
                Intent k1 = new Intent(MySubscribedActivity.this, ViewStreamsActivity.class);
                startActivity(k1);
                break;
            case 2:
                Intent k2 = new Intent(MySubscribedActivity.this, LoginActivity.class);
                startActivity(k2);
                break;
        }

        // Highlight the selected item, update the title, and close the drawer
        mDrawerList.setItemChecked(position, true);
        mDrawerLayout.closeDrawer(mDrawerList);
    }



}
