import java.lang.reflect.Array;
import java.util.*;

/**
 * Run uniform-cost search for finding the shortest path.
 */
public class SearchUCS<V> extends SearchAlgorithm<V> {

    SearchUCS(Graph<V> graph) {
        super(graph);
    }



    @Override
    public SearchResult<V> search(V start, V goal) {

        int iterations = 0;

        // Empty priority queue
        PriorityQueue<UCSEntry> pqueue = new PriorityQueue<>(Comparator.comparingDouble(entry -> entry.costToHere));

        // Create Set of visited nodes
        HashSet<V> visited = new HashSet<>();


        // Add first node to priority queue.
        UCSEntry firstNode = new UCSEntry(start, null, null, 0);
        pqueue.offer(firstNode);

        while (!pqueue.isEmpty()) {

            // Pop first entry from Priority Queue
            UCSEntry current = pqueue.poll();

            iterations++;

            // If current state is the goal return path
            if (current.state.equals(goal)) {
                return new SearchResult<>(this.graph, true, start, goal, current.costToHere, extractPath(current), iterations);
            }

            // If current state is not already visited add adjacent neighbours.
            if (!visited.contains(current.state)) {

                visited.add(current.state);

                for (Edge<V> edge: this.graph.outgoingEdges(current.state)) {

                    UCSEntry nextEntry = new UCSEntry(edge.end, edge, current, current.costToHere + edge.weight);

                    // Check if the next state has already been visited.
                    if (!visited.contains(nextEntry.state)) {
                        pqueue.add(nextEntry);
                    }

                }

            }

        }

        //---------- TASK 1a+c: Uniform-cost search -----------------------//
        // TODO: Replace these lines with your solution!
        // Note: Every time you remove a state from the priority queue, you should increment `iterations`.
        //---------- END TASK 1a+c ----------------------------------------//

        // Return this if you didn't find a path.
        return new SearchResult<>(this.graph, false, start, goal, -1, null, iterations);
    }

    /**
     * Extracts the path from the start to the current priority queue entry.
     */
    public List<Edge<V>> extractPath(UCSEntry entry) {
        //---------- TASK 1b: Extracting the path -------------------------//
        List<Edge<V>> extractedPath = new ArrayList<>();

        while(entry != null){

            if(entry.lastEdge != null){
                extractedPath.add(entry.lastEdge);
            }
            entry = entry.backPointer;
        }
        // Reversing the order of extracted Path
        Collections.reverse(extractedPath);


        // TODO: Replace these lines with your solution! 
        return extractedPath;
        //---------- END TASK 1b ------------------------------------------//
    }
    /**
     * Entries to put in the priority queues in `SearchUCS`.
     */
    public class UCSEntry {
        public final V state;
        public final Edge<V> lastEdge;      // null for the starting entry
        public final UCSEntry backPointer;  // null for the starting entry
        public final double costToHere;

        UCSEntry(V state, Edge<V> lastEdge, UCSEntry backPointer, double costToHere) {
            this.state = state;
            this.lastEdge = lastEdge;
            this.backPointer = backPointer;
            this.costToHere = costToHere;
        }
    }

}

