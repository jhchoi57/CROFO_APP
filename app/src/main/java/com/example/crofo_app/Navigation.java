package com.example.crofo_app;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.media.Image;
import android.os.AsyncTask;
import android.speech.tts.TextToSpeech;
import android.widget.Toast;

import com.skt.Tmap.TMapData;
import com.skt.Tmap.TMapMarkerItem;
import com.skt.Tmap.TMapPOIItem;
import com.skt.Tmap.TMapPoint;
import com.skt.Tmap.TMapPolyLine;
import com.skt.Tmap.TMapView;
import static android.speech.tts.TextToSpeech.ERROR;
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
