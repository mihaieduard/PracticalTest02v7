package ro.pub.cs.systems.eim.precticaltest02v7.alarm;

import java.net.*;
import java.io.*;

public class Client {
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;

    public Client(String ip, int port) throws IOException {
        socket = new Socket(ip, port);
        out = new PrintWriter(socket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    public void setAlarm(int hour, int minute) {
        out.println("set," + hour + "," + minute);
    }

    public void resetAlarm() {
        out.println("reset");
    }

    public String checkAlarm() {
        out.println("poll");
        try {
            return in.readLine();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}