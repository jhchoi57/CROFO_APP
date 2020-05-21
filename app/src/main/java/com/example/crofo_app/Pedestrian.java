package com.example.crofo_app;

public class Pedestrian {
    private int[] pedestrianLocation;
    private int pedestrianDirection;

    public Pedestrian(int[] pL, int pD) {
        pedestrianLocation = pL;
        pedestrianDirection = pD;
    }

    public int[] getPedestrianLocation() {
        return pedestrianLocation;
    }

    public int getPedestrianDirection() {
        return pedestrianDirection;
    }

    public void setPedestrianLocation(int[] pL){
        pedestrianLocation = pL;
    }

    public void setPedestrianDirection(int pD){
        pedestrianDirection = pD;
    }

}
