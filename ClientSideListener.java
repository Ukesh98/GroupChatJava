public class ClientSideListener implements Runnable {

    private Client client;

    public ClientSideListener(Client client) {
        this.client = client;
    }

    public void run() {

        try {


            while (true) {

                String message = client.receiveMessage();
                client.addMessage(message);
                
            }

        } catch (Exception e) {

            System.out.println(e);
        }
    }
}