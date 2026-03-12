## `volatile` keyword in java

`volatile` keyword in Java is used to ensure **visibility of changes to variables across threads**.

When a variable is declared as `volatile`, all reads and writes to that variable **directly interact with main memory (heap)** instead of using cached values stored in CPU registers or thread-local caches.

This ensures that **when one thread updates the variable, other threads will immediately see the updated value.**

## Example

```java
class FlagExample {

    volatile boolean running = true;

    void stop() {
        running = false;
    }

    void runTask() {
        while (running) {
            // task keeps running
        }
    }
}
```

Execution flow:

```
Thread A
    running = false

Thread B
    reads running
```

Because `running` is declared `volatile`:

- The write by **Thread A** is immediately visible.
- **Thread B** will observe the updated value and exit the loop.

---

## Happens-Before Guarantee with `volatile`

The Java Memory Model defines the following rule:

```
A write to a volatile variable happens-before every subsequent read of that same variable.
```

### What this means

If:

```
Thread A
    writes volatile variable

Thread B
    reads same volatile variable
```

Then:

- The write operation in **Thread A** becomes visible to **Thread B**.
- Thread B is guaranteed to see **the most recent value**.

---

### How `volatile` Works Internally

When a variable is declared `volatile`, the JVM inserts **memory barriers** that affect how reads and writes interact with main memory.

#### On Write

When a thread writes to a volatile variable:

1. **All previous writes by the thread are flushed to main memory.**
2. The volatile variable is then written to main memory.

This ensures that **any variable updates made before the volatile write become visible to other threads**.

#### On Read

When a thread reads a volatile variable:

1. The thread **reads the volatile value from main memory**.
2. **All subsequent reads will see the latest values from main memory.**

---

## Why `volatile` Does NOT Solve Concurrency

`volatile` only guarantees **visibility**, not **mutual exclusion**.

This means multiple threads can still **modify a variable at the same time**, which can lead to incorrect results when operations involve multiple steps.

---

### Example

```java
class Counter {

    volatile int count = 0;

    void increment() {
        count++;
    }
}
```

The operation:

```
count++
```

is actually composed of multiple steps:

```
1. Read count
2. Increment value
3. Write updated value
```

If two threads execute this simultaneously:

```
Thread A → read count = 5
Thread B → read count = 5

Thread A → write 6
Thread B → write 6
```

The final value becomes:

```
6 instead of 7
```

Even though `count` is volatile, **both threads were allowed to execute the operation concurrently.**

---
