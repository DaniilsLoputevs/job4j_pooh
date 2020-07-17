package example;

import general.IOGeneral;

import java.io.*;
import java.net.Socket;

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
                    var in = new DataInputStream(clientDialog.getInputStream());
                ) {

                System.out.println("SERVER: DataInputStream created");
                System.out.println("SERVER: in stream value: " + in);

                System.out.println("SERVER: DataOutputStream  created");
                System.out.println("SERVER: out stream value: " + out);
                //////////////////////////////////////////////////////////////////////////////////////////////
                // основная рабочая часть //
                //////////////////////////////////////////////////////////////////////////////////////////////

                // начинаем диалог с подключенным клиентом в цикле, пока сокет не
                // закрыт клиентом
                while (clientDialog.isConnected()) {
                    System.out.println("SERVER: Server reading from channel");

                    // серверная нить ждёт в канале чтения (inputstream) получения
                    // данных клиента после получения данных считывает их


                    var clientRequest = IOGeneral.reedFromInput(in);

                    // и выводит в консоль
                    System.out.println("SERVER: client message: \"" + clientRequest + "\"");

                    // отправляем эхо обратно клиенту

                    System.out.println("SERVER: Server try writing to channel");

                    var tempString = "Server reply client message: \"" + clientRequest + "\" - OK";
                    IOGeneral.writeToInput(out, tempString);
                    System.out.println("SERVER: " + tempString);


                    // освобождаем буфер сетевых сообщений

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
            } catch (IOException e) {
                e.printStackTrace();
            }
//        }
    }
}
