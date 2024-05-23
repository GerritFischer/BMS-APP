package com.example.rubmotorsportgroup2.datamanager;

import android.util.Base64;

import java.lang.reflect.Array;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;


public class DataManager {

    private final int CELL_COUNT = 11;
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
                 //System.out.format("0x%x ", byteData[i*4+3]);
            intArray[i] = (byteData[i*4+3]);
            intArray[i] = (intArray[i]  << 8)+  ((int) byteData[i*4+2] & 0xFF) ;
            intArray[i] = (intArray[i]  << 8)+  ((int) byteData[i*4+1] & 0xFF) ;
            intArray[i] = (intArray[i] << 8) +  ((int) byteData[i*4] & 0xFF) ;
        }


        return intArray;
    }

    String[] fault_names1 = {"CT_UV_FLT", "CT_OV_FLT", "AN_UT_FLT", "AN_OT_FLT",
            null, null, null, null,
            null, "COM_ERR_FLT", "COM_LOSS_FLT", "VPWR_LV_FLT",
            "VPWR_OV_FLT", "COM_ERR_OVR_FLT", "RESET_FLT", "POR"
    };

    String[] fault_names2 = {"FUSE_ERR_FLT", "DED_ERR_FLT", "OSC_ERR_FLT", "CB_OPEN_FLT",
            "CB_SHORT_FLT", null, null, null,
            "IC_TSD_FLT", "GND_LOSS_FLT", "ADC1_A_FLT", "ADC1_B_FLT",
            "VANA_UV_FLT", "VANA_OV_FLT", "VCOM_UV_FLT", "VCOM_OV_FLT"
    };

    public DataSet generateDataSet(int[] data){
        System.out.println(data[0] - 1);
        int segmentCount = (data[0] - 1) / 50; //50 fields per segment, 4 bytes per field, -4 because length is included
        ArrayList<SegmentData> segments = new ArrayList<SegmentData>();
        int offset = 1;
        //for each segment, create segment data
        for(int i = 0; i < segmentCount; i++){

            SegmentData segment = new SegmentData();
            CIDData cid1 = new CIDData();

            //add voltage of cid1
            for(int j = 0; j < CELL_COUNT; j++){
                float value = intToFloatByte(data[offset]);
                segment.addVoltage_combined(value);
                cid1.addVoltage(value);
                offset++;

            }

            //add temp of cid1
            for(int j = 0; j < TEMP_COUNT; j++){
                float value = intToFloatByte(data[offset]);
                segment.addTemperature_combined(value);
                cid1.addTemperature(value);
                offset++;
            }

            //set stack voltage of cid1
            cid1.setStack_voltage(intToFloatByte(data[offset]));
            offset++;
            //set ic temp of cid1
            cid1.setIC_temp(intToFloatByte(data[offset]));
            offset++;

            int register_fault1_fault2 = data[offset];

            for(int j = 0; j < 16; j++){
                if(fault_names1[j] != null && (((register_fault1_fault2 >> 16) >> j) & 1) == 1){
                    cid1.addFault(fault_names1[j]);
                }
            }

            for(int j = 0; j < 16; j++){
                if(fault_names2[j] != null && (((register_fault1_fault2) >> j) & 1) == 1){
                    cid1.addFault(fault_names2[j]);
                }
            }
            offset++;

            int register_fault3_com = data[offset];
            //eot_cid1
            for(int j = 0; j < 14; j++){
                if(!(j >= 4 && j <=6)){ //skip the disconnected terminals
                    cid1.addEot_cb( (((register_fault3_com >> 16) >> j) & 1) == 1);
                }
            }
            short error_count = (short)(register_fault3_com & 1);
            for(int j = 0; j < 7; j++){
                error_count = (short) ((error_count << 1) + (short)((register_fault3_com >> 1) >> j) & 1);
            }
            cid1.setCOM_ERR_COUNT(error_count);

            offset++;
            int register_anotut_celluv = data[offset];
            for(int j = 0; j < 7; j++){
                cid1.addAN_ut((((register_anotut_celluv >> 1) >> j) & 1) == 1);
            }
            for(int j = 0; j < 7; j++){
                cid1.addAN_ot((((register_anotut_celluv >> 9) >> j) & 1) == 1);
            }

            for(int j = 0; j < 14; j++){
                if(!(j >= 4 && j <=6)){ //skip the disconnected terminals
                    cid1.addCT_uv( (((register_anotut_celluv >> 16) >> j) & 1) == 1);
                }
            }
            offset++;
            int register_cellov_open = data[offset];
            for(int j = 0; j < 14; j++){
                if(!(j >= 4 && j <=6)){ //skip the disconnected terminals
                    cid1.addCT_ov( (((register_cellov_open >> 16) >> j) & 1) == 1);
                }
            }
            for(int j = 0; j < 14; j++){
                if(!(j >= 4 && j <=6)){ //skip the disconnected terminals
                    cid1.addCT_open( (((register_cellov_open) >> j) & 1) == 1);
                }
            }
            offset++;
            int register_short = data[offset];
            for(int j = 0; j < 14; j++){
                if(!(j >= 4 && j <=6)){ //skip the disconnected terminals
                    cid1.addCT_short( (((register_short >> 16) >> j) & 1) == 1);
                }
            }
            offset++;
            segment.addCID(cid1);

            CIDData cid2 = new CIDData();

            //add voltage of cid2
            for(int j = 0; j < CELL_COUNT; j++){
                float value = intToFloatByte(data[offset]);
                segment.addVoltage_combined(value);
                cid2.addVoltage(value);
                offset++;
            }
            //add temp of cid2
            for(int j = 0; j < TEMP_COUNT; j++){
                float value = intToFloatByte(data[offset]);
                segment.addTemperature_combined(value);
                cid2.addTemperature(value);
                offset++;
            }
            //set stack voltage of cid2
            cid2.setStack_voltage(intToFloatByte(data[offset]));
            offset ++;
            //set ic temp of cid2
            cid2.setIC_temp(intToFloatByte(data[offset]));
            offset++;

            register_fault1_fault2 = data[offset];


            for(int j = 0; j < 16; j++){
                if(fault_names1[j] != null && (((register_fault1_fault2 >> 16) >> j) & 1) == 1){
                    cid2.addFault(fault_names1[j]);
                }
            }

            for(int j = 0; j < 16; j++){
                if(fault_names2[j] != null && (((register_fault1_fault2) >> j) & 1) == 1){
                    cid2.addFault(fault_names2[j]);
                }
            }
            offset++;

            register_fault3_com = data[offset];
            //eot_cid1
            for(int j = 0; j < 14; j++){
                if(!(j >= 4 && j <=6)){ //skip the disconnected terminals
                    cid2.addEot_cb( (((register_fault3_com >> 16) >> j) & 1) == 1);
                }
            }
            error_count = (short)(register_fault3_com & 1);
            for(int j = 0; j < 7; j++){
                error_count = (short) ((error_count << 1) + (short)((register_fault3_com >> 1) & 1) >> j);
            }
            cid2.setCOM_ERR_COUNT(error_count);

            offset++;
            register_anotut_celluv = data[offset];
            for(int j = 0; j < 7; j++){
                cid2.addAN_ut((((register_anotut_celluv >> 1) >> j) & 1) == 1);
            }
            for(int j = 0; j < 7; j++){
                cid2.addAN_ot((((register_anotut_celluv >> 9) >> j) & 1) == 1);
            }

            for(int j = 0; j < 14; j++){
                if(!(j >= 4 && j <=6)){ //skip the disconnected terminals
                    cid2.addCT_uv( (((register_anotut_celluv >> 16) >> j) & 1) == 1);
                }
            }
            offset++;
            register_cellov_open = data[offset];
            for(int j = 0; j < 14; j++){
                if(!(j >= 4 && j <=6)){ //skip the disconnected terminals
                    cid2.addCT_ov( (((register_cellov_open >> 16) >> j) & 1) == 1);
                }
            }
            for(int j = 0; j < 14; j++){
                if(!(j >= 4 && j <=6)){ //skip the disconnected terminals
                    cid2.addCT_open( (((register_cellov_open) >> j) & 1) == 1);
                }
            }
            offset++;
            register_short = data[offset];
            for(int j = 0; j < 14; j++){
                if(!(j >= 4 && j <=6)){ //skip the disconnected terminals
                    cid2.addCT_short( (((register_short >> 16) >> j) & 1) == 1);
                }
            }
            offset++;
            segment.addCID(cid2);
            segments.add(segment);
        }

        return new DataSet(segments);
    }

    public float intToFloatByte(int d){
        return Float.intBitsToFloat(d);

    }
    public int[] intToDoubleInt16(){
        return null;
    }
    public SegmentData getSegmentData(int segID) {
        return dataSet.segmentDataArrayList.get(segID);
        //TODO: check if id is valid, get corresponding segment from dataset and return
    }

    public ArrayList<Float> getSegmentVoltage(int segID){
        //TODO: return voltage list, use getSegmentData here

        return dataSet.segmentDataArrayList.get(segID).voltage_combined;
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
