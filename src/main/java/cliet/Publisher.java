package cliet;

import general.Http;
import general.IOGeneral;
import models.Message;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Publisher {
    public static final List<String> log = new ArrayList<>();

    public boolean postMessage(Message message) {
        try (var socket = new Socket("localhost", 8080)) {
            try (var out = new DataOutputStream(socket.getOutputStream());
                 var in = new DataInputStream(socket.getInputStream())) {

                log.add("PUB - write");
                log.add("PUB - request:");
                var local = Http.makeRequest(Http.STATUS_POST, "ADD MSG JSON", message.toStringHttp());
                log.add(local);
                log.add("PUB - request finish");

                IOGeneral.writeToInput(out, local);
                var response = IOGeneral.reedFromInput(in);
                return Http.checkResponse(response);

            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

//    /**
//     * Prepare & send HTTP request(post) to server.
//     *
//     * @param message -
//     */
//    private String sendRequestPost(Message message) {
//        return  "POST/HTTP/1.1 200 OK\r\n"
//                + "server.Server: job4j/2020-07-12\r\n"
//                + "Content-Type: text/html\r\n"
//                + "Content-Length: " + message.getBody().length() + "\r\n"
//                + "body: ADD MSG JSON\r\n"
//                + message.toStringHttp() + "\r\n"
//                + "\r\n";
//    }

}