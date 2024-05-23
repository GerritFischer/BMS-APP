package com.example.rubmotorsportgroup2.datamanager;

import java.util.ArrayList;

public class SegmentData {
    public ArrayList<Float> voltage_combined = new ArrayList<Float>(); //22
    public ArrayList<Float> temperature_combined = new ArrayList<Float>(); //14

    public ArrayList<CIDData> cids = new ArrayList<>(); //2

    public void addVoltage_combined(float f){
        this.voltage_combined.add(f);
    }

    public void addTemperature_combined(float f){
        this.temperature_combined.add(f);
    }

    public void addCID(CIDData cid){
        cids.add(cid);
    }


    //TODO: add faults

}
