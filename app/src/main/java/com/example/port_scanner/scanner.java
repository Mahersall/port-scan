package com.example.port_scanner;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class scanner extends AppCompatActivity
{
    EditText IP,Port;
    Button Scan,Back,Save,Myscans,subnet;

    TextView results,t1;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);


        IP = findViewById(R.id.etIP);
        Port = findViewById(R.id.etPorts);
        Scan = findViewById(R.id.btnScan);
        Back = findViewById(R.id.btnBack);
        Save = findViewById(R.id.btnSave);
        Myscans = findViewById(R.id.btnMyScans);
        results = findViewById(R.id.etResults);
        subnet = findViewById(R.id.subnet);


        db = openOrCreateDatabase("credentials",MODE_PRIVATE,null);
        String query = "CREATE TABLE IF NOT EXISTS saves(id INTEGER PRIMARY KEY AUTOINCREMENT, email TEXT, content TEXT)";
        db.execSQL(query);
        Intent i = getIntent();
        String email2 = i.getStringExtra("EMAIL");

        Back.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                finish();
            }
        });
        Scan.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String ip1 = IP.getText().toString().trim();
                String port0 = Port.getText().toString().trim();


                if (!isValidIPAddress(ip1))
                {
                    Toast.makeText(getApplicationContext(), "Invalid IP address", Toast.LENGTH_SHORT).show();
                    return;
                }


                if (!isValidPort(port0)) {
                    Toast.makeText(getApplicationContext(), "Invalid port number", Toast.LENGTH_SHORT).show();
                    return;
                }

                int port1 = Integer.parseInt(port0);

                PortScannerTask a = new PortScannerTask(ip1, port1);
                results.setText(a.scanPort());
            }
        });
        Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = results.getText().toString().trim();
                String ipAddress = IP.getText().toString().trim();

                if (content.isEmpty() || ipAddress.isEmpty()) {
                    Toast.makeText(scanner.this, "Content or IP cannot be empty", Toast.LENGTH_SHORT).show();
                    return;
                }

                try {
                    db = openOrCreateDatabase("credentials", MODE_PRIVATE, null);

                    String saveContent = ipAddress + " : " + content;

                    String query = "INSERT INTO saves(email, content) VALUES ('" + email2 + "','" + saveContent + "')";
                    db.execSQL(query);

                    Toast.makeText(scanner.this, "Scan saved", Toast.LENGTH_SHORT).show();
                }
                catch (Exception e)
                {
                    Toast.makeText(scanner.this, "Something went wrong. Please try again.", Toast.LENGTH_SHORT).show();
                    e.printStackTrace(); // Print the exception for debugging purposes
                } finally {
                    if (db != null) {
                        db.close();
                    }
                }
            }
        });
        Myscans.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                try
                {
                    List<String> savedContent = getAllSavedContent(scanner.this);

                    Intent i = new Intent(scanner.this, Saved_scans.class);
                    i.putStringArrayListExtra("stringList", (ArrayList<String>) savedContent);
                    startActivity(i);
                } catch (Exception e) {
                    // Display a toast if an error occurs
                    Toast.makeText(scanner.this, "Something went wrong. Please try again.", Toast.LENGTH_SHORT).show();
                    e.printStackTrace(); // Print the exception for debugging purposes
                }
            }
        });

        subnet.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                Intent j = new Intent(scanner.this,Subnet_activity.class);
                startActivity(j);

            }
        });


    }

    private List<String> getAllSavedContent(Context context)
    {
        Intent i = getIntent();
        String email2 = i.getStringExtra("EMAIL");


        List<String> contentList = new ArrayList<>();
        String query = "SELECT * FROM saves WHERE email = '" + email2 + "'";
        Cursor cursor = db.rawQuery(query,null);


        if (cursor != null)
        {
            while (cursor.moveToNext())
            {
                int index = cursor.getColumnIndex("content");
                contentList.add(cursor.getString(index));
            }
            cursor.close();
        }
        db.close();
        return contentList;
    }
    private StringBuilder printContent(List<String> contentList)
    {
        StringBuilder stringBuilder = new StringBuilder();
        for (String content : contentList) {
            stringBuilder.append(content).append("\n");
        }
        return stringBuilder;
    }
    private boolean isValidIPAddress(String ipAddress)
    {
        // Regular expression for validating IP address
        String ipPattern = "^((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$";
        return ipAddress.matches(ipPattern);
    }
    private boolean isValidPort(String port)
    {
        try {
            int portNum = Integer.parseInt(port);
            return portNum >= 0 && portNum <= 65535; // Port numbers range from 0 to 65535
        } catch (NumberFormatException e) {
            return false; // If port cannot be parsed to integer, it's not a valid port
        }
    }

}