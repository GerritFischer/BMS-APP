package com.example.rubmotorsportgroup2.datamanager;

public class DataManager {
    private DataSet dataSet;
    private DataRetriever dataRetriever;

    public DataManager() {
        dataSet = new DataSet();
        dataRetriever = new DataRetriever(this);
        dataRetriever.start();
    }
    public void updateDataSet(String dataString){
        String decodedDataString = decodeDataString(dataString);
        dataSet = generateDataSet(decodedDataString); //generate new dataset
        System.out.println(dataString);
    }
    public String decodeDataString(String dataString){
        //TODO: decode string from base64 to hex, maybe no need for ext func
        return "decoded";
    }
    public DataSet generateDataSet(String dataString){
        //TODO: transform data to valid dataset object
        //do it step by step, first generate segment data, then combine to dataset
        return new DataSet();
    }
    public SegmentData getSegmentData(int segID) {
        //TODO: check if id is valid, get corresponding segment from dataset and return
        return new SegmentData();
    }



}
