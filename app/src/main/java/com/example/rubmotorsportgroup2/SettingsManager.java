package com.example.rubmotorsportgroup2;

import android.content.Context;
import android.widget.Toast;

import java.util.ArrayList;


public class SettingsManager {
    private ArrayList<String> mail;
    private boolean darkmode;
    private boolean sentrymode;
    private boolean tabletmode;
    private String password;
    public void loadSettings(Context context){
        JsonConfig json= new JsonConfig(context);
        //default Set Up if no JSON is found
        if(json.isEmpty()){
            darkmode= true;
            sentrymode=true;
            tabletmode=false;
            password="";
            mail=new ArrayList<>();
            return;
        }
        //if settings found stores the right
        if(json.hasSettings()){
            darkmode= json.isDarkmode();
            sentrymode=json.isSentrymode();
            tabletmode=json.isTabletmode();
        }
        else{
            //no Settings found so default settings
            darkmode= true;
            sentrymode=true;
            tabletmode=false;
        }
        if(json.hasPassword()){
            //if Password is stored get password from JSON
            password=json.getPassword();
        }
        else{
            //no password so empty
            password="";
        }
        if(json.hasEmailArray()){
            //get all the Email if JSON stores any Email
            mail=json.getMailArray();
        }
        else{
            //create a List to store Email since we got none
            mail=new ArrayList<>();
        }
    }
    public void saveSettings(Context context){
        JsonConfig json= new JsonConfig(context);
        if (json.isEmpty()){
            //if JSON not exist create one
            json.createJson();
        }
        //save the JSON with the current settings
        json.setPassword(password);
        json.setDarkmode(darkmode);
        json.setSentryMode(sentrymode);
        json.setTabletMode(tabletmode);
        json.setEmailList(mail);
    }

    public void setSentryMode(boolean isSentry){
        sentrymode=isSentry;
    }
    public boolean isSentryMode(){
        return sentrymode;
    }
    public void setDarkMode(boolean isDark){
        darkmode=isDark;
    }
    public boolean isDarkMode(){
        return darkmode;
    }
    public void setTabletMode(boolean isTablet){
        tabletmode=isTablet;
    }
    public boolean isTabletMode(){
        return tabletmode;
    }

    public void setPassword(String pass){
        password=pass;
    }
    public String getPassword(){
        return password;
    }

    private boolean mailExist(String email){
        for (int i =0; i<mail.size();i++){
            //iterates over the Arraylist and check if it is already inside the Arraylist
            if(email.equals(mail.get(i))){
                return true;
            }
        }
        return false;
    }

    public void addMail(String email){
        if (!mailExist(email)){
            mail.add(email);
        }
    }

    public void removeMail(String email){
        if(mailExist(email)){
            //if Mail exist get the index of the Arraylist and remove it
            for (int i=0; i<mail.size();i++){
                if (email.equals(mail.get(i))){
                    mail.remove(i);
                    return;
                }
            }
        }
    }
}
