package models;

public class Request {
    private final String status;
    private final String fullHttp;
    private final String body;

    public Request(String status, String fullHttp, String body) {
        this.status = status;
        this.fullHttp = fullHttp;
        this.body = body;
    }

    public String getStatus() {
        return status;
    }

    public String getFullHttp() {
        return fullHttp;
    }

    public String getBody() {
        return body;
    }

    /* ####### static things ####### */

    public static Request httpToRequest(String http) {
        var temp = http.split("\r\n");

        var status = temp[0];
        var fullHttp = http.substring(0, http.indexOf("body:"));
        var body = http.substring(http.indexOf("body:"));

        return new Request(status, fullHttp, body);
    }

}
