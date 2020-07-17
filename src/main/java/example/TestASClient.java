package example;

import general.IOGeneral;

import java.io.*;
import java.net.Socket;

public class TestASClient {

    public static void main(String[] args) throws InterruptedException {

        // запускаем подключение сокета по известным координатам и нициализируем приём сообщений с консоли клиента
        try (var socket = new Socket("localhost", 8080);
             var br = new BufferedReader(new InputStreamReader(System.in));
             var out = new DataOutputStream(socket.getOutputStream());
             var in = new DataInputStream(socket.getInputStream());
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

                    var temp = IOGeneral.writeToInput(out, clientCommand);

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

                    var serverResponse = IOGeneral.reedFromInput(in);
                    System.out.println(serverResponse);
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