# SOLID Principles in Java

SOLID is a set of **five object-oriented design principles** that help developers write **clean, maintainable, scalable, and flexible code**.

The term **SOLID** was popularized by **Robert C. Martin (Uncle Bob)**.

## What does SOLID stand for?

| Letter | Principle |
|------|------|
| S | Single Responsibility Principle |
| O | Open/Closed Principle |
| L | Liskov Substitution Principle |
| I | Interface Segregation Principle |
| D | Dependency Inversion Principle |

---

# 1. Single Responsibility Principle (SRP)

## Definition

A class should have **only one reason to change**.

This means a class should handle **only one responsibility or functionality**.

---

## ❌ Wrong Example

One class performing multiple responsibilities.

```java
class Report {

    public void generateReport() {
        System.out.println("Generating report...");
    }

    public void saveToFile() {
        System.out.println("Saving report to file...");
    }

    public void sendEmail() {
        System.out.println("Sending report via email...");
    }
}
```

### Problems

This class handles:

- Report generation
- File saving
- Email sending

If any of these behaviors change, the class must change.

This makes the class **hard to maintain**.

---

## ✅ Correct Example

Separate responsibilities into different classes.

```java
class ReportGenerator {

    public void generateReport() {
        System.out.println("Generating report...");
    }
}

class ReportSaver {

    public void saveToFile() {
        System.out.println("Saving report to file...");
    }
}

class EmailService {

    public void sendEmail() {
        System.out.println("Sending email...");
    }
}
```

### Usage

```java
public class Main {

    public static void main(String[] args) {

        ReportGenerator generator = new ReportGenerator();
        generator.generateReport();

        ReportSaver saver = new ReportSaver();
        saver.saveToFile();

        EmailService emailService = new EmailService();
        emailService.sendEmail();
    }
}
```

Each class now has **one clear responsibility**.

---

# 2. Open/Closed Principle (OCP)

## Definition

Software entities should be:

- **Open for extension**
- **Closed for modification**

This means we should **add new functionality without modifying existing code**.

---

## ❌ Wrong Example

```java
class DiscountCalculator {

    public double calculateDiscount(String customerType) {

        if(customerType.equals("Regular")) {
            return 10;
        }

        else if(customerType.equals("Premium")) {
            return 20;
        }

        return 0;
    }
}
```

### Problem

If we introduce a new customer type like **GoldCustomer**, we must modify this class.

This **violates OCP**.

---

## ✅ Correct Example

Use **abstraction and polymorphism**.

```java
interface Customer {

    double getDiscount();
}
```

Concrete implementations:

```java
class RegularCustomer implements Customer {

    public double getDiscount() {
        return 10;
    }
}

class PremiumCustomer implements Customer {

    public double getDiscount() {
        return 20;
    }
}

class GoldCustomer implements Customer {

    public double getDiscount() {
        return 30;
    }
}
```

### Usage

```java
public class Main {

    public static void main(String[] args) {

        Customer c1 = new RegularCustomer();
        Customer c2 = new PremiumCustomer();
        Customer c3 = new GoldCustomer();

        System.out.println(c1.getDiscount());
        System.out.println(c2.getDiscount());
        System.out.println(c3.getDiscount());
    }
}
```

Now new customer types can be **added without modifying existing code**.

---

# 3. Liskov Substitution Principle (LSP)

## Definition

Objects of a **subclass should be replaceable with objects of the superclass** without breaking the program.

---

## ❌ Wrong Example

```java
class Bird {

    void fly() {
        System.out.println("Bird is flying");
    }
}

class Ostrich extends Bird {

    void fly() {
        throw new UnsupportedOperationException("Ostrich cannot fly");
    }
}
```

### Usage

```java
Bird bird = new Ostrich();
bird.fly(); // Runtime exception
```

### Problem

The subclass **cannot behave like the parent class**, so substitution fails.

This violates **LSP**.

---

## ✅ Correct Example

Separate flying and non-flying birds.

```java
class Bird {
}

class FlyingBird extends Bird {

    void fly() {
        System.out.println("Flying...");
    }
}

class Sparrow extends FlyingBird {
}

class Ostrich extends Bird {
}
```

### Usage

```java
FlyingBird bird = new Sparrow();
bird.fly();
```

Now the substitution works correctly.

---

# 4. Interface Segregation Principle (ISP)

## Definition

Clients should **not be forced to implement methods they do not use**.

Interfaces should be **small and specific**.

---

## ❌ Wrong Example

```java
interface Worker {

    void work();

    void eat();
}
```

Robot implementation:

```java
class Robot implements Worker {

    public void work() {
        System.out.println("Robot working...");
    }

    public void eat() {
        throw new UnsupportedOperationException();
    }
}
```

### Problem

Robot does not need `eat()`.

The interface forces unnecessary methods.

---

## ✅ Correct Example

Split the interface.

```java
interface Workable {

    void work();
}

interface Eatable {

    void eat();
}
```

Implementations:

```java
class Human implements Workable, Eatable {

    public void work() {
        System.out.println("Human working...");
    }

    public void eat() {
        System.out.println("Human eating...");
    }
}

class Robot implements Workable {

    public void work() {
        System.out.println("Robot working...");
    }
}
```

### Usage

```java
Workable robot = new Robot();
robot.work();

Human human = new Human();
human.eat();
```

Now classes implement **only required behavior**.

---

# 5. Dependency Inversion Principle (DIP)

## Definition

High-level modules should **not depend on low-level modules**.

Both should depend on **abstractions**.

---

## ❌ Wrong Example

```java
class Keyboard {
}

class Computer {

    private Keyboard keyboard = new Keyboard();
}
```

### Problem

`Computer` is tightly coupled with `Keyboard`.

Switching to another device requires modifying the class.

---

## ✅ Correct Example

Introduce an abstraction.

```java
interface InputDevice {
}
```

Concrete implementations:

```java
class Keyboard implements InputDevice {
}

class Mouse implements InputDevice {
}
```

High-level class:

```java
class Computer {

    private InputDevice device;

    public Computer(InputDevice device) {
        this.device = device;
    }
}
```

### Usage

```java
public class Main {

    public static void main(String[] args) {

        InputDevice keyboard = new Keyboard();
        Computer computer1 = new Computer(keyboard);

        InputDevice mouse = new Mouse();
        Computer computer2 = new Computer(mouse);
    }
}
```

Now the `Computer` class depends on **abstractions instead of concrete classes**.

---

# Final Summary

| Principle | Key Idea |
|------|------|
| SRP | One class should have one responsibility |
| OCP | Extend behavior without modifying existing code |
| LSP | Subclasses must safely replace parent classes |
| ISP | Interfaces should be small and specific |
| DIP | Depend on abstractions instead of concrete classes |

---

# Why SOLID Principles Matter

Using SOLID leads to:

- Cleaner architecture
- Easier maintenance
- Better scalability
- Easier testing
- Lower coupling between classes

These principles are widely used in modern Java frameworks such as:

- Spring Framework
- Spring Boot
- Hibernate

---