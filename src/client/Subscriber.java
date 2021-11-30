package client;

import java.io.*;
import java.net.Socket;
import java.text.Normalizer;
import java.util.List;
import java.util.Scanner;

public class Subscriber {
    boolean listenerDone = false;
    String name;
    Socket connect;
    DataInputStream inStream;
    DataOutputStream ouStream;
    BufferedReader userInput;

    public Subscriber(Integer port, String name) throws IOException {
        connect = new Socket("127.0.0.1", port);
        inStream = new DataInputStream(connect.getInputStream());
        ouStream = new DataOutputStream(connect.getOutputStream());
        userInput = new BufferedReader(new InputStreamReader(System.in));
        this.name = name;
    }
    public static void main(String[] args) throws IOException, InterruptedException {
        System.out.println("Enter your name:");
        Subscriber subscriber = new Subscriber(1234, new Scanner(System.in).nextLine());

        //Listen to incoming messages from server
        Thread listen = new Thread(() -> {
            while (true) {
                try {
                    String serverResponse = subscriber.inStream.readUTF();
                    Message newServerResponse = new Message(serverResponse);
                    List<String> payload = newServerResponse.getMsg();
                    System.out.println("---------------------------CURRENT FREE SLOTS----------------------------");
                    for (String slot : payload) {
                        System.out.println(slot);
                    }
                    subscriber.listenerDone = true;
                } catch (IOException e) {
                    System.out.println("Server unreachable");
                    break;
                }
            }
        });
        listen.start();

        //Listen to user Input
        Thread listenInput = new Thread(() -> {
            while (true) {
                if(!subscriber.listenerDone) continue;
                String time;
                String option;
                String name;
                try {
                    System.out.println("Enter 1 to make a reservation:");
                    System.out.println("Enter 2 to unreserve:");
                    option = subscriber.userInput.readLine().trim();
                    if ("1".equals(option)) {
                        System.out.println("Enter hour");
                        time = subscriber.userInput.readLine().trim();
                        try {
                            if (!FormatValidator.validateFormatTime(time))
                                throw new NumberFormatException("Bad format");
                        } catch (NumberFormatException e) {
                            System.out.println(e.getMessage());
                        }
                        subscriber.ouStream.writeUTF(option + " " + time + " " + subscriber.name);
                    }

                    else if ("2".equals(option)) {
                        subscriber.ouStream.writeUTF(option + " " + subscriber.name);
                    }
                    else System.out.println("Wrong option");
                } catch (IOException e) {
                    System.out.println("Server unreachable");
                    break;
                }
            }
        });
        listenInput.start();
    }
}
