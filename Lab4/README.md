
# Lab: Path finder

In this lab your task is to write a program that calculates the shortest path between two vertices in a graph. You will try this on several kinds of graphs.


## About the labs

* This lab is part of the examination of the course. Therefore, you must not copy code from or show code to other students. You are welcome to discuss general ideas with one another, but anything you do must be **your own work**.

* Further info on Canvas, see e.g. the page called "Performing the assignments".

* You can solve this lab in either Java or Python, and it's totally up to you.
  - Note that since Python is an interpreted language, it's slower than Java – about 3–4 times slower on the tasks in this lab.
  - You grade will not be affected by your choice of language!


## Getting started

The lab files consist of a number of Python/Java files (explained below), a directory of **graphs**, and a file **answers.txt** where you will answer questions for the lab.


## Background

Your task is to write a program solving one of the most important graph problems: calculating the shortest path between two vertices. This is similar to what map applications do (e.g. Waze, OpenStreetMap, Google, Apple, Garmin, etc.), although your solution will not quite be able to handle as large road maps as them. But this is also useful in several other circumstances, and you will see some in this laboration.

There is a main program **pathfinder.py/PathFinder.java** which you can compile and run right away. It takes three required arguments:

- the algorithm ("random", "ucs", or "astar"),
- the type of graph to read ("AdjacencyGraph", "NPuzzle", "GridGraph", or "WordLadder"),
- the graph itself (usually a file name into the "graphs" subfolder, but for the NPuzzle it's a number denoting the size of the puzzle).

You can specify these arguments directly on the command line, like this:

<table><tr><th>Python</th><th>Java</th></tr><tr><td>

```
python pathfinder.py -a random -t AdjacencyGraph -g ../graphs/AdjacencyGraph/citygraph-SE.txt
```

</td><td>

```
javac *.java
java PathFinder -a random -t AdjacencyGraph -g ../graphs/AdjacencyGraph/citygraph-SE.txt
```

</td></tr></table>

But you can also just run the program, and it will let you enter the arguments (just like the previous labs).

The program first prints some information about the specified graph:

```
Adjacency graph with 729 vertices and 5454 edges.
Random vertices with outgoing edges:
  Stavsnäs ---> Djurö [9], Mörtnäs [17]
  Västervik ---> Ankarsrum [24], Gamleby [24], Gunnebo [13], Kristdala [62], Oskarshamn [69]
  Åkersberga ---> Bålsta [55], Kungsängen [39], Norrtälje [50], Resarö [21], Rimbo [37], Rosersberg [35], Sollentuna [25], Täby [19], Upplands Väsby [27], Vallentuna [18], Vaxholm [22]
  (...)
```

It then allows you to (repeatedly) perform path searches by specifying start and goal:

```
(ENTER to quit)
Start: Stockholm
Goal: Kiruna

Searching: Stockholm --> Kiruna
Searching the graph took 0.01 seconds.
Loop iterations: 5285
Cost of path from Stockholm to Kiruna: 232417.00
Number of edges: 5284
Stockholm --[16]--> Huddinge --[7]--> Tullinge --[7]--> Huddinge --[16]--> Stockholm --[6]--> ... --[190]--> Töre --[25]--> Rolfs --[25]--> Töre --[100]--> Övertorneå --[288]--> Kiruna
```

Alternatively, you may directly specify start and goal states as additional program arguments. You do that by adding `-q Stockholm Kiruna` to the command line above. In that case, the program directly prints the path found.


Note that a random walk will typically not find the shortest path. It will usually find a different path every time it runs. It may even run forever (or it would, if we didn't terminate the random walk after visiting 10,000 vertices).

```
Searching: Stockholm --> Kiruna
Searching the graph took 0.01 seconds.
Loop iterations: 10000
No path found from Stockholm to Kiruna
```


## Descriptions of the source files and classes

Just a for the plagiarism detection lab, this program consists of quite many files, and here is a quick summary of them.

Files and clases for defining different kinds of *graphs*:

- **edge.py/Edge.java**: This contains the class `Edge` for weighted directed graph edges.
- **graph.py/Graph.java**: This is an generic interface for weighted directed graphs. It specifies the main methods that a graph must implement. There are four different implementations of this interface:
  - **adjacency_graph.py/AdjacencyGraph.java**: This is the "standard" implementation where a graph is stored as a mapping from vertices to outgoing edges.
  - **gridgraph.py/GridGraph.java**: In this implementation the graph is a 2d-matrix of "pixels", and you can move around to neighbouring pixels. (This class is not finished and you will complete it in part 4)
  - **npuzzle.py/NPuzzle.java**: This is an implementation of the 15-puzzle.
  - **wordladder.py/WordLadder.java**: In this graph the vertices are words, and the edges are small modifications of the words. (This class is not finished and you will complete it in parts 2 and 4)
- **point.py/Point.java**: This is a basic class for 2d points or coordinates. It is used by **GridGraph** and **NPuzzle**.

Files and classes for different *search algorithms*:

- **search_result.py/SearchResult.java**: This file/class defines how search results look like.
- **search_algorithm.py/SearchAlgorithm.java**: This is a very simple generic interface/abstract class for search algorithms. There are three implementations:
  - **search_random.py/SearchRandom.java**: This is an extremely stupid algorithm, that just moves around randomly in the graph until it (hopefully) reaches the goal.
  - **search_ucs.py/SearchUCS.java**: This file implements the UCS (uniform cost search) algorithm, also known as Dijkstra's algorithm. (It is not finished and you will complete it in part 1)
  - **search_astar.py/SearchAstar.java**: This is an extension of the UCS algorithm which uses heuristics to guide the search. (It is not finished and you will complete it in part 3)
- **pathfinder.py/PathFinder.java**: This is the main file that loads the selected graph and calls the selected search algorithm.

And finally there are some utility files:

- **commmand_parser.py/CommandParser.java**: For parsing command-line arguments – it is the same as in the previous labs.
- **stopwatch.py/Stopwatch.java**: For timing – the same as in previous labs.
- **priority_queue.py** (only in Python): This is a wrapper around the `heapq` module, so that we get an API that is consistent with Java's built-in **PriorityQueue**.

Note: Python programs usually don't consist of this many files – instead it's common to put several class definitions in one file. Java, on the other hand, prefers to have exactly one class per source file. To show the similarity between the Java and Python implementations, we have followed the Java convention with one class per file.

### The search classes

The classes **SearchRandom**, **SearchUCS** and **SearchAstar** are the ones that do the heavy work of path finding. They all have a method `search` with the same signature, taking a start state and a goal state, returning a result:

<table><tr><th>Python</th><th>Java</th></tr><tr><td>

```python
class SearchUCS(SearchAlgorithm[V]):
    def search(self, start: V, goal: V) -> SearchResult[V]:
        ...
```

</td><td>

```java
public class SearchUCS<V> extends SearchAlgorithm<V> {
    public SearchResult<V> search(V start, V goal) {
        ...
    }
}
```

</td></tr></table>

UCS and A* search use a priority queue, and the elements stored in the priority queue are objects of the classes **UCSEntry** and **AstarEntry** respectively. In addition the class **SearchUCS** defines the method `extractPath` which is used by both UCS and A* search.

- `SearchUCS.search`: this is Task 1a+c
- `SearchUCS.extractPath`: this is Task 1b
- `SearchAstar.search` and `AstarEntry`: this is Task 3

### Edges and graphs

Directed weighted edges are of the form **Edge(V)**, where **V** can be any kind of vertex type:

- the constructor is `Edge(start, end)` or `Edge(start, end, weight)`
- if the weight isn't provided (such as for the graph types **WordLadder** and **NPuzzle**), it defaults to 1

Directed weighted graphs, **Graph(V)**, define the following two methods (among several others):

<table><tr><th>Python</th><th>Java</th></tr><tr><td>

```python
class Graph(Generic[V]):
    def outgoingEdges(self, v: V) -> List[Edge[V]]: 
        ...
    def guessCost(self, v: V, w: V) -> float: 
        ...
```

</td><td>

```java
interface Graph<V> {
    List<Edge<V>> outgoingEdges(V v);
    double guessCost(V v, V w);
}
```

</td></tr></table>

Note that this interface differs from the graph interface in the course API (it lacks several of the API methods). But it is enough for the purposes in this lab.

### AdjacencyGraph

The **AdjacencyGraph** reads a generic finite graph, one edge per line, and stores it as an adjacency list as described in the course book and the lectures. The graph can represent anything. In the graph repository, there are distance graphs for cities (city graphs) in several regions, including EU, Sweden (SE) and Västra Götaland (VGregion). There is also a link graph between more than 4500 Wikipedia pages, "wikipedia-graph.txt":

```
$ python pathfinder.py -a random -t AdjacencyGraph -g ../graphs/AdjacencyGraph/wikipedia-graph.txt -q Sweden Norway
Searching: Sweden --> Norway
Searching the graph took 0.00 seconds.
Loop iterations: 1674
Cost of path from Sweden to Norway: 1673.00
Number of edges: 1673
Sweden --> Minnesota --> Middle_East --> Monarchy --> Lesotho --> ... --> Roman_Empire --> Italy --> Greece --> Netherlands --> Norway
```

### NPuzzle

**NPuzzle** is an encoding of the [n-puzzle](https://en.wikipedia.org/wiki/15_puzzle). The nodes for this graph are the possible states of the puzzle. An edge represents a move in the game, swapping the empty tile with an adjacent tile.

We represent each state as a string encoding of an *N* x *N* matrix. The tiles are represented by characters starting from the letter A (`A`…`H` for *N*=3, and `A`…`O` for *N*=4). The empty tile is represented by `_`. To make it more readable for humans, every row is separated by `/`:

```
% java PathFinder -a random -t NPuzzle -g 2 -q /_C/BA/ /AB/C_/
Searching: /_C/BA/ --> /AB/C_/
Searching the graph took 0.00 seconds.
Loop iterations: 65
Cost of path from /_C/BA/ to /AB/C_/: 64,00
Number of edges: 64
/_C/BA/ --> /BC/_A/ --> /BC/A_/ --> /B_/AC/ --> /BC/A_/ --> ... --> /BC/A_/ --> /B_/AC/ --> /_B/AC/ --> /AB/_C/ --> /AB/C_/

$ python pathfinder.py -a random -t NPuzzle -g 3 -q /ABC/DEF/HG_/ /ABC/DEF/GH_/
Searching: /ABC/DEF/HG_/ --> /ABC/DEF/GH_/
Searching the graph took 0.16 seconds.
Loop iterations: 10000
No path found from /ABC/DEF/HG_/ to /ABC/DEF/GH_/
```

**Note:**
It's no use trying the "random" algorithm on **NPuzzle** sizes larger than 2: it will almost certainly never find a solution. In fact, already for *N* = 4, the number of states is 16! ≈ 2 · 10<sup>13</sup>. Thus, we cannot even store the set of vertices in memory.

### GridGraph

**GridGraph** is a 2D-map encoded as a bitmap, or an *N* x *M* matrix of characters. Some characters are passable, others denote obstacles. A node is a point in the bitmap, consisting of an x- and a y-coordinate. This is defined by the helper class **Point**. You can move from each point to the eight points around it. The edge costs are 1 (for up/down/left/right) and sqrt(2) (for diagonal movement). A point is written as `x:y`, like this:

```
% python pathfinder.py -a random -t GridGraph -g ../graphs/GridGraph/maze-10x5.txt
Bitmap graph of dimensions 41 x 11 pixels.
+---+---+---+---+---+---+---+---+---+---+
        |   |                       |   |
+---+   +   +   +---+   +---+---+   +   +
|   |   |           |   |   |           |
+   +   +---+---+---+   +   +   +---+---+
|               |       |   |           |
+---+   +---+---+   +---+   +---+   +   +
|       |                   |       |   |
+   +---+   +---+---+---+---+   +---+   +
|           |                   |        
+---+---+---+---+---+---+---+---+---+---+

Random example points with outgoing edges:
 * 38:3 --> 37:2 [1.41], 37:3 [1], 38:2 [1], 39:2 [1.41], 39:3 [1]
 * 23:9 --> 22:9 [1], 24:9 [1]
 * 15:2 --> 14:1 [1.41], 14:2 [1], 14:3 [1.41], 15:1 [1], 15:3 [1], 16:1 [1.41], 16:3 [1.41]
 * 1:4 --> 1:3 [1], 1:5 [1], 2:3 [1.41], 2:4 [1], 2:5 [1.41]
 * 29:9 --> 28:9 [1], 29:8 [1], 30:8 [1.41], 30:9 [1]
 * 29:1 --> 28:1 [1], 30:1 [1]
 * 20:1 --> 19:1 [1], 21:1 [1], 21:2 [1.41]
 * 21:2 --> 20:1 [1.41], 21:1 [1], 21:3 [1], 22:1 [1.41], 22:2 [1], 22:3 [1.41]

(ENTER to quit)
Start: 1:1
Goal: 39:9

Searching: 1:1 --> 39:9
Searching the graph took 0.25 seconds.
Loop iterations: 10000
No path found from 1:1 to 39:9

(ENTER to quit)
Start: 1:1
Goal: 39:9

Searching: 1:1 --> 39:9
Searching the graph took 0.27 seconds.
Loop iterations: 7836
+---+---+---+---+---+---+---+---+---+---+
*S******|   |         * ************|***|
+---+***+   +   +---+***+---+---+***+***+
|***|***|           |***|   |***********|
+***+***+---+---+---+***+   +***+---+---+
|***************|**** **|   |***********|
+---+***+---+---+***+---+   +---+***+***+
|*******|*************      |*******|***|
+***+---+***+---+---+---+---+***+---+***+
|***********|               ****|     *G 
+---+---+---+---+---+---+---+---+---+---+

Cost of path from 1:1 to 39:9: 9158.41
Number of edges: 7835
1:1 --[1]--> 0:1 --[1]--> 1:1 --[1]--> 0:1 --[1]--> 1:1 --[1]--> ... --[1]--> 38:7 --[1]--> 39:7 --[1.41]--> 38:8 --[1]--> 39:8 --[1]--> 39:9
```

The asterisks (`*`) in the final graph make up the path, and almost all available space is filled since the algorithm moves around randomly until it finds its way out.

As you can see in the example, often the random search won't find a path even for the smallest maze.

### WordLadder

This class is unfinished. You will complete it in Task 2.

> Word ladder is a word game invented by Lewis Carroll. A word ladder puzzle begins with two words, and to solve the puzzle one must find a chain of other words to link the two, in which two adjacent words (that is, words in successive steps) differ by one letter.
>
> ([Wikipedia](https://en.wikipedia.org/wiki/Word_ladder))

We model this problem as a graph. The nodes denote words in a dictionary and the edges denote one step in this word ladder. Note that edges only connect words of the same length.

The class does not store the full graph in memory, just the dictionary of words. The edges are then computed on demand. The class already contains code that reads the dictionary, but you must complete the rest of the class.


## About the graphs in the collection

**Note:**
All graph files are encoded in UTF-8 (Unicode). If you experience problems searching for words with special characters (`å`, `ä`, `ö`), your setup may have a character encoding problem. Try switching to an English or Swedish system locale.

#### AdjacencyGraph:

- All graphs **citygraph-XX.txt** are extracted from freely available [mileage charts](https://www.mileage-charts.com/). The smallest graph has 130 cities and 838 edges (citygraph-VGregion.txt). The largest one 996 cities and 28054 edges (citygraph-EU.txt). All edge costs are in kilometers.
  * Suggested searches: `Göteborg` to `Götene` (**citygraph-VGregion.txt**); `Lund` to `Kiruna` (**citygraph-SE.txt**); `Porto, Portugal` to `Vorkuta, Russia` (**citygraph-EU.txt**)

- **wikipedia-graph.txt** is converted from [the Wikispeedia dataset](http://snap.stanford.edu/data/wikispeedia.html) in SNAP (Stanford Large Network Dataset Collection). It contains 4587 Wikipedia pages and 119882 page links. All edges have cost 1.
  * Suggested search: `Superconductivity` to `Anna_Karenina`

#### NPuzzle:

- **NPuzzle** does not need a file for initializing the graph, just a number giving the size of the puzzle. Larger sizes than 3 are usually too difficult, even for the algorithms in this lab.
  * Suggested search for size 2: `/_C/BA/` to goal `/AB/C_/`
  * Suggested searches for size 3: any of `/_AB/CDE/FGH/`, `/CBA/DEF/_HG/`, `/FDG/HE_/CBA/` or `/HFG/BED/C_A/`, to the goal `/ABC/DEF/GH_/`
  * Try also the following size-3 puzzle, which doesn't have a solution (why is that?): `/ABC/DEF/HG_/` to `/ABC/DEF/GH_/`

#### GridGraph:

- **AR0011SR.txt** and **AR0012SR.txt** are taken from the [2D Pathfinding Benchmarks](https://www.movingai.com/benchmarks/grids.html) in Nathan Sturtevant's Moving AI Lab. The maps are from the collection "Baldurs Gate II Original maps", and are grids of sizes 216 x 224 and 148 x 139, respectively. There are also associated PNG files, so that you can see how they look like.
  * Suggested searches: `23:161` to `130:211` (**AR0011SR.txt**); `11:73` to `85:127` (**AR0012SR.txt**)

- **maze-10x5.txt**, **maze-20x10.txt**, and **maze-100x50.txt** are generated by a [random maze generator](http://www.delorie.com/game-room/mazes/genmaze.cgi). They are grids of sizes 41 x 11, 81 x 21, and 201 x 101, respectively.
  * Suggested searches: `1:1` to `39:9` (**maze-10x5.txt**); `1:1` to `79:19` (maze-20x10.txt); `1:1` to `199:99` (**maze-100x50.txt**)

#### WordLadder:

- **swedish-romaner.txt** and **swedish-saldo.txt** are two Swedish word lists compiled from [Språkbanken Text](https://spraakbanken.gu.se/resurser). They contain 75,740 words (**swedish-romaner.txt**) and 888,275 words (**swedish-saldo.txt**), respectively.
  * Suggested searches (after you have completed Task 2 below): `eller` to `glada` (**swedish-romaner.txt**); `njuta` to `övrig` (**swedish-saldo.txt**)
  * Another interesting combination is to try any combination of the following words: `ämnet`, `åmade`, `örter`, `öring` (**swedish-romaner.txt**)

- **english-crossword.txt** comes from the official crossword lists in the [Moby project](https://en.wikipedia.org/wiki/Moby_Project). It consists of 117,969 words.
  * Suggested searches: any start and goal of the same length (between 4 and 8 characters)

## Part 1: Uniform-cost search

There is a skeleton method `search` in **search_ucs.py/SearchUCS.java**. Your goal in Part 1 is to implement uniform-cost search (UCS). This is a variant of Dijkstra's algorithm which can handle infinite and very large graphs. It is also arguably easier to understand than the usual formulation of Dijkstra's.

### Task 1a: The simple UCS algorithm

The main data structure used in UCS is a priority queue. It contains graph nodes paired with the cost to reach them. We store this information as the class `UCSEntry` (which is already implemented):

<table><tr><th>Python</th><th>Java</th></tr><tr><td>

```python
@total_ordering
@dataclass
class UCSEntry(Generic[V]):
    state: V
    lastEdge: Optional[Edge[V]]
    backPointer: Optional['UCSEntry[V]']
    costToHere: float
    def __lt__(self, other):
        return self.costToHere < other.costToHere
```

</td><td>

```java
public class SearchUCS<V> {
    public class UCSEntry {
        public final V state;
        public final Edge<V> lastEdge;
        public final UCSEntry backPointer;
        public final double costToHere;
    }
}
```

</td></tr></table>

The `backPointer` is necessary for recreating the final path from the start node to the goal. More about this in Task 1b below. The very first entry will not have any previous entry, so we set `lastEdge` and `backPointer` to `None/null`.

The priority queue has to know how to compare its entires. In Python we do this by implementing the `__lt__` method in this class, while in Java we do it by specifying a **Comparator** when we initialise the priority queue.

Here is pseudocode of the simplest version of UCS:

```python
search(graph, start, goal):
    create a new priority queue
    add an initial entry to the priority queue
    while there is an entry to remove from the priority queue:
        if the state of the entry is the goal:
            SUCCESS:) extract the path and return it
        for every edge starting at the entry's state:
            add the edge target as a new entry to the priority queue 
            the cost of the new entry is that of the current entry plus the edge weight
    FAILURE:( there is no path
```

It is important that we return as soon as we reach the goal. Otherwise, we will continue adding new entries to the queue indefinitely.

**Java hint**: `removeMin` is called `remove` in the Java API for priority queues.

Implement this algorithm in the method `SearchUCS.search`. It should return a **SearchResult** object. When you return a result, use `None/null` for the `path` argument for now. You should increase the counter `iterations` every time you remove an entry from the priority queue. When you have done this, you should be able to run queries for nodes not too far apart:

```
$ python pathfinder.py -a ucs -t AdjacencyGraph -g ../graphs/AdjacencyGraph/citygraph-VGregion.txt -q Skara Lerum
Searching: Skara --> Lerum
Searching the graph took 2.18 seconds.
Loop iterations: 68981
Cost of path from Skara to Lerum: 115.00
WARNING: You have not implemented extractPath!
```

But there are two problems with this implementation:

1. the path found is not printed, and
2. it becomes extremely slow on more difficult problems (e.g., try to find the way from Skara to Torslanda).

We will address these problems in Tasks 1b and 1c.

### Task 1b: Extracting the solution

Now you should write code to extract the solution, the list of edges forming the shortest path. For this, implement and make use of the skeleton method `extractPath`:


<table><tr><th>Python</th><th>Java</th></tr><tr><td>

```python
class SearchUCS(Generic[V]):
    def extractPath(self, entry: UCSEntry[V]) -> List[Edge[V]]
```

</td><td>

```java
public class SearchUCS<V> {
    public List<Edge<V>> extractPath(UCSEntry entry);
}
```

</td></tr></table>

Make sure you get the order of edges right!

After this is completed, your output will change:

```
$ java PathFinder -a ucs -t AdjacencyGraph -g ../graphs/AdjacencyGraph/citygraph-VGregion.txt -q Skara Lerum    
Searching: Skara --> Lerum
Searching the graph took 0.19 seconds.
Loop iterations: 66240
Cost of path from Skara to Lerum: 115,00
Number of edges: 6
Skara --[35]--> Vara --[28]--> Vårgårda --[9]--> Jonstorp --[15]--> Alingsås --[23]--> Stenkullen --[5]--> Lerum
```

As you can see, the result is the same as before, but now the path is printed too. Check that you get the same path as shown here.


### Task 1c: Remembering visited nodes

The reason why the algorithm is slow is that it will revisit the same node every time it is reached. There are hundreds of ways to get from Skara to Alingsås, and the algorithm visits most of them before it finds its way to Lerum. But all the subsequent visits to Alingsås are unnecessary because the first visit is already via the shortest path (why is that?).

Therefore, a simple solution is to record the visited nodes in a set. Immediately after you retrieve a node from the priority queue, check if it has already been visited. Only proceed if the node is "fresh". Don't forget to then add it to the visited nodes: otherwise there won't be much of an optimisation.

When this is done, you should see a drastic improvement:

```
$ java PathFinder -a ucs -t AdjacencyGraph -g ../graphs/AdjacencyGraph/citygraph-VGregion.txt -q Skara Lerum
Searching: Skara --> Lerum
Searching the graph took 0.01 seconds.
Loop iterations: 291
Cost of path from Skara to Lerum: 115,00
Number of edges: 6
Skara --[35]--> Vara --[28]--> Vårgårda --[9]--> Jonstorp --[15]--> Alingsås --[23]--> Stenkullen --[5]--> Lerum
```

The number of loop iterations went down by a factor of 200! Now you should be able to solve all kinds of problems in adjacency graphs, n-puzzles, and grid graphs:

```
$ python pathfinder.py -a ucs -t AdjacencyGraph -g ../graphs/AdjacencyGraph/citygraph-EU.txt -q "Volos, Greece" "Oulu, Finland"
Searching: Volos, Greece --> Oulu, Finland
Searching the graph took 0.13 seconds.
Loop iterations: 23517
Cost of path from Volos, Greece to Oulu, Finland: 3488.00
Number of edges: 12
Volos, Greece --[923]--> Timişoara, Romania --[55]--> Arad, Romania --[114]--> Oradea, Romania --[83]--> Debrecen, Hungary --[50]--> ... --[169]--> Lublin, Poland --[253]--> Białystok, Poland --[825]--> Tallinn, Estonia --[88]--> Helsinki, Finland --[607]--> Oulu, Finland

$ java PathFinder -a ucs -t NPuzzle -g 3 -q /_AB/CDE/FGH/ /ABC/DEF/GH_/
Searching: /_AB/CDE/FGH/ --> /ABC/DEF/GH_/
Searching the graph took 0.25 seconds.
Loop iterations: 152439
Cost of path from /_AB/CDE/FGH/ to /ABC/DEF/GH_/: 22,00
Number of edges: 22
/_AB/CDE/FGH/ --> /A_B/CDE/FGH/ --> /ADB/C_E/FGH/ --> /ADB/_CE/FGH/ --> /ADB/FCE/_GH/ --> ... --> /ABC/GDE/_HF/ --> /ABC/_DE/GHF/ --> /ABC/D_E/GHF/ --> /ABC/DE_/GHF/ --> /ABC/DEF/GH_/

$ python pathfinder.py -a ucs -t GridGraph -g ../graphs/GridGraph/maze-100x50.txt -q 1:1 199:99
Searching: 1:1 --> 199:99
Searching the graph took 0.32 seconds.
(...)
Loop iterations: 26478
Cost of path from 1:1 to 199:99: 1216.48
Number of edges: 1016
1:1 --[1]--> 1:2 --[1]--> 1:3 --[1]--> 1:4 --[1.41]--> 2:5 --[1.41]--> ... --[1]--> 196:97 --[1]--> 197:97 --[1]--> 198:97 --[1.41]--> 199:98 --[1]--> 199:99
```

Go on! Try the suggestions for the different graphs in the section "About the graphs in the collection" above!

***Important***:
Make sure you get the cost (shortest path length) shown in these examples. If you got a higher cost, then UCS didn't find the optimal path. If you got a lower cost, there's an error in how you calculate the path costs (or you take some illegal shortcuts). Furthermore, it is a good sign (but not required) if your implementation has the same number of loop iterations as shown above (or very close).

### Questions for part 1

List the number of loop iterations, minimal costs and shortest paths, for the queries in **answers.txt**.


## Part 2: Word ladders

The class **WordLadder** is not fully implemented. This task is to make it work correctly. What is implemented is the reading of the dictionary, adding of words, and some auxiliary methods. What is missing is the implementation of `outgoingEdges`:


<table><tr><th>Python</th><th>Java</th></tr><tr><td>

```python
# Note: Word is just a synonym for a plain string (`str`)
def outgoingEdges(self, v: Word) -> List[Edge[Word]]
```

</td><td>

```java
public List<Edge<String>> outgoingEdges(String w);
```

</td></tr></table>

An edge is one step in the word ladder. The target word must:

- be in the dictionary,
- be of the same length, and
- differ by exactly one letter.

At your disposal are the following two instance variables:

<table><tr><th>Python</th><th>Java</th></tr><tr><td>

```python
# Note: Word and Char are just synonyms for plain strings (`str`)
dictionary : Set[Word]
alphabet : Set[Char]
```

</td><td>

```java
private Set<String> dictionary;
private Set<Character> alphabet;
```

</td></tr></table>

Here, `alphabet` is the set of letters appearing in dictionary words. Use this instead of iterating over a fixed collection of characters.

**Note**: You should not iterate over all words in the dictionary (that's too expensive).

After you completed your implementation, you should be able to solve the following word ladders:

```
$ python pathfinder.py -a ucs -t WordLadder -g ../graphs/WordLadder/swedish-romaner.txt
Word ladder graph with 75740 words.
Alphabet: abcdefghijklmnopqrstuvwxyzàáâäåæçèéêëíîïñóõöøúü

Random example words with ladder steps:
 * spiskvarter with no outgoing edges
 * handstil with no outgoing edges
 * gryr --> bryr, gnyr, gror, grym, gryn
 * mönstret --> fönstret, monstret, mönstrat, mönstren
 * klarabron with no outgoing edges
 * cement with no outgoing edges
 * komprometterat --> komprometterad, komprometterar
 * extralång with no outgoing edges

(ENTER to quit)
Start: mamma    
Goal: pappa

Searching: mamma --> pappa
Searching the graph took 0.03 seconds.
Loop iterations: 772
Cost of path from mamma to pappa: 6.00
Number of edges: 6
mamma --> mumma --> summa --> sumpa --> pumpa --> puppa --> pappa

(ENTER to quit)
Start: katter
Goal: hundar

Searching: katter --> hundar
Searching the graph took 0.20 seconds.
Loop iterations: 9075
Cost of path from katter to hundar: 14.00
Number of edges: 14
katter --> kanter --> tanter --> tanten --> tanden --> ... --> randas --> randad --> rundad --> rundar --> hundar

(ENTER to quit)
Start: örter
Goal: öring

Searching: örter --> öring
Searching the graph took 0.34 seconds.
Loop iterations: 20091
Cost of path from örter to öring: 30.00
Number of edges: 30
örter --> arter --> artar --> antar --> andar --> ... --> slang --> klang --> kling --> kring --> öring
```

### Question for part 2

List the number of loop iterations, minimal costs and shortest paths, for the query in **answers.txt**.


## Part 3: The A* algorithm

The UCS algorithm finds an optimal path, but there is an optimisation which can help discover it much faster! This algorithm is called A*. Your task is to implement it in the method `search` of the class **SearchAstar**.

The basic structure of A* is that of UCS, so you can start by copying your UCS code to this method. For each entry in the priority queue, A* doesn't just keep track of the cost so far, as in UCS, but also of the *estimated total cost* from the start, via this node, to the goal. The latter score is used as the priority.

To be able to do this efficiently, you will need to use the class **AstarEntry** which is an extension of **UCSEntry**.

To work, A* needs a *distance heuristic*, an educated guess of the distance between two nodes. This requires some additional insight into the problem, so the heuristics are different for different types of graphs and problems. Our graph API (the interface **Graph**) provides this heuristic in the form of the method `guessCost`, which takes two nodes as argument and returns a cost estimate: 

<table><tr><th>Python</th><th>Java</th></tr><tr><td>

```python
class Graph(Generic[V]):
    def guessCost(self, v: V, w: V) -> float
```

</td><td>

```java
interface Graph<V> {
    double guessCost(V v, V w);
}
```

</td></tr></table>

The estimated total cost for an entry is then defined as the cost so far *plus* the estimated cost from the current node to the goal.

**Note (Python)**: You have to decide how the class **AstarEntry** should be compared, which is how the priority queue decides which entry should be returned first. You are allowed to add fields to the class to help you compare the entries.

**Note (Java)**: You decide how to compare **AstarEntry** instances when you create the priority queue in `AstarSearch.search`.

**Important**:
Make sure your implementation doesn't call `guessCost` too many times. This could slow down your search. The priority queue comparator should not call `guessCost` directly, but instead use a value stored with the priority queue entry. Also avoid operations on the priority queue that take linear time such as iterating over it, or testing if it contains an entry.

**Important**:
There are other versions of A* that modify the priority of an existing entry in the priority queue (this is called a `decreaseKey` operation). The given priority queue class does not support this operation.

When you have implemented A*, try it out for **NPuzzle** problems. This is the only graph type with a ready-baked heuristic (see the next task for how it works):

```
$ java PathFinder -a ucs -t NPuzzle -g 3 -q /CBA/DEF/_HG/ /ABC/DEF/GH_/
Searching: /CBA/DEF/_HG/ --> /ABC/DEF/GH_/
Searching the graph took 0.26 seconds.
Loop iterations: 292528
Cost of path from /CBA/DEF/_HG/ to /ABC/DEF/GH_/: 24,00
Number of edges: 24
/CBA/DEF/_HG/ --> /CBA/_EF/DHG/ --> /_BA/CEF/DHG/ --> /B_A/CEF/DHG/ --> /BA_/CEF/DHG/ --> ... --> /AC_/DBF/GEH/ --> /A_C/DBF/GEH/ --> /ABC/D_F/GEH/ --> /ABC/DEF/G_H/ --> /ABC/DEF/GH_/

$ java PathFinder -a astar -t NPuzzle -g 3 -q /CBA/DEF/_HG/ /ABC/DEF/GH_/
Searching: /CBA/DEF/_HG/ --> /ABC/DEF/GH_/
Searching the graph took 0.02 seconds.
Loop iterations: 4536
Cost of path from /CBA/DEF/_HG/ to /ABC/DEF/GH_/: 24,00
Number of edges: 24
/CBA/DEF/_HG/ --> /CBA/DEF/H_G/ --> /CBA/D_F/HEG/ --> /C_A/DBF/HEG/ --> /_CA/DBF/HEG/ --> ... --> /_AB/DEC/GHF/ --> /A_B/DEC/GHF/ --> /AB_/DEC/GHF/ --> /ABC/DE_/GHF/ --> /ABC/DEF/GH_/
```

Note that A* visits much fewer nodes (4,500 compared to 300,000 for UCS!), but finds a path of the same length as UCS. If your implementation doesn't, then there's probably a bug somewhere.

If we don't have a way of guessing the cost, we should use 0. That's the default implementation of `guessCost` in **graph.py/Graph.java**. In that case, the A* algorithm behaves exactly like UCS (why is that?). Try that! If you get different numbers of nodes visited, you might have a bug.

### Questions for part 3

List the number of loop iterations and minimal costs, for the queries in **answers.txt**.


## Part 4: Guessing the cost
 
The graph API method `guessCost` should return an *optimistic* guess for the cost (distance) between two nodes. This is already implemented for **NPuzzle**, but is missing for the other graph types. The implementation for **NPuzzle** estimates the cost as the sum over each tile of the [Manhattan distance](https://en.wikipedia.org/wiki/Taxicab_geometry) between its positions in the source and target state. This is the implementation (in pseudocode):

```python
guessCost(s, t):
    cost = 0
    for tile in tiles (excluding the empty tile):
        (sx, sy) = coordinates of tile in s
        (tx, ty) = coordinates of tile in t
        cost += |sx - tx| + |sy - ty|
    return cost
```

Your task is to implement the following `guessCost` heuristic for **GridGraph** and **WordLadder**:

- **GridGraph**:
  The [straight-line distance](https://en.wikipedia.org/wiki/Euclidean_distance) between the two points. You may find some methods of the **Point** class useful here. After implementing it, you should see an improvement in the number of loop iterations.

    ```
    $ python pathfinder.py -a ucs -t GridGraph  -g ../graphs/GridGraph/AR0012SR.txt -q 11:73 85:127
    Searching: 11:73 --> 85:127
    Searching the graph took 0.21 seconds.
    Loop iterations: 40276
    (...)
    Cost of path from 11:73 to 85:127: 147.68
    Number of edges: 122
    11:73 --[1.41]--> 12:74 --[1]--> 13:74 --[1.41]--> 14:75 --[1.41]--> 15:76 --[1]--> ... --[1]--> 88:123 --[1.41]--> 87:124 --[1]--> 87:125 --[1.41]--> 86:126 --[1.41]--> 85:127

    $ python pathfinder.py -a astar -t GridGraph  -g ../graphs/GridGraph/AR0012SR.txt -q 11:73 85:127
    Searching: 11:73 --> 85:127
    Searching the graph took 0.13 seconds.
    (...)
    Loop iterations: 16693
    Cost of path from 11:73 to 85:127: 147.68
    Number of edges: 122
    11:73 --[1]--> 12:73 --[1]--> 13:73 --[1]--> 14:73 --[1.41]--> 15:74 --[1.41]--> ... --[1.41]--> 88:123 --[1.41]--> 87:124 --[1.41]--> 86:125 --[1.41]--> 85:126 --[1]--> 85:127
    ```

  Note the improvement from 40,000 iterations to 17,000.

- **WordLadder**:
  The number of character positions where the letters differ from each other. For example, `guessCost("örter", "arten")` should return 2: the first and last characters differ (`ö`/`a` and `r`/`n`), but the middle ones (`rte`) are the same. Your method should not fail if it happens to be called on words of different length, but the return value then doesn't matter much (why is that?) – you can, e.g., return a very large number.

    ```
    $ java PathFinder -a ucs -t WordLadder -g ../graphs/WordLadder/swedish-saldo.txt -q eller glada
    Searching: eller --> glada
    Searching the graph took 0.24 seconds.
    Loop iterations: 25481
    Cost of path from eller to glada: 7.00
    Number of edges: 7
    eller --> elles --> ellas --> elias --> glias --> glids --> glads --> glada

    $ java PathFinder -a astar -t WordLadder -g ../graphs/WordLadder/swedish-saldo.txt -q eller glada
    Searching: eller --> glada
    Searching the graph took 0.01 seconds.
    Loop iterations: 192
    Cost of path from eller to glada: 7.00
    Number of edges: 7
    eller --> elles --> ellas --> elias --> glias --> glids --> glads --> glada
    ```

  Note the improvement from 25,000 iterations to only 200!

- You don't have to implement `guessCost` for **AdjacencyGraph**. That would need domain-specific information about the graph, which the class does not have. But see the optional tasks below.

### Important note

The A* algorithm works correctly only if the heuristic is *admissible*, which means that the heuristic must never over-estimate the cost. It's fine to under-estimate: it will still find an optimal path, but if the heuristic is too under-estimating, it will take longer.

### Questions for part 4

List the number of loop iterations and minimal costs, for the queries in **answers.txt**.



## Part 5: Reflections

Answer the final reflection questions in the file **answers.txt**.


## Submission

Double check:
* Have you done all the tasks?
  - **PathFinder.java**: Part 1 and 3
  - **WordLadder.java**: Part 2 and 4
  - **GridGraph.java**: Part 4
* Have you answered the questions in **answers.txt**?
  (don't forget the ones in the appendix)
* Have you tested your code with **Robograder**?


## Optional tasks

This lab can be expanded in several ways, here are only some suggestions:

- Try the implementations on more graphs: There are several to download from [Moving AI Lab](https://www.movingai.com/benchmarks/grids.html), or [the SNAP project](http://snap.stanford.edu/data/index.html). You can also create more random mazes from several places on the web (search for "random maze generator").

- Show the results nicer, e.g., as an animation (for **NPuzzle**).

- **WordLadder** only connects words of the start and goal have the same length. Invent and implement word ladder rules for changing the number of letters in a word. Remember that all graph nodes must be words in the dictionary.

- You can assign different costs for different kinds of "terrain" (i.e., different characters) in **GridGraph**.

- Try to come up with an even better admissible heuristic than straight-line distance for **GridGraph**. Hint: Modify the [Manhattan distance](https://en.wikipedia.org/wiki/Taxicab_geometry) so that it allows for diagonal moves.

- Experiment with different representation of the state in **NPuzzle**. See the comments of the internal class **NPuzzle.State**.

- Implement code for reading other graph formats. For example, you could read an image as a graph where where dark pixels are considered obstacles. See the PNG files in `graphs/GridGraph` for examples.

- Implement a heuristic for roadmaps (**citygraph-XX.txt**). For this you need locations of all cities, and they can be found in the [DSpace-CRIS database](https://dspace-cris.4science.it/handle/123456789/31). You have to scrape the information you need from the database, i.e., the latitude-longitude of each city. Then you have to filter the database to only include the cities you want. You also need to read in the position database in the graph class, and finally you need to [translate latitude-longitude into kilometers](https://stackoverflow.com/questions/27928/calculate-distance-between-two-latitude-longitude-points-haversine-formula).

- What kind of heuristic could be useful for the link distance between two Wikipedia pages (remember **wikipedia-graph.txt**)? Assume e.g. that you know the text content of both pages. Or that you know the [categories](https://en.wikipedia.org/wiki/Help:Category) that each Wikipedia page belongs to.

