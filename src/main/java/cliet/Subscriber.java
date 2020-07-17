package cliet;

import cliet.additional.TestHelper;
import general.IOGeneral;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class Subscriber {
    private final String userName;
    private final Socket socket;

//    private static String logPath = "C:\\Danik\\Projects\\job4j_pooh\\src\\test\\java\\sub_log.txt";
    public static final List<String> log = new ArrayList<>();

    public Subscriber(String userName, Socket socket) {
        this.userName = userName;
        this.socket = socket;
    }

    public boolean register() {
//        try (var input = new BufferedReader(new InputStreamReader(socket.getInputStream(),
//                StandardCharsets.UTF_8));
//             var output = new PrintWriter(socket.getOutputStream())) {
        try (var out = new DataOutputStream(socket.getOutputStream());
             var in = new DataInputStream(socket.getInputStream())) {

//            System.out.println("\r\n### SPECIAL ###");

//            sendRequestReg(output);
            log.add("SUB - write");
            IOGeneral.writeToInput(out, sendRequestReg());

//            System.out.println("### SPECIAL ###\r\n");

            log.add("SUB - read");
//            var response = ClientTalk.receiveResponse(input);
            var response = IOGeneral.reedFromInput(in);
            log.add("SUB - read - closed");

            System.out.println("\r\n### SPECIAL ###");
            System.out.println(ClientTalk.checkResponse(response));
            System.out.println("### SPECIAL ###\r\n");

            return ClientTalk.checkResponse(response);

        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public void waitAndReadResponse(Consumer<String> howToOutput) {
        try (var input = new BufferedReader(new InputStreamReader(socket.getInputStream(),
                StandardCharsets.UTF_8))) {

            var response = ClientTalk.receiveResponse(input);
            howToOutput.accept(response);

        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Prepare & send HTTP request for registration on server.
     *
     * @param output - socket's output.
     */
//    private void sendRequestReg(PrintWriter output) {
//        var temp = "HTTP/1.1 200 OK\r\n"
//                + "server.Server: job4j/2020-07-12\r\n"
//                + "Content-Type: text/html\r\n"
//                + "Content-Length: " + this.userName.length() + "\r\n"
//                + "body: REG\r\n"
//                + this.userName;
//        output.println(temp);
//        output.flush();
//    }
    private String sendRequestReg() {
        return "HTTP/1.1 200 OK\r\n"
                + "server.Server: job4j/2020-07-12\r\n"
                + "Content-Type: text/html\r\n"
                + "Content-Length: " + this.userName.length() + "\r\n"
                + "body: REG\r\n"
                + this.userName;
    }

}