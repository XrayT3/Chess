package Server;

import java.io.BufferedReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Connection {
    Socket clientSocket = null;
    PrintWriter out = null;
    BufferedReader in = null;
    int id = -2;
}
