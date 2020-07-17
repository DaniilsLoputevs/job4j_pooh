package cliet;

import models.Message;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class Publisher {
    private final Socket connection;

    public Publisher(Socket connection) {
        this.connection = connection;
    }

    public boolean sendMessage(Message message) {
        try (var input = new BufferedReader(new InputStreamReader(connection.getInputStream(),
                StandardCharsets.UTF_8));
             var output = new PrintWriter(connection.getOutputStream())) {

            sendRequestPost(output, message);
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
    private void sendRequestPost(PrintWriter output, Message message) {
        var temp = "POST/HTTP/1.1 200 OK\r\n"
                + "server.Server: job4j/2020-07-12\r\n"
                + "Content-Type: text/html\r\n"
                + "Content-Length: " + message.getBody().length() + "\r\n"
                + "body: JSON\r\n"
                + message.toString();
//                + "Connection: close\r\n\r\n";
        output.println(temp);
        output.flush();
    }

}