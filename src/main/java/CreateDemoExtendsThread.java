//Nien Notebook 2 Chapter 1
//1.3.3 线程创建方法一：继承 Thread 类创建线程类
public class CreateDemoExtendsThread {
    public static final int MAX_TURN = 5;
    public static String getCurThreadName() {
        return Thread.currentThread().getName();
    }
    static int threadNo = 1;
    static class DemoThread extends Thread{
        public DemoThread(){
            super("DemoThread-" + threadNo++);
        }
        public void run(){
            for(int i = 1; i < MAX_TURN; i++){
                System.out.println(getName()+",轮次: "+i);
            }
            System.out.println(getName() + "运行结束.");
        }
    }

    public static void main(String[] args) {
        Thread thread;
        for(int i = 0; i < 2; i++){
            thread = new DemoThread();
            thread.start();
        }
        System.out.println(getCurThreadName() + "运行结束.");
    }
}
