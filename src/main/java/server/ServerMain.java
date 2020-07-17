package server;

import models.Message;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class ServerMain extends Thread{
    private final ExecutorService executeIt = Executors.newFixedThreadPool(4);
    private final MessageBroker broker = new MessageBroker();
    private final AtomicInteger subscribersAtomicCount = new AtomicInteger(0);

//    private final List<Message> queue = new LinkedList<>();
//    private final ConcurrentHashMap<String, LinkedList<Message>> bufferPrivateMessages = new ConcurrentHashMap<>();

    @Override
    public void start() {
        var thread = new Thread(() -> {
            System.out.println("SERVER-MAIN: Start");

            try (var server = new ServerSocket(8080)) {
                System.out.println("SERVER-MAIN: Server socket created");

                while (!server.isClosed()) {

                    Socket client = server.accept();
                    executeIt.execute(new ServerUserThread(client, broker, subscribersAtomicCount));
                    System.out.println("SERVER-MAIN: Connection accepted.");
                }

                executeIt.shutdown();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }, "Server Thread");

        thread.start();
    }

}