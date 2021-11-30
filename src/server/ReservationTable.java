package server;

import java.io.Serializable;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;



public class ReservationTable implements Serializable {
    private Map<Integer, String> slots;

    {
        slots = new LinkedHashMap<>();
        for (int i = 10; i < 19; i++) {
            slots.put(i, "Free");
        }
    }

    public boolean reserve(Integer hour, String who) {
        String tmp = slots.get(hour);
        System.out.println(tmp);
        if ("Free".equals(tmp)) {
            slots.put(hour, who);
            System.out.println(slots.get(hour));
            return true;
        }
        return false;
    }

    public boolean unreserve(String name) {
        int hour;
        for (var entry : slots.entrySet()) {
            if (entry.getValue().equals(name)) {
                hour = entry.getKey();
                slots.put(hour, "Free");
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        StringBuilder response = new StringBuilder();
        for (var entry : slots.entrySet()) {
            response.append(entry.getKey()).append(":").append(entry.getValue()).append("\n");
        }
        return response.toString();
    }
}
