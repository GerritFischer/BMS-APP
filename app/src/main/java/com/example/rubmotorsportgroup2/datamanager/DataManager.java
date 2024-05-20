package com.example.rubmotorsportgroup2.datamanager;

import java.util.ArrayList;

public class DataManager {
    private DataSet dataSet;

    public DataManager() {
        dataSet = new DataSet();
        DataRetriever dataRetriever = new DataRetriever(this);
        dataRetriever.start();
    }
    public void updateDataSet(String dataString){
        String decodedDataString = decodeDataString(dataString); //decode incoming data string
        dataSet = generateDataSet(decodedDataString); //generate new dataset
    }
    public String decodeDataString(String dataString){
        //TODO: decode string from base64 to hex, maybe no need for ext func
        return "decoded string";
    }
    public DataSet generateDataSet(String dataString){
        //TODO: transform data to valid dataset object
        //do it step by step, first generate segment data, then combine to dataset
        return new DataSet();
    }
    private SegmentData getSegmentData(int segID) {
        //TODO: check if id is valid, get corresponding segment from dataset and return
        return new SegmentData();
    }

    public ArrayList<Float> getSegmentVoltage(int segID){
        //TODO: return voltage list, use getSegmentData here
        return new ArrayList<Float>();
    }
    public ArrayList<Float> getSegmentTemperature(int segID){
        //TODO: return temperature list, use getSegmentData here
        return new ArrayList<Float>();
    }

    public boolean getError(int segID, int errorID){
        //TODO: return if there is an error in the segment
        return false;
    }




}
