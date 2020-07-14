package models;

public class Message {
    private final String target;
    private final String queue;
    private final String body;

    public Message(String target, String queue, String body) {
        this.target = target;
        this.queue = queue;
        this.body = body;
    }

    public String getTarget() {
        return target;
    }

    public String getBody() {
        return body;
    }

    @Override
    public String toString() {
        return '{' + "\r\n"
                + "\"target\" :" + "\"" + this.target + "\"" + "\r\n"
                + "\"queue\" :" + "\"" + this.queue + "\"" + "\r\n"
                + "\"text\" :" + "\"" + this.body + "\"" + "\r\n"
                + '}';
    }
}
