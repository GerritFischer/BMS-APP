package com.example.rubmotorsportgroup2;

import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.rubmotorsportgroup2.settingsmanager.SettingsManager;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class MailManager extends AppCompatActivity {
    private LinearLayout scrollview;
    private SettingsManager settings;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mail_manager);
        settings =MainActivity.sm;

        scrollview = findViewById(R.id.mailManager_MailList);

        for( String mail: settings.getMails()){
            addCard(mail);
        }
        Button addButton= findViewById(R.id.mailManager_addBtn);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView input= findViewById(R.id.mailManager_Input);
                String mail =input.getText().toString();
                if (!mail.trim().isEmpty()&& !settings.mailExist(mail.trim())){

                    input.setText("");
                    addCard(mail.trim());
                    settings.addMail(mail);
                    settings.saveSettings(MailManager.this);

                }
            }
        });
    }

    public void finish(View view){
        this.finish();
    }

    private void delete(CardView card, String mail){
        LinearLayout scrollview = findViewById(R.id.mailManager_MailList);
        scrollview.removeView(card);
        settings.removeMail(mail);
        settings.saveSettings(this);
    }

    private void addCard(String mail){
        CardView card =new CardView(this);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        layoutParams.setMargins(0,0,0,20);
        card.setLayoutParams(layoutParams);
        card.setRadius(5);
        int color = ContextCompat.getColor(this,R.color.ElementColor);
        card.setCardBackgroundColor(color);

        RelativeLayout relativeLayout = new RelativeLayout(this);
        RelativeLayout.LayoutParams relativeParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        relativeLayout.setLayoutParams(relativeParams);


        TextView text = new TextView(this);
        text.setText(mail);
        text.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);
        RelativeLayout.LayoutParams left = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        left.addRule(RelativeLayout.ALIGN_PARENT_START, RelativeLayout.TRUE);
        text.setLayoutParams(left);

        ImageButton deletebtn= new ImageButton(this);
        deletebtn.setImageResource(R.drawable.baseline_delete_24);
        int btncolor= ContextCompat.getColor(this,R.color.ErrorColor);
        deletebtn.setBackgroundColor(btncolor);
        deletebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                delete(card,mail);
            }
        });
        RelativeLayout.LayoutParams right=new RelativeLayout.LayoutParams(
                130,130);
        right.addRule(RelativeLayout.ALIGN_PARENT_END, RelativeLayout.TRUE);
        deletebtn.setLayoutParams(right);


        relativeLayout.addView(text);
        relativeLayout.addView(deletebtn);
        card.addView(relativeLayout);

        scrollview.addView(card);
    }
}