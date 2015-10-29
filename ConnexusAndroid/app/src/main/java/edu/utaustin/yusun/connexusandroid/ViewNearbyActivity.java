package edu.utaustin.yusun.connexusandroid;

import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * Created by yusun on 15/10/18.
 */
public class ViewNearbyActivity extends AppCompatActivity implements LocationListener {

    protected String mLatitudeLabel;
    protected String mLongitudeLabel;
    protected TextView mLatitudeText;
    protected TextView mLongitudeText;
    public static Location lastLocation;
    Context context = this;

    private static final String[] S = { "Out of Service",
            "Temporarily Unavailable", "Available" };

    private TextView output;
    private LocationManager locationManager;
    private String bestProvider;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_nearby);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mLatitudeLabel = getResources().getString(R.string.latitude_label);
        mLongitudeLabel = getResources().getString(R.string.longitude_label);
//        mLatitudeText = (TextView) findViewById((R.id.latitude_text));
        mLongitudeText = (TextView) findViewById((R.id.longitude_text));

        // Get the output UI
        output = (TextView) findViewById(R.id.latitude_text);

        // Get the location manager
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        // List all providers:
        List<String> providers = locationManager.getAllProviders();
        for (String provider : providers) {
            printProvider(provider);
        }

        Criteria criteria = new Criteria();
        bestProvider = locationManager.getBestProvider(criteria, false);
        output.append("\n\nBEST Provider:\n");
        printProvider(bestProvider);

        output.append("\n\nLocations (starting with last known):");
        try {
            lastLocation = locationManager.getLastKnownLocation(bestProvider);
            printLocation(lastLocation);
        } catch (SecurityException e) {

        }

//        float latitude = (float)ViewNearbyActivity.lastLocation.getLatitude();
//        float longitude = (float)ViewNearbyActivity.lastLocation.getLongitude();
        float latitude = 100.0f;
        float longitude = 100.0f;
        System.out.println(latitude);
        String ViewNearby_url = "http://connexuselvis.appspot.com/android_viewnearby?latitude="+latitude+"&longtitude="+longitude;
        System.out.println(ViewNearby_url);
        AsyncHttpClient httpClient = new AsyncHttpClient();
        httpClient.get(ViewNearby_url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                final ArrayList<String> picture_URLs = new ArrayList<String>();
                final ArrayList<String> distances = new ArrayList<String>();
                try {
                    JSONObject jObject = new JSONObject(new String(responseBody));
                    JSONArray picture_URLs_json = jObject.getJSONArray("picture_urls");
                    JSONArray distance_json = jObject.getJSONArray("distance");

                    for (int i = 0; i < picture_URLs_json.length(); i++) {
                        picture_URLs.add(picture_URLs_json.getString(i));
                        distances.add(distance_json.getString(i));
                        System.out.println(picture_URLs_json.getString(i));
                    }
                    ExpandableHeightGridView gridView = (ExpandableHeightGridView) findViewById(R.id.gridView2);
                    gridView.setAdapter(new ImageAdapter(context, picture_URLs));
                    gridView.setExpanded(true);
//                    gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                                                        @Override
//                                                        public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
//
//                                                        }
//                                                    }
//                    );
                } catch (JSONException j) {
                    System.out.println("JSON Error");
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });

    }

    /** Register for the updates when Activity is in foreground */
    @Override
    protected void onResume() {
        super.onResume();
        try {
            locationManager.requestLocationUpdates(bestProvider, 1000, 1, this);
        } catch (SecurityException e) {

        }

    }

    /** Stop the updates when Activity is paused */
    @Override
    protected void onPause() {
        super.onPause();
        try {
            locationManager.removeUpdates(this);
        } catch (SecurityException e) {

        }

    }

    public void onLocationChanged(Location location) {
        printLocation(location);

    }

    public void onProviderDisabled(String provider) {
        // let okProvider be bestProvider
        // re-register for updates
        output.append("\n\nProvider Disabled: " + provider);
    }

    public void onProviderEnabled(String provider) {
        // is provider better than bestProvider?
        // is yes, bestProvider = provider
        output.append("\n\nProvider Enabled: " + provider);
    }

    public void onStatusChanged(String provider, int status, Bundle extras) {
        output.append("\n\nProvider Status Changed: " + provider + ", Status="
                + S[status] + ", Extras=" + extras);
    }

    private void printProvider(String provider) {
        LocationProvider info = locationManager.getProvider(provider);
        output.append(info.toString() + "\n\n");
    }

    private void printLocation(Location location) {
        if (location == null)
            output.append("\nLocation[unknown]\n\n");
        else
            output.append("\n\n" + location.toString());
    }




}
