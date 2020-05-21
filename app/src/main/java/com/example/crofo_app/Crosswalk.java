package com.example.crofo_app;

import com.skt.Tmap.TMapPoint;

import java.util.ArrayList;

public class Crosswalk {
    private ArrayList<Pedestrian> pedestrianList;
    private ArrayList<Car> carList;
    private TMapPoint crosswalkLocation;

    public Crosswalk(ArrayList<Pedestrian> pL, ArrayList<Car> cL, TMapPoint cwL){
        pedestrianList = pL;
        carList = cL;
        crosswalkLocation = cwL;
    }

    public ArrayList<Pedestrian> getPedestrianList(){
        return pedestrianList;
    }

    public ArrayList<Car> getCarList(){
        return carList;
    }

    public TMapPoint getCrosswalkLocation(){
        return crosswalkLocation;
    }

    public void setPedestrianList(ArrayList<Pedestrian> pL){
        pedestrianList = pL;
    }

    public void setCarList(ArrayList<Car> cL){
        carList = cL;
    }

    public void setCrosswalkLocation(TMapPoint cwL){
        crosswalkLocation = cwL;
    }

}
