
# Lab: Plagiarism detection

In this lab your task is to modify a plagiarism detection program so that it makes better use of data structures. You will also implement important parts of an AVL tree, which is one of the standard balanced BST data structures.

## About the labs

* This lab is part of the examination of the course.
  Therefore, you must not copy code from or show code to other students.
  You are welcome to discuss general ideas with one another, but anything you do must be **your own work**.

* Further info on Canvas, see e.g. the page called "Performing the assignments".

* You can solve this lab in either Java or Python, and it's totally up to you.
  - Note that since Python is an interpreted language, it's much slower than Java – around 10 times slower on the tasks in this lab. So if you want something that's blazingly fast you should choose Java. 
  - A faster alternative to standard Python is to use [PyPy](https://www.pypy.org) instead. However, in this lab it seems to be only at most twice as fast than the standard Python interpreter.
  - You grade will not be affected by your choice of language! But if you choose Java you have to experiment with larger datasets than if you choose Python.


## Background

You are head of the Anti-Cheating Department at Palmers University of Mythology, where you are in charge of catching students who copy each others' work.

You recently bought a very expensive *plagiarism detection program*.
The program has the following job: it reads in a set of documents, finds similarities between them, and reports all pairs of documents that seem suspiciously similar.

You were very impressed with the program when you saw the salesperson demonstrate it on some small examples. Unfortunately, once you bought the program you realised that *it is extremely slow when given a large number of documents to check*. Reading the source code, you noticed that the program does not use appropriate data structures and therefore its time complexity is poor. (At Palmers University of Mythology, employees are expected to have taken a course in data structures and algorithms.)

In this lab, your task is to *speed up the plagiarism detection program* so that it works on much larger document sets. The lab has three parts:

1. Analyse the time complexity of the plagiarism detection program.

2. Improve the program by modifying it to use an intermediate data structure, an *index of n-grams*.

3. Speed up the indexes by implementing a *BST*, a binary search tree.

4. Speed up the indexes further by implementing an *AVL tree*, and re-analyse the program's complexity.


## Getting started

Your first task is to make sure that you can run the plagiarism detection program.

The Lab contains Java/Python source files, a directory of **documents** containing sample texts, and a file **answers.txt** where you will write down answers to questions in the lab. There are five document sets of various sizes called **tiny**, **small**, **medium**, **big** and **huge**, plus an extra set **badforbst** which you should ignore for now. All the document sets are collections of Wikipedia pages (downloaded automatically by following random links from a starting page).

**PlagiarismDetector.java/plagiarism_detector.py** contains the main program. It requires the path to a directory containing plain text files to check. Now compile and run the main program on the small document sets (**tiny** and **small**). If you are using the command line, you can do this with:

<table><tr><th>Java</th><th>Python</th></tr><tr><td>

```
javac *.java
java PlagiarismDetector -d ../documents/small
```

</td><td>

```
python plagiarism_detector.py -d ../documents/tiny
```

</td></tr></table>

**Note:** If you are running from an IDE you might have a different default directory for your project. If you get a **NoSuchFileException** (Java) or **FileNotFoundError** (Python), please try the path `documents/small`, `documents/tiny`, ..., instead.

Alternatively, if you don't specify any command-line arguments, the client will ask you to provide them. You can just press ENTER for all questions except the directory path, to keep their default values:

<pre>
(...a lot of information...)
Enter values:
 * path to directory of documents: <b><i>../documents/small</b></i>
(...more questions to answer...)
</pre>

After a few seconds, you should see the following output (probably with different timings – here I'm running Python on an oldish laptop):

```
Reading all input files took 1.14 seconds.
Building n-gram index took 0.00 seconds.
Computing intersections   100% [========================================]     23  of 23   |   15.9 s
Computing intersections took 15.92 seconds.
Finding the most similar files took 0.00 seconds.
In total the program took 17.06 seconds.

Balance statistics:
  fileNgrams: ListMap (size 23)
  ngramIndex: ListMap (empty)
  intersections: ListMap (size 27)

Plagiarism report:
                                          weighted  weighted  weighted
  absolute   jaccard    cosine   average   jaccard    cosine   average
        80     0.033     0.092     0.132      2.68      7.39     10.55  Find-me.txt                               Plagiarism-abbreviated.txt
        52     0.085     0.156     0.157      4.40      8.14      8.15  Find-me.txt                               Rogeting.txt
        30     0.015     0.040     0.052      0.46      1.19      1.55  Find-me.txt                               To-be-or-not-to-be.txt
        26     0.018     0.037     0.037      0.48      0.95      0.96  Patricia-Wrightson.txt                    Peter-Sis.txt
        26     0.044     0.091     0.098      1.15      2.37      2.56  Pelle-flyttar-till-Komfusenbo.txt         Peter-och-Petra.txt
        12     0.008     0.018     0.019      0.10      0.21      0.23  Hans-Christian-Andersen-Award.txt         International-Board-on-Books-for-Young-People.txt
        11     0.003     0.009     0.014      0.03      0.10      0.15  Convention-on-the-Rights-of-the-Child.txt Declaration-of-the-Rights-of-the-Child.txt
        11     0.003     0.006     0.007      0.03      0.07      0.07  Pippi-Longstocking-novel.txt              Pippi-Longstocking.txt
        10     0.002     0.005     0.005      0.02      0.05      0.05  Plagiarism-abbreviated.txt                Plagiarism-detection.txt
         6     0.006     0.011     0.012      0.03      0.07      0.07  Hans-Christian-Andersen-Award.txt         Peter-Sis.txt
```

The program's main output is the plagiarism report at the bottom. The report lists *pairs* of files which have similar content, together with some numbers indicating how similar they are (the next section describes how these numbers are calculated). Here we can see that **Find-me.txt** has similar content to several other files. In fact, if you look in these files you will see that **Find-me.txt** is heavily plagiarised. **Patricia-Wrightson.txt** and **Peter-Sis.txt** are also similar, and if you look into the files you can see that one sentence is extremely similar – it's up to you to decide if it is plagiarised or not. And what do you think about the similarities between **Convention-on-the-Rights-of-the-Child.txt** and **Declaration-of-the-Rights-of-the-Child.txt**?

We also see some performance statistics, including timings for the various phases of the program. We see that in total, the program took 17 seconds to run, and that almost all the time is in what is reported as "Computing intersections". We also see statistics about the sizes of the various maps that the program creates.

Now try running the program on the **medium** directory, which consists of 81 files – about three times as big as **small**. You will find that it takes a while – you don't have to wait until it's finished but please try. Go on and read the next section while you wait!

### How the program measures similarity

One way to measure the similarity of two documents is to count how many words they have in common. However, this approach leads to false positives because the same set of words can be used in two very different documents. A better way is to count word n-grams, which are sequences of n consecutive words in a document or string. For example, the string "the fat cat sat on the mat" contains the 5-grams "the fat cat sat on", "fat cat sat on the" and "cat sat on the mat".

Given two documents, we will define their similarity score as the number of unique 5-grams that appear in both documents – that is, the size of the intersection of the two documents' sets of 5-grams. For example, "*the fat cat sat on the mat with a hat*" and "*the cat sat on the mat with a fat hat*" have three 5-grams in common – "*cat sat on the mat*", "*sat on the mat with*", and "*on the mat with a*" – and therefore have a similarity score of 3. A high similarity score means that a lot of content is shared between the documents, and is a possible sign of plagiarism.

The plagiarism report also include some other similarity measures. You can read about these in the docstring for the method `similarity` in **SimilarityCalculator.java/similarity_calculator.py**. If you want you can sort the output according to any of these measures, by specifying the argument `--similarity` at the command line.

### The source code files

The plagiarism detection program consists of quite many files, and here are the main ones:

- **PlagiarismDetector.java/plagiarism_detector.py**: the main program.
- **SimilarityCalculator.java/similarity_calculator.py**: these contain the four phases that the main program calls.
- **FasterSimilarityCalculator.java/faster_similarity_calculator.py**: improving two of the phases from the default `SimilarityCalculator` (*to be completed in task 4*).
- **Ngram.java/ngram.py**: a class for representing and computing n-grams.

Three different implementations of maps (also called dictionaries) – these maps are quite limited, for example they lack deletion:

- **SimpleMap.java/simple_map.py**: an abstract class defining the methods of a simple map.
- **ListMap.java/list_map.py**: a very stupid implementation of a `SimpleMap`, based on a list of key-value pairs. 
- **BSTMap.java/bst_map.py**: a map implemented using a binary search tree (BST) (*to be completed in task 3a*).
- **AVLMap.java/avl_map.py**: a map implemented using an AVL tree (*to be completed in task 3a*).

Three different implementations of (simple) sets – they are implemented using simple maps, so the code in these files are just a few lines:

- **SimpleSet.java/simple_set.py**: an abstract class defining the methods of a simple set.
- **ListSet.java/list_set.py**, **BSTSet.java/bst_set.py**, and **AVLSet.java/avl_set.py**: sets implemented using as wrappers over simple maps. 

There are also some extra files used by the lab code. *You do not need* to look at or understand these files:

- **CommandParser.java/command_parser.py**: for parsing command-line arguments.
- **ProgressBar.java/progress_bar.py**: for showing progress in the form of a progress bar.
- **Stopwatch.java/stopwatch.py**: for measuring the execution time.
- (Java only) **PathPair.java**: a class that represents a pair of filenames (in Python this is done by a standard tuple).

Note: Python programs usually don't consist of this many files – instead it's common to put several class definitions in one file. Java, on the other hand, prefers to have exactly one class per source file. To show the similarity between the Java and Python implementations, we have followed the Java convention with one class per file.

### How the program works

**PlagiarismDetector.java/plagiarism_detector.py** consists of a single `main` function, which computes the similarities in four phases. Each of these phases are implemented as methods in the class `SimilarityCalculator` (in **SimilarityCalculator.java/simliarity_calculator.py**). This class has three instance variables (`fileNgrams`, `ngramIndex` and `intersections`), which are built in the different phases. The instance variables are all maps to sets, i.e., maps where each key is associated with a set of values. Here are the four phases:

1. `readNgramsFromFiles` reads the n-grams from all input files and builds the index `fileNgrams` which is a map from filenames to sets of n-grams.

2. `buildNgramIndex` builds an index of n-grams. Or rather, currently it does nothing – it's your task to implement this in **FasterSimilarityCalculator.java/faster_simliarity_calculator.py**.

3. `computeIntersections` computes the intersections of all pairs of n-gram sets, i.e., for each pair of n-gram sets *A* and *B*, it computes the intersection *A* ∩ *B*. These are stored in the index `intersections` which is a map from pairs of filenames to sets of n-grams.

4. `findMostSimilar` finds the most similar file pairs, arranged in decreasing order of similarity. It does this by calling the method `similarity` which in turn uses the indexes `fileNgrams` and `intersections` to compute the similarity score.

Here is the main structure of **PlagiarismDetector.java/plagiarism_detector.py**:

<table><tr><th>Java</th><th>Python</th></tr><tr><td>

```java
public static void main(String[] args) throws IOException {
    ...
    SimilarityCalculator calculator = new SimilarityCalculator();  // or: new FasterSimilarityCalculator();

    // Phase 1: Read n-grams from all input files.
    calculator.readNgramsFromFiles(paths, ngramSize);

    // Phase 2: Build index of n-grams.
    calculator.buildNgramIndex();

    // Phase 3: Compute n-gram intersections.
    calculator.computeIntersections();

    // Phase 4: Find most similar file pairs.
    PathPair[] mostSimilar = calculator.findMostSimilar(limitResults, similarityMeasure);
    
    // Print out some statistics, and the plagiarism report.
    ...
}
```

</td><td>

```python
def main():
    ...
    calculator = SimilarityCalculator()  # or: FasterSimilarityCalculator()

    # Phase 1: Read n-grams from all input files.
    calculator.readNgramsFromFiles(paths, options.ngram)

    # Phase 2: Build index of n-grams.
    calculator.buildNgramIndex()

    # Phase 3: Compute n-gram intersections.
    calculator.computeIntersections()

    # Phase 4: Find most similar file pairs.
    mostSimilar = calculator.findMostSimilar(options.limit, measure=options.similarity)

    # Print out some statistics, and the plagiarism report.
    ...
```

</td></tr></table>

And here is the main structure of **SimilarityCalculator.java/simliarity_calculator.py**

<table><tr><th>Java</th><th>Python</th></tr><tr><td>

```java
public class SimilarityCalculator {
    public SimpleMap<Path, SimpleSet<Ngram>> fileNgrams;
    public SimpleMap<Ngram, SimpleSet<Path>> ngramIndex;
    public SimpleMap<PathPair, SimpleSet<Ngram>> intersections;

    public void readNgramsFromFiles(Path[] paths, int N) throws IOException {
        ...
    }

    public void buildNgramIndex() {
        // Note: this currently does nothing.
    }

    public void computeIntersections() {
        ...
    }

    public PathPair[] findMostSimilar(int M, String measure) {
        ...
    }
}
```

</td><td>

```python
class SimilarityCalculator:
    fileNgrams: SimpleMap[Path, SimpleSet[Ngram]]
    ngramIndex: SimpleMap[Ngram, SimpleSet[Path]]
    intersections: SimpleMap[PathPair, SimpleSet[Ngram]]

    def readNgramsFromFiles(self, paths: List[Path], N: int): 
        ...

    def buildNgramIndex(self):
        # Note: this currently does nothing.

    def computeIntersections(self):
        ...

    def findMostSimilar(self, M: int, measure: str) -> List[PathPair]:
        ...
```

</td></tr></table>

### Why is it slow?

In the output above, most of the time was spent in the "Computing intersections" step. This corresponds to phase 3, the method `computeIntersections`. This method is currently programmed using the following brute-force algorithm:

```python
for each document d1:
    for each document d2:
        if d1 < d2:
            for each n-gram n1 in d1:
                for each n-gram n2 in d1:
                    if n1 == n2:
                        add n1 to the intersection of (d1, d2)
```

You might wonder about the if clause `if d1 < d2`. All our similarity measures are *reflexive*, meaning that `sim(d1,d2) == sim(d2,d1)`. Therefore we don't have to calculate the scores (or the intersections) for both `(d1,d2)` and `(d2,d1)`. That's why we can filter out half of the path pairs.

Make sure you understand how this algorithm works. Also, read the source code for the method `computeIntersections` and make sure you understand that too.

You can see lots of nested loops here which should be a warning sign for *bad complexity*! As we saw, most of the program's runtime is spent executing this algorithm. If we want to run **PlagiarismDetector.java/plagiarism_detector.py** on larger document sets, we will have to eliminate this brute force search!

## Part 1: Complexity analysis

Now answer the following questions (write your answers in the **answers.txt** file provided).

- What is the asymptotic complexity of `computeIntersections`?

As always, assume that basic operations such as array accesses and comparisons take a constant amount of time. Your answer should be in terms of *N*, where *N* is the total number of 5-grams in the document set.

You may assume that all documents are about the same size (i.e., have the same number of 5-grams). You may also assume that there is not much plagiarised text; specifically, that most 5-grams occur in only one file, and that the number of duplicate occurrences of 5-grams is a small *constant*.

<details>
<summary><b>Spoiler hint</b> (click to see)</summary>

If you get stuck, try analysing it the following way: let *D* be the number of documents and *K* be the number of 5-grams per document. First find an expression for the complexity in terms of both *D* and *K*. Then use the fact that *N = D·K* to find an expression purely in terms of *N*.
</details>

Here are the approximate number of 5-grams in the five document sets: **tiny** (*N* = 10,000), **small** (*N* = 20,000), **medium** (*N* = 100,000), **big** (*N* = 1,000,000), and **huge** (*N* = 4,000,000).

- How long did the program take to compute the intersections for the **tiny** and **small** directories? (And **medium** if you could wait that long...)

- Is the ratio between the runtimes as you would expect, given the complexity? Explain very briefly why.

- How long do you predict the program would take to compute the intersections for the **big** and **huge** directories, respectively? (If your theoretical and empirical results were different, you can use either to make your prediction.)


## Part 2: Using an intermediate data structure

The `computeIntersections` method is slow because it has to search through all pairs of 5-grams in the two documents, even though it only wants the 5-grams that are equal. Let's fix this by improving the program's use of different indexes (or maps/dictionaries).

The data structure you will add is an index that, given a 5-gram, allows us to figure out which files contain that 5-gram. We can represent this index as a map/dictionary, where the key is a 5-gram and the value is the set of all filenames that contain the 5-gram:

<table><tr><th>Java</th><th>Python</th></tr><tr><td>

```java
public SimpleMap<Ngram, SimpleSet<Path>> ngramIndex;
```

</td><td>

```python
ngramIndex: SimpleMap[Ngram, SimpleSet[Path]]
```

</td></tr></table>

In fact, **SimilarityCalculator.java/simliarity_calculator.py** contains a declaration for exactly that index. The `buildNgramIndex` method is supposed to create it, but it currently does nothing at all. Your task is therefore to build this index, and then modify `computeIntersections` to use that index.

### Task for part 2: Make use of an n-gram index

To be able to compare the implementations you *should not* modify the code in **SimilarityCalculator.java/simliarity_calculator.py**! Instead there are skeleton methods ready for you in **FasterSimilarityCalculator.java/faster_simliarity_calculator.py**. So go to that file and start implementing:

1. Finish the implementation of `buildNgramIndex`. You have access to the instance variable `fileNgrams`, which contains the 5-grams of each file, as described above. The method should build the instance variable `ngramIndex` containing all 5-grams contained in all input files; the value associated with a 5-gram should be the set of filenames containing that 5-gram.

   Note that `ngramIndex` is empty when you start, which means that there are no keys at all in the map. To make sure that the set for a given 5-gram is initialised, you should call the `SimpleMap` method `getDefault`.

2. Implement `computeIntersections` so that it makes appropriate use of `ngramIndex`. You will have to think how best to do this! The goal is to replace brute-force searches by lookups in `ngramIndex`.

You do not need to modify any methods except for `buildNgramIndex` and `ngramIndex`, and only in **FasterSimilarityCalculator.java/faster_simliarity_calculator.py**. In fact, you do not even need to understand the code of the other methods (although it does not hurt to read them).

### Testing your new implementation

When you are finished, re-run your program with the command line argument `--index`. This will use your new implementation instead of the stupid one. 

***Wait, what's happening here?? There's no improvement...***

Actually, there is a small improvement: the phase "Computing intersections" should be a little bit faster, but it takes a loooong time to build the n-gram index. You will solve this problem in part 3 below.

### Questions for part 2

- How long time does it take to (1) build the n-gram index, and (2) compute the intersections, for the **small** directory?

- Was this an improvement compared to the original implementation that didn't use an intermediate index?


## Part 3: Implementing a binary search tree, BST

The main remaining problem is that we are using a particularly bad implementation for maps (and sets). The default implementation is the `ListMap`, which stupidly stores the map as a list of key-value pairs. As you know, the complexity for this implementation is *linear* in the number of keys. No wonder everything is slow...

Luckily, you already know how to solve this – use a better data structure. So, your task now is to complete the implementation of `BSTMap` (in **BSTMap.java/bst_map.py**). What is missing is the code for looking up a key in the map:

<table><tr><th>Java</th><th>Python</th></tr><tr><td>

```java
public Value get(Key key) {
    ...

    // If the key does not exist, generate a default value and associate with the key:
    Value value = this.defaultValueSupplier.get();
    if (value != null)
        this.put(key, value);
    return value;
}
```

</td><td>

```python
def get(self, key: Key) -> Value:
    ...

    # If the key does not exist, generate a default value and associate with the key:
    value = self.defaultValueSupplier()
    if value is not None:
        self.put(key, value)
    return value
```

</td></tr></table>

The reason for calling `.put(key,value)` at the end is to be able to handle *mutable* objects, such as sets or lists.

The rest of the class is already finished, in particular the code for associating a value with a key (the method `put`).

### Testing the implementation

Now you can try out your new algorithm. Make sure to give the command line arguments `--index` *and* `--map bst`. If you forget one of them you will see a drastic slowdown.

Make sure that you get the same results as before, but a lot faster! First try with the **tiny** directory, and move your way upwards. Depending on exactly how you implemented `computeIntersections` and which programming environment you use, you may run into problems already for the **small** directory, or it could work for **medium**, or **big**, or even **huge**. In any case, the speed improvement you saw for the **tiny** directory might not be as impressive for the larger ones. This just depends on whether your algorithm tends to produce unbalanced BSTs. If it doesn't work, don't worry, you will fix it in the next section!

If you run your program on the **badforbst** document set (*N* = 30,000), you will get an exception (`StackOverflowError` in Java and `RecursionError` in Python). This is because this directory is specially designed to cause `ngramIndex` to become unbalanced (if you look at the contents of one specific file in this directory, you should see why). As usual, we see that BSTs must be somewhat balanced if we want to have good performance.

### Questions for part 3

Now answer the following question:

- How long time does it take to (1) build the n-gram index, and (2) compute the intersections, for the **tiny**, **small** and **medium** directories? (If you don't get a stack overflow error / recursion error)

- Which of the BSTs appearing in the program usually become unbalanced? (The program prints the size and height of all its BSTs.)

And here is an **optional extra question** for those who are interested:

- Is there a simple way you could prevent these BSTs from becoming unbalanced?


## Part 4: Implementing an AVL tree

In order to prevent the binary search trees created by the program from becoming too unbalanced, your final task is to implement an AVL tree, which is one of the standard self-balancing BSTs.

In **AVLMap.java/avl_map.py**, you will find an unfinished implementation of AVL trees. It is an extension of the BSTs, with the only difference being how to insert nodes. As you already know (right?), AVL trees work by first adding a node just as a normal BST, but then it rebalances the tree if a node becomes unbalanced. Therefore, the method `putHelper` first calls the BST implementation of the method, then it rebalances the tree if necessary:


<table><tr><th>Java</th><th>Python</th></tr><tr><td>

```java
protected Node putHelper(Node node, Key key, Value value) {
		node = super.putHelper(node, key, value);
		int balanceFactor = this.heightHelper(node.left) - this.heightHelper(node.right);
    ...
}
```

</td><td>

```python
  def _putHelper(self, node, key, value) -> Node:
      node = super()._putHelper(node, key, value)
      balanceFactor = self._heightHelper(node.left) - self._heightHelper(node.right)
      ...
```

</td></tr></table>

Your job is to finish the implementation of `putHelper`. When rebalancing the tree you will have to do some right and left rotations, which are done by the methods `rightRotate` and `leftRotate`. The methods are also unfinished, and you have to complete them too.

### Testing your AVL implementation

When you have finished implementing the methods, run **PlagiarismDetector.java/plagiarism_detector.py** on the document sets, with the command line arguments `--index` and `--map avl`.

You should find that all of the document sets work, and a lot faster than before. None of them should take more than a few minutes (the **huge** directory might take a while if you're using Python). If you like, you can look for plagiarised text in the sample document sets!

### Questions for part 4: Renewed complexity analysis

Finally, re-do the complexity analysis that you did at the beginning by answering the same question as in part 1:

- What is the time asymptotic complexity of running `buildNgramIndex`?

- What is the time asymptotic complexity of running `computeIntersections`?

You may make the following assumptions:
  * The document set contains a total of *N* 5-grams.
  * There is not much plagiarised text; specifically, most 5-grams occur in one file only. The number of 5-grams that occur in more than one file is a *small constant*. Only a *small constant* number of files contain a 5-gram that also appears in another file.

You will most likely need the following complexity facts (some of them are only true when using amortised analysis, but that is OK when you are analysing the performance of a whole program):
  * Adding an item to an AVL tree of size *O(N)* takes *O(log(N))* time.

- How much faster have the program become, now that you're using both an intermediate ngram index and self-balancing AVL trees?

And finally there is an **optional extra question**:

- In our complexity analysis, suppose we drop the assumption that there is not much plagiarised text. What is the program's asymptotic complexity in terms of *N* and *S*, where *S* is the total similarity score across all pairs of files?


## Looking back on what you have done

By introducing an extra data structure (an index from n-grams to files), you managed to speed up the execution of the plagiarism detection program enormously. You can check a document set in seconds that would have taken hours before, just by introducing one index. These are the kind of gains you get by basing your program on suitable data structures.

With some patience, you could now check a much larger set of documents – say 1GB. This might take an hour or so – but in the version of the program you started with, it would have taken about *5 years*!

We also saw that almost all of the program's runtime was spent in one small subtask. Seeing that our program was slow, it would have been a total waste of time to optimise the part that reads in the files, or that ranks the similarity scores. On the other hand, by focusing on the right part of the program, huge gains were possible. Try to remember Knuth's famous quote:

> "We should forget about small efficiencies, say about 97% of the time: premature optimization is the root of all evil. Yet we should not pass up our opportunities in that critical 3%."

You also implemented both BST and AVL trees, and saw that AVL trees avoid the problems caused by unbalanced BSTs. Plain BSTs are a little too unreliable for general use, and it is usually worth spending the extra effort to add balancing. 

In real life though, you would use a suitable data structure provided by your programming environment, such as:
- Java: you can use a [TreeMap](https://docs.oracle.com/javase/8/docs/api/java/util/TreeMap.html) or a [HashMap](https://docs.oracle.com/javase/8/docs/api/java/util/HashMap.html), instead of implementing your own map.
- Python: the built-in `dict` class is a highly optimised linear probing hash table

When programming, make sure you know your programming language's standard library of data structures.

Of course, a real plagiarism detection program should have many more features, and some possible feature ideas are listed below – but when processing text on this scale, behind every fancy detection feature there needs to be a smart data structure!


## Submission

Double check:
* Have you answered the questions in **answers.txt**?
  (don't forget the ones in the appendix)
* Have you tested your code with **Robograder**?

Read in Canvas how to submit your lab.


## Optional tasks

Here are a few ideas for things to do if you want to learn more. These are just a few possible starting points – robust plagiarism detection is a difficult and open-ended problem!

- Experiment with different n-gram sizes (the command line option `--ngram`), and/or with different similarity measures (the command line option `--similarity`). How does the ranking change and why?

- Implement `SimpleMap` and `SimpleSet` in terms of built-in data structures (`TreeMap/HashMap/TreeSet/HashSet` in Java, and `dict/set` in Python).

- Implement a hash table version of `SimpleMap`. Or any other kind of balanced BSTs, such as red-black trees, scapegoat trees, 2-3 trees, or treaps.

- Calculate what n-grams are most common and least common. When scoring similarity, weight less common shared n-grams higher. Or weight n-grams using [tf-idf](https://en.wikipedia.org/wiki/Tf%E2%80%93idf).

- Rather than just counting how many 5-grams two documents have in common, count n-grams for lots of different values of n simultaneously. You can even weigh the similarity by the length of the n-grams.

- Make the similarity score more robust. For example, figure out how to catch plagiarists who use a thesaurus to replace words by synonyms. (Is there a way for the program to normalise texts, e.g., replacing each word by its "best" synonym?) Similarly, deleting or inserting words from the text reduces the number of matching n-grams – can we fix that?

- Can this method be used to find plagiarised code? One problem is that many files often contain the same standard boilerplate (e.g. licence text, standard loop structures, …), which leads to high plagiarism scores – can this be fixed?


## Acknowledgements

This lab is inspired by the "Catching Plagiarists" lab by Baker Franke.

