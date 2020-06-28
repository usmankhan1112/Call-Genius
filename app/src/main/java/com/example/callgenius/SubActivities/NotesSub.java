package com.example.callgenius.SubActivities;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.callgenius.R;

public class NotesSub extends AppCompatActivity {

    private String Caller;
    private String Number;
    private int Status;
    private String Duration;
    private String Day;
    private String Time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_sub);

        Toolbar toolbar = findViewById(R.id.notes_toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(true);

        Bundle bundle = getIntent().getExtras();

        Caller = bundle.getString("Caller");
        Number = bundle.getString("Number");
        Status = bundle.getInt("Status");
        Duration = bundle.getString("Duration");
        Day = bundle.getString("Day");
        Time = bundle.getString("Time");

        TextView caller = findViewById(R.id.callername);
        TextView number = findViewById(R.id.number);
        TextView day = findViewById(R.id.day);
        TextView type = findViewById(R.id.type);
        TextView duration = findViewById(R.id.duration);
        TextView time = findViewById(R.id.time);

        ImageView img = findViewById(R.id.type_img);

        if(Caller != null){
            caller.setText(Caller);
        }
        else{
            caller.setText("Unsaved");
        }

        number.setText(Number);
        day.setText(Day);
        if(Status == 1){
            type.setText("Incoming");
            img.setImageResource(R.drawable.incoming);
        }
        else if(Status == 2){
            type.setText("Outgoing");
            img.setImageResource(R.drawable.outgoing);
        }
        else if(Status == 3){
            type.setText("Missed");
            img.setImageResource(R.drawable.missed);
        }

        else{
            type.setText("Unknown");
        }
        duration.setText("Duration: " + Duration);
        time.setText("Time: " + Time);
    }
}