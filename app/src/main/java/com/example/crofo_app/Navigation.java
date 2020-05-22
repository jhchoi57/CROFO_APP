package com.example.crofo_app;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.media.Image;
import android.os.AsyncTask;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.widget.Toast;

import com.skt.Tmap.TMapData;
import com.skt.Tmap.TMapMarkerItem;
import com.skt.Tmap.TMapPOIItem;
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
import java.util.logging.LogManager;

import javax.xml.parsers.ParserConfigurationException;

public class Navigation extends AsyncTask<TMapPoint, Void, Double> {

    private TMapPoint startPoint;   // 출발지 좌표
    private TMapPoint endPoint;     // 목적지 좌표
    private TMapView tMapView;      // View
    private ArrayList<String> turnTypeList = new ArrayList<String>();
    private ArrayList<String> descriptionList = new ArrayList<String>();
    private ArrayList<Double[]> coordinatesList = new ArrayList<Double[]>();
    private ArrayList<String> pointIndexList = new ArrayList<String>();

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
                                Double[] pointDouble = {Double.parseDouble(splitXY[1]), Double.parseDouble(splitXY[0])};
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
                    }

                }
            });



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

    public double getTrueBearing(Double[] point1, Double[] point2){
        // point[0] = lat , point[1] = lon

        Double[] point1_radian = new Double[2];
        Double[] point2_radian = new Double[2];

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

}
