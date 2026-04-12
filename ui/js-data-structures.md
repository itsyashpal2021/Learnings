# Arrays

---

## 🔹 Adding / Removing

| Method      | Description |
|------------|------------|
| push()     | Adds element(s) to the end of the array |
| pop()      | Removes the last element |
| unshift()  | Adds element(s) to the beginning |
| shift()    | Removes the first element |

---

## 🔹 Searching

| Method         | Description |
|---------------|------------|
| includes()    | Checks if value exists (returns true/false) |
| indexOf()     | Returns first index of value, or -1 |
| lastIndexOf() | Returns last index of value |
| find()        | Returns first element matching condition |
| findIndex()   | Returns index of matching element |

---

## 🔹 Iteration / Transformation

| Method     | Description |
|-----------|------------|
| forEach() | Loops through array (no return) |
| map()     | Creates new array by transforming each element |
| filter()  | Creates new array with matching elements |
| reduce()  | Reduces array to single value |

---

## 🔹 Modify

| Method   | Description |
|---------|------------|
| slice() | Returns shallow copy (non-mutating) |
| splice()| Adds/removes elements (mutates original array) |
| concat()| Merges arrays |
| flat()  | Flattens nested arrays |

---

## 🔹 Sorting

| Method   | Description |
|---------|------------|
| sort()  | Sorts elements (default is string sort) |
| reverse()| Reverses array |

---

# 💻 Complete Example (All Methods Used)

```js
let arr = [1, 2, 3, 4, 5];

// 🔹 Adding / Removing
arr.push(6);          // [1,2,3,4,5,6]
arr.pop();            // [1,2,3,4,5]
arr.unshift(0);       // [0,1,2,3,4,5]
arr.shift();          // [1,2,3,4,5]

// 🔹 Searching
console.log(arr.includes(3));      // true
console.log(arr.indexOf(3));       // 2
console.log(arr.lastIndexOf(3));   // 2
console.log(arr.find(x => x > 3)); // 4
console.log(arr.findIndex(x => x > 3)); // 3

// 🔹 Iteration / Transformation
arr.forEach(x => console.log(x));  

const mapped = arr.map(x => x * 2);     // [2,4,6,8,10]
const filtered = arr.filter(x => x % 2 === 0); // [2,4]
const reduced = arr.reduce((sum, x) => sum + x, 0); // 15

// 🔹 Modify
const sliced = arr.slice(1, 3);   // [2,3]
arr.splice(2, 1);                 // remove element at index 2

const arr2 = [6, 7];
const combined = arr.concat(arr2); // merge arrays

const nested = [1, [2, [3]]];
const flattened = nested.flat(2);  // [1,2,3]

// 🔹 Sorting
const unsorted = [5, 2, 8, 1];
unsorted.sort((a, b) => a - b); // [1,2,5,8]
unsorted.reverse();             // [8,5,2,1]
```

# Maps

---

## 🔹 Basic Operations

| Method        | Description |
|--------------|------------|
| set(key,val) | Adds or updates a key-value pair |
| get(key)     | Returns the value for a given key |
| has(key)     | Checks if a key exists |
| delete(key)  | Removes a key-value pair |
| clear()      | Removes all entries from the map |

---

## 🔹 Iteration

| Method     | Description |
|-----------|------------|
| keys()    | Returns an iterator of all keys |
| values()  | Returns an iterator of all values |
| entries() | Returns an iterator of [key, value] pairs |
| forEach() | Iterates over each key-value pair |

---

# 💻 Complete Example (All Methods Used)

```js
// create map
const map = new Map();

// 🔹 Basic Operations
map.set("a", 1);
map.set("b", 2);
map.set("c", 3);

// update value
map.set("a", 10);

// access
console.log(map.get("a"));   // 10

// check existence
console.log(map.has("b"));   // true

// delete key
map.delete("c");

// 🔹 Iteration

// keys
for (const key of map.keys()) {
  console.log("key:", key);
}

// values
for (const value of map.values()) {
  console.log("value:", value);
}

// entries
for (const [key, value] of map.entries()) {
  console.log(key, value);
}

// forEach
map.forEach((value, key) => {
  console.log(key, value);
});

// 🔹 Clear map
map.clear();

console.log(map.size); // 0
```

