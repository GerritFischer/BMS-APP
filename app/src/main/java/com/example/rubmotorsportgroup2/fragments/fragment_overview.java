package com.example.rubmotorsportgroup2.fragments;

import static com.example.rubmotorsportgroup2.MainActivity.dm;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Debug;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.rubmotorsportgroup2.R;
import com.example.rubmotorsportgroup2.datamanager.DataManager;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;

public class fragment_overview extends Fragment {

    private DataManager datam;
    private ArrayList<BarEntry> voltchart = new ArrayList<BarEntry>();
    private ArrayList<BarEntry> tempchart = new ArrayList<>();
    private BarChart voltbar;
    private TextView volttext;
    private TextView temptext;
    private BarChart tempbar;

    public fragment_overview() {

        //this.dm = dm;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_overview, container, false);
    }
    public void onViewCreated(View view, Bundle savedInstanceState) {
        voltbar = view.findViewById(R.id.VoltageChart);
        super.onViewCreated(view, savedInstanceState);
        tempbar = view.findViewById(R.id.TempChart);

        volttext = (TextView) view.findViewById(R.id.VoltageText);
        temptext = (TextView) view.findViewById(R.id.TemperatureText);

        voltchart.add(new BarEntry(2f,4));

        ImageView refreshview = view.findViewById(R.id.RefreshDataView);
        getNewData();

        refreshview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getNewData();
            }
        });

    }
    private void getNewData()//Get the data form the Data manager and formats them in the string used by the fail pass view
    {
        voltchart=  new ArrayList<>();
        tempchart = new ArrayList<>();
        int avgcountervolt = 0;
        float avgvoltage = 0f;
        int avgcountertemp = 0;
        float avgtemp = 0f;
        for( int i = 0; i<  dm.getCellCount(); i++)
        {

            ArrayList<Float> segvolt = dm.getSegmentVoltage(i);
            ArrayList<Float> segtemp = dm.getSegmentTemperature(i);

            for(int j = 0; j<segvolt.size(); j++)
            {
                avgvoltage = avgvoltage+segvolt.get(j);
                avgcountervolt++;
            }
            for(int t = 1; t<segtemp.size(); t++)
            {
                if (segtemp.get(t) <0)
                    continue;

                avgtemp = avgtemp+segtemp.get(t);
                avgcountertemp++;

            }


        }
        //following if statements are to prevent a /0 case
        if (avgcountervolt>0)
            avgvoltage=avgvoltage/avgcountervolt;
        else
            avgvoltage=0;
        if(avgcountertemp>0)
            avgtemp = avgtemp/avgcountertemp;
        else
            avgtemp = 0;


        tempchart.add(new BarEntry(2f, avgtemp));
        //Reuses old Variables
        BarDataSet barDataSet = new BarDataSet(tempchart,"Temperature");
        BarData barData= new BarData(barDataSet);
        tempbar.setData(barData);
        barDataSet.setDrawValues(false);
        tempbar.setDrawGridBackground(false);
        tempbar.setTouchEnabled(false);
        tempbar.setScaleEnabled(false);
        tempbar.setDragEnabled(false);
        tempbar.setPinchZoom(false);
        tempbar.getXAxis().setDrawGridLines(false);
        tempbar.getAxisLeft().setDrawGridLines(false);
        tempbar.getAxisRight().setDrawGridLines(false);
        tempbar.getAxisLeft().setEnabled(false);
        tempbar.getAxisRight().setEnabled(false);
        tempbar.getXAxis().setEnabled(false);
        tempbar.getDescription().setEnabled(false);
        barDataSet.setColor(Color.RED);
        tempbar.getAxisLeft().setAxisMaximum(50f);
        tempbar.getAxisLeft().setAxisMinimum(10f);
        tempbar.invalidate();
        System.out.println(avgtemp);
        temptext.setText("Temperature:\n" +avgtemp+"C°");

        voltchart.add(new BarEntry(2f,avgvoltage));
         barDataSet = new BarDataSet(voltchart,"Voltage");
         barData = new BarData(barDataSet);
        voltbar.setData(barData);
        barDataSet.setDrawValues(false);
        voltbar.setDrawGridBackground(false);
        voltbar.setTouchEnabled(false);
        voltbar.setScaleEnabled(false);
        voltbar.setDragEnabled(false);
        voltbar.setPinchZoom(false);
        voltbar.getXAxis().setDrawGridLines(false);
        voltbar.getAxisLeft().setDrawGridLines(false);
        voltbar.getAxisRight().setDrawGridLines(false);
        voltbar.getAxisLeft().setEnabled(false);
        voltbar.getAxisRight().setEnabled(false);
        voltbar.getXAxis().setEnabled(false);
        voltbar.getDescription().setEnabled(false);
        barDataSet.setColor(Color.GREEN);
        voltbar.getAxisLeft().setAxisMaximum(3.73f);
        voltbar.getAxisLeft().setAxisMinimum(3.72f);
        voltbar.invalidate();
        volttext.setText("Voltage:\n" +avgvoltage+"V");







       /* String ret = "Average Voltage: "+ avgvoltage+"V\n"
                + "Average Temperature: "+ avgtemp+"C°\n"
                + "Cell Balancing: "+"sec\n"
                +"Status: " + ":)";
                /*
        */

    }

}