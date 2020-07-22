package server;

import models.Request;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ServerUserThread extends Thread {
    private final Socket clientDialog;
    private final MessageBroker broker;

    public ServerUserThread(Socket client, MessageBroker broker) {
        this.clientDialog = client;
        this.broker = broker;
    }

    @Override
    public void run() {
        try (var out = new DataOutputStream(clientDialog.getOutputStream());
             var in = new DataInputStream(clientDialog.getInputStream())) {

            if (clientDialog.isConnected()) {

                System.out.println(getName() + ": SERVER: reading request from channel.");
                Request request = Request.ofDataInputStream(in);

                System.out.println(getName() + ": SERVER: processing request.");
                new ServerLogic().processing(broker, request, out);
            }
            System.out.println(getName() + ": SERVER: Client disconnected - close connections.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
