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
        if (!isValidEmail()){
            Toast.makeText(this,"Invalid E-Mail",Toast.LENGTH_SHORT).show();
            return;
        }
        if(!isValidPassword()){
            Toast.makeText(this,"Invalid Password",Toast.LENGTH_SHORT).show();
            return;
        }
        Intent intent=new Intent(this, MainActivity.class );
       // Toast.makeText(this,"Hey",Toast.LENGTH_SHORT).show();
        Bundle bundle= new Bundle();

        TextView email=(TextView)findViewById(R.id.enterEmail);
        bundle.putString("Email",email.getText().toString());

        TextView password =(TextView)findViewById(R.id.enterPassword);
        bundle.putString("Password",password.getText().toString());

        intent.putExtras(bundle);
        startActivity(intent);
    }
    public boolean isValidEmail() {
        TextView email = (TextView) findViewById(R.id.enterEmail);
        if(email.getText().toString().isEmpty())
        {
            return false;
        }
        return true;
    }
    public boolean isValidPassword(){
        TextView password= (TextView) findViewById(R.id.enterPassword);
        TextView passwordAgain= (TextView) findViewById(R.id.enterPassAgain);
        if(password.getText().toString().isEmpty() ||passwordAgain.getText().toString().isEmpty())
        {
            return false;
        }
        if (password.getText().toString().equals(passwordAgain.getText().toString())){
            return true;
        }
        return false;
    }
}