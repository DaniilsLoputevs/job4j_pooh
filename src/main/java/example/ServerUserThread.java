package example;

import java.io.*;
import java.net.Socket;
import java.util.Arrays;

public class ServerUserThread implements Runnable {
    private final Socket clientDialog;

    public ServerUserThread(Socket client) {
        this.clientDialog = client;
    }

    @Override
    public void run() {
//        while (true) {
            // инициируем каналы общения в сокете, для сервера

            // Следует сначала инициализировать канал записи,
            // а потом канал чтения, - иначе есть риск блокировки при ожидании заголовка в сокете.
            try (
                    var out = new DataOutputStream(clientDialog.getOutputStream());
                    // канал чтения из сокета
                 var in = new DataInputStream(clientDialog.getInputStream());
//                    var out = new PrintWriter(clientDialog.getOutputStream(), true);
//                    var in = new BufferedReader(new InputStreamReader(clientDialog.getInputStream()));
                ) {

                System.out.println("SERVER: DataInputStream created");

                System.out.println("SERVER: DataOutputStream  created");
                //////////////////////////////////////////////////////////////////////////////////////////////
                // основная рабочая часть //
                //////////////////////////////////////////////////////////////////////////////////////////////

                // начинаем диалог с подключенным клиентом в цикле, пока сокет не
                // закрыт клиентом
                while (clientDialog.isConnected()) {
                    System.out.println("SERVER: Server reading from channel");

                    // серверная нить ждёт в канале чтения (inputstream) получения
                    // данных клиента после получения данных считывает их
//                    var entry = Arrays.toString(in.readAllBytes());
                    var entry = in.readLine();

                    // и выводит в консоль
                    System.out.println("SERVER: client message: \"" + entry + "\"");

                    // инициализация проверки условия продолжения работы с клиентом
                    // по этому сокету по кодовому слову - quit в любом регистре
                    if ((entry.equalsIgnoreCase("quit"))) {

                        // если кодовое слово получено то инициализируется закрытие
                        // серверной нити
                        System.out.println("Client initialize connections suicide ...");
                        out.writeUTF("Server reply - " + entry + " - OK");
//                        out.write("Server reply - " + entry + " - OK");
                        Thread.sleep(3000);
                        break;
                    }

                    // если условие окончания работы не верно - продолжаем работу -
                    // отправляем эхо обратно клиенту

                    System.out.println("SERVER: Server try writing to channel");

                    var tempString = "SERVER: Server reply client message: \"" + entry + "\" - OK";
                    out.writeBytes(tempString);
//                    out.write(tempString);
                    System.out.println(tempString);

                    System.out.println("SERVER: Server write reply into ClientSocket");

                    // освобождаем буфер сетевых сообщений
                    out.flush();
                    System.out.println("SERVER: Server send reply into ClientSocket");
                    // возвращаемся в начало для считывания нового сообщения
                }

                //////////////////////////////////////////////////////////////////////////////////////////////
                // основная рабочая часть //
                //////////////////////////////////////////////////////////////////////////////////////////////

                // если условие выхода - верно выключаем соединения
                System.out.println("SERVER: Client disconnected");
                System.out.println("SERVER: Closing connections & channels.");

                // закрываем сначала каналы сокета !
                in.close();
                out.close();

                // потом закрываем сокет общения с клиентом в нити моносервера
                clientDialog.close();

                System.out.println("SERVER: Closing connections & channels - DONE.");
                // Auto-generated catch block
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
            }
//        }
    }
}
