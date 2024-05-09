package com.example.rubmotorsportgroup2;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.rubmotorsportgroup2.fragments.fragment_overview;
import com.example.rubmotorsportgroup2.fragments.fragment_segments;
import com.example.rubmotorsportgroup2.fragments.fragment_settings;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.example.rubmotorsportgroup2.databinding.ActivityMainBinding;



public class MainActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityMainBinding binding;

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        displayFragment(new fragment_overview());

        Bundle bundle= getIntent().getExtras();
        if (bundle!= null){ //Check if it was in editUser
            Bundle newBundle= new Bundle();
            Fragment frag= new fragment_settings();
            frag.setArguments(bundle);
            binding.bottomNavBar.setSelectedItemId(R.id.menu_settings);
            displayFragment(frag);
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