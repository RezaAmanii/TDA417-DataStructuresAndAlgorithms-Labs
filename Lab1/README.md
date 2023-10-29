
# Lab: Binary search

This short lab has two purposes:
* to introduce you to the lab system,
* to practice versions of binary search.


## About the labs

* This lab is part of the examination of the course. Therefore, you must not copy code from or show code to other students. You are welcome to discuss general ideas with one another, but anything you do must be **your own work**.

* Further info on Canvas, see e.g. the following pages:
  - "General information"
  - "The lab system"
  - "Running the labs"

* You can solve this lab in either Java or Python, and it's totally up to you.
  - You grade will not be affected by your choice of language!


## Overview

Binary search is a highly efficient way to find an item in a *sorted* array. It works by looking at increasingly smaller *ranges* of the array (where each range is given by a low index and a high index). Initiality, the range is the entire array. In each step, we compare the given item with the middle element of the current range:

* If the item is smaller, it can only be in the left side of the range.
* If the item is larger, it can only be in the right side of the range.

We continue with this, if necessary until the range is empty.

In this lab, you will implement three versions of binary search in **binary_search.py/BinarySearch.java** (without looking up how to do it):

* two basic versions that just checks if the item is in the array, and
* an advanced version that finds the *first position* where the item occurs.

All versions should use as few comparisons as possible. For the basic versions you should implement one *iterative* and one *recursive* version. For the advanced version you can decide if you want to make it iterative or recursive.

Finally, you will answer a few questions in `answers.txt`.


## Part 1: Implement an iterative and a recursive version

Your first task is to implement the following two functions in **binary_search.py/BinarySearch.java**:

<table><tr><th>Python</th><th>Java</th></tr><tr><td>

```python
def containsIterative(array: List[Comparable], value: Comparable) -> bool:
    ...


def containsRecursive(array: List[Comparable], value: Comparable) -> bool:
    ...

```

</td><td>

```java
    public static<V extends Comparable<? super V>> boolean containsIterative(V[] array, V value) {
        [...]
    }

    public static<V extends Comparable<? super V>> boolean containsRecursive(V[] array, V value) {
        [...]
    }
```

</td></tr></table>

You should assume that all arguments are non-null and `array` is sorted according to the natural ordering (i.e., smaller items come first).

These functions are *generic* in the type of the array elements, as long as they are `Comparable`. That means they can be called with any kind of elements that can be compared with each other, for example numbers (`int/Integer`) or strings (`str/String`).

But this also means that they only thing we can do with the elements is to compare them, which is done differently in Python and Java:

| Python   | Java                  |
| -------- | --------------------- |
| Use the comparison operators <, >, ==, etc.: | Use the `compareTo` method of one of the objects: |
| `x < y`  | `x.compareTo(y) < 0`  |
| `x == y` | `x.compareTo(y) == 0` |
| `x > y`  | `x.compareTo(y) > 0`  |

**Java note**:
In Java the primitive types (`char`, `int`, `float`, ...) are *not* `Comparable`, you have to use their class counterparts instead (`Char`, `Integer`, `Float`, ...).

### Different approaches

There are two main approaches for implementing binary search:

* the *iterative style*: using a loop,
* the *recursive style*: using a helper function that calls itself.

We want you to experiment with both styles. So you should implement **one iterative** and **another recursive** version of the contains function.

**Note**:
If you are stuck on the recursive approach, have a look at the page "Recursion practice" on Canvas. If you are really stuck, here are some hints (click to see):

<details>
<summary>Spoiler 1</summary>

Do you have an iterative solution (using a loop)? Try to convert it into a recursive solution by turning the loop into a recursive helper function.
</details>

<details>
<summary>Spoiler 2</summary>

For the recursive solution, you will have to add a helper function. This function should have the same signature as the original function, except for some extra arguments that change in the recursive call.
</details>

<details>
<summary>Spoiler 3</summary>

Try to give the recursive helper function two extra arguments `lo` and `hi`. These tell you which range of the array to search in. How does the helper function call itself?
</details>

### Requirements

* You are not allowed to call any searching methods on the list (e.g., `index` for Python lists), or use any libraries (e.g., `Arrays.binarySearch` from `java.util`), or look up code for binary search elsewhere. You have to implement your own binary search!

* Don't use *reference equality* (`x == y` in Java, and `x is y` in Python). This checks if two objects are exactly the same object. E.g., the strings `x="one"` and `y="one"` can be different objects but they have the same value, which means that:
  * in Java: `x.equals(y)` is true, but `x==y` is false
  * in Python: `x==y` is true, but `x is y` is false

* **Java only**: We consider values x and y equal if `x.compareTo(y)` returns 0. Do not use `x.equals(y)` (and don't use reference equality as already mentioned).

* All functions should use as few comparisons as possible for a given array size. Note that the array can contain lots of duplicate elements.


## Part 2: Find the first matching value

Now you should implement the function `firstIndexOf`, which returns the **first** index whose element matches the given search term. If the value doesn't exist in the array it should return -1.

<table><tr><th>Python</th><th>Java</th></tr><tr><td>

```python
def firstIndexOf(array: List[Comparable], value: Comparable) -> int:
    ...
```

</td><td>

```java
    public static<V extends Comparable<? super V>> int firstIndexOf(V[] array, V value) {
        [...]
    }
```

</td></tr></table>

For example, calling `firstIndexOf` on the array «1, 1, 3, 3, 5, 5» with search key 3 should return 2 because the first occurrence of 3 in the array is at index 2. But if we call it with key 7 it should return -1.

You can decide for yourself if you want to implement it iteratively or recursively.

## Testing

* The file has a `main` function with a few tests, using `assert` -- feel free to add your own tests. Just run the file, and it will report if any test fails.
  - **Python**: Run `python binary_search.py` from the command line.
  - **Java**: You need to enable assertions -- run `java -ea BinarySearch` from the command line.

* The debugger is very useful for finding out where your function goes wrong. It allows you to go through your function line-by-line and see how the variables change. If you have not used a debugger before, grab a teaching assistant to teach you.

* **Robograder** will run some pretty comprehensive tests on your code -- make sure to use it before you submit. Read in Canvas how to call Robograder.


## Part 3: Test your implementation

Run some tests to figure out the answer to the following question:
*How many comparisons does `firstIndexOf` use *at most* for an array of...*

- 10 elements?
- 100 elements?
- 1,000 elements?
- 1,000,000 elements?

Write down your answers in the file **answers.txt**.

*Hint*: If you use a debugger to through a program run line-by-line, you can count the number of comparisons.

Alternatively, you can (temporarily) insert print-statements, or increase a counter. (But make sure to remove those before submitting your solution!)


## Part 4: Reason about your implementation

Calculate the answer to the following question:
*How many comparisons would firstIndexOf need for 1,000,000,000,000 elements?*
(This is way too big for your computer's memory!)

Write down your answer and a justification in the file **answers.txt**.


## Submission

Double check:
* Have you answered the questions in **answers.txt**?
  (don't forget the ones in the appendix)
* Have you tested your code with **Robograder**?

Read in Canvas how to submit your lab.


