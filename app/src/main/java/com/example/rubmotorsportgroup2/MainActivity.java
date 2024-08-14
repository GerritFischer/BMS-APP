package com.example.rubmotorsportgroup2;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.example.rubmotorsportgroup2.databinding.ActivityMainBinding;
import com.example.rubmotorsportgroup2.datamanager.DataManager;
import com.example.rubmotorsportgroup2.fragments.fragment_overview;
import com.example.rubmotorsportgroup2.fragments.fragment_segments;
import com.example.rubmotorsportgroup2.fragments.fragment_settings;
import com.example.rubmotorsportgroup2.settingsmanager.JsonConfig;
import com.example.rubmotorsportgroup2.settingsmanager.SettingsManager;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    public static SettingsManager sm;


    public static DataManager dm;

    private boolean isLogged;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        dm = new DataManager();
        sm=new SettingsManager();
        sm.loadSettings(this);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        displayFragment(new fragment_overview());
        binding.bottomNavBar.setOnItemSelectedListener(item -> {
            int itemID = item.getItemId();
            isLogged=!sm.isPassPro();

            if (itemID == R.id.menu_overview){
                if(isLogged) {
                    displayFragment(new fragment_overview());
                    return true;
                }
                else{
                    displayPopUp();
                    return false;
                }
            }
            else if (itemID == R.id.menu_segments){
                if(isLogged){
                    displayFragment(new fragment_segments(dm, sm));
                    return true;
                }
                else{
                    displayPopUp();
                    return false;
                }
            }
            else if (itemID == R.id.menu_settings){
                if(isLogged){
                    displayFragment(new fragment_settings());
                    return true;
                }
                else{
                    displayPopUp();
                    return false;
                }
            }
            return false;
        });
    }
    private void displayFragment(Fragment fragment){
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentView, fragment).commit();
    }
    private void displayPopUp(){
        ConstraintLayout pop_up_wind=findViewById(R.id.pop_up_window);
        View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.pop_up_window,pop_up_wind,false);

        TextView cancel = view.findViewById(R.id.pop_up_cancelInput);
        TextView confirm = view.findViewById(R.id.pop_up_confirmInput);

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setView(view);
        final AlertDialog alertDialog= builder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                Toast.makeText(MainActivity.this, "Canceled",Toast.LENGTH_SHORT).show();
            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView password= view.findViewById(R.id.pop_up_passwordInput);
                String pass= password.getText().toString();
                if(pass.equals(sm.getPassword())){
                    Toast.makeText(MainActivity.this, "Access",Toast.LENGTH_SHORT).show();
                    isLogged=true;
                    sm.setPasswordPro(false);
                    sm.saveSettings(MainActivity.this);
                    alertDialog.dismiss();
                }
                else{
                    Toast.makeText(MainActivity.this,"Wrong Password",Toast.LENGTH_SHORT).show();
                }
            }
        });
        alertDialog.show();
    }

}