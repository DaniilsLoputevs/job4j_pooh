package server;

import models.Message;
import models.Response;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class MessageBroker {
    private final ConcurrentHashMap<String, LinkedList<Message>> map = new ConcurrentHashMap<>();
    private final AtomicInteger threadSynchronizeCounter = new AtomicInteger(0);

    private static final String TOPIC = "\"TOPIC\"";
    private static final String TOPIC2 = "\"TOPIC2\"";

    /**
     * TOPIC2 - for synchronize send public messages between threads.
     */
    public MessageBroker() {
        this.map.put(TOPIC, new LinkedList<>());
        this.map.put(TOPIC2, new LinkedList<>());
    }

    /**
     * @param queueName - subscriber name.
     */
    public void registerQueue(String queueName) {
        if (!"".equals(queueName)) {
            var jsonFormatQueueName = "\"" + queueName + "\"";
            map.put(jsonFormatQueueName, new LinkedList<>());
        }
    }

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