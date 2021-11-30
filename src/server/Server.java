package server;

import ClientService.ClientService;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {

    public static void main(String... args) throws IOException {
        int subscriberId = 0;
        final ClientService clientService = new ClientService();
        List<SubscriberHandler> subscribers = new ArrayList<>();
        ServerSocket server = new ServerSocket(1234);

        //Listen to incoming connections
        Thread listenConnections = new Thread(new Runnable() {
            @Override
            public void run() {
                while(true) {
                    try {
                        Socket conn = server.accept();
                        System.out.println("Subscriber has connected!");
                        SubscriberHandler newSub = new SubscriberHandler(conn, clientService);
                        subscribers.add(newSub);
                        newSub.start();

                    } catch (IOException e) {
                        System.out.println("Socket closed");
                    }

                }
            }
        });
        listenConnections.start();
    }
}
