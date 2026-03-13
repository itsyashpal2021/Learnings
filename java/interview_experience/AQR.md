# Java Interview Notes

---

# 1. Multithreading

## Question
- What is **multithreading** and how does it actually work?  
- Do threads truly run in parallel?  
- If there is a **single CPU core**, do threads still run in parallel?  
- Why should we even use multithreading on a single-core processor?

## Answer

### What is Multithreading
Multithreading is the ability of a program to execute **multiple threads (independent paths of execution)** within the same process.

A **thread** is a lightweight unit of execution that shares the same memory space with other threads in the process.

Example:
- One thread handles user input
- Another thread processes data
- Another thread performs network calls

---

### How Multithreading Works

The **Operating System scheduler** manages threads and assigns CPU time to them.

Threads execute using:

- **Time slicing**
- **Context switching**

Flow:
```
Thread A → CPU executes for few milliseconds
Thread B → CPU executes for few milliseconds
Thread C → CPU executes for few milliseconds
```

The switching happens extremely fast.

---

### Do Threads Truly Run in Parallel?

**Yes, but only when multiple CPU cores exist.**

Example:

| CPU Cores | Parallel Threads |
|-----------|------------------|
| 1 Core | No true parallelism |
| 2 Cores | 2 threads can run simultaneously |
| 4 Cores | 4 threads can run simultaneously |

On multi-core CPUs, each core can execute a different thread.

---

### What Happens on a Single-Core CPU?

On a **single-core processor**, threads do **not run in true parallel**.

Instead, the CPU rapidly switches between threads using **context switching**.

This creates the illusion of parallel execution.

Example:
```
Time Slice 1 → Thread A
Time Slice 2 → Thread B
Time Slice 3 → Thread C
```

---

### Why even use Multithreading on a Single Core?

Even on a single core, multithreading is beneficial.

#### 1. Better Responsiveness
Example: UI applications
``` 
Thread 1 → UI rendering
Thread 2 → Background task
```


Without multithreading, UI may freeze.

---

#### 2. I/O Wait Optimization

When one thread waits for I/O (disk, network), another thread can execute.

Example:
``` 
Thread 1 → Waiting for API response
Thread 2 → Processing user input
```


---

#### 3. Better Program Structure

Multithreading allows tasks to be logically separated.

Example:

- Logging thread
- Worker thread
- Network thread

---

### Benefits of Multithreading

- Improved CPU utilization
- Better application responsiveness
- Efficient I/O handling
- Parallel execution on multi-core CPUs
- Better scalability

---

# 2. Java Memory Model (JMM)

## Question
Explain the **Java Memory Model** and the difference between **Heap and Stack**.  
Where is the heap actually located?  
How does the CPU read heap memory?

---

## Answer

### Java Memory Model

The **Java Memory Model (JMM)** defines how **threads interact with memory**.

It specifies:

- How variables are stored
- How threads read/write shared variables
- Rules for **visibility**, **ordering**, and **atomicity**

JMM ensures predictable behavior in multithreaded programs.

---

## Heap vs Stack

| Feature | Heap | Stack |
|------|------|------|
| Stores | Objects | Method calls & local variables |
| Thread visibility | Shared across threads | Each thread has its own |
| Lifetime | Until garbage collected | Until method finishes |
| Size | Larger | Smaller |

---

## Stack Memory

Each thread has its **own stack memory**.

Stack stores:

- Method calls
- Local variables
- References to objects

Example:

```java
Person p = new Person();

// Memory Layout
Stack
-----
p → reference

Heap
-----
Person object
```
## Heap Memory

The heap is where all Java objects are stored.

Example:
```java
Person p = new Person();

// Memory representation -
Stack (Thread)
----------------
p → 0x12345

Heap
----------------
0x12345 → Person Object
```
All threads share heap memory.

### Where is Heap Actually Located?

The heap is:
- A region inside RAM
- Allocated to the JVM process

So physically it resides in main memory (RAM).

