package client;

import java.util.*;

public class Message {
    private List<String> response;

    public Message(String serverResponse) {
        response = new ArrayList<>();
        String[] msg = serverResponse.split("\\n");
        response.addAll(Arrays.asList(msg));
    }

    public List<String> getMsg() {
        return response;
    }
}
