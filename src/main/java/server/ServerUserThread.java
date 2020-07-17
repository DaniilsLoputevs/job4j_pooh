package server;

import general.IOGeneral;
import general.ServerCodeTools;
import models.Message;
import models.Request;
import models.Response;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class ServerUserThread extends Thread {
    private final Socket clientDialog;
    private final MessageBroker broker;
    private final AtomicInteger subscribersAtomicCount ;
    /**
     * var == null >>> Thread work with Publisher.
     * var != null >>> Thread work with Subscriber.
     */
    private String threadUserName = null;

    public ServerUserThread(Socket client, MessageBroker broker, AtomicInteger subscribersAtomicCount) {
        this.clientDialog = client;
        this.broker = broker;
        this.subscribersAtomicCount = subscribersAtomicCount;
    }

    @Override
    public void run() {
        try (var out = new DataOutputStream(clientDialog.getOutputStream());
             var in = new DataInputStream(clientDialog.getInputStream())) {

            //////////////////////////////////////////////////////////////////////////////////////////////
            // основная рабочая часть //
            //////////////////////////////////////////////////////////////////////////////////////////////


            while (clientDialog.isConnected()) {
                System.out.println(getName() + ": SERVER: Server reading from channel");

                var stringHttpRequest = IOGeneral.reedFromInput(in);
                var request = Request.httpToRequest(stringHttpRequest);

                System.out.println(getName() + ": SERVER: accept message: \r\n" + stringHttpRequest + "");

                if ((request.getStatus().contains("POST"))) {

                    if ((request.getBody().contains("JSON"))) {
                        var tempMessage = Message.requestBodyToMessage(request.getBody());
                        broker.addMessage(tempMessage);

                    } else if ((request.getBody().contains("REG"))) {
                        this.threadUserName = ServerCodeTools.parseRequestReg(request.getBody());
                        this.subscribersAtomicCount.incrementAndGet();
                        super.setName("THREAD-SUB-" + this.threadUserName);
                        broker.registerQueue(threadUserName);

                        IOGeneral.writeToInput(out, Response.HTTP + "REG");
                        System.out.println("User was registered: " + this.threadUserName);
                    }
                }

//                var tempString = "SERVER: Server reply client message: \"" + stringHttpRequest + "\"";

                if (threadUserName != null) {
                    var responseList = new ArrayList<Response>();
                    broker.setAllPublicMessages(responseList, threadUserName, subscribersAtomicCount);
                    ServerCodeTools.sendAllResponse(out, responseList);
                }

//                System.out.println(getName() + "SERVER: Server sent message");
            }

            //////////////////////////////////////////////////////////////////////////////////////////////
            // основная рабочая часть //
            //////////////////////////////////////////////////////////////////////////////////////////////

            // если условие выхода - верно выключаем соединения
            System.out.println(getName() + ": SERVER: Client disconnected");
            System.out.println(getName() + ": SERVER: Closing connections & channels.");

            // закрываем сначала каналы сокета !
            in.close();
            out.close();
            clientDialog.close();

            System.out.println(getName() + ": SERVER: Closing connections & channels - DONE.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
