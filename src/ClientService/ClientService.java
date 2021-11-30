package ClientService;

import server.ReservationTable;
import server.SubscriberHandler;

import java.util.*;

public class ClientService {
    private ReservationTable reservationTable;
    private Set<SubscriberHandler> subscribersSet;

    public ClientService() {
        subscribersSet = new HashSet<>();
        reservationTable = new ReservationTable();
    }

    public void reserveSlot(Integer hour, String who) {
        if (reservationTable.reserve(hour, who)) {
            broadcast();
        }
    }

    public void unreserveSlot(String name) {
        if (reservationTable.unreserve(name)) {
            broadcast();
        }
    }

    public void addSubscriber(SubscriberHandler subscriber) {
        subscribersSet.add(subscriber);
        subscriber.sendMessage(reservationTable.toString());
    }

    public void broadcast() {
        System.out.println("Broadcasting");
        String tableToSend = reservationTable.toString();
        System.out.println(tableToSend);
        for (SubscriberHandler sub : subscribersSet) {
            sub.sendMessage(tableToSend);
        }
    }
}
