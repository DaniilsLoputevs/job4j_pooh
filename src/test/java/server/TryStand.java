package server;

import general.IOGeneral;
import oldserver.Server;
import org.junit.Test;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TryStand {



    @Test
    public void tryRun() {
        var list = new ArrayList<String>();
        method(list);

        var temp = list;
        temp.clear();
        list.forEach(System.out::println);
    }

    public void method(List<String> list) {
        list.add("111");
        list.add("222");
        list.add("333");
    }

    public void threadSleep(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void runClient() {
        System.out.println("### CLIENT - START ###");
        try (var socket = new Socket("localhost", 8080);) {
            try (var out = new DataOutputStream(socket.getOutputStream());
                 var in = new DataInputStream(socket.getInputStream());) {

                System.out.println("### client - write ###");
                // client - write
                IOGeneral.writeToInput(out, "response-try123");
//                out.write("response-try123".getBytes());
//                out.flush();
                System.out.println("### client - write - close ###");

//
//                StringBuilder rsl = new StringBuilder();
//                byte[] buf = new byte[4096]; // 64 x 64
//                int total;
////                try {
////                while (in.available() <= 0) {
////                    System.out.println(Thread.currentThread().getName() + " wait in IOGeneral");
////                }
//                System.out.println(in.available());
//                if ((total = in.read(buf)) > -1) {
////            if (in.available() > 0) {
////                    total = in.read(buf);
//                    rsl.append(new String(Arrays.copyOfRange(buf, 0, total)));
//                }
//                System.out.println("CLIENT - read: " + rsl.toString());
//                System.out.println("### client - read - close ###");

                threadSleep(1000);

                System.out.println("### client - read ###");
                // client - read
                var temp = IOGeneral.reedFromInput(in);
                System.out.println("CLIENT - read: " + temp);
            }

        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    @Test
    public void runServer() {
        System.out.println("### SERVER - START ###");
        try {
//            System.out.println("### INIT - start ###");
            var mainServerSocket = new ServerSocket(8080);
            var serverSocket = mainServerSocket.accept();

//            System.out.println("### INIT - finish ###");

            try (var sout = new DataOutputStream(serverSocket.getOutputStream());
                 var sin = new DataInputStream(serverSocket.getInputStream());
            ) {
                threadSleep(5000);
                System.out.println("### Main - start ###");


                System.out.println("### server - read ###");
                // server - read
                var temp = IOGeneral.reedFromInput(sin);
                System.out.println("SERVER - read: " + temp);

//                StringBuilder rsl1 = new StringBuilder();
//                byte[] buf1 = new byte[4096]; // 64 x 64
//                int total1;
//
////                while (sin.available() <= 0) {
////                    System.out.println(Thread.currentThread().getName() + " wait in IOGeneral");
////                }
//                System.out.println(sin.available());
//                if ((total1 = sin.read(buf1)) > -1) {
////            if (in.available() > 0) {
////                    total1 = in.read(buf);
//                    rsl1.append(new String(Arrays.copyOfRange(buf1, 0, total1)));
//                }
//                System.out.println("SERVER - read: " + rsl1.toString());
                System.out.println("### server - read - close ###");

                System.out.println("### server - write ###");
                // Server - write
                IOGeneral.writeToInput(sout, "Test-try123");
//                sout.write("".getBytes());
//                sout.flush();
                System.out.println("### server - write - close ###");


                threadSleep(2000);



            } catch (IOException e) {
                e.printStackTrace();
            }


        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}