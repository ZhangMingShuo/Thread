//Nien Notebook 2 Chapter 1
// 1.2.2 线程的基本原理
public class Main {
    public static void main(String[] args) {
        System.out.println("当前线程名称" + Thread.currentThread().getName());
        System.out.println("当前线程ID" + Thread.currentThread().getId());
        System.out.println("当前线程状态" + Thread.currentThread().getState());
        System.out.println("当前线程优先级" + Thread.currentThread().getPriority());

    }
}

