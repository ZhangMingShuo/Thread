//Nien Notebook 2 Chapter 1
//1.3.7 线程创建方法三：使用 Callable 和 FutureTask 创建线程
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

public class CreateDemoByCallableAndFuture {
    public static final int MAX_TURN = 5;
    public static final int COMPUTE_TIMES = 100000000;

    //①创建一个 Callable 接口的实现类
    static class ReturnableTask implements Callable<Long> {
        //②编写好异步执行的具体逻辑，并且可以有返回值
        public Long call() throws Exception{
            long startTime = System.currentTimeMillis();
            System.out.println(Thread.currentThread().getName()+ " 线程运行开始.");
            Thread.sleep(10000);
            for (int i = 0; i < COMPUTE_TIMES; i++) {
                int j = i * 10000;
            }
            long used = System.currentTimeMillis() - startTime;
            System.out.println(Thread.currentThread().getName()+ " 线程运行结束.");
            return used;
        }
    }
    public static void main(String[] args) throws InterruptedException {
        ReturnableTask task=new ReturnableTask();//③
        FutureTask<Long> futureTask = new FutureTask<>(task);//④
        Thread thread = new Thread(futureTask, "returnableThread");//⑤
        thread.start();//⑥
        Thread.sleep(2500);
        System.out.println(Thread.currentThread().getName()+ " 让子弹飞一会儿.");
        System.out.println(Thread.currentThread().getName()+ " 做一点自己的事情.");
        for (int i = 0; i < COMPUTE_TIMES / 2; i++) {
            int j = i * 10000;
        }
        System.out.println(Thread.currentThread().getName()+ " 获取并发任务的执行结果.");

        try {
            System.out.println(thread.getName()+"线程占用时间："+ futureTask.get());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName()+ " 运行结束.");
     }
}
