package com.example.rubmotorsportgroup2;
import android.content.Context;
import android.widget.Toast;


import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONArray;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class JsonConfig {
    private String password;
    private Context context;
    public JsonConfig(Context context){this.context=context;}


    public void createJson()  {
        //create JSON Object with keys and value
        try{
            File file =new File(context.getFilesDir(),"credentials.json");
            JSONObject json= new JSONObject();
            FileWriter writer = new FileWriter(file);
            writer.write(json.toString());
            writer.close();
        }
        catch(Exception e){
            Toast.makeText(context,"Something went wrong createJson",Toast.LENGTH_SHORT).show();
        }

    }
    private void saveJson(String json) {
        //create credentials.json
        File file =new File(context.getFilesDir(),"credentials.json");
        try{
            // write in credentials.json
            FileWriter fileWriter= new FileWriter (file);
            fileWriter.write(json);
            fileWriter.close();

        }
        catch(Exception e){
            Toast.makeText(context,"Something went wrong saveJson", Toast.LENGTH_SHORT).show();
        }
    }
    public String readJson() {
        //create String output
        StringBuilder out = new StringBuilder();
        try {
            // try to get the file
            File file = new File(context.getFilesDir(), "credentials.json");
            if(!file.exists()){
                return"";
            }
            FileReader reader = new FileReader(file);
            // get ASCII therefore int
            int character;
            //stores output in character
            while ((character = reader.read()) != -1) {
                // translate from ASCII into char
                out.append((char)character);
            }
            reader.close();
        } catch (Exception e) {
            Toast.makeText(context,"Something went wrong readJson", Toast.LENGTH_SHORT).show();
        }
        // return Json String not the same as usual String
        return out.toString();
    }
    public String getPassword(){
        //decode Json
        String jsonOutput = readJson();
        String password = null;
        try {
            JSONObject jsonObject = new JSONObject(jsonOutput);
            if (jsonObject.has("Password")){
                //if you have password  get Password
                password = jsonObject.getString("Password");
                return password;
            }
            else{
                //when no password is stored return empty String
                return"";
            }
        } catch (JSONException e) {
            Toast.makeText(context,"Something went wrong getPassword", Toast.LENGTH_SHORT).show();
        }
        return password;
    }
    public boolean isEmpty(){
        File file = new File(context.getFilesDir(),"credentials.json");
        //Check if File exists
        if(file.exists()){
            //check if something is written there
            if (file.length()!=0){
                return false;
            }
        }
        return true;
    }
    public void deleteJson(){
        File file = new File(context.getFilesDir(), "credentials.json");
        if (file.exists()){
            //delete Json if File exist just for testing stuff
            file.delete();
        }
    }

    public void setPassword(String password){
        try{
            JSONObject json=new JSONObject(readJson());
            //set Password
            json.put("Password",password);
            saveJson(json.toString());
        }
        catch(Exception e){
            Toast.makeText(context,"Something went wrong setPassword",Toast.LENGTH_SHORT).show();
        }
    }


    public void setDarkmode(boolean isDarkmode){
        try{
            setSettingsObject();
            JSONObject json = new JSONObject(readJson());
            //get from JSON the JSONObject Settings
            JSONObject jsettings=  json.getJSONObject("Settings");
            //store the new darkmode key with the value
            jsettings.put("Darkmode",isDarkmode);
            // put the new JSON data inside our JSONObject again
            json.put("Settings",jsettings);
            saveJson(json.toString());
        }
        catch(Exception e){
            Toast.makeText(context, "Something went wrong setDarkmode", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean isDarkmode(){
        try{
            JSONObject json = new JSONObject(readJson());
            //get JSON settings object
            JSONObject jsettings=  json.getJSONObject("Settings");
            // get the value Darkmode and store in isDark
            boolean isDark = jsettings.getBoolean("Darkmode");
            return isDark;
        }
        catch(Exception e){
            Toast.makeText(context, "Something went wrong isDarkmode", Toast.LENGTH_SHORT).show();
            return false;
        }
    }
    public void setSentryMode(boolean isSentrymode){
        try{
            setSettingsObject();
            JSONObject json = new JSONObject(readJson());
            //get the JSONObject Settings
            JSONObject jsettings=  json.getJSONObject("Settings");
            //stores new value with Sentrymodekey
            jsettings.put("Sentrymode",isSentrymode);
            json.put("Settings",jsettings);
            saveJson(json.toString());
        }
        catch(Exception e){
            Toast.makeText(context, "Something went wrong setSentrymode", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean isSentrymode(){
        try{
            JSONObject json = new JSONObject(readJson());
            //get the JSONObject Settings
            JSONObject jsettings=  json.getJSONObject("Settings");
            //get the Sentrymode bool
            boolean isSentry = jsettings.getBoolean("Sentrymode");
            return isSentry;
        }
        catch(Exception e){
            Toast.makeText(context, "Something went wrong isSentrymode", Toast.LENGTH_SHORT).show();
            return false;
        }
    }
    public void setTabletMode(boolean isTabletmode){
        try{
            setSettingsObject();
            JSONObject json = new JSONObject(readJson());
            //get JSONObject Settings
            JSONObject jsettings=  json.getJSONObject("Settings");
            //stores new value inside from key Tabletmode
            jsettings.put("Tabletmode",isTabletmode);
            json.put("Settings",jsettings);
            saveJson(json.toString());
        }
        catch(Exception e){
            Toast.makeText(context, "Something went wrong setTabletmode", Toast.LENGTH_SHORT).show();
        }
    }
    public boolean isTabletmode(){
        try{
            JSONObject json = new JSONObject(readJson());
            // get JSONObject Settings
            JSONObject jsettings=  json.getJSONObject("Settings");
            //get Tabletmode bool
            boolean isTablet = jsettings.getBoolean("Tabletmode");
            return isTablet;
        }
        catch(Exception e){
            Toast.makeText(context, "Something went wrong isTabletmode", Toast.LENGTH_SHORT).show();
            return false;
        }
    }
    public boolean isPassPro(){
        try{
            JSONObject json = new JSONObject(readJson());
            // get JSONObject Settings
            JSONObject jsettings=  json.getJSONObject("Settings");
            //get Tabletmode bool
            boolean isPassPro = jsettings.getBoolean("PasswordProtection");
            return isPassPro;
        }
        catch(Exception e){
            Toast.makeText(context, "Something went wrong isPassPro", Toast.LENGTH_SHORT).show();
            return false;
        }
    }
    public void setPassPro(boolean isPassPro){
        try{
            setSettingsObject();
            JSONObject json = new JSONObject(readJson());
            //get JSONObject Settings
            JSONObject jsettings=  json.getJSONObject("Settings");
            //stores new value inside from key Tabletmode
            jsettings.put("PasswordProtection",isPassPro);
            json.put("Settings",jsettings);
            saveJson(json.toString());
        }
        catch(Exception e){
            Toast.makeText(context, "Something went wrong setPassPro", Toast.LENGTH_SHORT).show();
        }
    }
    public boolean hasSettings(){
        try{
            JSONObject json= new JSONObject(readJson());
            //check if inside the JSON is a JSONObject Settings
            return json.has("Settings");
        }
        catch (Exception e){
            Toast.makeText(context,"Something went wrong hasSettings",Toast.LENGTH_SHORT).show();
            return false;
        }

    }
    public boolean hasPassword(){
        try{
            JSONObject json= new JSONObject(readJson());
            //check if json has none Empty password
            if(json.has("Password")){
                return json.getString("Password").length() >= 4;
            }
            return false;
        }
        catch (Exception e){
            Toast.makeText(context,"Something went wrong hasSettings",Toast.LENGTH_SHORT).show();
            return false;
        }
    }
    public boolean hasEmailArray(){
        try{
            //Check if we got an JSONArray for Email
            JSONObject json= new JSONObject(readJson());
            return json.has("Mail");
        }
        catch (Exception e){
            Toast.makeText(context,"Something went wrong hasSettings",Toast.LENGTH_SHORT).show();
            return false;
        }
    }
    public ArrayList<String> getMailArray(){
        try{
            JSONObject json= new JSONObject(readJson());
            if(json.has("Mail")){
                JSONArray arr =json.getJSONArray("Mail");
                ArrayList<String> list = new ArrayList<>();
                //Iterate over all Elements of the JSONArray and stores them inside the Arraylist
                for (int i=0; i<arr.length();i++){
                    list.add(arr.getString(i));
                }
                return list;
            }
            //there is no JSONArray inside the JSON
            return new ArrayList<>();
        }
        catch (Exception e){
            Toast.makeText(context,"Something went wrong getMailArray",Toast.LENGTH_SHORT).show();
            return new ArrayList<>();
        }
    }
    public void setEmailList(ArrayList<String> mail){
        try{
            JSONObject json= new JSONObject(readJson());
            JSONArray arr = new JSONArray();
            for (int i =0;i< mail.size();i++){
                arr.put(mail.get(i));
            }
            //replace the old Array with a new one
            json.put("Mail",arr);
            saveJson(json.toString());
        }
        catch (Exception e){
            Toast.makeText(context,"Something went wrong setEmailArray",Toast.LENGTH_SHORT).show();
        }
    }
    public void setSettingsObject(){
        try{
            if(!hasSettings()){
                //if we don't got the key Settings we create one else is useless function
                JSONObject json= new JSONObject(readJson());
                json.put("Settings",new JSONObject());
                saveJson(json.toString());
            }

        }
        catch(Exception e){
            Toast.makeText(context,"Something went wrong setSettingsObject",Toast.LENGTH_SHORT).show();
        }
    }
}
