package com.example.crofo_app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.LinearLayout;

import com.skt.Tmap.TMapView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LinearLayout linearLayoutTmap = (LinearLayout)findViewById(R.id.linearLayoutTmap);
        TMapView tMapView = new TMapView(this);

        tMapView.setSKTMapApiKey( "l7xx57390f539bd74175a4783fe65224453e" );
        linearLayoutTmap.addView( tMapView );
    }
}
