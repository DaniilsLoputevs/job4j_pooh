package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class ServerMain extends Thread {
    private final ExecutorService executeIt = Executors.newFixedThreadPool(4);
    private final MessageBroker broker = new MessageBroker();
    private final AtomicInteger subscribersAtomicCount = new AtomicInteger(0);

    @Override
    public void start() {
        var thread = new Thread(() -> {
            System.out.println("SERVER-MAIN: Start");

            try (var server = new ServerSocket(8080)) {
//                System.out.println("SERVER-MAIN: Server socket created");

                while (!server.isClosed()) {

                    Socket client = server.accept();
                    executeIt.execute(new ServerUserThread(client, broker, subscribersAtomicCount));
//                    System.out.println("SERVER-MAIN: Connection accepted.");
                }

                executeIt.shutdown();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }, "Server Thread");

        thread.start();
    }

}