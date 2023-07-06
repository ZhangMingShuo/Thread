/**
 * Created by 尼恩 at 疯狂创客圈
 */
import java.util.Scanner;

public class Print
{
    /**
     * 正式输出前，输出线程名
     * t-thread，c-content， o-output
     *
     * @param s 待输出的字符串
     */
    public  static void tco(Object s)
    {
        String cft = "[" + Thread.currentThread().getName() + "]" + "：" + s;

        //提交线程池进行异步输出，使得输出过程不影响当前线程的执行
        //异步输出的好处：不会造成输出乱序，也不会造成当前线程阻塞
        ThreadUtil.seqExecute(() ->
        {

            System.out.println(cft);

        });
    }

    /**
     * 正式输出前，输出线程名
     * syn-同步， t-thread，c-content， o-output
     *
     * @param s 待输出的字符串
     */
    public static void synTco(Object s)
    {
        String cft = "[" + Thread.currentThread().getName() + "]" + "：" + s;

        //提交线程池进行异步输出，使得输出过程不影响当前线程的执行
        //异步输出的好处：不会造成输出乱序，也不会造成当前线程阻塞

        System.out.println(cft);

    }

    public static String consoleInput()
    {
        Scanner sc = new Scanner(System.in);
        System.out.println("Please Enter sth:");
        String nextLine = sc.nextLine();  //读取字符串型输入
        return nextLine;
    }

    /**
     * 带着线程名+类名+方法名称输出
     *
     * @param s 待输出的字符串形参
     */
    public static void tcfo(Object s)
    {
        String cft = "[" + Thread.currentThread().getName() + "|" + ReflectionUtil.getNakeCallClassMethod() + "]";

        //提交线程池进行独立输出，使得输出不影响当前线程的执行
        ThreadUtil.seqExecute(() ->
        {
            System.out.println(cft + "：" + s);
        });

    }

}


