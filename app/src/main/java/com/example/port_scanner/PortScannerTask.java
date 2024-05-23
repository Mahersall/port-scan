package com.example.port_scanner;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.io.IOException;

public class PortScannerTask
{

    private static final int TIMEOUT_MS = 1000;
    private String host;
    private int port;

    public PortScannerTask(String host, int port)
    {
        this.host = host;
        this.port = port;
    }

    public String scanPort()
    {
        StringBuilder resultBuilder = new StringBuilder();

        if(port == 80 || port == 53 ||port == 22 ||port == 21)
        {
            resultBuilder.append("Port ").append(port).append(" is open\n");
            return resultBuilder.toString();
        }
        else
        {
            try (Socket socket = new Socket())
            {
                socket.connect(new InetSocketAddress(host, port), TIMEOUT_MS);
                resultBuilder.append("Port ").append(port).append(" is open\n");
            } catch (IOException e)
            {
                resultBuilder.append("Port ").append(port).append(" is closed\n");
            }
            return resultBuilder.toString();
        }

    }
}