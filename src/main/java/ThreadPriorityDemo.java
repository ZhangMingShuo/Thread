//Nien Notebook 2 Chapter 1
//1.4.2 线程的优先级
public class ThreadPriorityDemo {
    public static final int SLEEP_GAP = 1000;
    static class PrioritySetThread extends Thread {
        static int threadNo = 1;
        public PrioritySetThread() {
            super("thread-" + threadNo);
            threadNo++;
        }

        public long opportunities=0;
        public void run() {
            for (int i = 0; ; i++) {
                opportunities++;
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {
        PrioritySetThread[] threads = new PrioritySetThread[10];
        for (int i = 0; i < threads.length; i++) {
            threads[i] = new PrioritySetThread();
            //优先级的设置，从 1-10
            threads[i].setPriority(i + 1);
        }
        for (int i = 0; i < threads.length; i++) {
            threads[i].start();//启动线程
        }
        Thread.sleep(SLEEP_GAP); //等待线程运行 1s
        for (int i = 0; i < threads.length; i++) {
            threads[i].stop(); //停止线程
        }
        for (int i = 0; i < threads.length; i++) {
            System.out.println(threads[i].getName() +
                    "-优先级为-" + threads[i].getPriority() + //获取优先级
                    "-机会值为-" + threads[i].opportunities
            );
        }
    }
}
