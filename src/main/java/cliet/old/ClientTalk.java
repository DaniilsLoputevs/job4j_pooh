package cliet.old;

import java.io.BufferedReader;
import java.io.IOException;

public class ClientTalk {
    public static String receiveResponse(BufferedReader input) {
        var response = new StringBuilder();
        try {
            while (!input.ready()) ;
            while (input.ready()) {
                response.append(input.readLine());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response.toString();
    }

    public static boolean checkResponse(String response) {
        var temp = response.split("\r\n");
        return temp[0].contains("OK");
    }
}
