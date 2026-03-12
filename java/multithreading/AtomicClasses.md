## Java Multithreading Notes – Atomic Classes

### What are Atomic Classes in Java?

Atomic classes are a set of classes in the `java.util.concurrent.atomic` package that provide **thread-safe operations on single variables without using locks**.

They allow multiple threads to **safely update shared variables concurrently** using **atomic operations**.

An **atomic operation** is an operation that **executes completely or not at all**, meaning **no other thread can observe it in a partially completed state**.

Atomic classes are typically used when:

- A variable is **shared between multiple threads**
- Operations must be **thread-safe**
- Using `synchronized` would introduce **unnecessary locking overhead**

---

## Common Atomic Classes

Some commonly used atomic classes include:

- `AtomicInteger`
- `AtomicLong`
- `AtomicBoolean`
- `AtomicReference`
- `AtomicIntegerArray`
- `AtomicLongArray`

Example:

```java
import java.util.concurrent.atomic.AtomicInteger;

class Counter {

    AtomicInteger count = new AtomicInteger(0);

    void increment() {
        count.incrementAndGet();
    }
}
```

Here:

- `incrementAndGet()` performs the **increment atomically**
- Multiple threads can safely call this method without synchronization

---

## Atomic Operations

Atomic classes provide built-in **atomic methods**.

Common methods include:

| Method | Description |
|------|-------------|
| `get()` | Returns the current value |
| `set(value)` | Sets a new value |
| `incrementAndGet()` | Increments and returns updated value |
| `getAndIncrement()` | Returns old value then increments |
| `compareAndSet(expected, update)` | Updates value only if it matches expected value |
| `getAndSet(value)` | Sets new value and returns old value |

Example:

```java
AtomicInteger counter = new AtomicInteger(0);

counter.incrementAndGet();
```

This operation happens **atomically**.

---

## How Atomic Classes Work

Atomic classes are implemented using **CPU-level atomic instructions** and **CAS (Compare-And-Swap)** operations.

Instead of locking an object like `synchronized`, they use **lock-free algorithms**.

This allows multiple threads to attempt updates simultaneously without blocking each other.

---

## Compare-And-Swap (CAS)

CAS is the fundamental mechanism used by atomic classes.

CAS works with three values:

```
memory location
expected value
new value
```

Operation:

```
if (current_value == expected_value)
    update to new_value
else
    operation fails
```

Example:

```java
AtomicInteger counter = new AtomicInteger(5);

counter.compareAndSet(5, 6);
```

Execution:

```
current value = 5
expected value = 5
update value = 6

→ update succeeds
```

If another thread had changed the value first:

```
current value = 7
expected value = 5

→ update fails
```

---

## How Atomic Increment Works

Example:

```java
AtomicInteger counter = new AtomicInteger(0);

counter.incrementAndGet();
```

Internally this works like:

```
loop:
    read current value
    newValue = current + 1

    if compareAndSet(current, newValue)
        return newValue
    else
        retry
```

If another thread modifies the value before the update succeeds, the operation **retries automatically**.

---

## Why Atomic Classes Solve the `volatile` Increment Problem

Earlier example with `volatile`:

```java
volatile int count = 0;

count++;
```

Problem:

```
1. read count
2. increment
3. write back
```

Multiple threads can interleave these steps.

Atomic version:

```java
AtomicInteger count = new AtomicInteger(0);

count.incrementAndGet();
```

The entire update is done using **CAS**, ensuring the operation happens **atomically**.

---

## Memory Visibility Guarantees

Atomic classes also provide **happens-before guarantees** similar to `volatile`.

Key rules:

- Atomic variables have **volatile-like memory semantics**
- Updates made before an atomic operation become **visible to other threads**

This means:

```
write to atomic variable
        ↓
visible to other threads reading that variable
```

So atomic variables provide both:

- **Atomicity**
- **Visibility**

---

## Advantages of Atomic Classes

1. **Lock-free thread safety**

   No blocking or monitor locks required.

2. **Better performance**

   Often faster than `synchronized` in low to moderate contention.

3. **Atomic operations**

   Operations like increment or update are **guaranteed to be atomic**.

4. **Volatile memory semantics**

   Ensures visibility between threads.

---

## Limitations of Atomic Classes

Atomic classes are designed for **single-variable atomic operations**.

They are **not suitable for complex multi-step operations** involving multiple variables.

Example:

```java
if (balance.get() > 100) {
    balance.set(balance.get() - 100);
}
```

Even with `AtomicInteger`, this sequence is **not atomic**.

In such cases, you need:

- `synchronized`
- `ReentrantLock`

---

## Key Summary

### Atomic Classes

Atomic classes provide **thread-safe operations on variables without using locks**.

They achieve this using:

```
CAS (Compare-And-Swap)
CPU atomic instructions
retry loops
```

---

### Guarantees

Atomic classes provide:

- **Atomic operations**
- **Visibility guarantees**
- **Happens-before relationship**

---

### When to Use Atomic Classes

Atomic classes are best used when:

- A **single variable** is shared between threads
- You need **atomic updates**
- You want to **avoid locking overhead**

---

### When NOT to Use Atomic Classes

Avoid atomic classes when:

- Multiple variables must be updated together
- Operations involve **complex critical sections**
- Strong coordination between threads is required

In those cases, use:

```
synchronized
ReentrantLock
higher-level concurrency utilities
```