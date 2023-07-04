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
            Thread thread = null;
            for(int i = 0;i < 2;i++){
                Runnable target = new RunTarget();
                thread = new Thread(target,"RunnableThread"+threadNo++);
                thread.start();
            }
        }
    }
}
//创建一个Runnable实现类,将其作为参数传入Thread的构造函数
