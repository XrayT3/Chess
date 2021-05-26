package Client;

import Figures.Cell;
import Server.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class TCP{

    Socket kkSocket = null;
    PrintWriter out = null;
    BufferedReader in = null;
    int player;
    int gameId;

    /**
     * Make TCP connection
     **/
    public TCP(){
        try {
            kkSocket = new Socket("127.0.0.1", 5555);
            out = new PrintWriter(kkSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(kkSocket.getInputStream()));
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host: localhost.");
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for the connection to: localhost.");
            System.exit(1);
        }
    }

    /**
     * Receives a package from the server
     * and makes a message from it
     * @return message
     **/
    public Message getPackage(){
        String buf = "";
        try {
            buf = in.readLine();
        } catch (IOException e){
            e.printStackTrace();
        }
        if(buf.length() == 0)
            return null;
        System.out.println("Client get: " + buf);
        return switch (buf.charAt(0)) {
            case 'm' -> new MoveMessage(buf);
            case 's' -> new StartGameMessage(buf);
            case 'w' -> new WaitForGameMessage(buf);
            default -> null;
        };
    }

    /**
     * Make package from the message
     * and send it to the server
     **/
    public void sendPackage(Message message){
        String fromUser = message.msgToBuffer();
        System.out.println("Client send: " + fromUser);
        out.println(fromUser);
    }

    public void sendGameRequest(String nick){
        WaitForGameMessage message = new WaitForGameMessage();
        message.playerName = nick;
        sendPackage(message);
    }

    public void sendMove(Cell begin, Cell end){
        MoveMessage message = new MoveMessage();
        message.begin = begin;
        message.end = end;
        message.player = player;
        message.gameId = gameId;
        sendPackage(message);
    }

    public Move getMove(){
        MoveMessage message = (MoveMessage) getPackage();
        return new Move(message.begin, message.end);
    }

    public void disconnection() {
        out.close();
        try {
            in.close();
            kkSocket.close();
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
