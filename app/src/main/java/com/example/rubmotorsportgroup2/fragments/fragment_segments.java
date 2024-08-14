package com.example.rubmotorsportgroup2.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.rubmotorsportgroup2.R;
import com.example.rubmotorsportgroup2.segmentview.RVSegmentAdapter;
import com.example.rubmotorsportgroup2.segmentview.SegmentActivity;
import com.example.rubmotorsportgroup2.datamanager.DataManager;
import com.example.rubmotorsportgroup2.datamanager.SegmentData;
import com.example.rubmotorsportgroup2.settingsmanager.SettingsManager;

import java.util.ArrayList;

public class fragment_segments extends Fragment implements RVSegmentAdapter.RecyclerViewEvent {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private final int tickTime = 1000;

    private String mParam1;
    private String mParam2;
    private UpdaterX u;
    DataManager dm;
    SettingsManager sm;
    boolean running = true;
    private RVSegmentAdapter a;
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
                            for(int i = 0; i < dm.getSegmentCount(); i++){
                                testData.set(i, dm.getSegmentData(i));
                                a.updateDataSet(i);
                            }
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
    public fragment_segments(DataManager dm, SettingsManager sm) {
        this.dm = dm;
        this.sm = sm;
    }
    public static fragment_segments newInstance(String param1, String param2) {
        fragment_segments fragment = new fragment_segments(null, null);
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }




    private ArrayList<SegmentData> testData = new ArrayList<SegmentData>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_segments, container, false);
        u = new UpdaterX();
        u.start();
        //Generating test data in form of Strings, later changed to objects for each segment
        //TODO: Create class for segment data
        int TEST_DATA_COUNT = 12;

        for (int i = 0; i < dm.getSegmentCount(); i++){
            testData.add(dm.getSegmentData(i));
        }

        //setting up adapter and assigning it to the RecyclerView
        RecyclerView r = (RecyclerView) view.findViewById(R.id.recyclerview_segments);
        a = new RVSegmentAdapter(this.getContext(), testData, this);
        r.setLayoutManager(new GridLayoutManager(this.getContext(), sm.getColumn()));
        r.setHasFixedSize(true);
        r.setAdapter(a);


        return view;
    }

    @Override
    public void onItemClick(int pos) {
        //starting new mock activity when clicking on an item
        Intent segmentActivity = new Intent(getActivity(), SegmentActivity.class);
        segmentActivity.putExtra("pos", pos);
        segmentActivity.putExtra("barColors", new int[]{
                        ContextCompat.getColor(this.getContext(), R.color.VeryLowColor),
                        ContextCompat.getColor(this.getContext(), R.color.LowColor),
                        ContextCompat.getColor(this.getContext(), R.color.OKColor),
                        ContextCompat.getColor(this.getContext(), R.color.WarningColor),
                        ContextCompat.getColor(this.getContext(), R.color.AlertColor)
                }
        );
        startActivity(segmentActivity);

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