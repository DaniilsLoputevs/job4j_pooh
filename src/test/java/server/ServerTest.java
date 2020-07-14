package server;

import cliet.Publisher;
import cliet.Subscriber;
import models.Message;
import models.User;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

/**
 * TODO нужно что бы, весь код отыграл сценарий, а не через Main и 2 и более консолей работал.
 */
public class ServerTest {
    private Server server;
    private Socket serverSocket;
    private Publisher publisher;
    private Subscriber sub;

    @Before
    public void setUp() throws Exception {
        try {
            serverSocket = new Socket(InetAddress.getByName("127.0.0.1"), 8080);
            publisher = new Publisher(serverSocket);
            sub = new Subscriber(new User("sub"), serverSocket);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void mainTest() {
        var serverThread = new Thread(() -> {
            {
               server = new Server();
               server.start();
               System.out.println("### Finish ###");

            }
        });
        serverThread.start();

        publisher.sendMessage(new Message("sub", "queue", "Vsem Privet!!!"));
        sub.register();



    }

}