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

import java.util.ArrayList;
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

        // 앱에서 현재위치 기준으로 반경 원 내에 들어오는 교차로 리스트 서버에 요청 및 수신

        // ROI 안에 들어왔는지

            // 2개 이상이면 현재 차량 방향 기준으로 필터링

            // 교차로 정보 요청




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

    public double getTrueBearing(double[] point1, double[] point2){
        // point[0] = lat , point[1] = lon

        double[] point1_radian = new double[2];
        double[] point2_radian = new double[2];

        // 현재 위치 : 위도나 경도는 지구 중심을 기반으로 하는 각도이기 때문에 라디안 각도로 변환한다.
        point1_radian[0] = point1[0] * (3.141592 / 180);        //lat
        point1_radian[1] = point1[1] * (3.141592 / 180);        //lon

        // 목표 위치 : 위도나 경도는 지구 중심을 기반으로 하는 각도이기 때문에 라디안 각도로 변환한다.
        point2_radian[0] = point2[0] * (3.141592 / 180);        //lat
        point2_radian[1] = point2[1] * (3.141592 / 180);        //lon

        // radian distance

        double radian_distance = 0;

        radian_distance = Math.acos(Math.sin(point1_radian[0]) * Math.sin(point2_radian[0]) + Math.cos(point1_radian[0]) * Math.cos(point2_radian[0]) * Math.cos(point1_radian[1] - point2_radian[1]));



        // 목적지 이동 방향을 구한다.(현재 좌표에서 다음 좌표로 이동하기 위해서는 방향을 설정해야 한다. 라디안값이다.

        double radian_bearing = Math.acos((Math.sin(point2_radian[0]) - Math.sin(point1_radian[0]) * Math.cos(radian_distance)) / (Math.cos(point1_radian[0]) * Math.sin(radian_distance)));
        // acos의 인수로 주어지는 x는 360분법의 각도가 아닌 radian(호도)값이다.

        double true_bearing = 0;

        if (Math.sin(point2_radian[1] - point1_radian[1]) < 0)

        {

            true_bearing = radian_bearing * (180 / 3.141592);

            true_bearing = 360 - true_bearing;

        }

        else

        {

            true_bearing = radian_bearing * (180 / 3.141592);

        }

        return true_bearing;

    }

    // ROI 안에 들어왔는지
    public boolean isInPolygon(ArrayList<double[]> roi, double[] point){
        //crosses는 점q와 오른쪽 반직선과 다각형과의 교점의 개수
        int crosses = 0;

        for(int i = 0 ; i < 4 ; i++){
            int j = (i+1)%4;
            //점 B가 선분 (p[i], p[j])의 y좌표 사이에 있음
            if((roi.get(i)[1] > point[1]) != (roi.get(j)[1] > point[1]) ){
                //atX는 점 B를 지나는 수평선과 선분 (p[i], p[j])의 교점
                double atX = (roi.get(j)[0] - roi.get(i)[0]) * (point[1] - roi.get(i)[1]) / (roi.get(j)[1] - roi.get(i)[1]) + roi.get(i)[0];
                //atX가 오른쪽 반직선과의 교점이 맞으면 교점의 개수를 증가시킨다.
                if(point[0] < atX)
                    crosses++;
            }
        }
        return crosses % 2 > 0;
    }

    // 2개 이상이면 현재 차량 기준으로 필터링
    public double[] ifHaveManyROI(double trueBearing, ArrayList<double[]> roi, double[] curLocation){
        int index = 0;
        double min = 360;
        for(int i = 0;i<roi.size();i++){
            double diff = Math.abs(getTrueBearing(curLocation, roi.get(i)) - trueBearing);
            if(min > diff){
                index = i;
                min = diff;
            }
        }
        return roi.get(index);
    }
}
