package Server;

import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;

public class TCP {

    int port = 5555;
    ServerSocket serverSocket = null;
//    Socket clientSocket = null;
//    PrintWriter out = null;
//    BufferedReader in = null;
    List<Connection> connectionList = new ArrayList<>();

    public TCP(){
        try {
            serverSocket = new ServerSocket(port);
        } catch (IOException e) {
            System.err.println("Could not listen on port: 4444.");
            System.exit(1);
        }
        try {
            serverSocket.setSoTimeout(100);
        } catch (SocketException ex) {
            // Nepodarilo se nastavit timeout
        }
    }

    public void tryConnection(){
        Connection c = new Connection();
        try {
            c.clientSocket = serverSocket.accept();
            c.clientSocket.setSoTimeout(100);
        } catch (IOException e) {
//            System.err.println("tryConnection: timeout");
            return;
//            System.exit(1);
        }
        try {
            c.out = new PrintWriter(c.clientSocket.getOutputStream(), true);
            c.in = new BufferedReader(new InputStreamReader(c.clientSocket.getInputStream()));
        } catch (IOException e){
            System.err.println("Connection failed");
        }
        if(c.clientSocket != null){
            c.id = connectionList.size();
            connectionList.add(c);
            System.out.println("New connection! Id:" + c.id);
        }
    }

    public Message getPackage(){
        String buf = "";
        for (Connection c : connectionList){
            try {
                buf = c.in.readLine();
            } catch (IOException e){
                continue;
            }
            if(buf == null)
                break;
        }
        if(buf == null || buf.length() == 0)
            return null;
        System.out.println("Server get: " + buf);
        return switch (buf.charAt(0)) {
            case 'm' -> new MoveMessage(buf);
            case 'f' -> new GameFinishedMessage(buf);
            case 's' -> new StartGameMessage(buf);
            case 'w' -> new WaitForGameMessage(buf);
            default -> null;
        };
    }

    public void sendPackage(Message msg){
        int id = msg.receiver;
        String outputLine = msg.msgToBuffer();
        System.out.println("Server send: " + outputLine);
        for(Connection c : connectionList){
            if(c.id == id){
                c.out.println(outputLine);
            }
        }
    }

}
