# 1. Multiprocessing vs Multithreading

## Multiprocessing

Multiprocessing means **running multiple processes simultaneously**.

- Each process has its **own memory space**
- Processes are **isolated from each other**
- Communication between processes happens via **IPC (Inter-Process Communication)**

Examples:
- Running Chrome + VSCode + Spotify simultaneously

### Advantages

- Better isolation
- One process crash doesn't affect another

### Disadvantages

- High memory usage
- Context switching is expensive

---

## Multithreading

Multithreading means **multiple threads running within the same process**.

- Threads **share the same memory space**
- Threads execute tasks **concurrently**
- Communication between threads is **easy via shared variables**

Example:
A web server handling multiple requests simultaneously.

### Advantages

- Lightweight compared to processes
- Faster communication due to shared memory

### Disadvantages

- Risk of race conditions
- Requires proper synchronization

---

# 2. How Threads Actually Execute

A CPU can execute **one instruction per core at a time**.

Modern CPUs have **multiple cores**, which allows multiple threads to run **in parallel**.

Example: 4-core CPU

Core1 → Thread A  
Core2 → Thread B  
Core3 → Thread C  
Core4 → Thread D


If there are **more threads than CPU cores**, the operating system performs **context switching**.

---

## Context Switching

Context switching means the CPU **pauses one thread and resumes another**.

Steps:

1. Save state of current thread
2. Load state of next thread
3. Resume execution

Time →

Thread A       ███ --  ███   
Thread B       --  ███ --  ███  
Thread C       --  --  --  --  ███


Even though only one thread runs at a time on a core, fast switching creates the **illusion of parallelism**.

---

# 3. Creating and Starting a Thread

Java provides two main ways to create threads.

---

## Method 1: Extending Thread Class

```java
class MyThread extends Thread {

    public void run() {
        System.out.println("Thread is running");
    }

}

public class Main {

    public static void main(String[] args) {

        MyThread t = new MyThread();
        t.start();

    }

}
```

***Important***:

start() → creates new thread  
run()   → executed inside the new thread

Never call run() directly because it will not create a new thread. Instead, it runs like a normal method call in the current thread. To start a new thread, you must call start().

## Method 2: Implementing Runnable (Preferred)

```java
class MyTask implements Runnable {

    public void run() {
        System.out.println("Task running in thread");
    }
}

public class Main {

    public static void main(String[] args) {

        Thread t = new Thread(new MyTask());
        t.start();

    }
}
```
Advantages:
- Better design
- Allows extending another class
- Used widely with thread pools

# 4. Thread Lifecycle

A **thread lifecycle** describes the different states a thread goes
through during its execution.

    NEW
     ↓
    RUNNABLE
     ↓
    RUNNING
     ↓
    BLOCKED / WAITING / TIMED_WAITING
     ↓
    TERMINATED

------------------------------------------------------------------------

## 1. NEW

A thread is in the **NEW** state when it is created but **not yet
started**.

``` java
Thread t = new Thread();
```

At this stage: - The thread object exists - The thread has **not started
execution**

------------------------------------------------------------------------

## 2. RUNNABLE

A thread enters the **RUNNABLE** state after calling `start()`.

``` java
t.start();
```

At this stage: - The thread is **ready to run** - It is waiting for the
**CPU scheduler** to allocate processor time

------------------------------------------------------------------------

## 3. RUNNING

A thread is in the **RUNNING** state when the **CPU is executing it**.

At this stage: - The thread actively runs its `run()` method.

------------------------------------------------------------------------

## 4. BLOCKED

A thread becomes **BLOCKED** when it is waiting for a **monitor lock**
(usually during synchronization).

Example situations: - Waiting to enter a synchronized block - Waiting
for another thread to release a lock

------------------------------------------------------------------------

## 5. WAITING

A thread enters the **WAITING** state when it waits **indefinitely** for
another thread to perform a particular action.

Examples:

    wait()
    join()

The thread will remain waiting until another thread **notifies or
completes execution**.

------------------------------------------------------------------------

## 6. TIMED_WAITING

