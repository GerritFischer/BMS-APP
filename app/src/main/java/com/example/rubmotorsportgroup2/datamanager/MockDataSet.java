package com.example.rubmotorsportgroup2.datamanager;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;

public class MockDataSet extends DataSet{



    //voltage controls
    float VOLT = 3.7f;
    boolean OVERVOLTAGE = false;
    boolean UNDERVOLTAGE = false;
    boolean CELL_BALANCING = false; //not running when true
    boolean SHORT = false;
    boolean OPEN = false;

    //temperature controls
    float TEMP = 25f;
    boolean OVERTEMP = false;
    boolean UNDERTEMP = false;






    public MockDataSet(ArrayList<SegmentData> segments, CBData cb) {
        super(segments);
        ArrayList<SegmentData> segs = new ArrayList<>();

        for (int i = 0; i < 12; i++){
            SegmentData sd = new SegmentData();
            //setting all temps
            for(int j = 0; j < 22; j++){
                sd.addVoltage_combined(VOLT);
            }
            for(int j = 0; j < 14; j++){
                sd.addTemperature_combined(TEMP);
            }
            for(int j = 0; j < 2; j++){
                CIDData cid = new CIDData();
                for(int k = 0; k < 11; k++){
                    cid.addVoltage(VOLT);
                    cid.addEot_cb(CELL_BALANCING);

                    if(!CELL_BALANCING && !cb.hasStarted(k)){
                        cb.setStart(k);
                    }
                    else if(Duration.between(cb.getStart(k), Instant.now()).getSeconds() > 30){
                        cb.setTime(k);
                    }



                    cid.addCT_ov(OVERVOLTAGE);
                    cid.addCT_uv(UNDERVOLTAGE);
                    cid.addCT_short(SHORT);
                    cid.addCT_open(OPEN);

                }
                for(int k = 0; k < 7; k++){
                    cid.addTemperature(TEMP);
                    cid.addAN_ot(OVERTEMP);
                    cid.addAN_ut(UNDERTEMP);
                }
                cid.setStack_voltage(VOLT);
                cid.setIC_temp(TEMP);

                sd.addCID(cid);

            }
            segs.add(sd);

        }
        super.segmentDataArrayList = segs;

    }
}
