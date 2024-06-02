package com.example.rubmotorsportgroup2;
import android.content.Context;


import org.json.JSONException;
import org.json.JSONObject;



import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;



public class Json {
    private String username;
    private String password;
    private Context context;
    public Json(Context context)
    {
        this.context=context;
    }
    public Json(String username, String password, Context context)
    {
        this.username=username;
        this.password=password;
        this.context=context;
    }
    public void createJson()  {
        //create JSON Object with keys and value
        JSONObject json= new JSONObject();
        try{
            json.put("Username",this.username);
            json.put("Password",this.password);
        }
        catch(Exception e){
            e.printStackTrace();
        }
        saveJson(json.toString());
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
            e.printStackTrace();
        }
    }
    private String readJson() {
        //create String output
        StringBuilder out = new StringBuilder();
        try {
            // try to get the file
            File file = new File(context.getFilesDir(), "credentials.json");
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
            e.printStackTrace();
        }
        // return Json String not the same as usual String
        return out.toString();
    }
    public String getPassword(){
        String jsonOutput = readJson();
        String password = null;
        try {
            // try if inside the JSON data is Password
            JSONObject jsonObject = new JSONObject(jsonOutput);
            password = jsonObject.getString("Password");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return password;
    }
    public String getUsername(){
        String jsonOutput = readJson();
        String username = null;
        try {
            //try if inside the JSON data is Username
            JSONObject jsonObject = new JSONObject(jsonOutput);
            username = jsonObject.getString("Username");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return username;
    }
    public boolean isEmpty(){
        File file = new File(context.getFilesDir(),"credentials.json");
        if(file.exists()){
            if (file.length()!=0){
                return false;
            }
        }
        return true;
    }

    public void deleteJson(){
        File file=new File(context.getFilesDir(), "credentials.json");
        try{
            file.delete();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
