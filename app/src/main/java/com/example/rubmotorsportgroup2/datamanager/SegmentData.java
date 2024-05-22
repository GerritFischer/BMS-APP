package com.example.rubmotorsportgroup2.datamanager;

import java.util.ArrayList;

public class SegmentData {
    public ArrayList<Float> voltage = new ArrayList<Float>(); //22
    public ArrayList<Float> temperature = new ArrayList<Float>(); //14

    public void addVoltage(float f){
        this.voltage.add(f);

    }
    public void addTemperature(float f){

        this.temperature.add(f);
    }
    //TODO: add faults

}
