package com.example.rubmotorsportgroup2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class EditUserID extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_id);

    }
    public void submitbtn(View view){
        //makes sure that Password is valid
        if(!isValidPassword()){
            Toast.makeText(this,"Invalid Password",Toast.LENGTH_SHORT).show();
            return;
        }
        // try to get to fragments_settings
        Intent intent=new Intent(this, MainActivity.class );

        // creates Bundle and store some parameters there
        Bundle bundle= new Bundle();

        // get Input
        TextView password =(TextView)findViewById(R.id.editUser_enterPassword);
        TextView mail= (TextView) findViewById(R.id.editUser_enterEmail);

        // create JSON
        SettingsManager sm= new SettingsManager();
        sm.loadSettings(this);
        sm.setPassword(password.getText().toString());
        sm.addMail(mail.getText().toString());
        //test
        TextView  test =(TextView)findViewById(R.id.editUser_enterPassAgain);
        String test1 = test.getText().toString();
        sm.removeMail(test1);


        // we were at settings
        bundle.putBoolean("settings",true);

        // put stores Bundle in Intent and starts activity
        intent.putExtras(bundle);
        sm.saveSettings(this);
        startActivity(intent);
    }
    public boolean isValidPassword(){
        TextView password= (TextView) findViewById(R.id.editUser_enterPassword);

        if(password.getText().toString().trim().isEmpty()) //check empty password
        {
            return false;
        }
        int minLength = 4;

        // Check Password Criteria
        if (password.length() < minLength) {
            return false;
        }

        return true;
    }
    //goes Back to fragment settings (Back button)
    public void finishBtn(View view)
    {
        JsonConfig jsonConfig = new JsonConfig(this);
        jsonConfig.deleteJson();
        finish();
    }
}
