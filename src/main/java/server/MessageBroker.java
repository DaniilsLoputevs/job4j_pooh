package server;

import models.Message;
import models.Response;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * RE-WRITE !!!! MUST HAVE!!!
 */
public class MessageBroker {
    private final ConcurrentHashMap<String, LinkedList<Message>> map = new ConcurrentHashMap<>();
    private final AtomicInteger threadSynchronizeCounter = new AtomicInteger(0);

    private static final String TOPIC = "\"TOPIC\"";
    private static final String TOPIC2 = "\"TOPIC2\"";

//    /**
//     * TOPIC2 - for synchronize send public messages between threads.
//     */
    public MessageBroker() {
//        this.map.put(TOPIC, new LinkedList<>());
//        this.map.put(TOPIC2, new LinkedList<>());
    }

    public List<Response> getSubscriberMsg(String queueName) {
//        queueName = "\"" + queueName + "\"";

        System.out.println("\r\n### SPECIAL GET ###");
        System.out.println("queueName: " + queueName);
        System.out.println("map.get(): " +  map.get(queueName));
        System.out.println("### SPECIAL GET ###\r\n");
        return map.get(queueName).stream().map(Response::new).collect(Collectors.toList());
    }

    public void addMsg(Message msg) {
        if (TOPIC.equals(msg.getQueue())) {
           addToEach(msg);
        } else {
            var msgQueueList = map.get(msg.getQueue());
            System.out.println("\r\n### SPECIAL ADD ###");
            System.out.println("msg.getQueue(): " + msg.getQueue());
            System.out.println("msgQueueList: " + msgQueueList);
            System.out.println("### SPECIAL ADD ###\r\n");
            if (msgQueueList != null) {
                msgQueueList.add(msg);
            }
        }
    }

    private synchronized void addToEach(Message msg) {
        for (var entry : map.entrySet()) {

            entry.getValue().add(msg);
            System.out.println("\r\n### SPECIAL ADD_EACH ###");
            System.out.println("entry.getKey(): " + entry.getKey());
            System.out.println("entry.getValue(): " + entry.getValue().toString());
            System.out.println("### SPECIAL ADD_EACH ###\r\n");
        }
    }

    /**
     * @param queueName - subscriber name.
     */
    public void registerQueue(String queueName) {
        if (!map.containsKey(queueName)) {
            System.out.println(Thread.currentThread().getName() + "queue was register: " + queueName);
            map.put(queueName, new LinkedList<>());
        }
    }


    @Deprecated
    /**
     * if {@code message.queue == TOPIC} ==> message add to queue with name {@code TOPIC2}.
     *
     * @param message -
     */
    public void addMessage(Message message) {
        if (TOPIC.equals(message.getQueue())) {
            map.get(TOPIC2).add(message);
        } else {
            var tempMessageQ = map.get(message.getQueue());
            if (tempMessageQ != null) {
                tempMessageQ.add(message);
            }
        }

    }

    @Deprecated
    /**
     * WRITE JAVADOC FOR THIS MULTI-THREAD MAGIC!!!
     *
     * @param responseList -
     */
    public void setAllPublicMessages(List<Response> responseList, String userName,
                                     AtomicInteger subscribersAtomicCount) {
        if (map.get(TOPIC).size() != 0) {
            if (threadSynchronizeCounter.getAndIncrement() < subscribersAtomicCount.get()) {
                map.get(TOPIC).forEach(message -> responseList.add(new Response(message)));
            } else {
                threadSynchronizeCounter.set(0);
                map.put(TOPIC, map.get(TOPIC2));
                map.put(TOPIC2, new LinkedList<>());
            }
        }
        var temp = map.get(userName);
        if (!temp.isEmpty()) {
            temp.forEach(message -> responseList.add(new Response(message)));
            temp.clear();
        }
    }

}