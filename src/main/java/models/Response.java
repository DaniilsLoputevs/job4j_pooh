package models;

import general.Http;

public class Response {
    private final Message message;

    public Response(Message message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return Http.STATUS_POST + Http.HTTP_DEFAULT_TEMPLATE
                + "Content-Length: " + message.getBody().length() + Http.HTTP_LS
                + "body: JSON" + Http.HTTP_LS
                + message.toStringHttp();
    }

}