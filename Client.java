import java.net.*;
import java.util.*;
import javax.swing.*;
import java.io.*;
import java.awt.event.*;

public class Client extends JFrame {

    private Socket socket;
    private String name;
    private JList messageList;

    List <String> messages;

    public void addMessage(String message) {

        messages.add(message);
        messageList.setListData(messages.toArray(new String[0]));
        
    }

    public void connect() throws Exception {

        System.out.println("Enter IP address of server...");
        Scanner scanner = new Scanner(System.in);
        String ip = scanner.nextLine(); 

        this.socket = new Socket(ip,Constants.PORT);

        negotiateName();
    }

    private void negotiateName() throws Exception {

        Scanner scanner = new Scanner(System.in);
        while (true) {
            
            System.out.println("Choose username...");
            String name = scanner.nextLine();
            sendMessage(name);

            String [] response = receiveMessage().split(": ");

            if (Constants.SUCCESS.equals(response[1])) {

                this.name = name;
                System.out.println("Success");
                break;

            } else {

                System.out.println(response[0] + ": " + response[1]);
            }
        }
    }

    public void sendMessage(String message) throws Exception {

        DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
        dos.writeUTF(message);
    }

    public String receiveMessage() throws Exception {

        DataInputStream dis = new DataInputStream(socket.getInputStream());
        return dis.readUTF();
    }

    public Client() throws Exception {

        this.messages = new LinkedList <> ();

        connect();

        setupUI();

    }

    private void setupUI() {

        this.setName("Chat Sophie");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(250,550);
        this.setLayout(null);

        messageList = new JList();
        messageList.setBounds(10,10,210,400);
        messageList.setVisible(true);
        this.add(messageList);

        JTextArea textArea = new JTextArea();
        textArea.setBounds(10,420,230,70);
        textArea.setVisible(true);

        textArea.addKeyListener(new KeyListener() {            
                public void keyTyped(KeyEvent e) {
                    if(e.getKeyCode() == KeyEvent.VK_ENTER) {
                        //input.setText(inputValue);
                        //System.out.println("up is pressed");
                        
                    }       
                }

                @Override
                public void keyPressed(KeyEvent e) {
                    // TODO Auto-generated method stub  
                }

                @Override
                public void keyReleased(KeyEvent e) {
                    String message = textArea.getText();
                    if (e.getKeyCode() ==  KeyEvent.VK_ENTER) {
                        try {
                            sendMessage(message);
                            System.out.println(message);
                        } catch (Exception ex) {
                            System.out.println(ex);
                        }    
                    }
                }
            });

        this.add(textArea);

        this.setVisible(true);
    }

    public static void main(String args[]) throws Exception {

        Client client = new Client();
        ClientSideListener clientSideListener = new ClientSideListener(client);
        new Thread(clientSideListener).start();
     
    }
}
