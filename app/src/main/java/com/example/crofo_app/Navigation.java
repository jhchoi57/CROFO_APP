package com.example.crofo_app;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PointF;
import android.os.AsyncTask;
import android.util.Log;

import com.skt.Tmap.TMapData;
import com.skt.Tmap.TMapMarkerItem;
import com.skt.Tmap.TMapPoint;
import com.skt.Tmap.TMapPolyLine;
import com.skt.Tmap.TMapView;
import static android.speech.tts.TextToSpeech.ERROR;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.LogManager;
import javax.xml.parsers.ParserConfigurationException;

public class Navigation extends AsyncTask<TMapPoint, Void, Double> {

    private TMapPoint startPoint;   // 출발지 좌표
    private TMapPoint endPoint;     // 목적지 좌표
    private TMapView tMapView;      // View
    private ArrayList<String> turnTypeList = new ArrayList<String>();
    private ArrayList<String> descriptionList = new ArrayList<String>();
    private ArrayList<double[]> coordinatesList = new ArrayList<double[]>();
    private ArrayList<String> pointIndexList = new ArrayList<String>();
    private TimerTask gpsCheckTimerTask;
    private ArrayList<CrossInfo> serverRequestCrossList = new ArrayList<CrossInfo>();
    private TMapMarkerItem markerItemCurrent = new TMapMarkerItem();
    private TMapPoint currentPoint;

    public Navigation(TMapPoint sPoint, TMapPoint ePoint, TMapView tView){
        super();
        this.startPoint = sPoint;
        this.endPoint = ePoint;
        this.tMapView = tView;
    }

    @Override
    protected void onPostExecute(Double distance) {
        super.onPreExecute();
    }

