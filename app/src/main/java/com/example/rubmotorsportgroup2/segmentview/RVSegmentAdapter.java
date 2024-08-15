package com.example.rubmotorsportgroup2.segmentview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.annotation.NonNull;

import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;


import com.example.rubmotorsportgroup2.CustomBarDataset;
import com.example.rubmotorsportgroup2.R;
import com.example.rubmotorsportgroup2.datamanager.SegmentData;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;

public class RVSegmentAdapter extends RecyclerView.Adapter<RVSegmentAdapter.ViewHolder> {

    Context context;
    private RecyclerViewEvent listener;
    private ArrayList<SegmentData> data;

    private ArrayList<RVSegmentAdapter.ViewHolder> viewHolders = new ArrayList<RVSegmentAdapter.ViewHolder>();

    public RVSegmentAdapter(Context context, ArrayList<SegmentData> data, RecyclerViewEvent listener) {
        this.context = context;
        this.data = data;
        this.listener = listener;
    }


    @NonNull
    @Override
    public RVSegmentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new ViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.segment_card,
                        parent,
                        false)
        );
    }
    public void updateDataSet(int position){
        try{
            viewHolders.get(position).t.setText(context.getString(R.string.segment)+ " " + position);
        }
        catch(Exception e){
            System.out.println("ERROR FOR SEGMENT (PROB SCREEN ROTATION) " + position);
        }

        ArrayList<BarEntry> entries = new ArrayList<>();
        for(int i = 0; i < 22; i++){
            try{
                entries.add(new BarEntry((float)i, data.get(position).voltage_combined.get(i)));
            }
            catch(Exception e){
                System.out.println("NO DATA FOR SEGMENT " + i);
            }


        }

        try{
            TextView smallstatus= viewHolders.get(position).s;

            if(data.get(position).getErrorsAsListInSegment().size()>0)
            {
                smallstatus.setText("!!!");
                smallstatus.setBackground(ResourcesCompat.getDrawable(context.getResources(), R.drawable.error_warning_overview, null));
            }
            else if(data.get(position).cids.size()==0)
            {
                smallstatus.setText("n/a");
                smallstatus.setBackground(ResourcesCompat.getDrawable(context.getResources(), R.drawable.no_data_background, null));
            }
            else {
                smallstatus.setText("OK");
                smallstatus.setBackground(ResourcesCompat.getDrawable(context.getResources(), R.drawable.tv_segment_status_drawable, null));
            }

            TextView temp_max = viewHolders.get(position).temp_max;
            TextView temp_min = viewHolders.get(position).temp_min;
            DecimalFormat df = new DecimalFormat("#.##");
            df.setMinimumFractionDigits(2);
            String temp_max_text = context.getString(R.string.temp_max);
            String temp_min_text = context.getString(R.string.temp_min);
            temp_max.setText(temp_max_text + " " + df.format(Collections.max(data.get(position).temperature_combined)));
            temp_min.setText(temp_min_text + " " + df.format(Collections.min(data.get(position).temperature_combined)));


        }
        catch(Exception e){
            System.out.println("ERROR FOR SEGMENT (PROB SCREEN ROTATION) " + position);
        }



        //setting up properties of barchart
        ArrayList<Float> intervals = new ArrayList<Float>();
        intervals.add(3.1f);
        intervals.add(3.4f);
        intervals.add(3.8f);
        intervals.add(4.0f);
        intervals.add(4.1f);

        CustomBarDataset barDataSet = new CustomBarDataset(entries, "data", intervals);
        barDataSet.setColors(ContextCompat.getColor(context, R.color.VeryLowColor),
                             ContextCompat.getColor(context, R.color.LowColor),
                             ContextCompat.getColor(context, R.color.OKColor),
                             ContextCompat.getColor(context, R.color.WarningColor),
                             ContextCompat.getColor(context, R.color.AlertColor)
                            );
        barDataSet.setDrawValues(false);
        BarData data = new BarData(barDataSet);
        try{
            viewHolders.get(position).b.setData(data);
            viewHolders.get(position).b.notifyDataSetChanged();
            viewHolders.get(position).b.invalidate();

        }
        catch(Exception e){
            System.out.println("ERROR FOR SEGMENT (PROB SCREEN ROTATION) " + position);
        }


    }
    @Override
    public void onBindViewHolder(@NonNull RVSegmentAdapter.ViewHolder holder, int position) {

        viewHolders.add(holder);
        //holder.t.setText("Test" + data.get(position).voltage_combined.get(2));



        //creating mock data

        holder.b.setDrawGridBackground(false);
        Legend l = holder.b.getLegend();
        l.setEnabled(false);
        holder.b.setTouchEnabled(false);
        holder.b.setScaleEnabled(false);
        holder.b.setDragEnabled(false);
        holder.b.setPinchZoom(false);
        holder.b.getXAxis().setDrawGridLines(false);
        holder.b.getAxisLeft().setDrawGridLines(false);
        holder.b.getAxisRight().setDrawGridLines(false);
        holder.b.getAxisLeft().setEnabled(false);
        holder.b.getAxisRight().setEnabled(false);
        holder.b.getXAxis().setEnabled(false);
        holder.b.getDescription().setEnabled(false);
        holder.b.setBackgroundColor(ContextCompat.getColor(context, R.color.ElementColor));
        holder.b.setFocusableInTouchMode(false);
        holder.b.getAxisLeft().setAxisMaximum(4.3f);
        holder.b.getAxisLeft().setAxisMinimum(2.9f);
        updateDataSet(position);

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView t;
        BarChart b;
        TextView s;
        TextView temp_max;
        TextView temp_min;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            t = itemView.findViewById(R.id.tv_segmentID);
            b = itemView.findViewById(R.id.bc_segmentData);
            s = itemView.findViewById(R.id.tv_segmentStatus);
            temp_max = itemView.findViewById(R.id.tv_temp_max);
            temp_min = itemView.findViewById(R.id.tv_temp_min);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            int pos = getAdapterPosition();
            //checking if id is valid, calling click function on item pos
            if (pos != RecyclerView.NO_POSITION){
                listener.onItemClick(pos);
            }
        }
    }

    //interface for click event
    public interface RecyclerViewEvent{
        void onItemClick(int pos);
    }
}
