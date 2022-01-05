package server;

import org.junit.Test;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TryStand {


    @Test
    public void tryRun() {
        ExecutorService service = Executors.newFixedThreadPool(5);
//        service.wait();

//2 помещаем в него задачу для выполнения
//        Future<String> task = service.submit(new Callable<>("Amigo") {
//            @Override
//            public String call() throws Exception {
//                return null;
//            }
//        });


//3 ждем пока задача выполнится
//        while(!task.isDone())
//        {
//            Thread.sleep(1);
//        }

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

}