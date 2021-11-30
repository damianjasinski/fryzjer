package server;

import ClientService.ClientService;

import javax.sound.midi.Soundbank;
import java.io.*;
import java.net.Socket;
import java.util.Map;

public class SubscriberHandler extends Thread {
    private final ClientService clientService;
    Socket subscriberSocket;
    DataInputStream inStream;
    DataOutputStream ouStream;
    String subscriberMsg;
    String topic;

    public SubscriberHandler(Socket subscriberSocket, ClientService clientService) throws IOException {
        this.subscriberSocket = subscriberSocket;
        this.clientService = clientService;
        this.inStream = new DataInputStream(subscriberSocket.getInputStream());
        this.ouStream = new DataOutputStream(subscriberSocket.getOutputStream());
        clientService.addSubscriber(this);
    }

    public void sendMessage(String response) {
        try {
            ouStream.writeUTF(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Listen to incoming messages from subscriber
    @Override
    public void run() {
        while(true) {
            try {
                subscriberMsg = inStream.readUTF();
                System.out.println("Got it");
                String[] splitMsg = subscriberMsg.split("\\s+");
                if ("1".equals(splitMsg[0])) {
                    // [0] = method, [1] hour, [2] name
                    System.out.println("Reserving slot for " + splitMsg[2]);
                    clientService.reserveSlot(Integer.parseInt(splitMsg[1]), splitMsg[2]);
                }
                if ("2".equals(splitMsg[0])) {
                    // [0] = method, [1] name
                    System.out.println("Unreserving slot for " + splitMsg[1]);
                    clientService.unreserveSlot(splitMsg[1]);
                }


            } catch (IOException e) {
                System.out.println("Disconnect" + subscriberSocket.getInetAddress());
                e.printStackTrace();
                return;
            }
        }
    }

}
