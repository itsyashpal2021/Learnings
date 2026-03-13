# Java Collections Notes

This document covers the key Java Collection interfaces and classes: List, Map, Set, Queue, Deque, and Stack, along with their methods, time complexities, and common implementations.

---

## 1. List

**Description:**  
A `List` is an ordered collection that allows duplicate elements. Elements can be accessed by their index.

**Common Methods and Complexity:**

| Method | Description | Time Complexity |
|--------|-------------|----------------|
| `add(E e)` | Appends element at end | O(1) amortized |
| `add(int index, E element)` | Inserts element at index | O(n) |
| `get(int index)` | Retrieve element at index | O(1) |
| `set(int index, E element)` | Replace element at index | O(1) |
| `remove(int index)` | Remove element at index | O(n) |
| `remove(Object o)` | Remove first occurrence | O(n) |
| `size()` | Returns number of elements | O(1) |
| `contains(Object o)` | Check if element exists | O(n) |
| `indexOf(Object o)` | Returns index of first occurrence | O(n) |
| `lastIndexOf(Object o)` | Returns index of last occurrence | O(n) |
| `iterator()` | Returns iterator | O(1) |

**Notable Implementations:**
- `ArrayList` – Resizable array, fast random access, slower insert/remove at middle.
- `LinkedList` – Doubly linked list, fast insert/remove, slower random access.
- `Vector` – Thread-safe ArrayList equivalent.
- `Stack` – Legacy stack class (extends Vector).

---

## 2. Map

**Description:**  
A `Map` is a key-value collection with unique keys. Keys map to values, and duplicates are not allowed for keys.

**Common Methods and Complexity:**

| Method | Description | Time Complexity (HashMap) |
|--------|-------------|---------------------------|
| `put(K key, V value)` | Adds or replaces value | O(1) |
| `get(Object key)` | Retrieve value | O(1) |
| `remove(Object key)` | Remove key-value pair | O(1) |
| `containsKey(Object key)` | Check key existence | O(1) |
| `containsValue(Object value)` | Check value existence | O(n) |
| `size()` | Number of entries | O(1) |
| `keySet()` | Returns keys set | O(1) |
| `values()` | Returns values collection | O(1) |
| `entrySet()` | Returns set of key-value entries | O(1) |
| `putAll(Map m)` | Adds all entries from another map | O(m) |

**Notable Implementations:**
- `HashMap` – Unordered, fast access.
- `LinkedHashMap` – Maintains insertion order.
- `TreeMap` – Sorted map, based on red-black tree.
- `Hashtable` – Legacy thread-safe map.

---

## 3. Set

**Description:**  
A `Set` is a collection that contains no duplicate elements. The order may or may not be preserved depending on implementation.

**Common Methods and Complexity:**

| Method | Description | Time Complexity (HashSet) |
|--------|-------------|---------------------------|
| `add(E e)` | Adds element if absent | O(1) |
| `remove(Object o)` | Removes element | O(1) |
| `contains(Object o)` | Check existence | O(1) |
| `size()` | Number of elements | O(1) |
| `isEmpty()` | Checks if set is empty | O(1) |
| `iterator()` | Returns iterator | O(1) |

**Notable Implementations:**
- `HashSet` – Unordered, fast access.
- `LinkedHashSet` – Preserves insertion order.
- `TreeSet` – Sorted set based on red-black tree.
- `EnumSet` – High-performance set for enum types.

---

## 4. Queue

**Description:**  
A `Queue` is a collection designed for holding elements prior to processing, typically following FIFO (first-in-first-out) order.

**Common Methods and Complexity:**

| Method | Description | Complexity |
|--------|-------------|------------|
| `add(E e)` | Inserts element, throws exception if full | O(1) |
| `offer(E e)` | Inserts element, returns false if full | O(1) |
| `remove()` | Retrieves and removes head, exception if empty | O(1) |
| `poll()` | Retrieves and removes head, returns null if empty | O(1) |
| `element()` | Retrieves head, exception if empty | O(1) |
| `peek()` | Retrieves head, returns null if empty | O(1) |

**Notable Implementations:**
- `LinkedList` – Doubly-linked list, can act as Queue.
- `PriorityQueue` – Elements sorted by natural order or comparator.
- `ArrayDeque` – Resizable array, can act as queue efficiently.

---

## 5. Deque

**Description:**  
A `Deque` (double-ended queue) allows insertion and removal of elements at both ends. Supports both FIFO and LIFO operations.

**Common Methods and Complexity:**

| Method | Description | Complexity |
|--------|-------------|------------|
| `addFirst(E e)` | Insert at front | O(1) |
| `addLast(E e)` | Insert at end | O(1) |
| `offerFirst(E e)` | Insert at front, returns false if full | O(1) |
| `offerLast(E e)` | Insert at end, returns false if full | O(1) |
| `removeFirst()` | Remove first element | O(1) |
| `removeLast()` | Remove last element | O(1) |
| `pollFirst()` | Remove first, null if empty | O(1) |
| `pollLast()` | Remove last, null if empty | O(1) |
| `getFirst()` | Retrieve first | O(1) |
| `getLast()` | Retrieve last | O(1) |
| `peekFirst()` | Retrieve first, null if empty | O(1) |
| `peekLast()` | Retrieve last, null if empty | O(1) |

**Notable Implementations:**
- `ArrayDeque` – Resizable array, fast at both ends.
- `LinkedList` – Doubly-linked, supports deque operations.

---

## 6. Stack

**Description:**  
`Stack` is a legacy class representing a last-in-first-out (LIFO) collection. Often replaced by `Deque` for modern usage.

**Common Methods and Complexity:**

| Method | Description | Complexity |
|--------|-------------|------------|
| `push(E item)` | Push element on top | O(1) |
| `pop()` | Remove top element | O(1) |
| `peek()` | Retrieve top element | O(1) |
| `empty()` | Check if stack is empty | O(1) |
| `search(Object o)` | Return 1-based position from top | O(n) |

**Notable Implementations:**
- `Stack` – Extends `Vector`, synchronized.
- `ArrayDeque` – Recommended modern alternative for stack operations.

---
## Things to Remember (Syntax)

### 1. Sorting a List
```java
import java.util.*;

// Sorting in natural order
List<Integer> list = new ArrayList<>(Arrays.asList(5, 2, 8, 1));
Collections.sort(list);  // [1, 2, 5, 8]

// Sorting in reverse order
Collections.sort(list, Collections.reverseOrder());  // [8, 5, 2, 1]

// Sorting with custom comparator
list.sort((a, b) -> b - a);  // descending order
```

### 2. Initializing PriorityQueue, TreeSet, TreeMap with Comparator
``` java
import java.util.*;

// PriorityQueue with custom comparator (min-heap by default)
PriorityQueue<Integer> pq = new PriorityQueue<>((a, b) -> b - a);  // max-heap

// TreeSet with custom comparator
TreeSet<String> set = new TreeSet<>((s1, s2) -> s2.compareTo(s1));  // reverse alphabetical

// TreeMap with custom comparator
TreeMap<Integer, String> map = new TreeMap<>((a, b) -> b - a);  // keys in descending order
```

### 3. Reversing a List or String
``` java
import java.util.*;

// Reverse a List
List<Integer> list = new ArrayList<>(Arrays.asList(1, 2, 3, 4));
Collections.reverse(list);  // [4, 3, 2, 1]

// Reverse a String
String str = "hello";
String reversed = new StringBuilder(str).reverse().toString();  // "olleh"
```