package com.example.crofo_app;

public class Car {
    private int[] carLocation;

    public Car(int[] cL){
        carLocation = cL;
    }

    public int[] getCarLocation(){
        return carLocation;
    }

    public void setCarLocation(int[] cL){
        carLocation = cL;
    }

}
