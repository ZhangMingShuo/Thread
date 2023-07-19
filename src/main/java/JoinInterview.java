/*
如何让线程t1 t2 t3顺序执行？
 */
public class JoinInterview {
    public static void main(String[] args){
        Thread t1 = new Thread(()->{
            System.out.println(Thread.currentThread().getName()+":"+Thread.currentThread().getState());
        },"t1");

        Thread t2 = new Thread(()->{
            try {
                 t1.join();  //t1执行完后 执行后面的
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println(Thread.currentThread().getName()+":"+Thread.currentThread().getState());

        },"t2");

        Thread t3 = new Thread(()->{
            try {
                 t2.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println(Thread.currentThread().getName()+":"+Thread.currentThread().getState());
        },"t3");

        t3.start();
        t2.start();
        t1.start();
    }
}
