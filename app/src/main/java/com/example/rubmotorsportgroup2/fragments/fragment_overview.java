package com.example.rubmotorsportgroup2.fragments;

import static com.example.rubmotorsportgroup2.MainActivity.dm;

import android.graphics.Color;
import android.os.Bundle;

import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.rubmotorsportgroup2.R;
import com.example.rubmotorsportgroup2.datamanager.DataManager;
import com.example.rubmotorsportgroup2.datamanager.Errorcode;
import com.github.mikephil.charting.charts.BarChart;
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
    private TextView statusmessageview;
    private TextView errormessage;
    private int errorcount = 0;
    private boolean running = true;
    private final int tickTime = 10;
    private boolean first = true;
    private OverviewUpdateThread overviewUpdateThread;



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
        statusmessageview = (TextView) view.findViewById(R.id.StatusMessage);

        overviewUpdateThread = new OverviewUpdateThread();
        overviewUpdateThread.start();

        /*ImageView refreshview = view.findViewById(R.id.RefreshDataView);
        getNewData();

        refreshview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getNewData();
            }
        });

         */

    }
    private class OverviewUpdateThread extends Thread {
        @Override
        public void run() {
            int timer = 1000;
            boolean first=true;
            while(running) {
                try {

                    if(timer >= 1000){
                        new Handler(Looper.getMainLooper()).post(() -> {
                            getNewData();

                        });
                        timer = 0;
                    }
                    // Sleeping
                    else{
                        Thread.sleep(tickTime);
                        timer += tickTime;
                    }
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }
    public void getNewData()//Get the data form the Data manager and formats them in the string used by the fail pass view
    {
        voltchart=  new ArrayList<>();
        tempchart = new ArrayList<>();
        int avgcountervolt = 0;
        float avgvoltage = 0f;
        int avgcountertemp = 0;
        float avgtemp = 0f;
        for( int i = 0; i<  dm.getSegmentCount(); i++)
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
        tempbar.setDrawGridBackground(true);
        Legend l = tempbar.getLegend();
        l.setEnabled(false);
        tempbar.setTouchEnabled(false);
        tempbar.setScaleEnabled(false);
        tempbar.setDragEnabled(false);
        tempbar.setPinchZoom(false);
        tempbar.getXAxis().setDrawGridLines(false);
        //tempbar.getAxisLeft().setDrawGridLines(false);
        tempbar.getAxisRight().setDrawGridLines(false);
        //tempbar.getAxisLeft().setEnabled(false);
        tempbar.getAxisLeft().setTextColor(Color.WHITE);
        tempbar.getAxisRight().setEnabled(false);
        tempbar.getXAxis().setEnabled(false);
        tempbar.getDescription().setEnabled(false);
        barDataSet.setColor(Color.RED);
        tempbar.getAxisLeft().setAxisMaximum(50f);
        tempbar.getAxisLeft().setAxisMinimum(10f);
        tempbar.invalidate();
        temptext.setText("Temperature:\n" + (Math.round(avgtemp * 100.0) / 100.0) +"C°");

        voltchart.add(new BarEntry(2f,avgvoltage));
        barDataSet = new BarDataSet(voltchart,"Voltage");
        barData = new BarData(barDataSet);
        voltbar.setData(barData);
        barDataSet.setDrawValues(false);
        voltbar.setDrawGridBackground(true);
        voltbar.setTouchEnabled(false);
        voltbar.setScaleEnabled(false);
        l = voltbar.getLegend();
        l.setEnabled(false);
        voltbar.setDragEnabled(false);
        voltbar.setPinchZoom(false);
        //voltbar.getXAxis().setDrawGridLines(false);
        //voltbar.getAxisLeft().setDrawGridLines(false);
        voltbar.getAxisRight().setDrawGridLines(false);
        //voltbar.getAxisLeft().setEnabled(false);
        voltbar.getAxisLeft().setTextColor(Color.WHITE);
        voltbar.getAxisRight().setEnabled(false);
        voltbar.getXAxis().setEnabled(false);
        voltbar.getDescription().setEnabled(false);
        barDataSet.setColor(Color.GREEN);
        voltbar.getAxisLeft().setAxisMaximum(4.5f);
        voltbar.getAxisLeft().setAxisMinimum(3.1f);
        voltbar.invalidate();
        volttext.setText("Voltage:\n" + Math.round(avgvoltage * 1000.0) / 1000.0 +"V");


        errorcount=dm.hasError();
        dm.checkforUV();
        ArrayList<Errorcode> test = dm.getErrorsAsList();

       /* for (int i = 0; i<test.size();i++){
            System.out.println( test.get(i).toString());
        }*/

        try {
            statusmessageview.setTextSize(48);
            if (!dm.getvalid())
            {
                statusmessageview.setText("No\n Data");
                statusmessageview.setTextSize(20);
                statusmessageview.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.no_data_background, null));

            }
            else if (errorcount == 0) {
                statusmessageview.setText("OK");
                statusmessageview.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.tv_segment_status_drawable, null));
                //errormessage.setText("0 Errors");
                //errormessage.setTextColor(0xff3dc03c);
            } else if (errorcount == 1) {
                statusmessageview.setText("!");
                statusmessageview.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.warning_background_overview, null));
                //errormessage.setText("1 Error");
                //errormessage.setTextColor(0xffFFEB3B);
            } else if (errorcount == 2) {
                statusmessageview.setText("!!");
                statusmessageview.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.double_warning_overview, null));
                //errormessage.setText("2 Errors");
                //errormessage.setTextColor(0xffFF7700);
            } else {
                statusmessageview.setText("!!!");
                statusmessageview.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.error_warning_overview, null));
                //errormessage.setText(""+errorcount+" Errors");
                //errormessage.setTextColor(0xffFF1100);
            }
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }

        if(first)
        {
            first = false;
            getNewData();
        }




       /* String ret = "Average Voltage: "+ avgvoltage+"V\n"
                + "Average Temperature: "+ avgtemp+"C°\n"
                + "Cell Balancing: "+"sec\n"
                +"Status: " + ":)";
                /*
        */


    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        running = false; // Stop the thread when fragment is destroyed
        try {
            overviewUpdateThread.join(); // Wait for the thread to finish
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }
    }

}