package com.example.rubmotorsportgroup2.fragments;

import static com.example.rubmotorsportgroup2.MainActivity.dm;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.rubmotorsportgroup2.R;
import com.example.rubmotorsportgroup2.datamanager.DataManager;
import com.example.rubmotorsportgroup2.datamanager.SegmentData;

import java.util.ArrayList;

public class fragment_overview extends Fragment {

    private DataManager datam;

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
        super.onViewCreated(view, savedInstanceState);
        TextView t = (TextView) view.findViewById(R.id.FailPassTextView);
        t.setText(getNewData());
        ImageView refreshview = view.findViewById(R.id.RefreshDataView);
        refreshview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                t.setText(getNewData());
            }
        });
    }
    private String getNewData()//Get the data form the Data manager and formats them in the string used by the fail pass view
    {
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
                if (t == segtemp.size()/2)
                    continue;
                if(i==0)
                    System.out.println(segtemp.get(t));
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


        String ret = "Average Voltage: "+ avgvoltage+"V\n"
                + "Average Temperature: "+ avgtemp+"C°\n"
                + "Cell Balancing: "+"sec\n"
                +"Status: " + ":)";
        return ret;

    }

}