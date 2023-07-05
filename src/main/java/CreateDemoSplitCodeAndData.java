////Nien Notebook 2 Chapter 1
//1.3.6 实现 Runnable 接口创建线程目标类的特点
import java.util.concurrent.atomic.AtomicInteger;

public class CreateDemoSplitCodeAndData {
    public static final int MAX_AMOUNT = 5;//商品数量
    static class StoreGoods extends Thread{
        StoreGoods(String name){
            super(name);
        }

        private int goodsAmount = MAX_AMOUNT;
        public void run(){
            for(int i = 0; i <= MAX_AMOUNT; i++){
                System.out.println(Thread.currentThread().getName() + "卖出一件,还剩: "+(--goodsAmount));
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            System.out.println(Thread.currentThread().getName()+" 运行结束.");
        }
    }

    //商场商品类型（target 销售线程的目标类），一个商品最多销售 4 次，可以多人销售
    static class MallGoods implements Runnable
    {
        //多人销售可能导致数据出错，使用原子数据类型保障数据安全
        private AtomicInteger goodsAmount = new AtomicInteger(MAX_AMOUNT);
        public void run()
        {
            for (int i = 0; i <= MAX_AMOUNT; i++)
            {
                if (this.goodsAmount.get() > 0)
                {
                    System.out.println((Thread.currentThread().getName() + " 卖出一件，还剩："
                            + (goodsAmount.decrementAndGet())));
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
            System.out.println(Thread.currentThread().getName() + " 运行结束.");
        }
    }
    public static void main(String[] args) throws InterruptedException
    {
        System.out.println("商店版本的销售");
        for (int i = 1; i <= 2; i++)
        {
            Thread thread;
            thread = new StoreGoods("店员-" + i);
            thread.start();
        }
        Thread.sleep(1000);
        System.out.println("商场版本的销售");
        MallGoods mallGoods = new MallGoods();
        for (int i = 1; i <= 2; i++)
        {
            Thread thread;
            thread = new Thread(mallGoods, "商场销售员-" + i);
            thread.start();
        }
        System.out.println(Thread.currentThread().getName()+" 运行结束");
    }

}
