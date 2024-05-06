package com.example.rubmotorsportgroup2;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Adapter;

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

        ArrayList<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(0f, 2f));
        entries.add(new BarEntry(1f, 2f));
        entries.add(new BarEntry(2f, 20f));
        entries.add(new BarEntry(3f, 10f));
        entries.add(new BarEntry(4f, 2f));
        entries.add(new BarEntry(5f, 2f));
        entries.add(new BarEntry(6f, 20f));
        entries.add(new BarEntry(7f, 10f));
        entries.add(new BarEntry(8f, 2f));
        entries.add(new BarEntry(9f, 2f));
        entries.add(new BarEntry(10f, 20f));
        entries.add(new BarEntry(11f, 10f));
        entries.add(new BarEntry(12f, 2f));
        entries.add(new BarEntry(13f, 2f));
        entries.add(new BarEntry(14f, 20f));
        entries.add(new BarEntry(15f, 10f));
        entries.add(new BarEntry(16f, 2f));
        entries.add(new BarEntry(17f, 2f));
        entries.add(new BarEntry(18f, 20f));
        entries.add(new BarEntry(19f, 10f));
        entries.add(new BarEntry(20f, 2f));
        entries.add(new BarEntry(21f, 10f));


        BarDataSet barDataSet = new BarDataSet(entries, "Hey");
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
        //holder.b.setDrawValueAboveBar(false);
        //holder.b.getBarData().setBarWidth((float) holder.b.getWidth() / 22);
        holder.b.setBackgroundColor(Color.GRAY);
        holder.b.setFocusableInTouchMode(false);

        holder.b.setData(data);




    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView t;
        CardView c;
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
            if (pos != RecyclerView.NO_POSITION){
                listener.onItemClick(pos);
            }
        }
    }

    public interface RecyclerViewEvent{
        void onItemClick(int pos);
    }
}
