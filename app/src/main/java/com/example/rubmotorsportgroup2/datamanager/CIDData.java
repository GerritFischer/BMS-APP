package com.example.rubmotorsportgroup2.datamanager;

import java.util.ArrayList;

public class CIDData {

    public ArrayList<Float> voltage = new ArrayList<Float>(); //11
    public ArrayList<Float> temperature = new ArrayList<Float>(); //7
    public float stack_voltage;
    public float IC_temp;

    public ArrayList<Boolean> eot_cb = new ArrayList<Boolean>();
    public ArrayList<Boolean> an_ot = new ArrayList<Boolean>();
    public ArrayList<Boolean> an_ut = new ArrayList<Boolean>();

    public ArrayList<Boolean> ct_ov = new ArrayList<Boolean>();

    public ArrayList<Boolean> ct_uv = new ArrayList<Boolean>();


    public ArrayList<Boolean> ct_open = new ArrayList<Boolean>();

    public ArrayList<Boolean> ct_short = new ArrayList<Boolean>();


    public void addVoltage(float f){
        this.voltage.add(f);

    }
    public void addTemperature(float f){
        this.temperature.add(f);
    }

    public void setIC_temp(float temp){
        this.IC_temp = temp;
    }
    public void setStack_voltage(float voltage){
        this.stack_voltage = voltage;
    }

    public void addEot_cb(boolean state){
        eot_cb.add(state);
    }

    public void addAN_ot(boolean state){an_ot.add(state);}


    public void addAN_ut(boolean state){an_ut.add(state);}

    public void addCT_uv(boolean state){ct_uv.add(state);}

    public void addCT_ov(boolean state){ct_ov.add(state);}

    public void addCT_open(boolean state){ct_open.add(state);}

    public void addCT_short(boolean state){ct_short.add(state);}

    //faults:
    ArrayList<String> faults = new ArrayList<String>();
    short COM_ERR_COUNT;

    public void setCOM_ERR_COUNT(short error_count){
        COM_ERR_COUNT = error_count;
    }
    public void addFault(String fault){
        faults.add(fault);
    }
}
