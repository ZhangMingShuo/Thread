//Nien Notebook 2 Chapter 1
//1.6.9 线程池的拒绝策略
import org.junit.Test;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class ThreadCustomIgnorePolicyDemo {
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

        //一个简单的线程工厂
        static public class SimpleThreadFactory implements ThreadFactory {
            static AtomicInteger threadNo = new AtomicInteger(1);

            //实现其唯一的创建线程方法
            @Override
            public Thread newThread(Runnable target) {
                String threadName = "simpleThread-" + threadNo.get();
                Print.tco("创建一条线程，名称为：" + threadName);
                threadNo.incrementAndGet();
                Thread thread = new Thread(target, threadName);
                thread.setDaemon(true);
                return thread;
            }
        }

        //自定义拒绝策略
        public static class CustomIgnorePolicy implements RejectedExecutionHandler {
            public void rejectedExecution(Runnable r, ThreadPoolExecutor e) {
                // 可做日志记录等
                Print.tco(r + " rejected; " + " - getTaskCount: " + e.getTaskCount());
            }
        }
        @Test
        public void testCustomIgnorePolicy() {
            int corePoolSize = 2; //核心线程数
            int maximumPoolSize = 4;  //最大线程数
            long keepAliveTime = 10;
            TimeUnit unit = TimeUnit.SECONDS;
            //最大排队任务数
            BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<>(2);
            //线程工厂
            ThreadFactory threadFactory = new SimpleThreadFactory();
            //拒绝和异常策略
            RejectedExecutionHandler policy = new CustomIgnorePolicy();
            ThreadPoolExecutor pool = new ThreadPoolExecutor(
                    corePoolSize,
                    maximumPoolSize,
                    keepAliveTime, unit,
                    workQueue,
                    threadFactory,
                    policy);

            // 预启动所有核心线程
            pool.prestartAllCoreThreads();
            for (int i = 1; i <= 10; i++) {
                pool.execute(new TargetTask());
            }
            //等待10秒
            ThreadUtil.sleepSeconds(10);
            Print.tco("关闭线程池");
            pool.shutdown();
        }
    }
}
