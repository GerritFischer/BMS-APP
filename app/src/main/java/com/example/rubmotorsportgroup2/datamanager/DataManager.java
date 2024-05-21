package com.example.rubmotorsportgroup2.datamanager;

import android.util.Base64;

import java.lang.reflect.Array;
import java.util.ArrayList;

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
        System.out.println("Still alive");
        for(int j = 0; j < 22; j++){
            System.out.print(testing.get(j) + " ");
        }
        System.out.println(" ");

    }
    public int[] decodeDataString(String dataString){

        byte[] byteData = Base64.decode(dataString, Base64.DEFAULT);

        int length = 0;
        for(int i = 0; i < 4; i++){
            length = (length + byteData[3-i]) << 8*(3-i);
        }

        int[] intArray = new int[length];
        intArray[0] = length;
        for(int i = 4; i < length * 4; i+=4){
            for(int j = 0; j < 4; j++){
                intArray[i/4] = (intArray[i/4] + byteData[i+(3-j)]) << 8*(3-j);
            }
        }

        return intArray;
    }
    public DataSet generateDataSet(int[] data){
        int segmentCount = (data[0] - 1) / 50; //50 fields per segment, 4 bytes per field, -4 because length is included
        ArrayList<SegmentData> segments = new ArrayList<SegmentData>();


        for(int i = 0; i < segmentCount; i++){

            SegmentData segment = new SegmentData();
            int offset = 0;

            for(int j = 0; j < 11; j++){

                segment.addVoltage(intToFloatByte(data[1+50*i+j+offset]));
            }


            offset += 11;
            for(int j = 0; j < 7; j++){
                segment.addTemperature(intToFloatByte(data[1+50*i+j+offset]));
            }
            offset += 7+6;
            for(int j = 0; j < 11; j++){
                segment.addVoltage(intToFloatByte(data[1+50*i+j+offset]));
            }
            offset += 11;
            for(int j = 0; j < 7; j++){
                segment.addTemperature(intToFloatByte(data[1+50*i+j+offset]));
            }
            segments.add(segment);
            //System.out.println(1+50*i+j+offset);

            System.out.println(i);
        }

        return new DataSet(segments);
    }

    public float intToFloatByte(int d){
        byte[] byteArray = new byte[]{
                (byte)(d >> 24),
                (byte)(d >> 16),
                (byte)(d >> 8),
                (byte)d
        };
        int n = 0;
        float foo = Float.intBitsToFloat( byteArray[n] ^ byteArray[n+1]<<8 ^ byteArray[n+2]<<16 ^ byteArray[n+3]<<24 );
        return foo;

    }
    public int[] intToDoubleInt16(){
        return null;
    }
    private SegmentData getSegmentData(int segID) {
        //TODO: check if id is valid, get corresponding segment from dataset and return
        return new SegmentData();
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
