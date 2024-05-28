package com.example.rubmotorsportgroup2.datamanager;

import java.util.ArrayList;

public class CIDData {

    private ArrayList<Float> voltage = new ArrayList<Float>(); //11
    private ArrayList<Float> temperature = new ArrayList<Float>(); //7
    private float stack_voltage;
    private float IC_temp;
    private ArrayList<Boolean> eot_cb = new ArrayList<Boolean>();
    private ArrayList<Boolean> an_ot = new ArrayList<Boolean>();
    private ArrayList<Boolean> an_ut = new ArrayList<Boolean>();
    private ArrayList<Boolean> ct_ov = new ArrayList<Boolean>();
    private ArrayList<Boolean> ct_uv = new ArrayList<Boolean>();
    private ArrayList<Boolean> ct_open = new ArrayList<Boolean>();
    private ArrayList<Boolean> ct_short = new ArrayList<Boolean>();
    private ArrayList<String> faults = new ArrayList<String>();
    short COM_ERR_COUNT;


    public void addVoltage(float f){
        this.voltage.add(f);

    }
    public void addTemperature(float f){
        temperature.add(f);
    }

    public void setIC_temp(float temp){
        IC_temp = temp;
    }
    public void setStack_voltage(float voltage){
        stack_voltage = voltage;
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

    //faults
    public void setCOM_ERR_COUNT(short error_count){
        COM_ERR_COUNT = error_count;
    }
    public void addFault(String fault){
        faults.add(fault);
    }

    public ArrayList<Boolean> getEot_cb(){return eot_cb;}
    public ArrayList<Boolean> getAn_ot(){return an_ot;}
    public ArrayList<Boolean> getAn_ut(){return an_ut;}
    public ArrayList<Boolean> getCt_ov(){return ct_ov;}
    public ArrayList<Boolean> getCt_uv(){return ct_uv;}
    public ArrayList<Boolean> getCt_open(){return ct_open;}
    public ArrayList<Boolean> getCt_short(){return ct_short;}



}
