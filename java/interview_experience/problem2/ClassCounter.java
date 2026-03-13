package interview_experience.problem2;

public class ClassCounter {
    private volatile int count;
//    private int count;

    ClassCounter() {
        count = 0;
    }

//    public  void incrementCount() {
    public synchronized void incrementCount() {
        count++;
    }

    public int getCount() {
        return count;
    }
}
