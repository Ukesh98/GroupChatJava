import java.util.*;
import java.net.*;
import java.io.*;

public class Server {

    private HashMap <String,Socket> clients;
    private ServerSocket serverSocket;

  

    public Server () throws Exception {

        this.clients = new HashMap <>();
        this.serverSocket = new ServerSocket(Constants.PORT);
    }

    public Socket accept() throws Exception {
        return serverSocket.accept();
    }

    public String receiveMessage(Socket client) throws Exception {

        DataInputStream dis = new DataInputStream(client.getInputStream());
        return dis.readUTF();
    }

    public void sendMessage(String sender, Socket client, String message) throws Exception {
        
        DataOutputStream dos = new DataOutputStream(client.getOutputStream());
        dos.writeUTF(sender + ": " + message);
    }

    public void removeClient(String clientName) throws Exception {

        clients.remove(clientName);
        broadcast("SERVER",clientName + " has left the lobby");
    }

    public void broadcast(String sender, String message) throws Exception {

        for (Map.Entry entry : clients.entrySet()) {
            Socket client = (Socket) entry.getValue();
            sendMessage((String)entry.getKey(),client,message);
        }
    }

    public Socket getClient(String clientName) {
        return clients.get(clientName);
    }
    
    public void addClient(String name, Socket client) {
        clients.put(name,client);
    }

    public static void main(String args[]) throws Exception {

        Server server = new Server();
        System.out.println("Listening at port " + Constants.PORT);

        while (true) {

            Socket client = server.accept();
            String name;

            while (true) {

                name = server.receiveMessage(client);

                if (server.getClient(name) == null) {

                    server.sendMessage("SERVER",client,Constants.SUCCESS);
                    break;

                } else {
                    server.sendMessage("SERVER",client,Constants.NAME_EXISTS);
                }
                
            } 

            server.addClient(name,client);

            ClientListener clientListener = new ClientListener(name,client,server);
            server.broadcast("SERVER: ",name + " has joined the lobby");
            
            new Thread(clientListener).start();

        }

    }
}
