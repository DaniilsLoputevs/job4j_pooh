package cliet;

import general.Http;
import general.IOGeneral;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class Subscriber {
    private final String userName;

    public static final List<String> log = new ArrayList<>();

    public Subscriber(String userName) {
        this.userName = "\"" + userName + "\"";
    }

    public boolean register() {
        var rsl = false;
        try (var socket = new Socket("localhost", 8080)) {
            try (var out = new DataOutputStream(socket.getOutputStream());
                 var in = new DataInputStream(socket.getInputStream())) {

//            System.out.println("\r\n### SPECIAL ###");

                log.add("SUB - write");
                log.add("SUB - request:");
                var bodyContent = httpForm(this.userName);
                var localRequest = Http.makeRequest(Http.STATUS_POST, "REG", bodyContent);
                log.add(localRequest);
                log.add("SUB - request finish");
                IOGeneral.writeToInput(out, localRequest);

//            System.out.println("### SPECIAL ###\r\n");

                log.add("SUB - read");
                var response = IOGeneral.reedFromInput(in);
                log.add("SUB - read - closed");

//                System.out.println("\r\n### SPECIAL ###");
//                System.out.println(Http.checkResponse(response));
//                System.out.println("### SPECIAL ###\r\n");

                rsl = Http.checkResponse(response);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return rsl;
    }

    /**
     * ask server, - do it have any massages for this user.
     * If it have, server will send it and finally it will be pushed into {@param howToAccept}.
     *
     * @param howToAccept -
     */
    public void checkUnreadMsg(Consumer<String> howToAccept) {
        try (var socket = new Socket("localhost", 8080)) {
            try (var out = new DataOutputStream(socket.getOutputStream());
                 var in = new DataInputStream(socket.getInputStream())) {

                var bodyContent = httpForm(this.userName);
                var localRequest = Http.makeRequest(Http.STATUS_GET, "GET MSG JSON", bodyContent);
                IOGeneral.writeToInput(out, localRequest);

                var response = IOGeneral.reedFromInput(in);
                howToAccept.accept(response);

            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//    /**
//     * Prepare HTTP request before sending on server.
//     */
//    private String sendRequest(String httpStatus, String serverCommand, String bodyContent) {
//        return httpStatus + "/HTTP/1.1 200 OK\r\n"
//                + "server.Server: job4j/2020-07-12\r\n"
//                + "Content-Type: text/html\r\n"
//                + "Content-Length: " + bodyContent.length() + "\r\n"
//                + "body: " + serverCommand + "\r\n"
//                + '{' + "\r\n"
//                + "\"user\" : " + bodyContent + "\r\n"
//                + "\"msgType\" : " + "\"all\"" + "\r\n"
//                + '}' + "\r\n"
//                + "\r\n";
//    }

    private String httpForm(String userName) {
        return '{' + Http.HTTP_LS
                + "\"user\" : " + this.userName + Http.HTTP_LS
                + "\"msgType\" : " + "\"all\"" + Http.HTTP_LS
                + '}' + Http.HTTP_LS
                + Http.HTTP_LS;
    }

}