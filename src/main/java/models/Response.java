package models;

public class Response {
    public static final String HTTP = "POST/HTTP/1.1 200 OK\r\n"
            + "server.Server: job4j/2020-07-12\r\n"
            + "Content-Type: text/html\r\n";
    private final Message message;

    public Response(Message message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return HTTP
                + "Content-Length: " + message.getBody().length() + "\r\n"
                + "body: JSON\r\n"
                + message.toStringHttp()
                + "\r\n";
    }

}
