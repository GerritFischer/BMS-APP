package com.example.rubmotorsportgroup2;

import android.graphics.Color;

import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.List;

public class CustomBarDataset extends BarDataSet {
    private List<Float> intervals;
    private List<BarEntry> yVals;
    public CustomBarDataset(List<BarEntry> yVals, String label, List<Float> intervals) {
        super(yVals, label);
        this.intervals = intervals;
        this.yVals = yVals;
    }

    @Override
    public int getColor(int index){
        float value = yVals.get(index).getY();
        for(int i = 0; i < intervals.size(); i++){
            if(value <= intervals.get(i) ){
                return mColors.get(i);
            }
        }
        return mColors.get(intervals.size() - 1);
    }
}
