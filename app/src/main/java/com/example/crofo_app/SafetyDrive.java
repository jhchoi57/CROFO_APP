package com.example.crofo_app;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import com.skt.Tmap.TMapMarkerItem;
import com.skt.Tmap.TMapPoint;
import com.skt.Tmap.TMapView;

import java.util.Timer;
import java.util.TimerTask;

public class SafetyDrive extends AsyncTask<TMapPoint, Void, Void> {
    private TimerTask gpsCheckTimerTask;
    private TMapPoint currentPoint;
    private TMapView tMapView;      // View
    private TMapMarkerItem markerItemCurrent = new TMapMarkerItem();

    Context context;

    public SafetyDrive(TMapView tView, Context ct){
        super();
        tMapView = tView;
        context = ct;
    }

    protected void onPostExecute() {
        super.onPreExecute();
    }

    @SuppressLint("WrongThread")
    @Override
    protected Void doInBackground(TMapPoint... tMapPoints){                       //실행부분
        tMapView.addMarkerItem("current",markerItemCurrent);
        setCurrentMarkerIcon(context);

        // 현재 위치 타이머로 0.5초마다 계속 얻기, 업데이트
        //tMapView.setTrackingMode(true);
        initTimerTask();
        Timer gpsCheckTimer = new Timer();
        gpsCheckTimer.schedule(gpsCheckTimerTask, 0, 500);


        return null;
    }

    public void initTimerTask(){
        gpsCheckTimerTask = new TimerTask(){
            @Override
            public void run(){
                // 타이머로 할 일
                // 현재 위치 가져오기
                Log.d("현재위치", String.valueOf(tMapView.getLocationPoint()));
                currentPoint = tMapView.getLocationPoint();
                tMapView.setCenterPoint(currentPoint.getLongitude(), currentPoint.getLatitude());
                markerItemCurrent.setTMapPoint(currentPoint);
            }
        };
    }

    public void setCurrentMarkerIcon(Context context){
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.mycar);
        bitmap = bitmap.createScaledBitmap(bitmap,100,100,true);
        markerItemCurrent.setIcon(bitmap);
    }

}
