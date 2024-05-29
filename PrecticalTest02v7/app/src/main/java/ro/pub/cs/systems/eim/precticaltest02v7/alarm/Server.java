package ro.pub.cs.systems.eim.precticaltest02v7.alarm;
import java.net.*;
import java.io.*;
import java.util.*;

public class Server extends Thread {
    private ServerSocket serverSocket;
    private Map<String, Alarm> alarms = new HashMap<>();

    public Server(int port) throws IOException {
        serverSocket = new ServerSocket(port);
    }

    public void run() {
        while (true) {
            try {
                Socket clientSocket = serverSocket.accept();
                new ClientHandler(clientSocket).start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private class ClientHandler extends Thread {
        private Socket clientSocket;
        private BufferedReader in;
        private PrintWriter out;

        public ClientHandler(Socket socket) {
            this.clientSocket = socket;
            try {
                in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                out = new PrintWriter(clientSocket.getOutputStream(), true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void run() {
            try {
                String request = in.readLine();
                String clientIp = clientSocket.getInetAddress().getHostAddress();
                if (request.startsWith("set")) {
                    String[] parts = request.split(",");
                    int hour = Integer.parseInt(parts[1]);
                    int minute = Integer.parseInt(parts[2]);
                    alarms.put(clientIp, new Alarm(hour, minute));
                    out.println("Alarm set");
                } else if (request.equals("reset")) {
                    alarms.remove(clientIp);
                    out.println("Alarm reset");
                } else if (request.equals("poll")) {
                    Alarm alarm = alarms.get(clientIp);
                    if (alarm == null) {
                        out.println("none");
                    } else {
                        TimeService timeService = new TimeService();
                        Time currentTime = timeService.getCurrentTime();
                        if (currentTime.isAfter(alarm)) {
                            out.println("active");
                        } else {
                            out.println("inactive");
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    clientSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}