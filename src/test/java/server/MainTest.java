package server;

import cliet.Publisher;
import cliet.Subscriber;
import models.Message;
import org.junit.Before;
import org.junit.Test;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

//TODO LOG in code with Thread's name.
public class MainTest {
    private static final List<String> log = new ArrayList<>();
    private static final List<String> logPub = new ArrayList<>();

    @Before
    public void setUp() throws Exception {
//        IOHelper.clearFile("C:\\Danik\\Projects\\job4j_pooh\\src\\test\\java\\sub_log.txt");
    }

    @Test
    public void mainTest() {
        var server = new ServerMain();
        server.start();


        /* Subscriber Thread */
        var subscriberThread = new Thread(() -> {
            log.add("String 1");
            var subscriber = new Subscriber("subscriber");

            log.add("String 2");
            subscriber.register();

            log.add("String 3");
            threadSleep(2000);

            log.add("String 4");
            System.out.println("Thread Sub - await and read");
            log.add("Next String is msg from server:");
            subscriber.checkUnreadMsg(log::add);
            log.add("Sub accepted msg!");

            System.out.println("### Subscriber finish ###");
        }, "subscriber Thread 1");
        subscriberThread.start();


        /* Publisher Thread */


        var publisherThread = new Thread(() -> {
            threadSleep(100);
            var publisher = new Publisher();
            var msg = new Message("TOPIC", "bodyTest");
            publisher.postMessage(msg);

            logPub.add("pub add msg");
            logPub.add("msg content: \r\n" + msg.toStringHttp());

            System.out.println("### Publisher finish ###");
        }, "Publisher Thread 1");
        publisherThread.start();


        threadSleep(3000);
        /* Log there Subscriber Thread stop */
        System.out.println("\r\n### Subscriber Log ###");
        log.forEach(System.out::println);
        System.out.println("### Subscriber Log ###\r\n");

//        /* Log from Subscriber.class */
//        System.out.println("\r\n### SPECIAL ###");
//        Subscriber.log.forEach(System.out::println);
//        System.out.println("### SPECIAL ###\r\n");

//        /* Log from Publisher.class */
//        System.out.println("\r\n### SPECIAL ###");
//        Publisher.log.forEach(System.out::println);
//        System.out.println("### SPECIAL ###\r\n");

        /* Log there Publisher Thread stop */
//        System.out.println("\r\n### Publisher Log ###");
//        logPub.forEach(System.out::println);
//        System.out.println("### Publisher Log ###\r\n");
        threadSleep(100000);

    }

    /**
     * just less code for this simple action.
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


    //### Other thing ###

    @Test
    public void OldTry() {
        var map = new ConcurrentHashMap<String, String>();

        Socket publisherSocket = null;
        var server = new ServerThread(map);

        System.out.println("server start");
        server.start();

        try {
            Thread.sleep(7000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            publisherSocket = new Socket("localhost", 1637);
        } catch (IOException e) {
            e.printStackTrace();
        }


//        var publisher = new Publisher(publisherSocket);
//        publisher.sendMessage(new Message("TopicTest\r\n", "bodyTest\r\n"));

        System.out.println(map.get("request"));
    }

    public class ServerThread extends Thread {
        private ConcurrentHashMap<String, String> map;

        public ServerThread(ConcurrentHashMap<String, String> map) {
            this.map = map;
        }

        @Override
        public void run() {
            super.setName("server");
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            ServerSocket mainServerSocket;
//            Socket mainServerSocket;
            Socket serverSocket = null;
            try {
                mainServerSocket = new ServerSocket(1637);
//                serverSocket.
                serverSocket = mainServerSocket.accept();
            } catch (IOException e) {
                e.printStackTrace();
            }

            try (var out = new DataOutputStream(serverSocket.getOutputStream());
                 var in = new DataInputStream(serverSocket.getInputStream());) {
                var messageFromSocket = in.readUTF();
                map.put("request", messageFromSocket);
                System.out.println("print message: ");
                System.out.println(messageFromSocket);
                out.writeUTF("OK\r\n");
                out.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }
}