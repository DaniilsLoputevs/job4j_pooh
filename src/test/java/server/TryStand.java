package server;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class TryStand {
    @Test
    public void tryRun() {
        var list = new ArrayList<String >();
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
}