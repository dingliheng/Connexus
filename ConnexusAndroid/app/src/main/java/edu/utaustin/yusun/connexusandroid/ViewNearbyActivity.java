package edu.utaustin.yusun.connexusandroid;

import android.app.Activity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by yusun on 15/10/18.
 */
public class ViewNearbyActivity extends Activity {
    private GoogleMap map;
    LatLngBounds.Builder builder;
    CameraUpdate cu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        /**get the reference of map from layout*/
        map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
                .getMap();
        /**call the map set up method*/
        mSetUpMap();

    }
    /**create method to set map view*/
    public void mSetUpMap() {
        /**clear the map before redraw to them*/
        map.clear();
        /**Create dummy Markers List*/
        List<Marker> markersList = new ArrayList<Marker>();
        Marker Delhi = map.addMarker(new MarkerOptions().position(new LatLng(
                28.61, 77.2099)).title("Delhi"));
        Marker Chaandigarh = map.addMarker(new MarkerOptions().position(new LatLng(
                30.75, 76.78)).title("Chandigarh"));
        Marker SriLanka = map.addMarker(new MarkerOptions().position(new LatLng(
                7.000, 81.0000)).title("Sri Lanka"));
        Marker America = map.addMarker(new MarkerOptions().position(new LatLng(
                38.8833, 77.0167)).title("America"));
        Marker Arab = map.addMarker(new MarkerOptions().position(new LatLng(
                24.000, 45.000)).title("Arab"));

        /**Put all the markers into arraylist*/
        markersList.add(Delhi);
        markersList.add(SriLanka);
        markersList.add(America);
        markersList.add(Arab);
        markersList.add(Chaandigarh);

        /**create for loop for get the latLngbuilder from the marker list*/
        builder = new LatLngBounds.Builder();
        for (Marker m : markersList) {
            builder.include(m.getPosition());
        }
        /**initialize the padding for map boundary*/
        int padding = 50;
        /**create the bounds from latlngBuilder to set into map camera*/
        LatLngBounds bounds = builder.build();
        /**create the camera with bounds and padding to set into map*/
        cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
        /**call the map call back to know map is loaded or not*/
        map.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                /**set animated zoom camera into map*/
                map.animateCamera(cu);

            }
        });
    }


//    Read more: http://www.androidhub4you.com/2015/06/android-maximum-zoom-in-google-map.html#ixzz3pbOuQqbn
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_view_nearby);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
//    }

}
