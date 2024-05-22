package com.example.rubmotorsportgroup2.datamanager;

import android.util.Base64;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class DataManager {

    private final int CELL_COUNT = 22;
    private final int TEMP_COUNT = 7;
    private DataSet dataSet;
    private DataRetriever dataRetriever;

    public DataManager() {
        dataSet = new DataSet(null);
        dataRetriever = new DataRetriever(this);
        dataRetriever.start();

    }
    public void updateDataSet(String dataString){
        int[] decodedData= decodeDataString(dataString); //decode incoming data string
        dataSet = generateDataSet(decodedData); //generate new dataset

        ArrayList<Float> testing = getSegmentVoltage(0);
        for(int j = 0; j < 22; j++){
            System.out.print(testing.get(j) + " ");
        }
        System.out.println(" ");

    }
    public int[] decodeDataString(String dataString){

        byte[] byteData = Base64.decode(dataString, Base64.DEFAULT);

        int length = 0;
        for(int i = 0; i < 4; i++){
            length = (length << 8 ) + byteData[3-i];
        }


        int[] intArray = new int[length];
        intArray[0] = length;
        for(int i = 1; i < length; i++){
            for(int j = 0; j < 4; j++){
                intArray[i] = (intArray[i] << 8) + byteData[i*4+(3-j)] ;
            }
        }
        return intArray;
    }
    public DataSet generateDataSet(int[] data){
        System.out.println(data[0] - 1);
        int segmentCount = (data[0] - 1) / 50; //50 fields per segment, 4 bytes per field, -4 because length is included
        ArrayList<SegmentData> segments = new ArrayList<SegmentData>();

        System.out.println("SEGMENT COUNT: " + segmentCount);
        /*
        System.out.println("DATA SET 50: ");
        for(int i = 1; i < 51; i++){
            System.out.print(Float.intBitsToFloat(data[i]) + " ");
        }


         */

        for(int i = 0; i < segmentCount; i++){

            SegmentData segment = new SegmentData();
            int offset = 0;

            for(int j = 0; j < 11; j++){
                int d  = data[1+50*i+j+offset];
                if (d != 0){
                    segment.addVoltage(intToFloatByte(d));
                }

            }
            offset += 11;
            for(int j = 0; j < 7; j++){
                int d  = data[1+50*i+j+offset];
                if (d != 0){
                    segment.addTemperature(intToFloatByte(d));
                }
            }
            offset += 14;
            for(int j = 0; j < 11; j++){
                int d  = data[1+50*i+j+offset];
                if (d != 0){
                    segment.addVoltage(intToFloatByte(d));
                }
            }
            offset += 11;
            for(int j = 0; j < 7; j++){
                int d  = data[1+50*i+j+offset];
                if (d != 0){
                    segment.addTemperature(intToFloatByte(d));
                }
            }
            segments.add(segment);
        }

        System.out.println("added segments: " + segments.size());
        return new DataSet(segments);
    }

    public float intToFloatByte(int d){
        return Float.intBitsToFloat(d);
    }
    public int[] intToDoubleInt16(){
        return null;
    }
    public SegmentData getSegmentData(int segID) {
        return dataSet.segmentDataArrayList.get(0);
        //TODO: check if id is valid, get corresponding segment from dataset and return
    }

    public ArrayList<Float> getSegmentVoltage(int segID){
        //TODO: return voltage list, use getSegmentData here

        return dataSet.segmentDataArrayList.get(segID).voltage;
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
