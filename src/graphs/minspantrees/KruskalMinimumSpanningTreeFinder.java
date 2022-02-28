package graphs.minspantrees;

import disjointsets.DisjointSets;
import disjointsets.QuickFindDisjointSets;
// import graphs.AdjacencyListUndirectedGraph;
import graphs.BaseEdge;
import graphs.KruskalGraph;

import java.util.ArrayList;
// import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Computes minimum spanning trees using Kruskal's algorithm.
 * @see MinimumSpanningTreeFinder for more documentation.
 */
public class KruskalMinimumSpanningTreeFinder<G extends KruskalGraph<V, E>, V, E extends BaseEdge<V, E>>
    implements MinimumSpanningTreeFinder<G, V, E> {

    protected DisjointSets<V> createDisjointSets() {
        return new QuickFindDisjointSets<>();
        /*
        Disable the line above and enable the one below after you've finished implementing
        your `UnionBySizeCompressingDisjointSets`.
         */
        // return new UnionBySizeCompressingDisjointSets<>();

        /*
        Otherwise, do not change this method.
        We override this during grading to test your code using our correct implementation so that
        you don't lose extra points if your implementation is buggy.
         */
    }

    @Override
    public MinimumSpanningTree<V, E> findMinimumSpanningTree(G graph) {
        List<E> edges = new ArrayList<>(graph.allEdges());
        List<V> vertices = new ArrayList<>(graph.allVertices());
        if (edges.isEmpty() && vertices.size() >= 2) {
            return new MinimumSpanningTree.Failure<>();
        }

        List<V> exist = new ArrayList<>();
        edges.sort(Comparator.comparingDouble(E::weight));

        DisjointSets<V> disjointSets = createDisjointSets();


        // MinimumSpanningTree<V, E> finalMST = new MinimumSpanningTree.Success<>();
        Set<E> finalMST = new HashSet<>();
        for (E edge : edges) {
            if (!exist.contains(edge.from())) {
                disjointSets.makeSet(edge.from());
                exist.add(edge.from());
            }
            if (!exist.contains(edge.to())) {
                disjointSets.makeSet(edge.to());
                exist.add(edge.to());
            }
            int fromMST = disjointSets.findSet(edge.from());
            int toMST = disjointSets.findSet(edge.to());
            if (fromMST != toMST) {
                finalMST.add(edge);
                disjointSets.union(edge.from(), edge.to());
            }
        }


        for (int i = 0; i < exist.size() - 1; i++) {
            if (disjointSets.findSet(exist.get(i)) != disjointSets.findSet(exist.get(i + 1))) {
                return new MinimumSpanningTree.Failure<>();
            }
        }
        return new MinimumSpanningTree.Success<>(finalMST);
    }
}
