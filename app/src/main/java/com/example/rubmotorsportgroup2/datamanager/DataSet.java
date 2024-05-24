package com.example.rubmotorsportgroup2.datamanager;

import java.util.ArrayList;

public class DataSet {
    public ArrayList<SegmentData> segmentDataArrayList = new ArrayList<SegmentData>();
    //12
    public DataSet(ArrayList<SegmentData> segments){
        segmentDataArrayList = segments;
    }
    //TODO: add IsError or smth to quickly check if error occurred
    //TODO: maybe add segment count because it can vary
    //TODO: implement getter and setter
    //not sure yet if we implement getter/setter in segmentData like dm.getSegementData(1).getVoltage() or smth
    //or if we do it like dm.getSegmentVoltage(1)
}
