package interview_experience.problem2;

public class ClassCounter {
    private int count;

    ClassCounter(){
        count = 0;
    }

    public synchronized void incrementCount(){
        count++;
    }

    public int getCount(){
        return count;
    }
}
