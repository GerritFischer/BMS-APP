package com.example.rubmotorsportgroup2;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import androidx.fragment.app.Fragment;

import com.example.rubmotorsportgroup2.databinding.ActivityMainBinding;
import com.example.rubmotorsportgroup2.datamanager.DataManager;
import com.example.rubmotorsportgroup2.fragments.fragment_overview;
import com.example.rubmotorsportgroup2.fragments.fragment_segments;
import com.example.rubmotorsportgroup2.fragments.fragment_settings;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    DataManager dm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        dm = new DataManager();

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        displayFragment(new fragment_overview());
        //check if Bundle is there and if it comes from EditUserID
        Bundle bundle= getIntent().getExtras();
        if (bundle!= null){ //Check if it there is a bundle
            if (bundle.getBoolean("settings")==true){ //Check if it was in EditUserID
                //will condition is fullfilled go to fragment_settings
                Bundle newBundle= new Bundle();
                Fragment frag= new fragment_settings();
                frag.setArguments(bundle);
                binding.bottomNavBar.setSelectedItemId(R.id.menu_settings);
                displayFragment(frag);
            }
        }

        binding.bottomNavBar.setOnItemSelectedListener(item -> {
            int itemID = item.getItemId();

            if (itemID == R.id.menu_overview){
                displayFragment(new fragment_overview());
            }
            else if (itemID == R.id.menu_segments){
                displayFragment(new fragment_segments());
            }
            else if (itemID == R.id.menu_settings){
                displayFragment(new fragment_settings());
            }
            return true;
        });



    }

    private void displayFragment(Fragment fragment){
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentView, fragment).commit();
    }
}