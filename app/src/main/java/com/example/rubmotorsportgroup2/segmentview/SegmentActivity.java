package com.example.rubmotorsportgroup2.segmentview;

import static com.example.rubmotorsportgroup2.MainActivity.dm;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.rubmotorsportgroup2.R;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;


public class SegmentActivity extends AppCompatActivity {
    private int barColor;
    private boolean running = true;
    final private int tickTime = 1000;
    private int pos = 0;
    HorizontalBarChart bc_voltage;
    HorizontalBarChart bc_temperature;
    UpdaterX u;


    private void updateBarCharts(){
        ArrayList<BarEntry> entriesVoltage = new ArrayList<>();
        for(int i = 0; i < 22; i++){
            entriesVoltage.add(new BarEntry((float)i, dm.getSegmentVoltage(pos).get(i)));
        }

        BarDataSet barDataSetVoltage = new BarDataSet(entriesVoltage, "data");
        barDataSetVoltage.setColor(barColor);
        barDataSetVoltage.setDrawValues(false);
        BarData dataVoltage = new BarData(barDataSetVoltage);
        bc_voltage.setData(dataVoltage);
        bc_voltage.notifyDataSetChanged();
        bc_voltage.invalidate();

        ArrayList<BarEntry> entriesTemperature = new ArrayList<>();
        for(int i = 1; i < 7; i++){
            entriesTemperature.add(new BarEntry((float)i, dm.getSegmentTemperature(pos).get(i)));
        }

        BarDataSet barDataSetTemperature = new BarDataSet(entriesTemperature, "data");
        barDataSetTemperature.setColor(barColor);
        barDataSetTemperature.setDrawValues(false);
        BarData dataTemperature = new BarData(barDataSetTemperature);
        bc_temperature.setData(dataTemperature);
        bc_temperature.notifyDataSetChanged();
        bc_temperature.invalidate();
        
        
        
        
        
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.segment_activity); //loading detail layout
        TextView heading = (TextView)findViewById(R.id.tv_segmentheading);
        pos = getIntent().getExtras().getInt("pos");
        barColor = getIntent().getExtras().getInt("barColor");
        heading.setText(heading.getText() + " " + pos);
        bc_voltage = (HorizontalBarChart)findViewById(R.id.bc_voltage);
        bc_temperature = (HorizontalBarChart)findViewById(R.id.bc_temperature);

        bc_voltage.setDrawGridBackground(false);
        Legend l_voltage = bc_voltage.getLegend();
        l_voltage.setEnabled(false);
        bc_voltage.setDrawValueAboveBar(true);
        bc_voltage.setTouchEnabled(false);
        bc_voltage.setScaleEnabled(false);
        bc_voltage.setDragEnabled(false);
        bc_voltage.setPinchZoom(false);
        bc_voltage.getXAxis().setDrawGridLines(false);
        bc_voltage.getAxisLeft().setDrawGridLines(false);
        bc_voltage.getAxisRight().setDrawGridLines(false);
        bc_voltage.getAxisLeft().setEnabled(false);
        bc_voltage.getAxisRight().setEnabled(false);
        bc_voltage.getXAxis().setEnabled(false);
        bc_voltage.getDescription().setEnabled(false);
        bc_voltage.setBackgroundColor(ContextCompat.getColor(getBaseContext(), R.color.ElementColor));
        bc_voltage.setFocusableInTouchMode(false);
        bc_voltage.getAxisLeft().setAxisMaximum(3.73f);
        bc_voltage.getAxisLeft().setAxisMinimum(3.72f);


        bc_temperature.setDrawGridBackground(false);
        Legend l_temperature = bc_temperature.getLegend();
        l_temperature.setEnabled(false);
        bc_temperature.setTouchEnabled(false);
        bc_temperature.setScaleEnabled(false);
        bc_temperature.setDragEnabled(false);
        bc_temperature.setPinchZoom(false);
        bc_temperature.getXAxis().setDrawGridLines(false);
        bc_temperature.getAxisLeft().setDrawGridLines(false);
        bc_temperature.getAxisRight().setDrawGridLines(false);
        bc_temperature.getAxisLeft().setEnabled(false);
        bc_temperature.getAxisRight().setEnabled(false);
        bc_temperature.getXAxis().setEnabled(false);
        bc_temperature.getDescription().setEnabled(false);
        bc_temperature.setBackgroundColor(ContextCompat.getColor(getBaseContext(), R.color.ElementColor));
        bc_temperature.setFocusableInTouchMode(false);
        bc_temperature.getAxisLeft().setAxisMaximum(25f);
        bc_temperature.getAxisLeft().setAxisMinimum(20f);
        updateBarCharts();

        u = new UpdaterX();
        u.start();


    }



    public void onClick(View view){
        finish();
    }
    private class UpdaterX extends Thread {
        @Override
        public void run() {
            int timer = 0;
            while(running) {
                try {

                    if(timer < 1000){
                        timer++;
                    }
                    // Sleeping
                    else{

                        new Handler(Looper.getMainLooper()).post(() -> {
                            updateBarCharts();

                        });
                    }
                    Thread.sleep(tickTime);
                    timer += tickTime;

                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        running = false; // Stop the thread when fragment is destroyed
        try {
            u.join(); // Wait for the thread to finish
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }
    }
}
