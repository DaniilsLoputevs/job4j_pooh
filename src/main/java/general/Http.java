package general;

public class Http {
    /**
     * Line separate in http protocol.
     */
    public static final String HTTP_LS = "\r\n";
    public static final String STATUS_POST = "POST/";
    public static final String STATUS_GET = "GET/";

    /**
     * Prepare HTTP request before sending on server.
     *
     * @param httpStatus    -
     * @param serverCommand -
     * @param bodyContent   -
     * @return - full complete http request in {@code String} format.
     */
    public static String makeRequest(String httpStatus, String serverCommand, String bodyContent) {
        return httpStatus + "HTTP/1.1 200 OK" + HTTP_LS
                + "server.Server: job4j/2020-07-12" + HTTP_LS
                + "Content-Type: text/html" + HTTP_LS
                + "Content-Length: " + bodyContent.length() + HTTP_LS
                + "body: " + serverCommand + HTTP_LS
                + bodyContent;
    }

    public static boolean checkResponse(String response) {
        return response.split(HTTP_LS)[0].contains("OK");
    }
}
