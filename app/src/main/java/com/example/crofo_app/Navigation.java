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
    private ArrayList<String> coordinatesList = new ArrayList<String>();
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
                                coordinatesList.add(nodeListPointItem.item(j).getTextContent().trim());
                                Log.d("debug", nodeListPointItem.item(j).getTextContent().trim() );

                                String xy  = nodeListPointItem.item(j).getTextContent().trim();

                                String [] splitXY = xy.split(",");

                                TMapPoint point = new TMapPoint(Double.parseDouble(splitXY[1]), Double.parseDouble(splitXY[0]));
                                TMapMarkerItem Marker = new TMapMarkerItem();
                                Marker.setTMapPoint(point);
                                tMapView.addMarkerItem("asd" + point, Marker);
                            }
                        }
                    }

                    for(int i = 0; i < coordinatesList.size(); i++){
                        System.out.println("좌표 : " + coordinatesList.get(i));
                        System.out.println("TurnType 11 직진 12 좌회전 13 우회전 : " + turnTypeList.get(i));
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

}
