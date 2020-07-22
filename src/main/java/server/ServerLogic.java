package server;

import general.Http;
import general.IOGeneral;
import models.Message;
import models.Request;
import models.Response;

import java.io.DataOutputStream;

public class ServerLogic {

    public void processing(MessageBroker broker, Request request, DataOutputStream out) {
        var msg = Message.takeJsonMsgFromRequest(request);

        /* Debug Prints */
//        debugPrint("\r\n### THREAD DEBUG ###\\r\\n");
//        debugPrint("Req status: " + request.getStatus());
//        debugPrint("Req command: " + request.getBodyCommand());
//        debugPrint("Req content: " + request.getBodyContent());

        if (request.getStatus().contains("POST")) {
//            debugPrint("flag: POST");
            if (request.getBodyCommand().equals("ADD MSG JSON")) {
//                debugPrint("flag: ADD");
                broker.addMsg(msg);
                IOGeneral.writeToInput(out, Http.HTTP_DEFAULT_OK);

            } else if (request.getBodyCommand().equals("REG")) {
//                debugPrint("flag: REG");
//                debugPrint("msg.getQueue(): " + msg.getQueue());
                broker.registerQueue(msg.getQueue());
//                debugPrint("send OK response");
                IOGeneral.writeToInput(out, Http.HTTP_DEFAULT_OK);
            }

        } else if (request.getStatus().contains("GET")) {
//            debugPrint("flag: GET");
            if (request.getBodyCommand().equals("GET MSG JSON")) {
//                debugPrint("flag: GET MSG JSON");

                Message subscriberMsg = broker.getSubscriberMsg(msg.getQueue());
                IOGeneral.writeToInput(out, new Response(subscriberMsg).toString());
            }
        }
        /* Debug Prints */
//        debugPrint("\r\n### THREAD DEBUG ###\r\n");
    }

    /**
     * Specially for debug!
     *
     * @param s -
     */
    private void debugPrint(String s) {
        System.out.println(Thread.currentThread().getName() + " : " + s);
    }
}
