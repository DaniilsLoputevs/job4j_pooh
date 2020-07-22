package cliet;

import general.Http;
import general.IOGeneral;
import models.Message;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Publisher {
    public static final List<String> SIMPLE_LOG = new ArrayList<>();

    public boolean postMessage(Message message) {
        try (var socket = new Socket("localhost", 8080)) {
            try (var out = new DataOutputStream(socket.getOutputStream());
                 var in = new DataInputStream(socket.getInputStream())) {

                SIMPLE_LOG.add("PUB - write");
                var local = Http.makeRequest(Http.STATUS_POST, "ADD MSG JSON", message.toStringHttp());
                SIMPLE_LOG.add(local);
                SIMPLE_LOG.add("PUB - request finish");

                IOGeneral.writeToInput(out, local);
                var response = IOGeneral.reedFromInput(in);
                return Http.checkHttpResponseOk(response);

            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

}