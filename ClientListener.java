import java.net.*;
import java.io.*;
import java.util.*;

class ClientListener implements Runnable {

    private Socket clientSocket;
    private Server server;
    private String clientName;

    public ClientListener(String clientName, Socket clientSocket, Server server) {
        this.clientSocket = clientSocket;
        this.clientName = clientName;
        this.server = server;
    }    

    public void run() {

        try {
            while (true) {

                String message = server.receiveMessage(clientSocket);
                server.broadcast(clientName,message);
            }
        } catch (Exception e) {
            System.out.println(e);
        }

        try {
            server.removeClient(clientName);
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}