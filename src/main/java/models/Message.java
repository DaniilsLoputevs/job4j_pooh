package models;

import general.Http;

/**
 * Examples:
 * TOPIC -
 * "queue" : "TOPIC"
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
        return '{' + Http.HTTP_LS
                + "\"queue\" : " + this.queue + Http.HTTP_LS
                + "\"text\" : " + this.body + Http.HTTP_LS
                + '}' + Http.HTTP_LS
                + Http.HTTP_LS;
    }

    /* ####### static things ####### */

    public static Message takeJsonMsgFromRequest(Request request) {
        var splitContent = request.getBodyContent().split(Http.HTTP_LS);

        var queue = parseJsonStringToValue(splitContent, splitContent.length - 3);
        var body = parseJsonStringToValue(splitContent, splitContent.length - 2);

        return new Message(queue, body);
    }

    private static String parseJsonStringToValue(String[] arr, int numArrElem) {
        /* Debug Prints */
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
        return arr[numArrElem].substring(fromIndex, lastIndex);
    }

}