package models;

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

    @Override
    public String toString() {
        return '{' + "\r\n"
                + "\"queue\" : " + this.queue + "\r\n"
                + "\"text\" : " + this.body + "\r\n"
                + '}';
    }


    /* ####### static things ####### */

    public static Message requestBodyToMessage(String requestBody) {
        var temp = requestBody.split("\r\n");

        var queue = parseJsonStringToValue(temp, temp.length - 3);
        var body = parseJsonStringToValue(temp, temp.length - 2);

        return new Message(queue, body);
    }

    private static String parseJsonStringToValue(String[] arr, int numArrElem) {
        var fromIndex = (arr[numArrElem].indexOf(":") + 2);
        var lastIndex = (arr[numArrElem].length() - 1);
        return arr[numArrElem].substring(fromIndex, lastIndex);
    }

}