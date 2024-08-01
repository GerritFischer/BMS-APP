package com.example.rubmotorsportgroup2.datamanager;

public class Errorcode {
    private  String  errortype;
    private int segment;
    private int cid;
    private int cell;
    boolean iscidfault;
    public Errorcode(String _error, int _seg, int _cid, int _cell, boolean isfault)
    {
        errortype = _error;
        segment = _seg ;
        cid = _cid ;
        cell=_cell;
        iscidfault = isfault;
    }
    public String getErrortype()
    {
        return errortype;
    }
    public int getSegment(){
        return segment;
    }
    public int getCid()
    {
        return cid;
    }
    public int getCell()
    {
        return cell;
    }
    @Override
    public String toString()
    {
        if(iscidfault)
            return ""+ errortype +" found in Segment "+ segment+" in CID "+cid;
        else
            return ""+ errortype +" found in Segment "+ segment+" in CID "+cid+ " in Cell "+ cell;
    }
    public String toStringIgnoringSegment()
    {
        if(iscidfault)
            return ""+ errortype +" found in CID "+cid;
        else
            return ""+ errortype +" found CID "+cid+ " in Cell "+ cell;
    }
}
