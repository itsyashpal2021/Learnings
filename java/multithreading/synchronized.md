## `synchronized` Keyword in Java
`synchronized` keyword in Java used to control access to shared resources in a multithreaded environment.

It ensures that only one thread at a time can execute a section of code that requires a specific monitor lock.

When a thread enters a synchronized region, it must first acquire the monitor lock associated with the object. While one thread holds the lock, other threads attempting to acquire the same lock must wait.

---

### How `synchronized` Works

1. Every object in Java has an intrinsic lock (monitor lock).
2. When a thread enters a synchronized block or method:
    - It acquires the monitor lock of the specified object.
3. While the lock is held:
    - No other thread can enter synchronized code guarded by the same lock.
4. When the thread exits the synchronized region:
    - The lock is released.

**Key idea**

Only one thread at a time can access a resource that has its monitor lock acquired.

---

### Synchronized Methods

#### Instance Synchronized Method

When an instance method is marked as `synchronized`, the monitor lock is acquired on the object instance (`this`).

```java
class Counter {

    int count = 0;

    synchronized void increment() {
        count++;
    }
}
```

Behavior:

- A thread must acquire the monitor lock of the object instance before executing `increment()`.
- If multiple threads call `increment()` on the same object, only one thread can execute it at a time.

---

#### Static Synchronized Method

When a static method is synchronized, the lock is acquired on the Class object stored in the heap.

```java
class Counter {

    static int count = 0;

    static synchronized void increment() {
        count++;
    }
}
```

Behavior:

- The thread acquires the monitor lock on `Counter.class`.
- Only one thread across all instances can execute that method at a time.

---

### Synchronized Blocks

Instead of synchronizing an entire method, Java allows synchronizing only a specific block of code.

```java
class Counter {

    int count = 0;

    void increment() {
        synchronized (this) {
            count++;
        }
    }
}
```

Here:

- The monitor lock is acquired on `this` object.
- Only the code inside the block is protected.

You can synchronize on any object:

```java
class Counter {

    int count = 0;
    private final Object lock = new Object();

    void increment() {
        synchronized (lock) {
            count++;
        }
    }
}
```

---

### Monitor Locks

Monitor locks are central to how `synchronized` works.

#### Instance Methods

For instance synchronized methods:

Lock acquired on → Object instance

Example:

```java
Counter counter = new Counter();
counter.increment();
```

The thread acquires the monitor lock on:

```
counter
```

---

#### Static Methods

For static synchronized methods:

Lock acquired on → Class object

Example:

```java
Counter.increment();
```

The thread acquires the monitor lock on:

```
Counter.class
```

---

### Happens-Before Guarantee with `synchronized`

`synchronized` also provides a happens-before relationship in the Java Memory Model.

#### Rule

A thread releasing a monitor lock happens-before another thread acquiring the same monitor lock.

This means:

- When a thread exits a synchronized block or method, all its changes become visible to the next thread that acquires the same lock.

#### Key Summary

1. **Only one thread can acquire a monitor lock on a resource at a time.**

2. When a thread **enters a synchronized block**:
    - Variables used by the thread are **loaded from main memory (heap)**.

3. When a thread **exits a synchronized block**:
    - All updates made by the thread are **flushed from registers / CPU cache back to main memory (heap)**.

These rules ensure that **changes made by one thread become visible to another thread acquiring the same monitor lock**, establishing the **happens-before guarantee**.

---

#### Example

```java
class Counter {

    int count = 0;

    synchronized void increment() {
        count++;
    }
}
```

Execution order:

```
Thread A
    acquires lock
    count++
    releases lock

Thread B
    acquires same lock
    reads updated value
```

Because of the happens-before guarantee, the updates made by Thread A are guaranteed to be visible to Thread B once it acquires the same monitor lock.

---