package com.example.rubmotorsportgroup2;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.rubmotorsportgroup2.databinding.ActivityMainBinding;
import com.example.rubmotorsportgroup2.fragments.fragment_overview;
import com.example.rubmotorsportgroup2.fragments.fragment_segments;
import com.example.rubmotorsportgroup2.fragments.fragment_settings;

public class SegmentActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.segment_activity);



    }

    public void onClick(View view){
        finish();
    }
}
