//Nien Notebook 2 Chapter 1
//1.6.5 线程池的任务调度流程
import org.junit.Test;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class CreateByThreadPoolExecutor {
    @Test
    public void testThreadPoolExecutor() {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                1,
                100,
                100,
                TimeUnit.SECONDS,
                new LinkedBlockingDeque<>(100)
        );
        for(int i = 0; i < 5; i++){
            int taskIndex = i;
            executor.execute(()->{
                Print.tco("taskIndex = " + taskIndex);
                try {
                    Thread.sleep(Long.MAX_VALUE);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });
        }
        while(true){
            Print.tco("- activeCount:" + executor.getActiveCount() + "- taskCount:"+executor.getTaskCount());
            ThreadUtil.sleepSeconds(1);
        }
    }
}
