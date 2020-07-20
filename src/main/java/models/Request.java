package models;

import general.IOGeneral;

import java.io.DataInputStream;
import java.util.HashMap;
import java.util.Map;

public class Request {
    //    private final String status;
//    private final String fullHttp;
//    private final String bodyCommand;
//    private final String bodyContent;
    private final Map<String, String> bodyContentMap = new HashMap<>();

//    public Request(String status, String fullHttp, String bodyCommand, String bodyContent) {
//        this.status = status;
//        this.fullHttp = fullHttp;
//        this.bodyCommand = bodyCommand;
//        this.bodyContent = bodyContent;
//    }

    public String getStatus() {
//        return status;
        return bodyContentMap.get(KEY_STATUS);
    }

    public String getFullHttp() {
//        return fullHttp;
        return bodyContentMap.get(KEY_FULL_HTTP);
    }

    public String getBodyContent() {
//        return bodyContent;
        return bodyContentMap.get(KEY_CONTENT);
    }

    public String getBodyCommand() {
//        return bodyCommand;
        return bodyContentMap.get(KEY_COMMAND);
    }

    public String getAny(String key) {
        return bodyContentMap.get(key);
    }

    /* ####### static things ####### */
    public static final String KEY_STATUS = "STATUS";
    public static final String KEY_FULL_HTTP = "FULL_HTTP";
    public static final String KEY_COMMAND = "COMMAND";
    public static final String KEY_CONTENT = "CONTENT";

    public static Request ofDataInputStream(DataInputStream in) {
        var stringHttpRequest = IOGeneral.reedFromInput(in);
//        return Request.httpToRequest(stringHttpRequest);
        return Request.ofHttpAndJson(stringHttpRequest);
    }

    private static Request ofHttpAndJson(String http) {
        var temp = http.split("\r\n");
        var instance = new Request();

        instance.bodyContentMap.put(KEY_STATUS, temp[0]);

        var fullHttp = http.substring(0, http.indexOf("body:"));
        instance.bodyContentMap.put(KEY_FULL_HTTP, fullHttp);

        var bodyTemp = http.substring(http.indexOf("body:"));

        var bodyCommand = bodyTemp.substring(bodyTemp.indexOf(":") + 2, bodyTemp.indexOf("\r\n"));
        var bodyContent = bodyTemp.substring(bodyTemp.indexOf("\r\n") + 2);
        instance.bodyContentMap.put(KEY_COMMAND, bodyCommand);
        instance.bodyContentMap.put(KEY_CONTENT, bodyContent);

//        System.out.println("\r\n### SPECIAL INST ###");
//        System.out.println("Status: {" + instance.getStatus() + '}');
//        System.out.println("Command: {" + instance.getBodyCommand() + '}');
//        System.out.println("Content: {" + instance.getBodyContent() + '}');
//        System.out.println("### SPECIAL INST ###\r\n");
        return instance;
    }


//    private static Request httpToRequest(String http) {
//        var temp = http.split("\r\n");
//
//        var status = temp[0];
//        var fullHttp = http.substring(0, http.indexOf("body:"));
//        var bodyTemp = http.substring(http.indexOf("body:"));
//        var bodyCommand = bodyTemp.substring(bodyTemp.indexOf(":") + 2, bodyTemp.indexOf("\r\n"));
//        var bodyContent = bodyTemp.substring(bodyTemp.indexOf("\r\n") + 2);
//
////        return new Request(status, fullHttp, bodyCommand, bodyContent);
//        return null;
//    }

}
