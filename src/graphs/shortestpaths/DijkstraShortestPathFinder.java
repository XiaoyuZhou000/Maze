package graphs.shortestpaths;

import graphs.BaseEdge;
import graphs.Graph;
import priorityqueues.DoubleMapMinPQ;
import priorityqueues.ExtrinsicMinPQ;

import java.util.ArrayList;
// import java.util.Collection;
import java.util.HashMap;
// import java.util.HashSet;
import java.util.List;
import java.util.Map;
// import java.util.Objects;
// import java.util.PriorityQueue;
// import java.util.Set;

/**
 * Computes shortest paths using Dijkstra's algorithm.
 *
 * @see SPTShortestPathFinder for more documentation.
 */
public class DijkstraShortestPathFinder<G extends Graph<V, E>, V, E extends BaseEdge<V, E>>
    extends SPTShortestPathFinder<G, V, E> {

    protected <T> ExtrinsicMinPQ<T> createMinPQ() {
        return new DoubleMapMinPQ<>();
        /*
        If you have confidence in your heap implementation, you can disable the line above
        and enable the one below.
         */
        // return new ArrayHeapMinPQ<>();

        /*
        Otherwise, do not change this method.
        We override this during grading to test your code using our correct implementation so that
        you don't lose extra points if your implementation is buggy.
         */
    }

    @Override
    protected Map<V, E> constructShortestPathsTree(G graph, V start, V end) {

        Map<V, E> edgeTo = new HashMap<>();
        if (start.equals(end)) {
            return edgeTo;
        }

        Map<V, Double> distTo = new HashMap<>();
        DoubleMapMinPQ<V> known = new DoubleMapMinPQ<>();
        known.add(start, 0);
        distTo.put(start, 0.0);

        while (!known.peekMin().equals(end)) {
            V startPoint = known.removeMin();
            if (!startPoint.equals(end)) {
                for (E edge : graph.outgoingEdgesFrom(startPoint)) {
                    if (!edgeTo.containsKey(edge.to()) && !edge.to().equals(start)) {
                        edgeTo.put(edge.to(), edge);
                        distTo.put(edge.to(), distTo.get(edge.from()) + edge.weight());
                        known.add(edge.to(), distTo.get(edge.to()));
                    } else {
                        if (distTo.get(edge.from()) != null && distTo.get(edge.to()) != null) {
                            if (distTo.get(edge.from()) + edge.weight() < distTo.get(edge.to())) {
                                edgeTo.put(edge.to(), edge);
                                distTo.put(edge.to(), distTo.get(edge.from()) + edge.weight());
                                if (known.contains(edge.to())) {
                                    known.changePriority(edge.to(), distTo.get(edge.to()));
                                }
                            }
                        }

                    }
                }
            }
            if (known.isEmpty()) {
                return edgeTo;
            }
        }
        return edgeTo;
    }

    @Override
    protected ShortestPath<V, E> extractShortestPath(Map<V, E> spt, V start, V end) {


        if (start.equals(end)) {
            return new ShortestPath.SingleVertex<>(start);
        }
        if (spt.get(end) == null) {
            return new ShortestPath.Failure<>();
        }
        List<E> result = new ArrayList<>();
        E endEdge = spt.get(end);
        // ShortestPath<V, E> result = new ShortestPath.Success<>()
        DoubleMapMinPQ<V> vertices = new DoubleMapMinPQ<>();
        V startVertex = endEdge.from();
        result.add(endEdge);
        if (startVertex.equals(start)) {
            return new ShortestPath.Success<>(result);
        }
        vertices.add(startVertex, 0);

        while (!(vertices.isEmpty())) {

            V startPoint = vertices.removeMin();
            E startEdge = spt.get(startPoint);
            if (startEdge != null) {

                V endPoint = startEdge.from();
                vertices.add(endPoint, 0);
                result.add(startEdge);
            }
            // if (vertices.peekMin().equals(start)) {
            //     return new ShortestPath.Success<>(result);
            // }
        }
        List<E> newResult = new ArrayList<>();
        for (int i = result.size() - 1; i >= 0; i--) {
            newResult.add(result.get(i));
        }

        return new ShortestPath.Success<>(newResult);
    }

}
