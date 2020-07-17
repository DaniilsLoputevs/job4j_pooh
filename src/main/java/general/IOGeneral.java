package general;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Arrays;

public class IOGeneral {
    public static String reedFromInput(DataInputStream in) {
        StringBuilder rsl = new StringBuilder();
        byte[] buf = new byte[4096]; // 64 x 64
        int total;
        try {
//                while (in.available() <= 0) {
//                    System.out.println(Thread.currentThread().getName() + " wait in IOGeneral");
//                }
            System.out.println(in.available());
            if ((total = in.read(buf)) > -1) {
//            if (in.available() > 0) {
//                    total = in.read(buf);
                rsl.append(new String(Arrays.copyOfRange(buf, 0, total)));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return rsl.toString();
    }

    public static String writeToInput(DataOutputStream out, String string) {
        try {
            var temp = string.getBytes();;
            out.write(temp);
//            out.write(string.getBytes());
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return string;
    }
}
