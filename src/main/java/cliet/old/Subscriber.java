package cliet.old;

import models.old.User;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class Subscriber {
    private final User user;
    private final Socket connection;

    public Subscriber(User user, Socket connection) {
        this.user = user;
        this.connection = connection;
    }

    public boolean register() {
        try (var input = new BufferedReader(new InputStreamReader(connection.getInputStream(),
                StandardCharsets.UTF_8));
             var output = new PrintWriter(connection.getOutputStream())) {

            sendRegRequest(output);
            var response = ClientTalk.receiveResponse(input);
            return ClientTalk.checkResponse(response);

        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return false;
    }

//    public boolean sendMessage(String message) {
//        try (var input = new BufferedReader(new InputStreamReader(connection.getInputStream(),
//                StandardCharsets.UTF_8));
//             var output = new PrintWriter(connection.getOutputStream())) {
//
//            sendRequest(output, message);
//            return receiveResponse(input);
//
//        } catch (IOException ex) {
//            ex.printStackTrace();
//        }
//        return false;
//    }

    /**
     * Prepare & send HTTP request for registration on server.
     *
     * @param output  -
     */
    private void sendRegRequest(PrintWriter output) {
        var temp = "HTTP/1.1 200 OK\r\n"
                + "server.Server: job4j/2020-07-12\r\n"
                + "Content-Type: text/html\r\n"
                + "Content-Length: " + this.user.getName().length() + "\r\n"
                + "body: " + this.user.getName();
        output.println(temp);
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
