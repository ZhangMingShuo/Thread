import java.util.concurrent.*;

//1.3.8 线程创建方法四：通过线程池创建线程
public class CreateDemoByExecutors {
    public static final int MAX_TURN = 5;
    public static final int COMPUTE_TIMES = 100000000;
    //创建一个包含三个线程的线程池
    private static ExecutorService pool = Executors.newFixedThreadPool(3);
    static class DemoThread implements Runnable
    {
        @Override
        public void run()
        {
            for (int j = 1; j < MAX_TURN; j++)
            {
                System.out.println(Thread.currentThread().getName() + ", 轮次：" + j);
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }

    }

    static class ReturnableTask implements Callable<Long>{

        @Override
        public Long call() throws Exception {
            long startTime = System.currentTimeMillis();
            System.out.println(Thread.currentThread().getName()+"运行开始");
            for(int j =1 ;j < MAX_TURN; j++){
                System.out.println(Thread.currentThread().getName()+",轮次:" +j);
                Thread.sleep(10);
            }
            long used = System.currentTimeMillis() - startTime;
            System.out.println(Thread.currentThread().getName()+" 线程运行结束");
            return used;
        }
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        pool.execute(new DemoThread()); //方式一：执行线程实例，无返回
        //方式一：执行 Runnable 执行目标实例，无返回
        pool.execute(new Runnable()
        {
            @Override
            public void run()
            {
                for (int j = 1; j < MAX_TURN; j++)
                {
                    System.out.println(Thread.currentThread().getName()+",轮次:" +j);
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
        //提交 Callable 执行目标实例，有返回
        Future<Long> future = pool.submit(new ReturnableTask());
        Long result = (Long) future.get();
        System.out.println("异步任务的执行结果为:" + result);
        Thread.sleep(Integer.MAX_VALUE);
    }
}
