package com.example.port_scanner;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class Subnet_activity extends AppCompatActivity
{

    Button back3, subnet;
    EditText IP2, minHostsEditText;
    TextView t1;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subnet);

        back3 = findViewById(R.id.back3);
        subnet = findViewById(R.id.subnet2);
        IP2 = findViewById(R.id.IP2);
        minHostsEditText = findViewById(R.id.Min_subnets);
        t1 = findViewById(R.id.tvSubnet);

        back3.setOnClickListener(new View.OnClickListener()
        {
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
                String minHostsStr = minHostsEditText.getText().toString().trim();

                if (isValidIPAddress(ipAddress) && isValidHostsCount(minHostsStr))
                {
                    int minHosts = Integer.parseInt(minHostsStr);
                    SubnetInfo subnetInfo = calculateSubnetInfo(ipAddress, minHosts);
                    if (subnetInfo != null)
                    {
                        StringBuilder result = new StringBuilder();
                        result.append("Subnet Mask: ").append(subnetInfo.subnetMask).append("\n")
                                .append("Increment: ").append(subnetInfo.increment).append("\n")
                                .append("Subnets:\n");
                        for (String subnet : subnetInfo.subnetAddresses)
                        {
                            result.append(subnet).append("\n");
                        }
                        t1.setText(result.toString());
                    }
                    else
                    {
                        t1.setText("Invalid input or insufficient IP addresses for the given hosts count.");
                    }
                } else {
                    t1.setText("Invalid IP Address or hosts count");
                }
            }
        });
    }

    private boolean isValidIPAddress(String ip)
    {
        return ip.matches("^([0-9]{1,3}\\.){3}[0-9]{1,3}$") && isValidIPRange(ip);
    }

    private boolean isValidIPRange(String ip)
    {
        String[] parts = ip.split("\\.");
        for (String part : parts) {
            int i = Integer.parseInt(part);
            if (i < 0 || i > 255) return false;
        }
        return true;
    }

    private boolean isValidHostsCount(String count) {
        return count.matches("\\d+") && Integer.parseInt(count) > 0;
    }

    private SubnetInfo calculateSubnetInfo(String ipAddress, int minHosts)
    {
        int subnetBits = 0;
        int totalHosts = 0;
        while (totalHosts < minHosts + 2) { // +2 for network and broadcast addresses
            subnetBits++;
            totalHosts = (int) Math.pow(2, subnetBits);
        }

        int subnetMask = 32 - subnetBits;
        int increment = totalHosts;
        List<String> subnetAddresses = new ArrayList<>();

        try {
            String[] octets = ipAddress.split("\\.");
            int baseIP = (Integer.parseInt(octets[0]) << 24) | (Integer.parseInt(octets[1]) << 16) |
                    (Integer.parseInt(octets[2]) << 8) | Integer.parseInt(octets[3]);

            for (int i = 0; i < (256 / increment); i++) {
                int subnet = baseIP + (i * increment);
                subnetAddresses.add(((subnet >> 24) & 0xFF) + "." +
                        ((subnet >> 16) & 0xFF) + "." +
                        ((subnet >> 8) & 0xFF) + "." +
                        (subnet & 0xFF));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return new SubnetInfo(subnetMask, increment, subnetAddresses);
    }

    private static class SubnetInfo
    {
        int subnetMask;
        int increment;
        List<String> subnetAddresses;

        SubnetInfo(int subnetMask, int increment, List<String> subnetAddresses)
        {
            this.subnetMask = subnetMask;
            this.increment = increment;
            this.subnetAddresses = subnetAddresses;
        }
    }
}