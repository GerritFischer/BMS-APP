package com.example.rubmotorsportgroup2.fragments;

import static android.widget.Toast.makeText;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.rubmotorsportgroup2.MainActivity;
import com.example.rubmotorsportgroup2.R;
import com.example.rubmotorsportgroup2.RVSegmentAdapter;
import com.example.rubmotorsportgroup2.SegmentActivity;

import java.util.ArrayList;

public class fragment_segments extends Fragment implements RVSegmentAdapter.RecyclerViewEvent {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public fragment_segments() {
        // Required empty public constructor
    }
    public static fragment_segments newInstance(String param1, String param2) {
        fragment_segments fragment = new fragment_segments();
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

    private ArrayList<String> testData = new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_segments, container, false);

        //Generating test data in form of Strings, later changed to objects for each segment
        //TODO: Create class for segment data
        int TEST_DATA_COUNT = 12;
        for (int i = 0; i < 12; i++){
            testData.add("TestData " + i);

        }

        RecyclerView r = (RecyclerView) view.findViewById(R.id.recyclerview_segments);
        RVSegmentAdapter a = new RVSegmentAdapter(this.getContext(), testData, this);
        r.setLayoutManager(new GridLayoutManager(this.getContext(), 2));
        r.setHasFixedSize(true);
        r.setAdapter(a);


        return view;
    }

    @Override
    public void onItemClick(int pos) {
        Toast.makeText(this.getContext(), (CharSequence) testData.get(pos) , Toast.LENGTH_SHORT).show();
        Intent segmentActivity = new Intent(getActivity(), SegmentActivity.class);
        startActivity(segmentActivity);

    }
}