# Sets

---

## 🔹 Basic Operations

| Method      | Description |
|------------|------------|
| add(value) | Adds a value to the set |
| has(value) | Checks if a value exists |
| delete(value) | Removes a value from the set |
| clear()    | Removes all values from the set |

---

## 🔹 Iteration

| Method     | Description |
|-----------|------------|
| keys()    | Returns iterator of values (same as values()) |
| values()  | Returns iterator of values |
| entries() | Returns [value, value] pairs |
| forEach() | Iterates over each value |

---

# 💻 Complete Example (All Methods Used)

```js
// create set
const set = new Set();

// 🔹 Basic Operations
set.add(1);
set.add(2);
set.add(3);

// duplicate (ignored)
set.add(2);

// check existence
console.log(set.has(2)); // true

// delete value
set.delete(3);

// 🔹 Iteration

// keys (same as values)
for (const key of set.keys()) {
  console.log("key:", key);
}

// values
for (const value of set.values()) {
  console.log("value:", value);
}

// entries
for (const [k, v] of set.entries()) {
  console.log(k, v); // value, value
}

// forEach
set.forEach(value => {
  console.log("forEach:", value);
});

// 🔹 Common Use Case: Remove Duplicates
const arr = [1, 2, 2, 3, 3, 4];
const unique = [...new Set(arr)];
console.log(unique); // [1,2,3,4]

// 🔹 Clear set
set.clear();

console.log(set.size); // 0
```

# Strings

---

## 🔹 Searching

| Method         | Description |
|---------------|------------|
| includes()    | Checks if substring exists (returns true/false) |
| indexOf()     | Returns first index of substring, or -1 |
| lastIndexOf() | Returns last index of substring |
| startsWith()  | Checks if string starts with given substring |
| endsWith()    | Checks if string ends with given substring |

---

## 🔹 Extraction

| Method       | Description |
|-------------|------------|
| slice()     | Extracts part of string (supports negative index) |
| substring() | Extracts part of string (no negative index support) |

---

## 🔹 Modification

| Method          | Description |
|----------------|------------|
| replace()      | Replaces first match |
| replaceAll()   | Replaces all matches |
| toUpperCase()  | Converts string to uppercase |
| toLowerCase()  | Converts string to lowercase |
| trim()         | Removes whitespace from both ends |

---

## 🔹 Conversion / Access

| Method         | Description |
|---------------|------------|
| split()       | Converts string to array |
| charAt()      | Returns character at index |
| charCodeAt()  | Returns Unicode (UTF-16) of character |

---

# 💻 Complete Example (All Methods Used)

```js
const str = "  Hello JavaScript World  ";

// 🔹 Searching
console.log(str.includes("JavaScript"));   // true
console.log(str.indexOf("World"));         // index
console.log(str.lastIndexOf("o"));         // last occurrence
console.log(str.startsWith("  He"));       // true
console.log(str.endsWith("  "));           // true

// 🔹 Extraction
const sliced = str.slice(2, 7);            // "Hello"
const sub = str.substring(2, 7);           // "Hello"

// 🔹 Modification
const replaced = str.replace("World", "JS");
const replacedAll = str.replaceAll("o", "0");
const upper = str.toUpperCase();
const lower = str.toLowerCase();
const trimmed = str.trim();

// 🔹 Conversion / Access
const words = str.split(" ");
const char = str.charAt(2);
const code = str.charCodeAt(2);

console.log({
  sliced,
  sub,
  replaced,
  replacedAll,
  upper,
  lower,
  trimmed,
  words,
  char,
  code
});
```
