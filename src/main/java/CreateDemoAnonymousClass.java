//Nien Notebook 2 Chapter 1
//1.3.5 优雅创建 Runnable 线程目标类的两种方式
// 1. 通过匿名类优雅创建 Runnable 线程目标类
public class CreateDemoAnonymousClass {
    public static final int MAX_TURN = 5;
    public static String getCurThreadName() {
        return Thread.currentThread().getName();
    }
    static int threadNo = 1;


    public static void main(String[] args) {
        Thread thread;
        for(int i = 0;i < 2;i++){
            thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    for(int j = 1; j < MAX_TURN; j++){
                        System.out.println(getCurThreadName() + ",轮次:"+j);
                    }
                    System.out.println(getCurThreadName()+",运行结束");
                }
            }, "RunnableThread" + threadNo++);
            thread.start();
        }
        System.out.println(getCurThreadName() + " 运行结束");
    }

}

