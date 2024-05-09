package com.example.rubmotorsportgroup2.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.rubmotorsportgroup2.EditUserID;
import com.example.rubmotorsportgroup2.R;

public class fragment_settings extends Fragment{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    public fragment_settings(){
        // Required empty public constructor
    }
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment fragment_details.
     */
    // TODO: Rename and change types and number of parameters
    public static fragment_settings newInstance(String param1, String param2) {
        fragment_settings fragment = new fragment_settings();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View inflate=inflater.inflate(R.layout.fragment_settings, container, false);
        Button btn= inflate.findViewById(R.id.editBtn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(getActivity(), EditUserID.class);
                startActivity(intent);
            }
        });
        Bundle bundle=getArguments();
        if(bundle!=null) {
            String getemail = bundle.getString("Email");
            String getpassword = bundle.getString("Password");

            TextView username =inflate.findViewById(R.id.emailView);
            TextView password= inflate.findViewById(R.id.passwordView);

            username.setText("");
            username.setText(getemail);
            StringBuilder hiddenpass= new StringBuilder();
            for(int i=0;i<getpassword.length();i++)
            {
                hiddenpass.append("*");
            }
            password.setText(hiddenpass);
        }

        // Inflate the layout for this fragment
        return inflate;
    }


}
