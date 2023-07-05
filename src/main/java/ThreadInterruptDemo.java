//Nien Notebook 2 Chapter 1
//1.5.3 线程的 interrupt 操作

import org.junit.Test;

public class ThreadInterruptDemo
{
    public static final int SLEEP_GAP = 5000;//睡眠时长
    public static final int MAX_TURN = 50;//睡眠次数
    static class SleepThread extends Thread
    {
        static int threadSeqNumber = 1;
        public SleepThread()
        {
            super("sleepThread-" + threadSeqNumber);
            threadSeqNumber++;
        }
        public void run()
        {
            try
            {
                Print.tco(getName() + " 进入睡眠.");
                // 线程睡眠一会
                Thread.sleep(SLEEP_GAP);
            } catch (InterruptedException e)
            {
                e.printStackTrace();
                Print.tco(getName() + " 发生被异常打断.");
                return;
            }
            Print.tco(getName() + " 运行结束.");
        }
    }
    public static void main(String[] args) throws InterruptedException
    {
        Thread thread1 = new SleepThread();
        thread1.start();
        Thread thread2 = new SleepThread();
        thread2.start();
        ThreadUtil.sleepSeconds(2);//主线程等待 2 秒
        thread1.interrupt(); //打断线程 1
        ThreadUtil.sleepSeconds(5);//主线程等待 5 秒
        thread2.interrupt(); //打断线程 2，此时线程 2 已经终止
        ThreadUtil.sleepSeconds(1);//主线程等待 1 秒
        Print.tco("程序运行结束.");
    }

    //测试用例：获取异步调用的结果
    @Test
    public void testInterrupted2()
    {
        Thread thread2 = new Thread(){
            @Override
            public void run() {
                System.out.println("hello thread");
            }
        };
        thread2.start();
        Thread thread = new Thread()
        {
            public void run()
            {
                Print.tco("线程启动了");
                //一直循环
                while (true)
                {
                    Print.tco(isInterrupted());
                    ThreadUtil.sleepMilliSeconds(SLEEP_GAP);

                    //如果调用 interrupt 为true，退出死循环
                    if (isInterrupted())
                    {
                        Print.tco("线程结束了");
                        return;
                    }
                }
            }
        };
        thread.start();
        ThreadUtil.sleepSeconds(2);//等待2秒
        thread.interrupt(); //打断线程1
        ThreadUtil.sleepSeconds(2);//等待2秒
        thread.interrupt();
    }
}
