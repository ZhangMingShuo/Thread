//Nien Notebook 2 Chapter 1
//1.5.6 线程的 daemon 操作

public class DaemonDemo
{
    public static final int SLEEP_GAP = 500; //每一轮的睡眠时长
    public static final int MAX_TURN = 4; //用户线程执行轮次
    public static Thread getCurThread()
    {
        return Thread.currentThread();
    }
    //守护线程实现类
    static class DaemonThread extends Thread
    {

        public DaemonThread()
        {
            super("daemonThread");
        }

        public void run()
        {
            Print.synTco("--daemon线程开始.");

            for (int i = 1; ; i++)
            {
                Print.synTco("--轮次：" + i + "--守护状态为:" + isDaemon());
                // 线程睡眠一会
                ThreadUtil.sleepMilliSeconds(SLEEP_GAP);
            }
        }

    }


    public static void main(String args[]) throws InterruptedException
    {

        Thread daemonThread = new DaemonThread();
        daemonThread.setDaemon(true);
        daemonThread.start();

        Thread userThread = new Thread(() ->
        {
            Print.synTco(">>用户线程开始.");
            for (int i = 1; i <= MAX_TURN; i++)
            {
                Print.synTco(">>轮次：" + i + " -守护状态为:" + getCurThread().isDaemon());
                ThreadUtil.sleepMilliSeconds(SLEEP_GAP);
            }
            Print.synTco(">>用户线程结束.");
        }, "userThread");
        userThread.start();
        //主线程合入userThread，等待userThread执行完成
//        userThread.join();
        Print.synTco(" 守护状态为:" + getCurThread().isDaemon());

        Print.synTco(" 运行结束.");
    }

}
