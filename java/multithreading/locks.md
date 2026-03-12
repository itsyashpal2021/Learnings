## Java Multithreading Notes – Locks

### What are Locks in Java?

Locks are advanced synchronization mechanisms provided in the `java.util.concurrent.locks` package that allow **explicit control over thread synchronization**.

Locks provide the same **mutual exclusion** behavior as `synchronized`, but with **more flexibility and additional features**.

Using locks, a thread must explicitly:

```
1. acquire the lock
2. execute critical section
3. release the lock
```

Example:

```java
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class Counter {

    private int count = 0;
    private final Lock lock = new ReentrantLock();

    void increment() {

        lock.lock();

        try {
            count++;
        } finally {
            lock.unlock();
        }
    }
}
```

Here:

- `lock.lock()` acquires the lock
- `lock.unlock()` releases the lock
- `try-finally` ensures the lock is always released

---

## Locks vs `synchronized`

Both locks and `synchronized` provide **mutual exclusion**, ensuring only one thread executes a critical section at a time.

### `synchronized`

- Built-in JVM feature
- Automatically acquires and releases monitor locks
- Simpler syntax
- Less flexible

Example:

```java
synchronized (this) {
    count++;
}
```

---

### Locks

- Part of `java.util.concurrent`
- Require **manual acquisition and release**
- Provide **more advanced features**

Example:

```java
lock.lock();

try {
    count++;
} finally {
    lock.unlock();
}
```

---

## Advantages of Locks over `synchronized`

Locks provide additional capabilities not available with `synchronized`.

### 1. Try Lock (Non-blocking lock attempt)

A thread can attempt to acquire a lock without waiting indefinitely.

```java
if (lock.tryLock()) {
    try {
        // critical section
    } finally {
        lock.unlock();
    }
}
```

---

### 2. Timed Lock Attempt

A thread can attempt to acquire a lock with a timeout.

```java
lock.tryLock(2, TimeUnit.SECONDS);
```

If the lock is not available within the specified time, the attempt fails.

---

### 3. Interruptible Lock Acquisition

A thread waiting for a lock can be **interrupted**.

```java
lock.lockInterruptibly();
```

This is not possible with `synchronized`.

---

### 4. Multiple Condition Variables

Locks allow multiple condition variables using `Condition`.

Example:

```java
Condition condition = lock.newCondition();
```

This is similar to `wait()` and `notify()` but **more flexible**.

---

## How Locks Work Internally

Locks are implemented using **low-level JVM mechanisms and atomic operations**.

Internally they rely on:

```
CAS (Compare-And-Swap)
volatile memory semantics
queueing of waiting threads
```

The lock attempts to acquire ownership using **CAS operations**.

If the lock is unavailable:

```
Thread → added to wait queue
Thread → parked by JVM
```

When the lock becomes available:

```
Next waiting thread is unparked
```

This design allows locks to be **efficient under contention**.

---

## Types of Locks in Java

Java provides several lock implementations.

### 1. ReentrantLock

### 2. ReadWriteLock

### 3. StampedLock (advanced)

The most commonly used ones are:

```
ReentrantLock
ReadWriteLock
```

---

## ReentrantLock

`ReentrantLock` is the most common explicit lock implementation.

It behaves similarly to `synchronized` but with more features.

The term **reentrant** means:

> The same thread can acquire the same lock multiple times without causing a deadlock.

Example:

```java
import java.util.concurrent.locks.ReentrantLock;

class Counter {

    private int count = 0;
    private final ReentrantLock lock = new ReentrantLock();

    void increment() {

        lock.lock();

        try {
            count++;
        } finally {
            lock.unlock();
        }
    }
}
```

---

### Reentrancy Example

```java
class Example {

    ReentrantLock lock = new ReentrantLock();

    void methodA() {
        lock.lock();

        try {
            methodB();
        } finally {
            lock.unlock();
        }
    }

    void methodB() {
        lock.lock();

        try {
            // same thread acquires lock again
        } finally {
            lock.unlock();
        }
    }
}
```

The same thread can **reacquire the lock**, and the lock maintains a **hold count**.

The lock is released only when the hold count reaches **zero**.

---

## ReadWriteLock

`ReadWriteLock` allows **multiple readers but only one writer**.

This is useful when:

```
many threads read data
few threads modify data
```

Using a normal lock would block all threads, but a read-write lock allows **concurrent reads**.

---

### Example

```java
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

class DataStore {

    private int value = 0;

    private final ReadWriteLock lock = new ReentrantReadWriteLock();

    void write(int newValue) {

        lock.writeLock().lock();

        try {
            value = newValue;
        } finally {
            lock.writeLock().unlock();
        }
    }

    int read() {

        lock.readLock().lock();

        try {
            return value;
        } finally {
            lock.readLock().unlock();
        }
    }
}
```

---

### Behavior

```
Multiple threads can acquire read lock simultaneously
Only one thread can acquire write lock
Write lock blocks all readers and writers
```

Execution example:

```
Thread A → read lock
Thread B → read lock
Thread C → write lock (must wait)
```

---

## When to Use ReadWriteLock

ReadWriteLock works best when:

```
read operations >> write operations
```

Example scenarios:

- caching systems
- configuration data
- shared lookup tables
- in-memory data stores

---

## Key Summary

### Locks

Locks provide **explicit synchronization mechanisms** with more flexibility than `synchronized`.

They allow:

```
manual lock control
timeout-based locking
interruptible locking
multiple condition variables
```

---

### ReentrantLock

ReentrantLock:

- provides mutual exclusion
- allows the same thread to acquire the lock multiple times
- supports advanced lock features like `tryLock()` and `lockInterruptibly()`

---

### ReadWriteLock

ReadWriteLock allows:

```
multiple readers
single writer
```

This improves performance when **reads greatly outnumber writes**.

---

### When to Use Locks vs `synchronized`

Use `synchronized` when:

```
simple mutual exclusion is required
```

Use Locks when you need:

```
advanced control
timeouts
interruptible waits
read-write separation
```