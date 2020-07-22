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

    public static final List<String> SIMPLE_LOG = new ArrayList<>();

    public Subscriber(String userName) {
        this.userName = "\"" + userName + "\"";
    }

    public boolean register() {
        var rsl = false;
        try (var socket = new Socket("localhost", 8080)) {
            try (var out = new DataOutputStream(socket.getOutputStream());
                 var in = new DataInputStream(socket.getInputStream())) {

                var bodyContent = toHttpFormat();
                var localRequest = Http.makeRequest(Http.STATUS_POST, "REG", bodyContent);

                SIMPLE_LOG.add(localRequest);
                SIMPLE_LOG.add("SUB - request finish");
                IOGeneral.writeToInput(out, localRequest);

                SIMPLE_LOG.add("SUB - read");
                var response = IOGeneral.reedFromInput(in);
                SIMPLE_LOG.add("SUB - read - closed");

                rsl = Http.checkHttpResponseOk(response);
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

                var bodyContent = toHttpFormat();
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

    private String toHttpFormat() {
        return '{' + Http.HTTP_LS
                + "\"user\" : " + this.userName + Http.HTTP_LS
                + "\"msgType\" : " + "\"all\"" + Http.HTTP_LS
                + '}' + Http.HTTP_LS
                + Http.HTTP_LS;
    }

}