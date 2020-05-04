package com.example.crofo_app;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;

import com.skt.Tmap.TMapData;
import com.skt.Tmap.TMapPoint;
import com.skt.Tmap.TMapPolyLine;
import com.skt.Tmap.TMapTapi;
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

        System.out.println("안뇽\n");

        TMapPoint startPoint = new TMapPoint(37.570841, 126.985302);            //SKT타워
        TMapPoint endPoint = new TMapPoint(37.551135, 126.988205);              //N서울타워
        Navigation naviTest = new Navigation(startPoint, endPoint, tMapView);
        naviTest.execute(startPoint, endPoint);

        tMapView.setCenterPoint(126.988205, 37.551135);

    }
}
