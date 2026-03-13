package interview_experience.problem2;

public class Example {
    public static void main(String[] args) {
        MyRunnable runnable = new MyRunnable();
        Thread t1 = new Thread(runnable, "thread1");
        Thread t2 = new Thread(runnable, "thread2");


        t1.start();
        t2.start();

        /*
        * Case1 : count is volatile, there will be missed increments due to threads invoking increment concurrently.
        * Case2 : synchronized increment method, at least one thread will reach count 20000.
        * */
    }
}
