package edu.utaustin.yusun.connexusandroid;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import cz.msebera.android.httpclient.Header;
import java.util.ArrayList;
/**
 * Created by yusun on 15/10/18.
 */
public class ViewAStreamActivity extends AppCompatActivity implements View.OnClickListener {
    Context context = this;
    private String TAG  = "Display Pictures";
    Button btnClosePopup;
    Button btnCreatePopup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_astream);

        Intent intent = getIntent();
        String sream_name = intent.getStringExtra(ViewStreamsActivity.NAME);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        findViewById(R.id.upload_button).setOnClickListener(this);
        findViewById(R.id.view_streams_button).setOnClickListener(this);

        TextView streamName = (TextView) findViewById(R.id.textView2);
        streamName.setText(sream_name);

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
            pwindo = new PopupWindow(layout, 300, 370, true);
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
            pwindo.dismiss();

        }
    };
    private View.OnClickListener camera_button_click_listener = new View.OnClickListener() {
        public void onClick(View v) {
            pwindo.dismiss();

        }
    };
    private View.OnClickListener cancel_button_click_listener = new View.OnClickListener() {
        public void onClick(View v) {
            pwindo.dismiss();

        }
    };



}
