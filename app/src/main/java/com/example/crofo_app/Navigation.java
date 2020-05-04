package com.example.crofo_app;

import android.graphics.Color;
import android.os.AsyncTask;

import com.skt.Tmap.TMapData;
import com.skt.Tmap.TMapPOIItem;
import com.skt.Tmap.TMapPoint;
import com.skt.Tmap.TMapPolyLine;
import com.skt.Tmap.TMapView;

import org.xml.sax.SAXException;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Hashtable;
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

    @Override
    protected Double doInBackground(TMapPoint... tMapPoints){                       //실행부분
        TMapData tMapData = new TMapData();
        TMapPolyLine tMapPolyLine = null;
        try
        {

            tMapPolyLine = tMapData.findPathData(tMapPoints[0], tMapPoints[1]);     //길찾기
            tMapPolyLine.setLineColor(Color.BLUE);                                  //선 색
            tMapPolyLine.setLineWidth(2);                                           //선 굵기

            tMapView.addTMapPolyLine("Line123", tMapPolyLine);                  //맵에 추가
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
