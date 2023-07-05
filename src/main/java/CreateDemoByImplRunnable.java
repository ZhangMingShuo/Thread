//Nien Notebook 2 Chapter 1
//1.3.4 线程创建方法二：实现 Runnable 接口创建线程目标类
public class CreateDemoByImplRunnable {
    public static final int MAX_TURN = 5;
    public static String getCurThreadName() {
        return Thread.currentThread().getName();
    }
    static int threadNo = 1;
    static class RunTarget implements Runnable{
        public void run(){
            for(int j = 1; j < MAX_TURN; j++){
                System.out.println(getCurThreadName() + ",轮次:"+j);
            }
            System.out.println(getCurThreadName()+",运行结束");
        }

        public static void main(String[] args) {
            Thread thread;
            for(int i = 0;i < 2;i++){
                Runnable target = new RunTarget();
                //创建一个Runnable实现类,将其作为参数传入Thread的构造函数
                thread = new Thread(target,"RunnableThread"+threadNo++);
                thread.start();
            }
        }
    }
}