A thread enters **TIMED_WAITING** when it waits for a **specific amount
of time**.

Examples:

    sleep()
    join(timeout)
    wait(timeout)

Example:

``` java
Thread.sleep(1000);
```

The thread pauses execution for **1000 milliseconds**.

------------------------------------------------------------------------

## 7. TERMINATED

A thread enters the **TERMINATED** state after it has **finished
executing**.

This happens when: - The `run()` method completes - The thread execution
ends

------------------------------------------------------------------------

# 5. Important Thread Methods

Java provides several methods to control thread behavior.

------------------------------------------------------------------------

## start()

Starts a new thread.

``` java
t.start();
```

-   Creates a **new thread**
-   Internally calls the `run()` method.

------------------------------------------------------------------------

## run()

Contains the **code executed by the thread**.

``` java
public void run() {
    // thread logic
}
```

------------------------------------------------------------------------

## sleep()

Pauses the current thread for a specified time.

``` java
Thread.sleep(1000);
```

-   Time is in **milliseconds**
-   Thread enters **TIMED_WAITING**

------------------------------------------------------------------------

## join()

Makes the current thread **wait until another thread finishes
execution**.

``` java
t.join();
```

Example use case: - Main thread waits for worker thread to finish.

------------------------------------------------------------------------

## yield()

Hints the **thread scheduler** that the current thread is willing to
pause and allow other threads to execute.

``` java
Thread.yield();
```

Note: - It is only a **suggestion** to the scheduler.

------------------------------------------------------------------------

## interrupt()

Interrupts a thread that is waiting or sleeping.

``` java
t.interrupt();
```

Used to: - Stop blocking operations - Signal thread cancellation

------------------------------------------------------------------------

# 6.  Problems in Multithreading

Two major issues:

1. Concurrency (Race Conditions)
2. Visibility Problems

## 6.1  Concurrency Problem (Race Condition)

Occurs when multiple threads modify shared data simultaneously.

Example:
``` java
class Counter {

    int count = 0;

    void increment() {
        count++;
    }

}
```

Two threads running:

``` java
Thread1 -\> increment()
Thread2 -\> increment()
```

Expected: count = 2

Actual possible result: count = 1

Why?

Because count++ is not atomic.

It actually performs:

1.  read count
2.  add 1
3.  write count

Interleaving example:

Thread1 read 0  
Thread2 read 0  
Thread1 write 1  
Thread2 write 1

Final value = 1 instead of 2

## 6.2  Visibility Problem

Occurs when one thread updates a variable but another thread doesn't see
the updated value.

Consider the following class:

``` java
class Counter {

    int count = 0;

    void increment() {
        count++;
    }
}
```

The `count++` operation modifies the shared variable `count`.

Even if **Thread1 and Thread2 start at different times**, a visibility
problem can still occur.

### Scenario

1.  **Thread1** starts first and executes:

``` java
 increment()
```

This performs:

    read count
    add 1
    write count

So `count` becomes:

    count = 1

2.  Later, **Thread2** starts and calls:

``` java
 increment()
```


### Expected Behavior

Thread2 should see:

    count = 1

and after increment:

    count = 2

### Possible Actual Behavior

Thread2 may still see:

    count = 0

and after increment:

    count = 1

### Why This Happens

When **Thread1 updates `count`**, the updated value may be stored in:

-   CPU **register**
-   CPU **cache**

The change might **not yet be flushed to main heap memory**.

So when **Thread2 starts**, it may read the value of `count` from **main
memory**, which still contains the **old stale value**.

### Simplified Memory View

    Heap Memory
    count = 0

    CPU Cache / Register (Thread1)
    count = 1

Thread2 reads from **heap memory**:

    count = 0

instead of the updated value.

### Result

Thread2 operates on a **stale value** of `count`.

This is called a **visibility problem**, where changes made by one
thread are **not immediately visible to another thread**.

# 7.  Solutions to Multithreading Problems

Java provides mechanisms to fix above via:

- synchronized
- volatile
- Atomic classes
- Executor framework