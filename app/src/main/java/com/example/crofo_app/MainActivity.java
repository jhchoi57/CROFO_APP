package com.example.crofo_app;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PointF;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.content.Context;

import com.skt.Tmap.TMapData;
import com.skt.Tmap.TMapMarkerItem;
import com.skt.Tmap.TMapPoint;
import com.skt.Tmap.TMapPolyLine;
import com.skt.Tmap.TMapTapi;
import com.skt.Tmap.TMapView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    TMapView tMapView;
    TMapMarkerItem markerItem1 = new TMapMarkerItem();
    TMapPoint tMapMarkerPoint = new TMapPoint(37.570841, 126.985302); // 임의로 찍어둠 : SKT타워
    TMapPoint startPoint;
    TMapPoint endPoint;
    Button btnStarting;
    Button btnDestination;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        LinearLayout linearLayoutTmap = (LinearLayout)findViewById(R.id.linearLayoutTmap);
        tMapView = new TMapView(this);

        tMapView.setSKTMapApiKey( "l7xx57390f539bd74175a4783fe65224453e" );
        linearLayoutTmap.addView( tMapView );

        System.out.println("안뇽\n");
        startPoint = new TMapPoint(37.570841, 126.985302);            //SKT타워
        endPoint = new TMapPoint(37.551135, 126.988205);              //N서울타워
        Navigation naviTest = new Navigation(startPoint, endPoint, tMapView);
        naviTest.execute(startPoint, endPoint);

        tMapView.setCenterPoint(126.988205, 37.551135);

        // 연홍's코드 수정ing
        Context context = this;
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.poi_dot);

        markerItem1.setIcon(bitmap); // 마커 아이콘 지정
        markerItem1.setPosition(0.5f, 1.0f); // 마커의 중심점을 중앙, 하단으로 설정
        markerItem1.setTMapPoint( tMapMarkerPoint ); // 마커의 좌표 지정
        markerItem1.setName("SKT타워"); // 마커의 타이틀 지정
        tMapView.addMarkerItem("markerItem1", markerItem1); // 지도에 마커 추가

        btnStarting = (Button)findViewById(R.id.btnStart);
        btnDestination = (Button)findViewById(R.id.btnEnd);
        btnStarting.setOnClickListener(mClickListener);
        btnDestination.setOnClickListener(mClickListener);

        // tMapview 클릭 이벤트
        tMapView.setOnClickListenerCallBack(new TMapView.OnClickListenerCallback(){
            @Override
            public boolean onPressEvent(ArrayList arrayList, ArrayList arrayList1, TMapPoint tMapPoint, PointF pointF) {
                return false;
            }

            @Override
            public boolean onPressUpEvent(ArrayList arrayList, ArrayList arrayList1, TMapPoint tMapPoint, PointF pointF) {
                return false;
            }
        });

        // tMapview 롱 클릭 이벤트
        tMapView.setOnLongClickListenerCallback(new TMapView.OnLongClickListenerCallback() {
            @Override
            public void onLongPressEvent(ArrayList arrayList, ArrayList arrayList1, TMapPoint tMapPoint) {
                tMapMarkerPoint = tMapPoint;
                markerItem1.setTMapPoint( tMapMarkerPoint ); // 마커의 좌표 지정
                markerItem1.setName(""); // 마커의 타이틀 지정
                tMapView.addMarkerItem("markerItem1", markerItem1); // 지도에 마커 추가
                btnStarting.setVisibility(View.VISIBLE);
                btnDestination.setVisibility(View.VISIBLE);
            }
        });
    }

    // btnStart, btnEnd 클릭 이벤트
    Button.OnClickListener mClickListener = new View.OnClickListener(){
        public void onClick(View v){
            switch(v.getId()){
                case R.id.btnStart :
                    startPoint = tMapMarkerPoint;
                    Toast.makeText(getApplicationContext(), "위도 : " + startPoint.getLatitude() + "\n경도 : " + startPoint.getLongitude(), Toast.LENGTH_LONG).show();
                    break;
                case R.id.btnEnd :
                    endPoint = tMapMarkerPoint;
                    Toast.makeText(getApplicationContext(), "위도 : " + endPoint.getLatitude() + "\n경도 : " + endPoint.getLongitude(), Toast.LENGTH_LONG).show();
                    break;
            }
        }
    };
}