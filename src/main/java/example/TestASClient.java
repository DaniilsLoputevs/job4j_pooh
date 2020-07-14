package example;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class TestASClient {

    public static void main(String[] args) throws InterruptedException {
//        try (BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_16));
//             PrintWriter output = new PrintWriter(socket.getOutputStream()))
//        try (var out = new PrintWriter(clientScoket.getOutputStream(), true);
//             var in = new BufferedReader(new InputStreamReader(socke.getInputStream()))) {

        // запускаем подключение сокета по известным координатам и нициализируем приём сообщений с консоли клиента
        try (var socket = new Socket("localhost", 8080);
             var br = new BufferedReader(new InputStreamReader(System.in));
             var out = new DataOutputStream(socket.getOutputStream());
             var in = new DataInputStream(socket.getInputStream());
//             var out = new PrintWriter(socket.getOutputStream(), true);
//             var in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
             ) {


            System.out.println("Client connected to socket.");
            System.out.println();
            System.out.println("Client writing channel = out & reading channel = in initialized.");
            System.out.println("Enter your message: ");

            // проверяем живой ли канал и работаем если живой
            while (!socket.isOutputShutdown()) {

                // ждём консоли клиента на предмет появления в ней данных
                if (br.ready()) {


                    // данные появились - работаем
                    System.out.println("Client start writing in channel...");

                    Thread.sleep(1000);
                    String clientCommand = br.readLine();

                    // пишем данные с консоли в канал сокета для сервера
                    out.writeBytes(clientCommand);
//                    out.write(clientCommand);
                    out.flush();
                    System.out.println("Client sent message " + clientCommand + " to server.");
                    Thread.sleep(1000);
                    // ждём чтобы сервер успел прочесть сообщение из сокета и ответить

                    // проверяем условие выхода из соединения
                    if (clientCommand.equalsIgnoreCase("quit")) {

                        // если условие выхода достигнуто разъединяемся
                        System.out.println("Client kill connections");
                        Thread.sleep(2000);

                        // смотрим что нам ответил сервер на последок перед закрытием ресурсов
                        if ((in.read()) > -1) {
                            System.out.println("reading...");
//                            String serverResponse = in.readLine();
//                            System.out.println(serverResponse);
                        }

                        // после предварительных приготовлений выходим из цикла записи чтения
                        break;
                    }

                    // если условие разъединения не достигнуто продолжаем работу
                    System.out.println("Client sent message & start waiting for data from server...");
                    Thread.sleep(2000);

                    // проверяем, что нам ответит сервер на сообщение(за предоставленное
                    // ему время в паузе он должен был успеть ответить)

//                    byte[] temp;
                    int temp;
//                    if ((temp = in.readAllBytes())[0] > -1) {
                    if ((temp = in.readLine().length()) > -1) {
                        System.out.println(temp);
                        System.out.println("reading...");
//                        System.out.println("reading...");
//                        String serverResponse = Arrays.toString(in.readAllBytes());
                        String serverResponse = in.readLine();
//                        String stop = serverResponse;
                        System.out.println(serverResponse);
                    }

//                    if (in.read() > -1) {
//
//                        // если успел забираем ответ из канала сервера в сокете и
//                        // сохраняем её в in переменную, печатаем на свою клиентскую консоль
//                        System.out.println("reading...");
//                        String in = in.readUTF();
//                        String stop = in;
//                        System.out.println(in);
//                    }
                }
            }
            // на выходе из цикла общения закрываем свои ресурсы
            System.out.println("Closing connections & channels on clientSide - DONE.");
            System.out.println("### Next Message ###");

            // Auto-generated catch block
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}