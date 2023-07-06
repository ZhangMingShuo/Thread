//Nien Notebook 2 Chapter 1
//1.6.8 调度器的钩子方法
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class CreateThreadPoolHooksDemo {
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

    //线程本地变量,用于记录线程异步任务的开始执行时间
    private static final ThreadLocal<Long> startTime = new ThreadLocal<>();

    @org.junit.Test
    public void testHooks() {
        ExecutorService pool = new ThreadPoolExecutor(2, //coreSize
                4, //最大线程数
                60,//空闲保活时长
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(2)) //等待队列
        {
            //继承：调度器终止钩子
            @Override
            protected void terminated() {
                Print.tco("调度器已经终止!");
            }

            //继承：执行前钩子
            @Override
            protected void beforeExecute(Thread t, Runnable target) {
                Print.tco(target + "前钩被执行");
                //记录开始执行时间
                startTime.set(System.currentTimeMillis());
                super.beforeExecute(t, target);
            }

            //继承：执行后钩子
            @Override
            protected void afterExecute(Runnable target, Throwable t)
            {
                super.afterExecute(target, t);
                //计算执行时长
                long time = (System.currentTimeMillis() - startTime.get()) ;
                Print.tco( target + " 后钩被执行, 任务执行时长（ms）：" + time);
                //清空本地变量
                startTime.remove();
            }
        };

        for (int i = 1; i <= 5; i++)
        {
            pool.execute(new TargetTask());
        }
        //等待 10 秒
        ThreadUtil.sleepSeconds(10);
        Print.tco("关闭线程池");
        pool.shutdown();
    }
}
