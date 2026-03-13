**Create a class `Employee` which has 3 fields `firstName` `lastName` and `age`.**  
```java
Employee e1 = new Employee("name1", "lastName1", 15);
Employee e2 = new Employee("name1", "lastName1", 15);
```

-  what will be output e1 == e2 and e1.equals(e2) ?
- How can we make e1.equals(e2) to return true if `firstName` is same ?
- Given a list of employees, how to sort them based on age in ascending order >


**Question:** What does `o1.equals(o2)` return if the `equals()` method is not overridden?

**Answer:**  
If the `equals()` method is **not overridden** in a class, it uses the default implementation from the **`Object` class**. The default `Object.equals()` method performs **reference comparison**, meaning it checks whether both references point to the **exact same object in memory**. Therefore, `o1.equals(o2)` will return **`true` only if `o1` and `o2` refer to the same object instance**, and **`false` if they are different objects**, even if their internal data is identical. In other words, without overriding `equals()`, the comparison behaves similarly to the `==` operator for objects, because both effectively check **object identity rather than object content**.

- Override equals method from Object for custom equals logic on first name.
- Implement a comparator for sorting list of employees by age.