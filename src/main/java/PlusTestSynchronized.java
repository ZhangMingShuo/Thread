
//Nien Notebook 2 Chapter 2
//2.2.1 synchronized 同步方法
import java.util.concurrent.CountDownLatch;
class NotSafePlusSync
{
    private Integer amount = 0;
    //自增
    public synchronized void selfPlus()
    {
        amount++;
    }
    public Integer getAmount()
    {
        return amount;
    }
}

public class PlusTestSynchronized {
    final int MAX_TREAD = 10;
    final int MAX_TURN = 1000;
    @org.junit.Test
    public void testNotSafePlus() throws InterruptedException{
        //倒数闩，需要倒数 MAX_TREAD 次
        CountDownLatch latch = new CountDownLatch(MAX_TREAD);
        NotSafePlusSync counter = new NotSafePlusSync();
        Runnable runnable = () ->
        {
            for (int i = 0; i < MAX_TURN; i++)
            {
                counter.selfPlus();
            }
            latch.countDown(); // 倒数闩减少一次
        };
        for (int i = 0; i < MAX_TREAD; i++)
        {
            new Thread(runnable).start();
        }
        latch.await(); // 等待倒数闩的次数减少到 0，所有的线程执行完成
        Print.tcfo("理论结果：" + MAX_TURN * MAX_TREAD);
        Print.tcfo("实际结果：" + counter.getAmount());
        Print.tcfo("差距是：" + (MAX_TURN * MAX_TREAD - counter.getAmount()));
    }
}
