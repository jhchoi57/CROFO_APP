package com.example.crofo_app;

import com.skt.Tmap.TMapPoint;

public class CrossInfo {
    private Crosswalk frontCrosswalk;       // 전방 횡단보도
    private Crosswalk rightCrosswalk;       // 우측 횡단보도
    private double[] crossLocation;         // 교차로 위치 lat lon 순서

    public CrossInfo(Crosswalk fC, Crosswalk rC, double[] cL){
        frontCrosswalk = fC;
        rightCrosswalk = rC;
        crossLocation = cL;
    }

    public Crosswalk getFrontCrosswalk(){
        return frontCrosswalk;
    }

    public Crosswalk getRightCrosswalk(){
        return rightCrosswalk;
    }

    public double[] getCrossLocation(){
        return crossLocation;
    }

    public void setFrontCrosswalk(Crosswalk fC){
        frontCrosswalk = fC;
    }

    public void setRightCrosswalk(Crosswalk rC){
        rightCrosswalk = rC;
    }

    public void setCrossLocation(double[] cL){
        crossLocation = cL;
    }

}
