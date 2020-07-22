package models;

import general.IOGeneral;

import java.io.DataInputStream;

public class Request {
    private final String status;
    private final String fullHttp;
    private final String bodyCommand;
    private final String bodyContent;

    public Request(String status, String fullHttp, String bodyCommand, String bodyContent) {
        this.status = status;
        this.fullHttp = fullHttp;
        this.bodyCommand = bodyCommand;
        this.bodyContent = bodyContent;
    }

    public String getStatus() {
        return status;
    }

    public String getFullHttp() {
        return fullHttp;
    }

    public String getBodyContent() {
        return bodyContent;
    }

    public String getBodyCommand() {
        return bodyCommand;
    }


    /* ####### static things ####### */

    public static Request ofDataInputStream(DataInputStream in) {
        var stringHttpRequest = IOGeneral.reedFromInput(in);
        return Request.httpToRequest(stringHttpRequest);
    }

    private static Request httpToRequest(String http) {
        var temp = http.split("\r\n");

        var status = temp[0];
        var fullHttp = http.substring(0, http.indexOf("body:"));
        var bodyTemp = http.substring(http.indexOf("body:"));
        var bodyCommand = bodyTemp.substring(bodyTemp.indexOf(":") + 2, bodyTemp.indexOf("\r\n"));
        var bodyContent = bodyTemp.substring(bodyTemp.indexOf("\r\n") + 2);

        return new Request(status, fullHttp, bodyCommand, bodyContent);
    }

}
