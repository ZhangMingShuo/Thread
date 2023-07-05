public class CreateDemoEmptyThread {
    public static void main(String[] args) {
        //使用 Thread 类创建和启动线程
        Thread thread = new Thread();
        System.out.println("线程名称："+thread.getName());

        System.out.println("线程 ID："+thread.getId());
        System.out.println("线程状态："+thread.getState());
        System.out.println("线程优先级："+thread.getPriority());
        System.out.println(Thread.currentThread().getName() + " 运行结束.");
        thread.start();
    }
}
