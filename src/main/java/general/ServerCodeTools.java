package general;

import models.Response;

import java.io.DataOutputStream;
import java.util.List;

public class ServerCodeTools {
    public static String parseRequestReg(String requestBody) {
        if (requestBody != null) {
            var temp = requestBody.split("\r\n");
            return temp[temp.length - 1];
        } else return "";
    }

    public static void sendAllResponse(DataOutputStream out, List<Response> responseList) {
        for (var response : responseList) {
            IOGeneral.writeToInput(out, response.toString());
        }
    }
}
