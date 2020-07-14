package cliet;

import models.Message;
import server.Server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ConcurrentHashMap;

// send sms
// receive answer
// check answer
public class Publisher {
    private final Socket connection;

    public Publisher(Socket connection) {
        this.connection = connection;
    }

    public boolean sendMessage(Message message) {
        try (var input = new BufferedReader(new InputStreamReader(connection.getInputStream(),
                StandardCharsets.UTF_8));
             var output = new PrintWriter(connection.getOutputStream())) {

            sendRequest(output, message);
            var response = ClientTalk.receiveResponse(input);
            return ClientTalk.checkResponse(response);

        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    /**
     * Prepare & send HTTP request(post) to server.
     *
     * @param output  -
     * @param message -
     */
    private void sendRequest(PrintWriter output, Message message) {
        var temp = "HTTP/1.1 200 OK\r\n"
                + "server.Server: job4j/2020-07-12\r\n"
                + "Content-Type: text/html\r\n"
                + "Content-Length: " + message.getBody().length() + "\r\n"
                + "body: message in JSON format:\r\n"
                + message.toString();
//                + "Connection: close\r\n\r\n";
        output.println(temp);
        output.flush();
    }

    public static void main(String[] args) {
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


        var publisher = new Publisher(publisherSocket);
        publisher.sendMessage(new Message("targetTest\r\n", "TopicTest\r\n", "bodyTest\r\n"));

        System.out.println(map.get("request"));
    }
    public static class ServerThread extends Thread {
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

//    private String receiveResponse(BufferedReader input) {
//        var response = new StringBuilder();
//        try {
//            while (!input.ready()) ;
//            while (input.ready()) {
//                response.append(input.readLine());
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return response.toString();
//    }
//
//    private boolean checkResponse(String response) {
//        var temp = response.split("\r\n");
//        return temp[0].contains("OK");
//    }

}