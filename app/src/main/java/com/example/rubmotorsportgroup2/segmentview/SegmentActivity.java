package com.example.rubmotorsportgroup2.segmentview;

import static com.example.rubmotorsportgroup2.MainActivity.dm;

import android.graphics.Typeface;
import android.graphics.fonts.Font;
import android.graphics.fonts.FontFamily;
import android.graphics.fonts.FontStyle;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import com.example.rubmotorsportgroup2.CustomBarDataset;
import com.example.rubmotorsportgroup2.R;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.time.Duration;
import java.util.ArrayList;


public class SegmentActivity extends AppCompatActivity {
    private int[] barColors;
    private boolean running = true;
    final private int tickTime = 1000;
    private int pos = 0;
    HorizontalBarChart bc_voltage;
    HorizontalBarChart bc_temperature;
    UpdaterX u;

    private ArrayList<TextView> cbstates = new ArrayList<TextView>();
    private ArrayList<TextView> cbtimes = new ArrayList<TextView>();



    private void updateBarCharts(){
        ArrayList<String> xAxisNames = new ArrayList<>();
        xAxisNames.add("A01");
        xAxisNames.add("A02");
        xAxisNames.add("A03");
        xAxisNames.add("A04");
        xAxisNames.add("A05");
        xAxisNames.add("A06");
        xAxisNames.add("A07");
        xAxisNames.add("A08");
        xAxisNames.add("A09");
        xAxisNames.add("A10");
        xAxisNames.add("A11");
        xAxisNames.add("B01");
        xAxisNames.add("B02");
        xAxisNames.add("B03");
        xAxisNames.add("B04");
        xAxisNames.add("B05");
        xAxisNames.add("B06");
        xAxisNames.add("B07");
        xAxisNames.add("B08");
        xAxisNames.add("B09");
        xAxisNames.add("B10");
        xAxisNames.add("B11");
        ArrayList<BarEntry> entriesVoltage = new ArrayList<>();
        DecimalFormat df = new DecimalFormat("#.####");
        for(int i = 0; i < 22; i++){
            xAxisNames.set(i, xAxisNames.get(i) + " " + df.format(dm.getSegmentVoltage(pos).get(i)));
            entriesVoltage.add(new BarEntry((float)i, dm.getSegmentVoltage(pos).get(i), xAxisNames.get(i) + "   " + dm.getSegmentVoltage(pos).get(i)) );
        }

        ArrayList<Float> intervalsVoltage = new ArrayList<Float>();
        intervalsVoltage.add(3.1f);
        intervalsVoltage.add(3.4f);
        intervalsVoltage.add(3.8f);
        intervalsVoltage.add(4.0f);
        intervalsVoltage.add(4.1f);

        CustomBarDataset barDataSetVoltage = new CustomBarDataset(entriesVoltage, "data", intervalsVoltage);
        barDataSetVoltage.setColors(ContextCompat.getColor(getBaseContext(), R.color.VeryLowColor),
                ContextCompat.getColor(getBaseContext(), R.color.LowColor),
                ContextCompat.getColor(getBaseContext(), R.color.OKColor),
                ContextCompat.getColor(getBaseContext(), R.color.WarningColor),
                ContextCompat.getColor(getBaseContext(), R.color.AlertColor)
        );
        barDataSetVoltage.setDrawValues(false);
        BarData dataVoltage = new BarData(barDataSetVoltage);
        bc_voltage.getXAxis().setValueFormatter(new IndexAxisValueFormatter(xAxisNames));
        bc_voltage.setData(dataVoltage);
        bc_voltage.notifyDataSetChanged();
        bc_voltage.invalidate();


        ArrayList<BarEntry> entriesTemperature = new ArrayList<>();
        for(int i = 1; i < 7; i++){
            //added 25 to temp to make it positive values only
            entriesTemperature.add(new BarEntry((float)i, dm.getSegmentTemperature(pos).get(i) + 25f));
        }

        ArrayList<Float> intervalsTemperature = new ArrayList<Float>();
        intervalsTemperature.add(15f);
        intervalsTemperature.add(25f);
        intervalsTemperature.add(55f);
        intervalsTemperature.add(65f);
        intervalsTemperature.add(80f);

        CustomBarDataset barDataSetTemperature = new CustomBarDataset(entriesTemperature, "data", intervalsTemperature);
        barDataSetTemperature.setColors(ContextCompat.getColor(getBaseContext(), R.color.VeryLowColor),
                ContextCompat.getColor(getBaseContext(), R.color.LowColor),
                ContextCompat.getColor(getBaseContext(), R.color.OKColor),
                ContextCompat.getColor(getBaseContext(), R.color.WarningColor),
                ContextCompat.getColor(getBaseContext(), R.color.AlertColor)
        );
        barDataSetTemperature.setDrawValues(false);
        BarData dataTemperature = new BarData(barDataSetTemperature);
        bc_temperature.setData(dataTemperature);
        bc_temperature.notifyDataSetChanged();
        bc_temperature.invalidate();

    }

