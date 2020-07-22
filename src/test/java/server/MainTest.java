package server;

import cliet.Publisher;
import cliet.Subscriber;
import models.Message;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;

public class MainTest {
    private static final List<String> SUB_LOG = new ArrayList<>();
    private static final List<String> PUB_LOG = new ArrayList<>();

    @Test
    public void mainTest() {
        System.out.println("MainTest - test start\r\n");

        var server = new ServerMain();
        server.start();


        /* Subscriber Thread */
        var subscriberThread = new Thread(() -> {
            System.out.println("### Subscriber start ###");

            var subscriber = new Subscriber("subscriber");
            SUB_LOG.add("reg");
            subscriber.register(); // Server Thread 1
            SUB_LOG.add("sleep");
            threadSleep(300);

            SUB_LOG.add("Next String is response from server:");
            subscriber.checkUnreadMsg(SUB_LOG::add); // Server Thread 4
            SUB_LOG.add("Sub accepted msg!");
            threadSleep(200);
            SUB_LOG.add("Next String is response from server:");
            subscriber.checkUnreadMsg(SUB_LOG::add); // Server Thread 5
            SUB_LOG.add("Sub accepted msg!");

            System.out.println("### Subscriber finish ###");
        }, "subscriber Thread 1");
        subscriberThread.start();


        /* Publisher Thread */
        var publisherThread = new Thread(() -> {
            threadSleep(150);
            System.out.println("### Publisher start ###");

            var publisher = new Publisher();
            var msgTopic = new Message("TOPIC", "bodyTest");
            var msgPrivate = new Message("subscriber", "privateMsg");

            publisher.postMessage(msgTopic); // Server Thread 2
            PUB_LOG.add("pub add msg");
            PUB_LOG.add("msg content: " + msgTopic.toStringHttp());

            threadSleep(150);
            publisher.postMessage(msgPrivate); // Server Thread 3
            PUB_LOG.add("pub add msg");
            PUB_LOG.add("msg content: " + msgPrivate.toStringHttp());

            System.out.println("### Publisher finish ###");
        }, "Publisher Thread 1");
        publisherThread.start();

        threadSleep(1500);

        /* Log there Subscriber Thread stop */
        System.out.println("\r\n### Subscriber Thread Log ###");
        SUB_LOG.forEach(System.out::println);
        System.out.println("### Subscriber Thread Log ###\r\n");

//        /* Log from Subscriber.class */
//        System.out.println("\r\n### Subscriber Class Log ###");
//        Subscriber.log.forEach(System.out::println);
//        System.out.println("### Subscriber Class Log ###\r\n");

//        /* Log there Publisher Thread stop */
//        System.out.println("\r\n### Publisher Thread Log ###");
//        logPub.forEach(System.out::println);
//        System.out.println("### Publisher Thread Log ###\r\n");

//        /* Log from Publisher.class */
//        System.out.println("\r\n### Publisher Class Log ###");
//        Publisher.log.forEach(System.out::println);
//        System.out.println("### Publisher Class Log ###\r\n");

        System.out.println("MainTest - test Finish\r\n");

//        threadSleep(100000);
        threadSleep(1000);
        assertTrue(SUB_LOG.contains("POST/HTTP/1.1 200 OK\r\n"
                + "server.Server: job4j/2020-07-12\r\n"
                + "Content-Type: text/html\r\n"
                + "Content-Length: 10\r\n"
                + "body: JSON\r\n"
                + "{\r\n"
                + "\"queue\" : \"TOPIC\"\r\n"
                + "\"text\" : \"bodyTest\"\r\n"
                + "}\r\n"
                + "\r\n"
        ));
        assertTrue(SUB_LOG.contains("POST/HTTP/1.1 200 OK\r\n"
                + "server.Server: job4j/2020-07-12\r\n"
                + "Content-Type: text/html\r\n"
                + "Content-Length: 12\r\n"
                + "body: JSON\r\n"
                + "{\r\n"
                + "\"queue\" : \"subscriber\"\r\n"
                + "\"text\" : \"privateMsg\"\r\n"
                + "}\r\n"
                + "\r\n"
        ));
    }

    /**
     * just less code for simple action.
     *
     * @param ms milliseconds.
     */
    public void threadSleep(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}