    @SuppressLint("WrongThread")
    @Override
    protected Double doInBackground(TMapPoint... tMapPoints){                       //실행부분
        TMapData tMapData = new TMapData();
        TMapPolyLine tMapPolyLine = null;
        try
        {
            TMapMarkerItem startMarker = new TMapMarkerItem();
            TMapMarkerItem endMarker = new TMapMarkerItem();


            tMapPolyLine = tMapData.findPathData(tMapPoints[0], tMapPoints[1]);     //길찾기
            tMapPolyLine.setLineColor(Color.BLUE);                                  //선 색
            tMapPolyLine.setLineWidth(2);                                           //선 굵기

            tMapView.addTMapPolyLine("Line123", tMapPolyLine);                  //맵에 추가

            // 마커의 좌표 지정
            startMarker.setTMapPoint( startPoint );
            endMarker.setTMapPoint( endPoint );

            // 마커의 타이틀 지정
            startMarker.setName("StartPoint");
            endMarker.setName("EndPoint");

            // 지도에 마커 추가
            tMapView.addMarkerItem("StartPoint", startMarker);
            tMapView.addMarkerItem("EndPoint", endMarker);

            // 화면 중심 시작 지점으로 설정
            tMapView.setCenterPoint(startPoint.getLongitude(), startPoint.getLatitude());

            // 화면 최대 확대
            tMapView.setZoomLevel(19);

            // 나침반 모드로 변경
            tMapView.setCompassMode(true);

            // 현재 위치 트래킹인데 과연 사용할지?
            //tMapView.setTrackingMode(true);

            tMapData.findPathDataAllType(TMapData.TMapPathType.CAR_PATH, startPoint, endPoint, new TMapData.FindPathDataAllListenerCallback() {
                @Override
                public void onFindPathDataAll(Document document) {
                    Element root = document.getDocumentElement();
                    NodeList nodeListPlacemark = root.getElementsByTagName("Placemark");
                    for( int i=0; i<nodeListPlacemark.getLength(); i++ ) {
                        NodeList nodeListPlacemarkItem = nodeListPlacemark.item(i).getChildNodes();
                        for( int j=0; j<nodeListPlacemarkItem.getLength(); j++ ) {
                            if( nodeListPlacemarkItem.item(j).getNodeName().equals("tmap:turnType") ) {
                                turnTypeList.add(nodeListPlacemarkItem.item(j).getTextContent().trim());
                                Log.d("debug", nodeListPlacemarkItem.item(j).getTextContent().trim() );
                            }
                            if( nodeListPlacemarkItem.item(j).getNodeName().equals("description") ) {
                                descriptionList.add(nodeListPlacemarkItem.item(j).getTextContent().trim());
                                Log.d("debug", nodeListPlacemarkItem.item(j).getTextContent().trim() );
                            }
                        }
                    }

                    NodeList nodeListPoint = root.getElementsByTagName("Point");
                    for( int i=0; i<nodeListPoint.getLength(); i++ ) {
                        NodeList nodeListPointItem = nodeListPoint.item(i).getChildNodes();
                        for( int j=0; j<nodeListPointItem.getLength(); j++ ) {
                            if( nodeListPointItem.item(j).getNodeName().equals("coordinates") ) {

                                Log.d("debug", nodeListPointItem.item(j).getTextContent().trim() );

                                String xy  = nodeListPointItem.item(j).getTextContent().trim();

                                String [] splitXY = xy.split(",");
                                TMapPoint point = new TMapPoint(Double.parseDouble(splitXY[1]), Double.parseDouble(splitXY[0]));
                                double[] pointDouble = {Double.parseDouble(splitXY[1]), Double.parseDouble(splitXY[0])};
                                coordinatesList.add(pointDouble);
                                TMapMarkerItem Marker = new TMapMarkerItem();
                                Marker.setTMapPoint(point);
                                tMapView.addMarkerItem("asd" + point, Marker);
                            }
                        }
                    }

                    for(int i = 0; i < coordinatesList.size(); i++){
                        double trueBearing = 0;
                        System.out.println("좌표 : " + coordinatesList.get(i)[0] + "    " + coordinatesList.get(i)[1]);
                        System.out.println("TurnType : " + turnTypeList.get(i));
                        if(i==0) continue;
                        trueBearing = getTrueBearing(coordinatesList.get(i), coordinatesList.get(i-1));
                        System.out.println("교차로 진입 방향 : " + "  " + trueBearing);

                        serverRequestCrossList.add(new CrossInfo(coordinatesList.get(i), trueBearing));
                    }

                }
            });

            new CrossRequest(serverRequestCrossList).execute("http://192.168.0.13:8080/app"); // 처음에 경로 찾고 교차로 목록 이렇게 보내면 됨.


            // 현재 위치 타이머로 0.5초마다 계속 얻기, 업데이트
            //tMapView.setTrackingMode(true);
            initTimerTask();
            Timer gpsCheckTimer = new Timer();
            gpsCheckTimer.schedule(gpsCheckTimerTask, 0, 500);

        }
        catch( Exception e )
        {
            e.printStackTrace();
        }

        return tMapPolyLine.getDistance();
    }

    public TMapPoint getStartPoint(){
        return this.startPoint;
    }

    public TMapPoint getEndPoint(){
        return this.endPoint;
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

    public double getdistance(double[] point1, double[] point2){
        double distance = 0;
        double theta = 0;
        double lat1 = point1[0];
        double lon1 = point1[1];
        double lat2 = point2[0];
        double lon2 = point2[1];

        theta = lon1 - lon2;
        distance = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));
        distance = Math.acos(distance);
        distance = rad2deg(distance);

        distance = distance * 60 * 1.1515;
        distance = distance * 1.609344;    // 단위 mile 에서 km 변환.
        distance = distance * 1000.0;      // 단위  km 에서 m 로 변환

        return distance;
    }

    // 주어진 도(degree) 값을 라디언으로 변환
    private double deg2rad(double deg){
        return (double)(deg * Math.PI / (double)180d);
    }

    // 주어진 라디언(radian) 값을 도(degree) 값으로 변환
    private double rad2deg(double rad){
        return (double)(rad * (double)180d / Math.PI);
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

    public void setCurrentMarkerIcon(Context context){
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.mycar);
        bitmap = bitmap.createScaledBitmap(bitmap,100,100,true);
        markerItemCurrent.setIcon(bitmap);
    }


}
