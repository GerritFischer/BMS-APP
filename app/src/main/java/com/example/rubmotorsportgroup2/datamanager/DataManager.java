package com.example.rubmotorsportgroup2.datamanager;

import android.os.Handler;
import android.os.Looper;
import android.service.autofill.CharSequenceTransformation;
import android.util.Base64;
import java.util.ArrayList;

public class DataManager {

    private final int CELL_COUNT = 11;
    private final int TEMP_COUNT = 7;
    private DataSet dataSet;
    private DataRetriever dataRetriever;
    private CBData cbData;
    private long checksum = 0;

    public DataManager() {
        dataSet = new DataSet(null);
        dataRetriever = new DataRetriever(this);
        dataRetriever.start();
        cbData = new CBData(CELL_COUNT*2+6); //TODO: +6 is needed for ignoring unused terminals, edit later maybe
    }

    public void updateDataSet(String dataString) {
        int[] decodedData = decodeDataString(dataString); //decode incoming data string, int because 4 byte
        dataSet = generateDataSet(decodedData); //generate new dataset
    }


    public void updateCellBalancingTimer(){
        //update the timers
        System.out.println("updated cell balance");
    }
    public int[] decodeDataString(String dataString) {

        byte[] byteData = Base64.decode(dataString, Base64.DEFAULT);
        int length = 0;
        for (int i = 0; i < 4; i++) {
            length = (length << 8) + byteData[3 - i];

        }

        int[] intArray = new int[length+1];//
        intArray[0] = length;

        for (int i = 1; i < length+1; i++) {
            intArray[i] = (byteData[i * 4 + 3]);
            intArray[i] = (intArray[i] << 8) + ((int) byteData[i * 4 + 2] & 0xFF);
            intArray[i] = (intArray[i] << 8) + ((int) byteData[i * 4 + 1] & 0xFF);
            intArray[i] = (intArray[i] << 8) + ((int) byteData[i * 4] & 0xFF);
        }

        //Test for Checksum
        long sendedChecksum = 0;
        for(int i = 0;  i<4;i++)//gets the last 4 bits of the data streams(the sended checksum)
            sendedChecksum = (sendedChecksum <<8 ) + byteData[byteData.length-1-i];

        long calculatedChecksum = 0;
        for(int i = 0;i<length*4;i++)
        {
            long shift = Byte.toUnsignedInt(byteData[i]);
            calculatedChecksum = calculatedChecksum^(shift << (i%24));//calculates the checksum with the given data
        }
        //System.out.println("-------------------\n"+Long.toHexString(calculatedChecksum));
        //System.out.println(Long.toHexString(sendedChecksum));
        checksum = calculatedChecksum;
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
                    boolean cb_state = false; //(((register_fault3_cellov >> 16) >> j) & 1) == 1;
                    if(!cb_state && !cbData.hasStarted(j)){
                        cbData.setStart(j);
                    }
                    else if(cb_state && cbData.hasStarted(j)){
                        cbData.setTime(j);
                    }
                    cid1.addEot_cb(cb_state);
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
                    boolean cb_state = (((register_fault3_cellov >> 16) >> j) & 1) == 1;
                    if(!cb_state && !cbData.hasStarted(14+j)){
                        cbData.setStart(14+j);
                    }
                    else if(cb_state && cbData.hasStarted(14+j)){
                        cbData.setTime(14+j);
                    }
                    cid2.addEot_cb(cb_state);
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
            //System.out.println("CB 0 START: " + cbData.hasStarted(0));
        }
        //System.out.println(Integer.toHexString(data[data.length-1]));
        if(checksum != data[data.length-1] )
            System.out.println("hä");
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
        int OT = 0;
        int UT = 0;
        int OV = 0;
        int UV = 0;
        int CTO = 0;
        int CTS = 0;
        int otherfaults=0;
        for (int segmentcount = 0; segmentcount < CELL_COUNT; segmentcount++) {

            SegmentData segment = getSegmentData(segmentcount);
            if(segment.cids.size() == 0) continue;
            for (int cidcount = 0; cidcount < 2; cidcount++) {
                CIDData cid = segment.cids.get(cidcount);

                //Over/under temperature
                for (int k = 0; k < cid.getAn_ot().size(); k++) {
                    if(cid.getAn_ot().get(k)) {
                        counterrors++;
                        OT++;
                    }
                }
                for (int k = 0; k < cid.getAn_ut().size(); k++) {
                    if(cid.getAn_ut().get(k)) {
                        counterrors++;
                        UT++;
                    }
                }
                // Over/Undervoltage
                for (int k = 0; k < cid.getCt_ov().size(); k++) {
                    if(cid.getCt_ov().get(k)) {
                        counterrors++;
                        OV++;
                    }
                }
                for (int k = 0; k < cid.getCt_uv().size(); k++) {
                    if(cid.getCt_uv().get(k)) {
                        counterrors++;
                        UV++;
                        //System.out.println("UV at Segment "+ segmentcount +" in CID "+ cidcount +" in cell "+k + " with voltage of "+ cid.getVoltage().get(k));
                    }
                }
                //openload
                for (int k = 0; k < cid.getCt_open().size(); k++) {
                    if(cid.getCt_open().get(k)) {
                        counterrors++;
                        CTO++;
                    }
                }
                //shortcircuit
                for (int k = 0; k < cid.getCt_short().size(); k++) {
                    if(cid.getCt_short().get(k)) {
                        counterrors++;
                        CTS++;
                    }
                }
                counterrors = counterrors+ cid.getImportantFaults();
                otherfaults = otherfaults+ cid.getImportantFaults();

            }

        }

            /*
            System.out.println("Total "+ counterrors);
            System.out.println("OT "+OT);
            System.out.println("UT "+UT);
            System.out.println("OV "+OV);
            System.out.println("UV "+UV);
            System.out.println("CSO "+ CTO);
            System.out.println("CTS "+CTS);
             System.out.println("Other faults "+ otherfaults);
*/
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
    public void checkforUV()
    {
        int uv = 0;
        for (int segmentcount = 0; segmentcount < CELL_COUNT; segmentcount++) {

            ArrayList<Float> voltagelist= getSegmentVoltage(segmentcount);
           for(int voltagecount = 0;voltagecount < voltagelist.size();voltagecount++ )
               if(voltagelist.get(voltagecount)< 3.6f)
               {
                   System.out.println(voltagelist.get(voltagecount));
                   /*
                   if (voltagecount<voltagelist.size()/2)
                        System.out.println("UV at Segment "+ segmentcount +" in CID "+0 +" in cell "+voltagecount + " with voltage of "+voltagelist.get(voltagecount));
                   else
                       System.out.println("UV at Segment "+ segmentcount +" in CID "+1 +" in cell "+(voltagecount- (voltagelist.size()/2))+ " with voltage of "+voltagelist.get(voltagecount));
               */
               }

        }
    }
    public long calculateChecksum(Byte[] data)
    {
            long ret = 0;
            for(int i = 0; i< data.length;i++)
            {
                ret = ret^(data[i]<<(i%24));
            }
            System.out.println("ret");
            return ret;
    }

    public CBData getCbData(){
        return cbData;
    }

}