### How CPU Reads Heap Memory

The CPU does not directly understand Java objects.

Process:

- CPU executes JVM instructions
- JVM references memory addresses
- Data is fetched from RAM
- CPU loads it into CPU cache

Flow:
```java
CPU Register
   ↓
CPU Cache (L1, L2, L3)
   ↓
Main Memory (RAM)
   ↓
JVM Heap
   ↓
Java Objects
```

Because CPUs use caches, **visibility issues** can occur in multithreading.

Java solves this with:

- volatile
- synchronized
- happens-before rules

# 3. Java: Call by Value vs Reference (Using Person Example)

In **Java, everything is passed by value**, including object references.  
When you pass an object to a method, **the value of the reference (memory address) is copied**, not the object itself.

Let’s analyze both scenarios.

---

### Initial Code (Conceptual)

```java
Person p = new Person();
p.setCountry("country1");

setCountry(p);
```
p → Person object (memory address) → country = "country1" (actual object in heap)

### Scenario 1
```java
void setCountry(Person x) {
    x.setCountry("country2");
}
```
What Happens

1. p holds a reference to the Person object.
2. When calling setCountry(p), the reference value is copied into x.
3. Now both p and x point to the same object.
    ```java
    p ──┐
    ├──> Person object → country = "country1"
    x ──┘
    ```
4. `x.setCountry("country2")` modifies the same object.
After execution:
      ```java
        p ──┐
        ├──> Person object → country = "country2"
        x ──┘
     ```
### Scenario 2
```java
void setCountry(Person x) {
    x = new Person();
    x.setCountry("country2");
}
```
1. p reference is copied into x.
      ```java
        p ──┐
        ├──> Person object → country = "country1"
        x ──┘
        ```
2. Inside the method: ` x = new Person();`  
   Now x points to a new object.
    ```java
    p ───> Person object → country = "country1"
    
    x ───> New Person object → country = null
    ```
3. Then:
    ```java
    x.setCountry("country2")
    x ───> New Person object → country = "country2"
    ```
   But p still points to the original object.
4. Final result `p.country` is still `country1`.

# 4. How Java works with wrapper classes and why they are needed?

Wrapper classes (like `Integer`, `Double`, `Boolean`, etc.) are object representations of primitive types. They are needed because many Java APIs (such as collections like `ArrayList`, `HashMap`, etc.) work only with objects, not primitives. Java provides **autoboxing** (primitive → wrapper) and **unboxing** (wrapper → primitive) so primitives can automatically convert to their corresponding wrapper objects when required.

### What happens in `void incr(Integer i){ i++; }` and does the value of Integer object passed to method change?

- The `++` operator cannot work directly on objects, so Java performs **unboxing → increment → boxing**.  
- Internally it behaves like: `int temp = i.intValue(); temp = temp + 1; i = Integer.valueOf(temp);`. 
- This creates a **new Integer object** and assigns it to the local variable `i`. Since Java passes the **reference by value**, only the local copy of the reference changes, so the original `Integer` object outside the method **does not change**.

# 5. Abstract Class vs Interface

**What is the difference between an abstract class and an interface? If abstract classes already exist, why do we need interfaces? Can interfaces have member variables and can they be initialized?**

 - An **abstract class** and an **interface** are both used in Java to achieve abstraction, but they serve slightly different design purposes. 
 - An abstract class is a partially implemented class that can contain both **abstract methods (without implementation)** and **concrete methods (with implementation)**, along with **instance variables**, constructors, and different access modifiers. It is typically used when classes share a **common base with shared behavior or state**. A class can extend **only one abstract class** due to Java’s single inheritance restriction. 
 - In contrast, an **interface** defines a **contract that classes must follow** and is mainly used to represent capabilities or behaviors that can be shared across unrelated classes. A class can implement **multiple interfaces**, which is the main reason interfaces exist even though abstract classes already provide abstraction — they allow **multiple inheritance of type**, enabling greater flexibility in design. 
 - Interfaces cannot have normal instance variables; any variables declared in an interface are **implicitly `public`, `static`, and `final`**, meaning they are **constants**. Because they are `final`, they **must be initialized at the time of declaration** and cannot be changed later. 
 - Interfaces mainly contain **abstract methods**, but since Java 8 they can also include **default methods and static methods**, allowing some method implementations while still maintaining their primary role as a contract for implementing classes.

