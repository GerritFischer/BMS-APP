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
    }
}