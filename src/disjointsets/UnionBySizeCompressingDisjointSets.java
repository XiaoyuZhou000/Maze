package disjointsets;

// import edu.princeton.cs.algs4.In;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * A quick-union-by-size data structure with path compression.
 *
 * @see DisjointSets for more documentation.
 */
public class UnionBySizeCompressingDisjointSets<T> implements DisjointSets<T> {
    // Do NOT rename or delete this field. We will be inspecting it directly in our private tests.
    List<Integer> pointers;
    private Map<T, Integer> ids;
    private int size;
    /*
    However, feel free to add more fields and private helper methods. You will probably need to
    add one or two more fields in order to successfully implement this class.
    */

    public UnionBySizeCompressingDisjointSets() {

        pointers = new ArrayList<>();
        size = 0;
        ids = new HashMap<>();
    }

    @Override
    public void makeSet(T item) {

        ids.put(item, size);
        size++;
        pointers.add(-1);
    }

    @Override
    public int findSet(T item) {

        Integer nextIndex = this.ids.get(item);
        if (nextIndex == null) {
            throw new IllegalArgumentException(item + " is not in any set.");
        }
        Set<Integer> previous = new HashSet<>();
        while (pointers.get(nextIndex) >= 0) {
            previous.add(nextIndex);
            nextIndex = pointers.get(nextIndex);
        }
        for (int each : previous) {
            pointers.set(each, nextIndex);
        }
        return nextIndex.intValue();
    }

    @Override
    public boolean union(T item1, T item2) {

        int rootA = findSet(item1);
        int rootB = findSet(item2);
        if (rootA == rootB) {
            return false;
        }
        Integer weightA = pointers.get(rootA);
        Integer weightB = pointers.get(rootB);
        if (Math.abs(weightA) >= Math.abs(weightB)) {
            pointers.set(rootB, rootA);
            pointers.set(rootA, weightA + weightB);
        } else {
            pointers.set(rootA, rootB);
            pointers.set(rootB, weightA + weightB);
        }
        return true;
    }
}
