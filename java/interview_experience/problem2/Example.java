package interview_experience.problem2;

public class Example {
    public static void main(String[] args) {
        MyRunnable runnable = new MyRunnable();
        Thread t1 = new Thread(runnable, "thred1");
        Thread t2 = new Thread(runnable, "thread2");


        t1.start();
        t2.start();
    }
}
