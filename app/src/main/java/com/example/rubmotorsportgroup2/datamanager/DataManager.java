package com.example.rubmotorsportgroup2.datamanager;

import android.util.Base64;
import java.util.ArrayList;

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

    public void updateDataSet(String dataString) {
        int[] decodedData = decodeDataString(dataString); //decode incoming data string, int because 4 byte
        dataSet = generateDataSet(decodedData); //generate new dataset
    }

    public int[] decodeDataString(String dataString) {

        byte[] byteData = Base64.decode(dataString, Base64.DEFAULT);

        int length = 0;
        for (int i = 0; i < 4; i++) {
            length = (length << 8) + byteData[3 - i];
        }

        int[] intArray = new int[length];
        intArray[0] = length;

        for (int i = 1; i < length; i++) {
            intArray[i] = (byteData[i * 4 + 3]);
            intArray[i] = (intArray[i] << 8) + ((int) byteData[i * 4 + 2] & 0xFF);
            intArray[i] = (intArray[i] << 8) + ((int) byteData[i * 4 + 1] & 0xFF);
            intArray[i] = (intArray[i] << 8) + ((int) byteData[i * 4] & 0xFF);
        }
        return intArray;
    }

    //storing fault names for pushing them into fault list
    private final String[] fault_names1 = {"CT_UV_FLT", "CT_OV_FLT", "AN_UT_FLT", "AN_OT_FLT",
            null, null, null, null,
            null, "COM_ERR_FLT", "COM_LOSS_FLT", "VPWR_LV_FLT",
            "VPWR_OV_FLT", "COM_ERR_OVR_FLT", "RESET_FLT", "POR"
    };
    private final String[] fault_names2 = {"FUSE_ERR_FLT", "DED_ERR_FLT", "OSC_ERR_FLT", "CB_OPEN_FLT",
            "CB_SHORT_FLT", null, null, null,
            "IC_TSD_FLT", "GND_LOSS_FLT", "ADC1_A_FLT", "ADC1_B_FLT",
            "VANA_UV_FLT", "VANA_OV_FLT", "VCOM_UV_FLT", "VCOM_OV_FLT"
    };

    public DataSet generateDataSet(int[] data) {
        int segmentCount = (data[0] - 1) / 50; //50 fields per segment, -1 because length int
        ArrayList<SegmentData> segments = new ArrayList<SegmentData>();
        int offset = 1; //set to the first relevant entry
        //for each segment, create segment data
        for (int i = 0; i < segmentCount; i++) {
            SegmentData segment = new SegmentData();
            //add values for cid1
            CIDData cid1 = new CIDData();

            //add voltage of cid1
            for (int j = 0; j < CELL_COUNT; j++) {
                float value = Float.intBitsToFloat(data[offset]);
                segment.addVoltage_combined(value);
                cid1.addVoltage(value);
                offset++;
            }
            //add temp of cid1
            for (int j = 0; j < TEMP_COUNT; j++) {
                float value = Float.intBitsToFloat(data[offset]);
                segment.addTemperature_combined(value);
                cid1.addTemperature(value);
                offset++;
            }
            //set stack voltage of cid1
            cid1.setStack_voltage(Float.intBitsToFloat(data[offset]));
            offset++;
            //set ic temp of cid1
            cid1.setIC_temp(Float.intBitsToFloat(data[offset]));
            offset++;

            //save register and extract data out of it
            //--------------------------------------------------------------------------------------
            int register_fault1_fault2 = data[offset];
            //fault1
            for (int j = 0; j < 16; j++) {
                if (fault_names1[j] != null && (((register_fault1_fault2 >> 16) >> j) & 1) == 1) {
                    cid1.addFault(fault_names1[j]);
                }
            }
            //fault2
            for (int j = 0; j < 16; j++) {
                if (fault_names2[j] != null && (((register_fault1_fault2) >> j) & 1) == 1) {
                    cid1.addFault(fault_names2[j]);
                }
            }
            offset++;
            //--------------------------------------------------------------------------------------
            int register_fault3_cellov = data[offset];
            //eot_cid1
            for (int j = 0; j < 14; j++) {
                if (!(j >= 4 && j <= 6)) { //skip the disconnected terminals
                    cid1.addEot_cb((((register_fault3_cellov >> 16) >> j) & 1) == 1);
                }
            }
            //cell over voltage
            for (int j = 0; j < 14; j++) {
                if (!(j >= 4 && j <= 6)) { //skip the disconnected terminals
                    cid1.addCT_ov(((register_fault3_cellov >> j) & 1) == 1);
                }
            }

            offset++;
            //--------------------------------------------------------------------------------------
            int register_celluv_anotut = data[offset];
            //cell under voltage
            for (int j = 0; j < 14; j++) {
                if (!(j >= 4 && j <= 6)) { //skip the disconnected terminals
                    cid1.addCT_uv((((register_celluv_anotut >> 16) >> j) & 1) == 1);
                }
            }
            //over and under temperature
            for (int j = 0; j < 7; j++) {
                cid1.addAN_ut((((register_celluv_anotut >> 1) >> j) & 1) == 1);
            }
            for (int j = 0; j < 7; j++) {
                cid1.addAN_ot((((register_celluv_anotut >> 9) >> j) & 1) == 1);
            }
            offset++;
            //--------------------------------------------------------------------------------------
            int register_com_open = data[offset];

            //com error count
            short error_count = (short) ((register_com_open >> 16) & 1);
            for (int j = 0; j < 7; j++) {
                error_count = (short) ((error_count << 1) + (short) ((register_com_open >> 17) >> j) & 1);
            }
            cid1.setCOM_ERR_COUNT(error_count);
            //cell open load
            for (int j = 0; j < 14; j++) {
                if (!(j >= 4 && j <= 6)) { //skip the disconnected terminals
                    cid1.addCT_open((((register_com_open) >> j) & 1) == 1);
                }
            }
            offset++;
            //--------------------------------------------------------------------------------------
            int register_short = data[offset];
            //cell short circuit
            for (int j = 0; j < 14; j++) {
                if (!(j >= 4 && j <= 6)) { //skip the disconnected terminals
                    cid1.addCT_short((((register_short >> 16) >> j) & 1) == 1);
                }
            }
            offset++;
            //--------------------------------------------------------------------------------------
            segment.addCID(cid1);


            CIDData cid2 = new CIDData();

            //add voltage of cid2
            for (int j = 0; j < CELL_COUNT; j++) {
                float value = Float.intBitsToFloat(data[offset]);
                segment.addVoltage_combined(value);
                cid2.addVoltage(value);
                offset++;
            }
            //add temp of cid2
            for (int j = 0; j < TEMP_COUNT; j++) {
                float value = Float.intBitsToFloat(data[offset]);
                segment.addTemperature_combined(value);
                cid2.addTemperature(value);
                offset++;
            }
            //set stack voltage of cid2
            cid2.setStack_voltage(Float.intBitsToFloat(data[offset]));
            offset++;
            //set ic temp of cid2
            cid2.setIC_temp(Float.intBitsToFloat(data[offset]));
            offset++;

            //save register and extract data out of it
            //--------------------------------------------------------------------------------------
            register_fault1_fault2 = data[offset];
            //fault1
            for (int j = 0; j < 16; j++) {
                if (fault_names1[j] != null && (((register_fault1_fault2 >> 16) >> j) & 1) == 1) {
                    cid2.addFault(fault_names1[j]);
                }
            }
            //fault2
            for (int j = 0; j < 16; j++) {
                if (fault_names2[j] != null && (((register_fault1_fault2) >> j) & 1) == 1) {
                    cid2.addFault(fault_names2[j]);
                }
            }
            offset++;
            //--------------------------------------------------------------------------------------
            register_fault3_cellov = data[offset];
            //eot_cid2
            for (int j = 0; j < 14; j++) {
                if (!(j >= 4 && j <= 6)) { //skip the disconnected terminals
                    cid2.addEot_cb((((register_fault3_cellov >> 16) >> j) & 1) == 1);
                }
            }
            //cell over voltage
            for (int j = 0; j < 14; j++) {
                if (!(j >= 4 && j <= 6)) { //skip the disconnected terminals
                    cid2.addCT_ov(((register_fault3_cellov >> j) & 1) == 1);
                }
            }

            offset++;
            //--------------------------------------------------------------------------------------
            register_celluv_anotut = data[offset];
            //cell under voltage
            for (int j = 0; j < 14; j++) {
                if (!(j >= 4 && j <= 6)) { //skip the disconnected terminals
                    cid2.addCT_uv((((register_celluv_anotut >> 16) >> j) & 1) == 1);
                }
            }
            //over and under temperature
            for (int j = 0; j < 7; j++) {
                cid2.addAN_ut((((register_celluv_anotut >> 1) >> j) & 1) == 1);
            }
            for (int j = 0; j < 7; j++) {
                cid2.addAN_ot((((register_celluv_anotut >> 9) >> j) & 1) == 1);
            }
            offset++;
            //--------------------------------------------------------------------------------------
            register_com_open = data[offset];

            //com error count
            error_count = (short) ((register_com_open >> 16) & 1);
            for (int j = 0; j < 7; j++) {
                error_count = (short) ((error_count << 1) + (short) ((register_com_open >> 17) >> j) & 1);
            }
            cid2.setCOM_ERR_COUNT(error_count);
            //cell open load
            for (int j = 0; j < 14; j++) {
                if (!(j >= 4 && j <= 6)) { //skip the disconnected terminals
                    cid2.addCT_open((((register_com_open) >> j) & 1) == 1);
                }
            }
            offset++;
            //--------------------------------------------------------------------------------------
            register_short = data[offset];
            //cell short circuit
            for (int j = 0; j < 14; j++) {
                if (!(j >= 4 && j <= 6)) { //skip the disconnected terminals
                    cid2.addCT_short((((register_short >> 16) >> j) & 1) == 1);
                }
            }
            offset++;
            //--------------------------------------------------------------------------------------
            segment.addCID(cid2);
            segments.add(segment);
        }
        //generate and return new data set
        return new DataSet(segments);
    }


    public SegmentData getSegmentData(int segID) {
        if(dataSet.segmentDataArrayList == null)
            return new SegmentData();
        else if ((segID < 0 || segID > CELL_COUNT) ) return null;
        return dataSet.segmentDataArrayList.get(segID);
        //TODO: check if id is valid, get corresponding segment from dataset and return
    }

    public ArrayList<Float> getSegmentVoltage(int segID) {
        //TODO: return voltage list, use getSegmentData here

        return getSegmentData(segID).voltage_combined;
    }

    public ArrayList<Float> getSegmentTemperature(int segID) {
        //TODO: return temperature list, use getSegmentData here
        return getSegmentData(segID).temperature_combined;
    }

    public boolean getErrors(int segID, String fault,int errid) //returns the value of the selected segment with the selected fault and id
    {
        for (int l = 0; l < 2; l++) {
            CIDData cid1 = getSegmentData(segID).cids.get(l);
           if(fault.equals("eot_cb"))
           {
               return cid1.getEot_cb().get(errid);
           }
           else if(fault.equals("an_ot"))
           {
               return cid1.getAn_ot().get(errid);
           }
           else if(fault.equals("an_ut"))
           {
               return cid1.getAn_ut().get(errid);
           }
           else if(fault.equals("ct_ov"))
           {
               return cid1.getCt_ov().get(errid);
           }
           else if(fault.equals("ct_uv"))
           {
               return cid1.getCt_uv().get(errid);
           }
           else if(fault.equals("ct_open"))
           {
               return cid1.getCt_open().get(errid);
           }
           else if(fault.equals("ct_short"))
           {
               return cid1.getCt_uv().get(errid);
           }

        }
        return true;

    }

    public int hasError() {//returns how many errors are found

        int counterrors = 0;
        for (int i = 0; i < CELL_COUNT; i++) {

            SegmentData segment = getSegmentData(i);
            if(segment.cids.size() == 0) continue;
            for (int l = 0; l < 2; l++) {
                CIDData cid1 = segment.cids.get(l);

                //Over/under temperature
                for (int k = 0; k < cid1.getAn_ot().size(); k++) {
                    if(cid1.getAn_ot().get(k))
                        counterrors++;
                }
                for (int k = 0; k < cid1.getAn_ut().size(); k++) {
                    if(cid1.getAn_ut().get(k))
                        counterrors++;
                }
                // Over/Undervoltage
                for (int k = 0; k < cid1.getCt_ov().size(); k++) {
                    if(cid1.getCt_ov().get(k))
                        counterrors++;
                }
                for (int k = 0; k < cid1.getCt_uv().size(); k++) {
                    if(cid1.getCt_uv().get(k))
                        counterrors++;
                }
                //openload
                for (int k = 0; k < cid1.getCt_open().size(); k++) {
                    if(cid1.getCt_open().get(k))
                        counterrors++;
                }
                //shortcircuit
                for (int k = 0; k < cid1.getCt_short().size(); k++) {
                    if(cid1.getCt_short().get(k))
                        counterrors++;
                }

            }
        }
        return counterrors;
    }
    public int getCellCount()
    {
        return  CELL_COUNT;
    }
    public int getTempCount()
    {
        return TEMP_COUNT;
    }

}