# 6. Thread Communication in Java

**How do two threads communicate with each other? Suppose thread **T1** wants to signal something to **T2**. What if the threads do not know each other's name or their instances are not accessible? Where does the `notify()` method belong — the `Thread` class or some other class? Also, what is the opposite scenario of a thread notifying another thread (for example, waiting for another thread to finish)?**

**Answer:** 
- In Java, threads typically communicate using **shared objects and synchronization mechanisms** rather than directly calling methods on each other. 
- The most common low-level mechanism is the **`wait()` / `notify()` / `notifyAll()`** pattern used together with **object monitors** inside a `synchronized` block. 
- If thread **T1** wants to signal **T2**, both threads synchronize on the same shared object. Thread **T2** may call `wait()` on that object to suspend itself until a condition changes, and thread **T1** later calls `notify()` or `notifyAll()` on the same object to wake waiting threads. 
- Importantly, `wait()`, `notify()`, and `notifyAll()` do **not belong to the `Thread` class**; they are methods of the **`Object` class**, because every object in Java can act as a **monitor (lock)** used for thread coordination. Threads therefore do not need to know each other's identity — they simply coordinate through a **shared monitor object or shared state**. 
- If thread instances are not directly accessible, threads can still communicate through **shared variables, concurrent data structures, locks, semaphores, blocking queues, or higher-level concurrency utilities from `java.util.concurrent`**. 
- Regarding the opposite scenario of notifying another thread, it is typically **waiting for another thread to complete its work**. The `join()` method allows one thread to **block until another thread finishes execution**, which is conceptually the inverse pattern: instead of signaling progress, a thread **waits for completion** of another thread.

# 7. Deadlock in Java MultiThreading

**What is a deadlock in multithreading? When can it happen? What are ways to avoid or overcome deadlocks? Does Java provide anything built-in to deal with deadlocks?**

**Answer:**  
- A **deadlock** occurs in multithreading when **two or more threads are permanently blocked because each thread is waiting for a resource (lock) that another thread holds**, creating a circular waiting situation where none of the threads can proceed. 
- Deadlocks typically occur when multiple threads try to acquire **multiple locks in different orders**.   
  - For example, thread **T1** holds lock **A** and waits for lock **B**, while thread **T2** holds lock **B** and waits for lock **A**; 
  - since neither thread releases its lock, both remain stuck forever.  
- Deadlocks generally arise when four conditions exist simultaneously: 
  - **mutual exclusion** (a resource can be used by only one thread at a time)
  - **hold and wait** (a thread holds a resource while waiting for another)
  - **no preemption** (a resource cannot be forcibly taken away), and 
  - **circular wait** (a cycle of threads each waiting for the next). 
- To **avoid deadlocks**, common strategies include: 
  - always **acquiring locks in a consistent global order**, 
  - **reducing the number of locks**, 
  - **avoiding nested synchronized blocks**, 
  - **using timeouts when acquiring locks**, 
  - or **using higher-level concurrency utilities** that reduce manual locking. 
- Java provides several built-in tools to help prevent or manage deadlocks, such as 
  - **`ReentrantLock`** with `tryLock()` (which allows attempting to acquire a lock with a timeout instead of blocking forever), 
  - and higher-level utilities in **`java.util.concurrent`** like **concurrent collections, executors, and blocking queues** that reduce the need for explicit locking. 
  - Additionally, Java provides monitoring tools such as **`ThreadMXBean`** and tools like **`jstack` or VisualVM** that can detect deadlocked threads during runtime, helping developers diagnose and resolve deadlock situations.
 