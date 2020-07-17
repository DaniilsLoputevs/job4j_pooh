package oldserver;

import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicInteger;

public class Buffer {
    private final LinkedList<String> userSend = new LinkedList<>();
    private final LinkedList<String> servSend = new LinkedList<>();
    AtomicInteger userIndex = new AtomicInteger(0);
    AtomicInteger servIndex = new AtomicInteger(0);

    public void addUserSend(String string) {
        System.out.println(string);
        userSend.add(string);
    }
    public void addServSend(String string) {
        System.out.println(string);
        servSend.add(string);
    }
    public String getUserSend() {
        return userSend.get(userIndex.getAndIncrement());
    }
    public String getServerSend() {
        return servSend.get(servIndex.getAndIncrement());
    }
}
