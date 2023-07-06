import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.LockSupport;

public class ThreadUtil {
    /**
     * 定制的线程工厂
     */
    private static class CustomThreadFactory implements ThreadFactory
    {
        //线程池数量
        private static final AtomicInteger poolNumber = new AtomicInteger(1);
        private final ThreadGroup group;

        //线程数量
        private final AtomicInteger threadNumber = new AtomicInteger(1);
        private final String threadTag;

        CustomThreadFactory(String threadTag)
        {
            SecurityManager s = System.getSecurityManager();
            group = (s != null) ? s.getThreadGroup() :
                    Thread.currentThread().getThreadGroup();
            this.threadTag = "apppool-" + poolNumber.getAndIncrement() + "-" + threadTag + "-";
        }

        @Override
        public Thread newThread(Runnable target)
        {
            Thread t = new Thread(group, target,
                    threadTag + threadNumber.getAndIncrement(),
                    0);
            if (t.isDaemon())
            {
                t.setDaemon(false);
            }
            if (t.getPriority() != Thread.NORM_PRIORITY)
            {
                t.setPriority(Thread.NORM_PRIORITY);
            }
            return t;
        }
    }
    /**
     * 顺序排队执行
     *
     * @param command
     */
    public static void seqExecute(Runnable command)
    {
        getSeqOrScheduledExecutorService().execute(command);
    }

    /**
     * 获取可调度线程池（包含提交延迟、定时、周期性、顺序性执行的任务）
     *
     * @return
     */
    public static ScheduledThreadPoolExecutor getSeqOrScheduledExecutorService()
    {
        return SeqOrScheduledTargetThreadPoolLazyHolder.EXECUTOR;
    }

    //懒汉式单例创建线程池：用于定时任务、顺序排队执行任务
    static class SeqOrScheduledTargetThreadPoolLazyHolder
    {
        //线程池：用于定时任务、顺序排队执行任务
        static final ScheduledThreadPoolExecutor EXECUTOR = new ScheduledThreadPoolExecutor(
                1,
                new CustomThreadFactory("seq"));

        static
        {
            //JVM关闭时的钩子函数
            Runtime.getRuntime().addShutdownHook(
                    new ShutdownHookThread("定时和顺序任务线程池", new Callable<Void>()
                    {
                        @Override
                        public Void call() throws Exception
                        {
                            //优雅关闭线程池
                            shutdownThreadPoolGracefully(EXECUTOR);
                            return null;
                        }
                    }));
        }

    }

    public static void shutdownThreadPoolGracefully(ExecutorService threadPool)
    {
        if (!(threadPool instanceof ExecutorService) || threadPool.isTerminated())
        {
            return;
        }
        try
        {
            threadPool.shutdown();   //拒绝接受新任务
        } catch (SecurityException e)
        {
            return;
        } catch (NullPointerException e)
        {
            return;
        }
        try
        {
            // 等待 60 s，等待线程池中的任务完成执行
            if (!threadPool.awaitTermination(60, TimeUnit.SECONDS))
            {
                // 调用 shutdownNow 取消正在执行的任务
                threadPool.shutdownNow();
                // 再次等待 60 s，如果还未结束，可以再次尝试，或则直接放弃
                if (!threadPool.awaitTermination(60, TimeUnit.SECONDS))
                {
                    System.err.println("线程池任务未正常执行结束");
                }
            }
        } catch (InterruptedException ie)
        {
            // 捕获异常，重新调用 shutdownNow
            threadPool.shutdownNow();
        }
        //任然没有关闭，循环关闭1000次，每次等待10毫秒
        if (!threadPool.isTerminated())
        {
            try
            {
                for (int i = 0; i < 1000; i++)
                {
                    if (threadPool.awaitTermination(10, TimeUnit.MILLISECONDS))
                    {
                        break;
                    }
                    threadPool.shutdownNow();
                }
            } catch (InterruptedException e)
            {
                System.err.println(e.getMessage());
            } catch (Throwable e)
            {
                System.err.println(e.getMessage());
            }
        }
    }

    /**
     * 线程睡眠
     *
     * @param second 秒
     */
    public static void sleepSeconds(int second)
    {
        LockSupport.parkNanos(second * 1000L * 1000L * 1000L);
    }
    /**
     * 线程睡眠
     *
     * @param millisecond 毫秒
     */
    public static void sleepMilliSeconds(int millisecond)
    {
        LockSupport.parkNanos(millisecond * 1000L * 1000L);
    }
    /**
     * 获取当前线程名称
     */
    public static String getCurThreadName()
    {
        return Thread.currentThread().getName();
    }
    /**
     * 空闲保活时限，单位秒
     */
    private static final int KEEP_ALIVE_SECONDS = 30;

    /**
     * 有界队列size
     */
    private static final int QUEUE_SIZE = 10000;
    public static final String MIXED_THREAD_AMOUNT = "mixed.thread.amount";
    private static final int MIXED_MAX = 128;  //最大线程数

    //懒汉式单例创建线程池：用于混合型任务
    private static class MixedTargetThreadPoolLazyHolder
    {
        //首先从环境变量 mixed.thread.amount 中获取预先配置的线程数
        //如果没有对 mixed.thread.amount 做配置，则使用常量 MIXED_MAX 作为线程数
        private static final int max = (null != System.getProperty(MIXED_THREAD_AMOUNT)) ?
                Integer.parseInt(System.getProperty(MIXED_THREAD_AMOUNT)) : MIXED_MAX;
        //线程池： 用于混合型任务
        private static final ThreadPoolExecutor EXECUTOR = new ThreadPoolExecutor(
                max,
                max,
                KEEP_ALIVE_SECONDS,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue(QUEUE_SIZE),
                new CustomThreadFactory("mixed"));

        static
        {
            EXECUTOR.allowCoreThreadTimeOut(true);
            //JVM关闭时的钩子函数
            Runtime.getRuntime().addShutdownHook(new ShutdownHookThread("混合型任务线程池", new Callable<Void>()
            {
                @Override
                public Void call() throws Exception
                {
                    //优雅关闭线程池
                    shutdownThreadPoolGracefully(EXECUTOR);
                    return null;
                }
            }));
        }
    }

    /**
     * 获取执行混合型任务的线程池     *
     *
     * @return
     */
    public static ThreadPoolExecutor getMixedTargetThreadPool()
    {
        return MixedTargetThreadPoolLazyHolder.EXECUTOR;
    }

}
