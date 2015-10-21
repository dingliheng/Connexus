package edu.utaustin.yusun.connexusandroid;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

public class ViewStreamsActivity extends AppCompatActivity implements
        View.OnClickListener {
    static final String[] numbers = new String[] {
            "A", "B", "C", "D", "E",
            "F", "G", "H", "I", "J",
            "K", "L", "M", "N", "O",
            "P", "Q", "R", "S", "T",
            "U", "V", "W", "X", "Y", "Z"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_streams);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ExpandableHeightGridView gridView = (ExpandableHeightGridView) findViewById(R.id.gridView);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, numbers);

        gridView.setAdapter(adapter);
        gridView.setExpanded(true);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                Intent k = new Intent(ViewStreamsActivity.this, ViewAStreamActivity.class);
                startActivity(k);
            }
        });
        // Set up button click listeners
        findViewById(R.id.search).setOnClickListener(this);
        findViewById(R.id.nearby).setOnClickListener(this);
        findViewById(R.id.view_subscribed_streams).setOnClickListener(this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    // [START on_click]
    @Override
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
        
    }

    //When click on the search button
    private void onSearchClicked() {

    }

    //When click on the view subscibed stream button
    private  void onViewSubscribedClicked() {

    }

}
