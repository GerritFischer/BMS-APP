package com.example.rubmotorsportgroup2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.util.Patterns;
import android.widget.Toast;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.rubmotorsportgroup2.fragments.fragment_settings;

import org.w3c.dom.Text;

import java.util.regex.Pattern;

public class EditUserID extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_id);

    }
    public void submitbtn(View view){
        // makes sure that Email is valid
        if (!isValidEmail()){
            Toast.makeText(this,"Invalid E-Mail",Toast.LENGTH_SHORT).show();
            return;
        }
        //makes sure that Password is valid
        if(!isValidPassword()){
            Toast.makeText(this,"Invalid Password",Toast.LENGTH_SHORT).show();
            return;
        }
        // try to get to fragments_settings
        Intent intent=new Intent(this, MainActivity.class );
       // Toast.makeText(this,"Hey",Toast.LENGTH_SHORT).show();
        // creates Bundle and store some parameters there
        Bundle bundle= new Bundle();

        TextView email=(TextView)findViewById(R.id.editUser_enterEmail);
        bundle.putString("Email",email.getText().toString());

        TextView password =(TextView)findViewById(R.id.editUser_enterPassword);
        bundle.putString("Password",password.getText().toString());

        bundle.putBoolean("settings",true);
        // put stores Bundle in Intent and starts activity
        intent.putExtras(bundle);
        startActivity(intent);
    }
    public boolean isValidEmail() {
        TextView email = (TextView) findViewById(R.id.editUser_enterEmail);
        if(email.getText().toString().trim().isEmpty())
        {
            return false;
        }
        return true;
    }
    public boolean isValidPassword(){
        TextView password= (TextView) findViewById(R.id.editUser_enterPassword);
        TextView passwordAgain= (TextView) findViewById(R.id.editUser_enterPassAgain);
        if(password.getText().toString().trim().isEmpty() ||passwordAgain.getText().toString().trim().isEmpty())
        {
            return false;
        }
        if (password.getText().toString().trim().equals(passwordAgain.getText().toString().trim())){
            return true;
        }
        return false;
    }
    //goes Back to fragment settings (Back button)
    public void finishBtn(View view)
    {
        finish();
    }
}