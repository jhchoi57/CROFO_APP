package com.example.crofo_app;

import com.skt.Tmap.TMapPoint;

public class CrossInfo {
    private Crosswalk frontCrosswalk;
    private Crosswalk rightCrosswalk;
    private TMapPoint crossLocation;

    public CrossInfo(Crosswalk fC, Crosswalk rC, TMapPoint cL){
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

    public TMapPoint getCrossLocation(){
        return crossLocation;
    }

    public void setFrontCrosswalk(Crosswalk fC){
        frontCrosswalk = fC;
    }

    public void setRightCrosswalk(Crosswalk rC){
        rightCrosswalk = rC;
    }

    public void setCrossLocation(TMapPoint cL){
        crossLocation = cL;
    }

}
