import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Set;
import java.util.HashSet;
import java.util.TreeSet;

/**
 * Run the A* algorithm for finding the shortest path.
 */
public class SearchAstar<V> extends SearchUCS<V> {

    SearchAstar(Graph<V> graph) {
        super(graph);
    }

    @Override
    public SearchResult<V> search(V start, V goal) {
        int iterations = 0;

        // Empty priority queue
        PriorityQueue<AstarEntry> pqueue = new PriorityQueue<>(Comparator.comparingDouble(entry -> entry.totalCost));

        // Create Set of visited nodes
        HashSet<V> visited = new HashSet<>();


        // Add first node to priority queue.
        AstarEntry firstNode = new AstarEntry(start, null, null, 0, 0);
        pqueue.offer(firstNode);

        while (!pqueue.isEmpty()){

            // Pop first entry from Priority Queue
            AstarEntry current = pqueue.remove();

            iterations++;

            // If current state is the goal return path
            if (current.state.equals(goal)) {
                return new SearchResult<>(this.graph, true, start, goal, current.costToHere, extractPath(current), iterations);
            }

            // If current state is not already visited add adjacent neighbours.
            if (!visited.contains(current.state)) {

                visited.add(current.state);

                for (Edge<V> edge: this.graph.outgoingEdges(current.state)) {


                    // Check if the next state has already been visited.
                    if (!visited.contains(edge.end)) {
                        pqueue.add(new AstarEntry(edge.end, edge, current, current.costToHere + edge.weight, current.costToHere + edge.weight + this.graph.guessCost(edge.end, goal)));
                    }

                }

            }

        }

        // Return this if you didn't find a path.
        return new SearchResult<>(this.graph, false, start, goal, -1, null, iterations);
    }

    /**
     * Entries to put in the priority queues in `searchAstar`.
     * This inherits all instance variables from `UCSEntry`, plus the ones you add.
     */
    public class AstarEntry extends UCSEntry {
        // These are inherited from UCSEntry:
        // public final V state;
        // public final Edge<V> lastEdge;      // null for the starting entry
        // public final UCSEntry backPointer;  // null for the starting entry
        // public final double costToHere;
        public final double totalCost;

        //---------- TASK 3: A* search, priority queue entries ------------//
        AstarEntry(V state, Edge<V> lastEdge, AstarEntry backPointer, double costToHere, double totalCost) {
            super(state, lastEdge, backPointer, costToHere);
            this.totalCost = totalCost;
        }
        //---------- END TASK 3 -------------------------------------------//
    }

}


