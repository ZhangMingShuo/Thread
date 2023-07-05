//Nien Notebook 2 Chapter 1
//1.5.1 线程名称的设置和获取

public class ThreadNameDemo {
    private static final int MAX_TURN = 3;
    //异步执行目标类
    static class RunTarget implements Runnable
    { // 实现 Runnable 接口
        public void run()
        { // 重新 run()方法
            for (int turn = 0; turn < MAX_TURN; turn++)
            {
                try {
                    Thread.sleep(500);//线程睡眠
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                Print.tco("线程执行轮次:" + turn);
            }
        }
    }
    public static void main(String[] args) throws InterruptedException {
        RunTarget target = new RunTarget(); // 实例化 Runnable 异步执行目标类
        new Thread(target).start(); // 系统自动设置线程名称
        new Thread(target).start(); // 系统自动命令线程名称
        new Thread(target).start(); // 系统自动命令线程名称
        new Thread(target, "手动命名线程-A").start(); // 手动设置线程名称
        new Thread(target, "手动命名线程-B").start(); // 手动设置线程名称
        Thread.sleep(Integer.MAX_VALUE); //主线程不能结束
    }
}
