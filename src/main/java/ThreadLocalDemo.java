//Nien Notebook 2 Chapter 1
//1.8.1 ThreadLocal 的基本使用
import lombok.Data;

import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicInteger;

public class ThreadLocalDemo {
    @Data
    static class Foo
    {
        //实例总数
        static final AtomicInteger AMOUNT = new AtomicInteger(0);
        //对象的编号
        int index = 0;
        //对象的内容
        int bar = 10;
        //构造器
        public Foo()
        {
            index = AMOUNT.incrementAndGet(); //总数增加，并且给对象的编号
        }
        @Override
        public String toString()
        {
            return index + "@Foo{bar=" + bar + '}';
        }
    }
    //定义线程本地变量
    private static final ThreadLocal<Foo> LOCAL_FOO = new
            ThreadLocal<Foo>();
    public static void main(String[] args) throws InterruptedException
    {
        //获取自定义的混合型线程池
        ThreadPoolExecutor threadPool =
                ThreadUtil.getMixedTargetThreadPool();
        //提交 5 个任务，将会用到 5 个线程
        for (int i = 0; i < 5; i++)
        {
            threadPool.execute(new Runnable()
            {
                @Override
                public void run()
                {
                    //获取“线程本地变量”中当前线程所绑定的值
                    if (LOCAL_FOO.get() == null)
                    {
                        //设置“线程本地变量”中当前线程所绑定的值
                        LOCAL_FOO.set(new Foo());
                    }
                    Print.tco("初始的本地值：" + LOCAL_FOO.get());
                    //每个线程执行 10 次
                    for (int i = 0; i < 10; i++)
                    {
                        Foo foo = LOCAL_FOO.get();
                        foo.setBar(foo.getBar() + 1); //值增 1
                        ThreadUtil.sleepMilliSeconds(10);
                    }
                    Print.tco("累加 10 次之后的本地值：" + LOCAL_FOO.get());
                    //删除“线程本地变量”中当前线程所绑定的值
                    LOCAL_FOO.remove(); //这点对于线程池中的线程尤其重要
                }
            });
        }
    }
}
