package interview_experience.problem2;

public class MyRunnable implements Runnable {
    ClassCounter counter;

    MyRunnable() {
        this.counter = new ClassCounter();
    }

    @Override
    public void run() {
        for (int i = 0; i < 10000; i++) {
            counter.incrementCount();
        }

        System.out.println("Final count in " + Thread.currentThread() + " : " + counter.getCount());
    }
}
