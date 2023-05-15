package com.lopuch.projects.poker;

import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Server implements Runnable {
    private String ip = "localhost";
    private int port = 51734;
    private Scanner scanner = new Scanner(System.in);
    private Thread thread;

    private Socket socket;
    private DataInputStream dataIn;
    private DataOutputStream dataOut;

    private ServerSocket serverSocket;




    public Server()
    {
        System.out.println("Please input the IP:");
        ip = scanner.nextLine();
        System.out.println("Please input port:");
        port = scanner.nextInt();
    }

    public void run()
    {
        while(true)
        {
            listenForServerRequest();
        }

    }

    private void listenForServerRequest()
    {
        Socket socket = null;
        try
        {
            socket = serverSocket.accept();
            dataOut = new DataOutputStream(socket.getOutputStream());
            dataIn = new DataInputStream(socket.getInputStream());

            System.out.println(("CLIENT HAS REQUESTED TO JOIN, WE ACCEPTED"));

        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

}
