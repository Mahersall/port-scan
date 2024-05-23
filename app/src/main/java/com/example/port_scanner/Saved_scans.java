package com.example.port_scanner;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.List;

public class Saved_scans extends AppCompatActivity
{

        Button back;

        ListView List;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_scans);

        List<String> receivedList = getIntent().getStringArrayListExtra("stringList");
        if (receivedList != null)
        {
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.activity_listview, R.id.t1, receivedList);
            List = findViewById(R.id.listt);
            List.setAdapter(adapter);
        }

        back = findViewById(R.id.btnBackk);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}