# Java Multithreading Guide

This document explains **Java multithreading fundamentals**, common problems, and mechanisms Java provides to handle them.

---

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

Example timeline:
Time →

Thread A ███ ███
Thread B ███ ███
Thread C ███


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

Never call run() directly.

