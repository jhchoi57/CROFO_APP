package com.example.crofo_app;

import com.skt.Tmap.TMapPoint;

public class CrossInfo {
    private int crossID;                    // 교차로 ID
    private Crosswalk frontCrosswalk;       // 저 앞 횡단보도
    private Crosswalk rightCrosswalk;       // 오른쪽 횡단보도
    private Crosswalk leftCrosswalk;        // 왼쪽 횡단보도
    private Crosswalk backCrosswalk;        // 앞 횡단보도
    private double[] centerLocation;         // 교차로 위치 lat lon 순서
    private double[] crossLocation0;         // 교차로 위치 lat lon 순서
    private double[] crossLocation1;         // 1 0
    private double[] crossLocation2;         // 2 3
    private double[] crossLocation3;         //
    private double trueBearing;             // 방향 북쪽 0도, 360도 기준

    public CrossInfo(){
        frontCrosswalk = null;
        rightCrosswalk = null;
        leftCrosswalk = null;
        backCrosswalk = null;
        centerLocation = null;
        crossLocation0 = null;
        crossLocation1 = null;
        crossLocation2 = null;
        crossLocation3 = null;
        trueBearing = -1;
    }

    public CrossInfo(Crosswalk fC, Crosswalk rC, Crosswalk lC, Crosswalk bC, double[] cenL, double[] cL0, double[] cL1, double[] cL2, double[] cL3, double tB){
        frontCrosswalk = fC;
        rightCrosswalk = rC;
        leftCrosswalk = lC;
        backCrosswalk = bC;
        centerLocation = cenL;
        crossLocation0 = cL0;
        crossLocation1 = cL1;
        crossLocation2 = cL2;
        crossLocation3 = cL3;
        trueBearing = tB;
    }

    public CrossInfo(int cid, double[] cenL, double[] cL0, double[] cL1, double[] cL2, double[] cL3){
        crossID = cid;
        centerLocation = cenL;
        crossLocation0 = cL0;
        crossLocation1 = cL1;
        crossLocation2 = cL2;
        crossLocation3 = cL3;
    }

    public int getCrossID() {
        return crossID;
    }

    public Crosswalk getFrontCrosswalk() {
        return frontCrosswalk;
    }

    public Crosswalk getRightCrosswalk() {
        return rightCrosswalk;
    }

    public Crosswalk getLeftCrosswalk() {
        return leftCrosswalk;
    }

    public Crosswalk getBackCrosswalk() {
        return backCrosswalk;
    }

    public double[] getCenterLocation() {
        return centerLocation;
    }

    public double[] getCrossLocation0() {
        return crossLocation0;
    }

    public double[] getCrossLocation1() {
        return crossLocation1;
    }

    public double[] getCrossLocation2() {
        return crossLocation2;
    }

    public double[] getCrossLocation3() {
        return crossLocation3;
    }

    public double getTrueBearing() {
        return trueBearing;
    }

    public void setCrossID(int cid) {
        this.crossID = cid;
    }

    public void setFrontCrosswalk(Crosswalk frontCrosswalk) {
        this.frontCrosswalk = frontCrosswalk;
    }

    public void setRightCrosswalk(Crosswalk rightCrosswalk) {
        this.rightCrosswalk = rightCrosswalk;
    }

    public void setLeftCrosswalk(Crosswalk leftCrosswalk) {
        this.leftCrosswalk = leftCrosswalk;
    }

    public void setBackCrosswalk(Crosswalk backCrosswalk) {
        this.backCrosswalk = backCrosswalk;
    }

    public void setCenterLocation(double[] centerLocation) {
        this.centerLocation = centerLocation;
    }

    public void setCrossLocation0(double[] crossLocation0) {
        this.crossLocation0 = crossLocation0;
    }

    public void setCrossLocation1(double[] crossLocation1) {
        this.crossLocation1 = crossLocation1;
    }

    public void setCrossLocation2(double[] crossLocation2) {
        this.crossLocation2 = crossLocation2;
    }

    public void setCrossLocation3(double[] crossLocation3) {
        this.crossLocation3 = crossLocation3;
    }

    public void setTrueBearing(double trueBearing) {
        this.trueBearing = trueBearing;
    }

}
