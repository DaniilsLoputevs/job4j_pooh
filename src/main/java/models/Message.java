package models;

import java.util.Stack;

/**
 * Examples:
 * TOPIC -
 * "queue" : "public"
 * "text" : "bodyText"
 * <p>
 * QUEUE -
 * "queue" : "subscriber's name"
 * "text" : "bodyText"
 */
public class Message {
    private final String queue;
    private final String body;

    public Message(String queue, String body) {
        this.queue = "\"" + queue + "\"";
        this.body = "\"" + body + "\"";
    }

    public String getQueue() {
        return queue;
    }

    public String getBody() {
        return body;
    }

    public String toStringHttp() {
        return '{' + "\r\n"
                + "\"queue\" : " + this.queue + "\r\n"
                + "\"text\" : " + this.body + "\r\n"
                + '}' + "\r\n"
                + "\r\n";
    }


    /* ####### static things ####### */

    public static Message of(Request request) {
        var arr = request.getBodyContent().split("\r\n");

//        System.out.println("\r\n### LOOK PARSE ###");
//        System.out.println("of Request: ");
//        for (int i = 0; i < arr.length; i++) {
//            System.out.println(arr[i]);
//        }
//        System.out.println("### LOOK PARSE ###\r\n");
        return null;
    }

    public static Message requestBodyToMessage(String requestBody) {
        var temp = requestBody.split("\r\n");
//        System.out.println("request body: " + requestBody);

        var queue = parseJsonStringToValue(temp, temp.length - 3);
        var body = parseJsonStringToValue(temp, temp.length - 2);

        return new Message(queue, body);
    }

    private static String parseJsonStringToValue(String[] arr, int numArrElem) {
//        System.out.println("\r\n### LOOK PARSE ###");
//        System.out.println("arr length: " + arr.length);
//        System.out.println("arr content: ");
//        for (int i = 0; i < arr.length; i++) {
//            System.out.println(arr[i]);
//        }
//        System.out.println("numArrElem: " + numArrElem);
//        System.out.println("### LOOK PARSE ###\r\n");
        var fromIndex = (arr[numArrElem].indexOf(":") + 3);
        var lastIndex = (arr[numArrElem].length() - 1);
        var rsl = arr[numArrElem].substring(fromIndex, lastIndex);
        return rsl;
    }

}