    private void updateCellBalancing(){
        for(int i = 0; i < (dm.getCellCount() * 2); i++){
            if (dm.getCbData().hasStarted(i)){
                cbstates.get(i).setText("Balancing...");
                cbtimes.get(i).setText(formatDuration(dm.getCbData().getTime(i)));
            }
        }

    }

    private String formatDuration(Duration duration) {
        long seconds = duration.getSeconds();
        long absSeconds = Math.abs(seconds);
        String positive = String.format(
                "%d:%02d:%02d",
                absSeconds / 3600,
                (absSeconds % 3600) / 60,
                absSeconds % 60);
        return seconds < 0 ? "-" + positive : positive;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.segment_activity); //loading detail layout
        TextView heading = (TextView)findViewById(R.id.tv_segmentheading);
        pos = getIntent().getExtras().getInt("pos");
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
        //bc_voltage.getXAxis().setDrawGridLines(false);
        bc_voltage.getAxisLeft().setDrawGridLines(false);
        bc_voltage.getAxisRight().setDrawGridLines(false);
        bc_voltage.getAxisLeft().setEnabled(false);
        bc_voltage.getAxisRight().setEnabled(false);
        XAxis xAxis = bc_voltage.getXAxis();
        xAxis.setTextSize(13f);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM_INSIDE);
        xAxis.setDrawGridLines(false);
        xAxis.setCenterAxisLabels(false);
        xAxis.setTypeface(Typeface.MONOSPACE);
        xAxis.setXOffset(100);
        xAxis.setGranularityEnabled(true);
        xAxis.setLabelCount(22);
        bc_voltage.getDescription().setEnabled(false);
        bc_voltage.setBackgroundColor(ContextCompat.getColor(getBaseContext(), R.color.ElementColor));
        bc_voltage.setFocusableInTouchMode(false);
        bc_voltage.getAxisLeft().setAxisMaximum(4.3f);
        bc_voltage.getAxisLeft().setAxisMinimum(2.9f);


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
        //added 25 to temp to make it positive values only
        bc_temperature.getAxisLeft().setAxisMaximum(90f);
        bc_temperature.getAxisLeft().setAxisMinimum(0f);
        updateBarCharts();

        LinearLayout cb_table = (LinearLayout)findViewById(R.id.ll_cbtimers);
        for(int i = 0; i < dm.getCellCount() * 2; i++){
            LinearLayout l = new LinearLayout(getBaseContext());
            TextView t1 = new TextView(getBaseContext());
            t1.setText("Not balancing");
            l.addView(t1);
            cbstates.add(t1);
            TextView t2 = new TextView(getBaseContext());
            t2.setText("Not balanced yet");
            t2.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
            l.addView(t2);
            cbtimes.add(t2);
            cb_table.addView(l);
        }








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
                            updateCellBalancing();

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
