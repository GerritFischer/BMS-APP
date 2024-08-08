package com.example.rubmotorsportgroup2.datamanager;

import java.util.ArrayList;

public class SegmentData {
    public ArrayList<Float> voltage_combined = new ArrayList<Float>(); //22
    public ArrayList<Float> temperature_combined = new ArrayList<Float>(); //14

    public ArrayList<CIDData> cids = new ArrayList<>(); //2

    public void addVoltage_combined(float f){
        this.voltage_combined.add(f);
    }

    public void addTemperature_combined(float f){
        this.temperature_combined.add(f);
    }

    public void addCID(CIDData cid){
        cids.add(cid);
    }

    public ArrayList<Errorcode> getErrorsAsListInSegment()
    {
        SegmentData segment =this;
        ArrayList<Errorcode> retlist = new ArrayList<>();
        if (segment.cids.size() == 0) return retlist;
        for (int cidcount = 0; cidcount < 2; cidcount++) {
            CIDData cid = segment.cids.get(cidcount);

            //Over/under temperature
            for (int k = 0; k < cid.getAn_ot().size(); k++) {
                if (cid.getAn_ot().get(k)) {
                    retlist.add(new Errorcode("Overtemperature", -1, cidcount, k, false));
                }
            }
            for (int k = 0; k < cid.getAn_ut().size(); k++) {
                if (cid.getAn_ut().get(k)) {
                    retlist.add(new Errorcode("Undertemperature", -1, cidcount, k, false));
                }
            }
            // Over/Undervoltage
            for (int k = 0; k < cid.getCt_ov().size(); k++) {
                if (cid.getCt_ov().get(k)) {
                    retlist.add(new Errorcode("Overvoltage", -1, cidcount, k, false));
                }
            }
            for (int k = 0; k < cid.getCt_uv().size(); k++) {
                if (cid.getCt_uv().get(k)) {
                    retlist.add(new Errorcode("Undervoltage", -1, cidcount, k, false));
                }
            }
            //openload
            for (int k = 0; k < cid.getCt_open().size(); k++) {
                if (cid.getCt_open().get(k)) {
                    retlist.add(new Errorcode("Openload", -1, cidcount, k, false));
                }
            }
            //shortcircuit
            for (int k = 0; k < cid.getCt_short().size(); k++) {
                if (cid.getCt_short().get(k)) {
                    retlist.add(new Errorcode("Short Circuit", -1, cidcount, k, false));
                }
            }
            ArrayList<Errorcode> faulterrors = this.cids.get(cidcount).getFaultsAsErrors(-1, cidcount);
            for (int i = 0; i < faulterrors.size(); i++) {
                retlist.add(faulterrors.get(i));
            }
        }
        return retlist;
    }


    //TODO: add faults

}
