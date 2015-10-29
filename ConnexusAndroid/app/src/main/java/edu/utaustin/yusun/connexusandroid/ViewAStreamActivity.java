package edu.utaustin.yusun.connexusandroid;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;
/**
 * Created by yusun on 15/10/18.
 */
public class ViewAStreamActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int SELECT_PHOTO = 100;
    public static final String STREAMNAME = "stream_name";
    public static final String BITMAPIMAGE = "BitmapImage";
    public static final String USEREMAIL = "user_email";
    Context context = this;
    private String TAG  = "Display Pictures";
    private String stream_name;
    private String stream_email;
    Button btnClosePopup;
    Button btnCreatePopup;

    private CharSequence mTitle;
    private String[] mPlanetTitles;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private CharSequence mDrawerTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_astream);

        Intent intent = getIntent();
        stream_name = intent.getStringExtra(ViewStreamsActivity.NAME);

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

        findViewById(R.id.upload_button).setOnClickListener(this);
        findViewById(R.id.view_streams_button).setOnClickListener(this);

        TextView streamName = (TextView) findViewById(R.id.textView2);
        streamName.setText(stream_name);

        final String view_astream_url = "http://connexuselvis.appspot.com/android_viewsingle?stream_name="+stream_name;
        AsyncHttpClient httpClient = new AsyncHttpClient();
        httpClient.get(view_astream_url, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        final ArrayList<String> picture_URLs = new ArrayList<String>();
                        try {
                            JSONObject jObject = new JSONObject(new String(responseBody));
                            JSONArray picture_URLs_json = jObject.getJSONArray("picture_urls");
                            stream_email = jObject.getString("user_email");

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

    }

    public void returnTostreams(View view) {
        Intent returnTostreams = new Intent(ViewAStreamActivity.this,ViewStreamsActivity.class);
        startActivity(returnTostreams);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.upload_button:
                initiatePopupWindow();
                break;
            case R.id.view_streams_button:
                Intent k2 = new Intent(ViewAStreamActivity.this, ViewStreamsActivity.class);
                startActivity(k2);
                break;

        }
    }

    private PopupWindow pwindo;

    private void initiatePopupWindow() {
        try {
            // We need to get the instance of the LayoutInflater
            LayoutInflater inflater = (LayoutInflater) ViewAStreamActivity.this
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View layout = inflater.inflate(R.layout.screen_popup,
                    (ViewGroup) findViewById(R.id.popup_element));
            pwindo = new PopupWindow(layout, 300, 500, true);
            pwindo.showAtLocation(layout, Gravity.CENTER, 0, 0);

            btnClosePopup = (Button) layout.findViewById(R.id.btn_close_popup);
            btnClosePopup.setOnClickListener(cancel_button_click_listener);

            btnClosePopup = (Button) layout.findViewById(R.id.btn_album);
            btnClosePopup.setOnClickListener(album_button_click_listener);

            btnClosePopup = (Button) layout.findViewById(R.id.btn_camera);
            btnClosePopup.setOnClickListener(camera_button_click_listener);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private View.OnClickListener album_button_click_listener = new View.OnClickListener() {
        public void onClick(View v) {
//            Intent intent = new Intent();
//            intent.setType("image/*");
//            intent.setAction(Intent.ACTION_GET_CONTENT);
//            startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);

            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
            photoPickerIntent.setType("image/*");
            startActivityForResult(photoPickerIntent, SELECT_PHOTO);

        }
    };
    private View.OnClickListener camera_button_click_listener = new View.OnClickListener() {
        public void onClick(View v) {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    };
    private View.OnClickListener cancel_button_click_listener = new View.OnClickListener() {
        public void onClick(View v) {
            pwindo.dismiss();

        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        switch(requestCode) {
            case SELECT_PHOTO:
                if(resultCode == RESULT_OK){
                    Uri selectedImage = imageReturnedIntent.getData();
                    Bitmap yourSelectedImage = null;
                    try {
                        yourSelectedImage = decodeUri(selectedImage);
                        System.out.println(yourSelectedImage);
                        Intent intent = new Intent(ViewAStreamActivity.this, UploadPictureActivity.class);
                        intent.putExtra(STREAMNAME,stream_name);
                        intent.putExtra(BITMAPIMAGE,yourSelectedImage);
                        intent.putExtra(USEREMAIL,stream_email);
                        System.out.println(stream_name);
                        System.out.println(stream_email);
                        startActivity(intent);
                        break;
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                }
                break;
            case REQUEST_IMAGE_CAPTURE:
                if (resultCode == RESULT_OK) {
                    Bundle extras = imageReturnedIntent.getExtras();
                    Bitmap imageBitmap = (Bitmap) extras.get("data");
//                    mImageView.setImageBitmap(imageBitmap);
                    Intent intent = new Intent(ViewAStreamActivity.this, UploadPictureActivity.class);
                    intent.putExtra("BitmapImage", imageBitmap);
                    startActivity(intent);
                }
        }
    }

    private Bitmap decodeUri(Uri selectedImage) throws FileNotFoundException {

        // Decode image size
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImage), null, o);

        // The new size we want to scale to
        final int REQUIRED_SIZE = 140;

        // Find the correct scale value. It should be the power of 2.
        int width_tmp = o.outWidth, height_tmp = o.outHeight;
        int scale = 1;
        while (true) {
            if (width_tmp / 2 < REQUIRED_SIZE
                    || height_tmp / 2 < REQUIRED_SIZE) {
                break;
            }
            width_tmp /= 2;
            height_tmp /= 2;
            scale *= 2;
        }

        // Decode with inSampleSize
        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        return BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImage), null, o2);

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
                Intent k1 = new Intent(ViewAStreamActivity.this, ViewStreamsActivity.class);
                startActivity(k1);
                break;
            case 2:
                Intent k2 = new Intent(ViewAStreamActivity.this, LoginActivity.class);
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
