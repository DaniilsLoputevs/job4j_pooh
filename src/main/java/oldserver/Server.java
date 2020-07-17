package oldserver;

import models.Message;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Server {
    private final List<Message> queue = new LinkedList<>();
    private final Map<String, LinkedList<Message>> bufferPrivateMessages = new ConcurrentHashMap<>();

    public void start() {
        try (var serverSocket = new ServerSocket(8080)) {
            System.out.println("Server started!");

            while (true) {
                // ожидаем подключения
                Socket socket = serverSocket.accept();
                System.out.println("Client connected!");

                // для подключившегося клиента открываем потоки
                // чтения и записи
                try (var input = new BufferedReader(new InputStreamReader(socket.getInputStream(),
                        StandardCharsets.UTF_8));
                     var output = new PrintWriter(socket.getOutputStream())) {

                    // ждем первой строки запроса
                    while (!input.ready()) ;

                    // считываем и печатаем все что было отправлено клиентом
                    System.out.println();
                    while (input.ready()) {
                        System.out.println(input.readLine());
                    }

                    // отправляем ответ
                    output.println("HTTP/1.1 200 OK");
                    output.println("Content-Type: text/html; charset=utf-8");
                    output.println();
                    output.println("<h1>Привет всем!</h1>");
                    output.println("<p>Привет всем!<p>");
                    output.flush();

                    // по окончанию выполнения блока try-with-resources потоки,
                    // а вместе с ними и соединение будут закрыты
                    System.out.println("Client disconnected!");
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }


    public static void main(String[] args) {
        Thread thread = new Thread(() -> {
            {
                for (int i = 0; i < 100000; i++) {
                    System.out.println(i);
                    if (Thread.currentThread().isInterrupted()) {
                        break;
                    }
                }
            }
        });
        thread.start();
        thread.interrupt();

//        Thread t = new Thread(
//                () -> {
//                    for (int i = 0; i < 100000; i++) {
//                        System.out.println(i);
//                    }
//                });


    }
}
