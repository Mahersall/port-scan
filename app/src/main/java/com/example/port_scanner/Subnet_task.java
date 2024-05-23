package com.example.port_scanner;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Subnet_task extends AppCompatActivity
{

    Button back3, subnet;
    EditText IP2;
    TextView t1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subnet);

        back3 = findViewById(R.id.back3);
        subnet = findViewById(R.id.subnet2);
        IP2 = findViewById(R.id.IP2);
        t1 = findViewById(R.id.tvSubnet);
        t1.setMovementMethod(new ScrollingMovementMethod());

        back3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        subnet.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String ipAddress = IP2.getText().toString().trim();
                if (isValidIPAddress(ipAddress)) {
                    String subnet = calculateSubnet(ipAddress, "255.255.255.0");
                    t1.setText("Subnet: " + subnet);
                } else {
                    t1.setText("Invalid IP Address");
                }
            }
        });
    }

    private boolean isValidIPAddress(String ip) {
        // Simple regex to check if the IP address is valid
        return ip.matches("^([0-9]{1,3}\\.){3}[0-9]{1,3}$");
    }

    private String calculateSubnet(String ipAddress, String subnetMask)
    {
        String[] ipOctets = ipAddress.split("\\.");
        String[] maskOctets = subnetMask.split("\\.");

        int[] subnetOctets = new int[4];
        for (int i = 0; i < 4; i++) {
            subnetOctets[i] = Integer.parseInt(ipOctets[i]) & Integer.parseInt(maskOctets[i]);
        }

        return subnetOctets[0] + "." + subnetOctets[1] + "." + subnetOctets[2] + "." + subnetOctets[3];
    }
}