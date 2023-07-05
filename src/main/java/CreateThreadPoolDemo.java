//Nien Notebook 2 Chapter 1
//1.6.2 Executors 四个快捷创建线程池方法

import org.junit.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;


public class CreateThreadPoolDemo {

    public static final int SLEEP_GAP = 500;
    public static final int MAX_TURN = 5;

    //异步的执行目标类
    public static class TargetTask implements Runnable {
        static AtomicInteger taskNo = new AtomicInteger(1);
        protected String taskName;

        public TargetTask() {
            taskName = "task-" + taskNo.get();
            taskNo.incrementAndGet();
        }

        public void run() {

            Print.tco("任务：" + taskName + " doing");
            // 线程睡眠一会
            ThreadUtil.sleepMilliSeconds(SLEEP_GAP);

            Print.tco(taskName + " 运行结束.");
        }

        @Override
        public String toString() {
            return "TargetTask{" + taskName + '}';
        }
    }
    @Test
    public void testSingleThreadExecutor()
    {
        ExecutorService pool = Executors.newSingleThreadExecutor();
        for (int i = 0; i < 5; i++)
        {
            pool.execute(new TargetTask());
            pool.submit(new TargetTask());
        }
        ThreadUtil.sleepSeconds(1000);
        //关闭线程池
        pool.shutdown();
    }
    @Test
    public void testNewFixedThreadPool(){
        ExecutorService pool = Executors.newFixedThreadPool(3);
        for (int i = 0; i < 5; i++)
        {
            pool.execute(new TargetTask());
            pool.submit(new TargetTask());
        }
        ThreadUtil.sleepSeconds(1000);
        //关闭线程池
        pool.shutdown();
    }
    @Test
    public void testNewCacheThreadPool()
    {
        ExecutorService pool = Executors.newCachedThreadPool();
        for (int i = 0; i < 5; i++)
        {
            pool.execute(new TargetTask());
            pool.submit(new TargetTask());
        }
        ThreadUtil.sleepSeconds(1000);
        //关闭线程池
        pool.shutdown();
    }
    @Test
    public void testNewScheduledThreadPool()
    {
        ScheduledExecutorService scheduled =
                Executors.newScheduledThreadPool(2);
        for (int i = 0; i < 2; i++)
        {
            scheduled.scheduleAtFixedRate(new TargetTask(),
                    0, 1000, TimeUnit.MILLISECONDS);
            //以上的参数中：
            // 0 表示首次执行任务的延迟时间，500 表示每次执行任务的间隔时间
            //TimeUnit.MILLISECONDS 执行的时间间隔数值单位为毫秒
        }
        ThreadUtil.sleepSeconds(1000);
        //关闭线程池
        scheduled.shutdown();
    }
}
