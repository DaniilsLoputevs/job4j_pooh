package server;

import general.IOGeneral;
import models.Message;
import models.Request;
import models.Response;

import java.io.DataOutputStream;

public class ServerLogic {

    public void processing(MessageBroker broker, Request request, DataOutputStream out) {
        var msg = Message.requestBodyToMessage(request.getBodyContent());
//        Message msg = Message.of(request);

        if (request.getStatus().contains("POST")) {
            if (request.getBodyCommand().equals("ADD MSG JSON")) {
                broker.addMsg(msg);
                IOGeneral.writeToInput(out, sendOkResponse());

            } else if (request.getBodyCommand().equals("REG")) {
//                this.threadUserName = ServerCodeTools.parseRequestReg(request.getBody());
//                this.subscribersAtomicCount.incrementAndGet();
//                super.setName("THREAD-SUB-" + this.threadUserName);
//                broker.registerQueue(threadUserName);

                broker.registerQueue(msg.getQueue());
                IOGeneral.writeToInput(out, sendOkResponse());

//                IOGeneral.writeToInput(out, Response.HTTP + "REG");
//                System.out.println("User was registered: " + this.threadUserName);
            }
        } else if (request.getStatus().contains("GET")) {
            if (request.getBodyCommand().equals("GET MSG JSON")) {
                var responseList = broker.getSubscriberMsg(msg.getQueue());
                responseList.forEach(response -> IOGeneral.writeToInput(out, response.toString()));
            }
        }
    }

    private String sendOkResponse() {
        return Response.HTTP + "\r\n";
    }
}
