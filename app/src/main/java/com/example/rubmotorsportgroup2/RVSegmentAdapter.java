package com.example.rubmotorsportgroup2;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import androidx.annotation.NonNull;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;


import com.example.rubmotorsportgroup2.datamanager.SegmentData;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;

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
        viewHolders.get(position).t.setText("Segment " + position);
        ArrayList<BarEntry> entries = new ArrayList<>();
        for(int i = 0; i < 22; i++){
            entries.add(new BarEntry((float)i, data.get(position).voltage_combined.get(i)));
        }


        //setting up properties of barchart
        BarDataSet barDataSet = new BarDataSet(entries, "data");
        barDataSet.setColor(ContextCompat.getColor(context, R.color.RubColorBlue));
        barDataSet.setDrawValues(false);
        BarData data = new BarData(barDataSet);
        viewHolders.get(position).b.setData(data);
        viewHolders.get(position).b.notifyDataSetChanged();
        viewHolders.get(position).b.invalidate();
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
        holder.b.getAxisLeft().setAxisMaximum(3.73f);
        holder.b.getAxisLeft().setAxisMinimum(3.72f);
        updateDataSet(position);

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView t;
        BarChart b;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            t = itemView.findViewById(R.id.tv_segmentID);
            b = itemView.findViewById(R.id.bc_segmentData);
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
