**Given a class `BaseAccount` having fields `name`, `accountNumber` and `balance`. Implement the class and methods to
achieve the following:**

- Implement a method `transfer(BaseAccount from, BaseAccount to, double amount)`
    - Add validations.
    - Add logic to update balances in each `from` and `to` accounts
- Is above implementation thread safe. Why?
- How can we make it thread safe
    - With `synchronized` keyword
    - With `synchronized` block. which object will lock be acquired on.
    - Nested `synchronized` blocks.
    - With using `locks` for each of the account objects.
- Is marking transfer method as `synchronized` optimal? Which of thread safety method above would be best.
- Can we use Atomic classes to ensure thread safety? Why?
- Can a deadlock occur in above?
    - Think if two transaction requests come `transfer(a1, a2, m1)` and `transfer(a2, a1, m2)`.
    - How can we avoid deadlock.