package server;

import models.Message;

import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;

public class MessageBroker {
    private final ConcurrentHashMap<String, LinkedList<Message>> map = new ConcurrentHashMap<>();

    private static final String TOPIC = "\"TOPIC\"";


    public Message getSubscriberMsg(String queueName) {
        /* Debug Prints */
//        System.out.println("\r\n### LOG GET SUB MSG ###");
//        System.out.println("queueName: " + queueName);
//        System.out.println("map.get(): " + map.get(queueName));
//        System.out.println("### LOG GET SUB MSG ###\r\n");

        var local = map.get(queueName).removeFirst();
        return (local != null) ? local : new Message("none", "none");
    }

    public void addMsg(Message msg) {
        if (TOPIC.equals(msg.getQueue())) {
            addToEach(msg);
        } else {
            var msgQueueList = map.get(msg.getQueue());
            /* Debug Prints */
//            System.out.println("\r\n### ADD MSG ###");
//            System.out.println("msg.getQueue(): " + msg.getQueue());
//            System.out.println("msgQueueList: " + msgQueueList);
//            System.out.println("### ADD MSG ###\r\n");
            if (msgQueueList != null) {
                msgQueueList.add(msg);
            }
        }
    }

    private void addToEach(Message msg) {
        for (var entry : map.entrySet()) {
            entry.getValue().add(msg);
            /* Debug Prints */
//            System.out.println("\r\n### ADD EACH ###");
//            System.out.println("entry.getKey(): " + entry.getKey());
//            System.out.println("entry.getValue(): " + entry.getValue().toString());
//            System.out.println("### ADD EACH ###\r\n");
        }
    }

    /**
     * @param queueName - subscriber's name || "TOPIC"
     */
    public void registerQueue(String queueName) {
        if (!map.containsKey(queueName)) {
            map.put(queueName, new LinkedList<>());
        }
    }

}