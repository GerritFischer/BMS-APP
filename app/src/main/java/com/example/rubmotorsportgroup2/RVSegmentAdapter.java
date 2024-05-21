package com.example.rubmotorsportgroup2;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;


import androidx.annotation.NonNull;

import androidx.recyclerview.widget.RecyclerView;


import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;

public class RVSegmentAdapter extends RecyclerView.Adapter<RVSegmentAdapter.ViewHolder> {

    Context context;
    private RecyclerViewEvent listener;
    private ArrayList<String> data;

    public RVSegmentAdapter(Context context, ArrayList<String> data, RecyclerViewEvent listener) {
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

    @Override
    public void onBindViewHolder(@NonNull RVSegmentAdapter.ViewHolder holder, int position) {


        holder.t.setText(data.get(position));



        //creating mock data
        ArrayList<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(0f, (float) Math.random() * 3));
        entries.add(new BarEntry(1f, (float) Math.random() * 3));
        entries.add(new BarEntry(2f, (float) Math.random() * 3));
        entries.add(new BarEntry(3f, (float) Math.random() * 3));
        entries.add(new BarEntry(4f, (float) Math.random() * 3));
        entries.add(new BarEntry(5f, (float) Math.random() * 3));
        entries.add(new BarEntry(6f, (float) Math.random() * 3));
        entries.add(new BarEntry(7f, (float) Math.random() * 3));
        entries.add(new BarEntry(8f, (float) Math.random() * 3));
        entries.add(new BarEntry(9f, (float) Math.random() * 3));
        entries.add(new BarEntry(10f, (float) Math.random() * 3));
        entries.add(new BarEntry(11f, (float) Math.random() * 3));
        entries.add(new BarEntry(12f, (float) Math.random() * 3));
        entries.add(new BarEntry(13f, (float) Math.random() * 3));
        entries.add(new BarEntry(14f, (float) Math.random() * 3));
        entries.add(new BarEntry(15f, (float) Math.random() * 3));
        entries.add(new BarEntry(16f, (float) Math.random() * 3));
        entries.add(new BarEntry(17f, (float) Math.random() * 3));
        entries.add(new BarEntry(18f, (float) Math.random() * 3));
        entries.add(new BarEntry(19f, (float) Math.random() * 3));
        entries.add(new BarEntry(20f, (float) Math.random() * 3));
        entries.add(new BarEntry(21f, (float) Math.random() * 3));

        //setting up properties of barchart
        BarDataSet barDataSet = new BarDataSet(entries, "data");
        barDataSet.setColor(Color.GREEN);
        barDataSet.setDrawValues(false);
        BarData data = new BarData(barDataSet);
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
        holder.b.setBackgroundColor(Color.GRAY);
        holder.b.setFocusableInTouchMode(false);
        holder.b.getAxisLeft().setAxisMaximum(5);
        holder.b.getAxisLeft().setAxisMinimum(0);

        holder.b.setData(data);




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
