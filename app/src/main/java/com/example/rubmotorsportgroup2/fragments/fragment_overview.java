package com.example.rubmotorsportgroup2.fragments;

import static com.example.rubmotorsportgroup2.MainActivity.dm;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.rubmotorsportgroup2.R;
import com.example.rubmotorsportgroup2.datamanager.DataManager;

public class fragment_overview extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private DataManager datam;

    public fragment_overview() {

        //this.dm = dm;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_overview, container, false);
    }
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView t = (TextView) view.findViewById(R.id.FailPassTextView);
        t.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String testvolt = "";
                 for(int k = 0; k < dm.CELL_COUNT; k++)
                 {
                     testvolt = testvolt + dm.getSegmentVoltage(1).get(2*k)+" V   "+ dm.getSegmentVoltage(1).get(2*k+1)+ " V\n";
                 }
                t.setText(testvolt);
            }
        });

        ImageView v = view.findViewById(R.id.imageView2);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                String testtemp = "";
                for(int k = 0; k < dm.getSegmentTemperature(1).size(); k++)
                {
                    testtemp = testtemp + dm.getSegmentTemperature(1).get(k)+ " C°\n";
                }
                t.setText(testtemp);
            }
        });
    }
}