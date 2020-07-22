package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServerMain extends Thread {
    private final ExecutorService executeIt = Executors.newFixedThreadPool(4);
    private final MessageBroker broker = new MessageBroker();

    @Override
    public void start() {
        var thread = new Thread(() -> {
            System.out.println("SERVER-MAIN: Start");

            try (var server = new ServerSocket(8080)) {
                while (!server.isClosed()) {
                    Socket client = server.accept();
                    executeIt.execute(new ServerUserThread(client, broker));
                }
                executeIt.shutdown();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }, "Server Thread");

        thread.start();
    }

}