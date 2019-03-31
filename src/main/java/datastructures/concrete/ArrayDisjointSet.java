package datastructures.concrete;

import datastructures.concrete.dictionaries.ChainedHashDictionary;
import datastructures.interfaces.IDictionary;
import datastructures.interfaces.IDisjointSet;

/**
 * See IDisjointSet for more details.
 */
public class ArrayDisjointSet<T> implements IDisjointSet<T> {
    // Note: do NOT rename or delete this field. We will be inspecting it
    // directly within our private tests.
    private int[] pointers;

    // However, feel free to add more methods and private helper methods.
    // You will probably need to add one or two more fields in order to
    // successfully implement this class.
    private int location;
    private IDictionary<T, Integer> inventory;
    private static final int INITIAL_SIZE = 50;

    
    public ArrayDisjointSet() {
        inventory = new ChainedHashDictionary<T, Integer>();
        location = 0;
        pointers = new int[INITIAL_SIZE];
    }

    @Override
    public void makeSet(T item) {
        if (inventory.containsKey(item)) {
            throw new IllegalArgumentException();
        }
        if (location >= pointers.length - 1) {
            resize();
        }
        inventory.put(item, location);
        pointers[location] = -1;
        location++;
    }

    // double size of pointers
    private void resize() {
        int[] newPointers = new int[2 * pointers.length];
        for (int i = 0; i < pointers.length; i++) {
            newPointers[i] = pointers[i];
        }
        pointers = newPointers;
    }

    @Override
    public int findSet(T item) {
        if (!inventory.containsKey(item)) {
            throw new IllegalArgumentException();
        }
        int loca = inventory.get(item);
        
        return find(loca);
    }
    // find root of tree
    private int find(int parent) {
        
        int newPar = pointers[parent];
        if (newPar < 0) {
            return parent;
        } 
        int par = find(newPar);
        pointers[parent] = par;
        return par;
    }

    @Override
    public void union(T item1, T item2) {
        int set1 = findSet(item1);
        int set2 = findSet(item2);
        if (set1 == set2) {
            throw new IllegalArgumentException();
        }
        int rank1 = pointers[set1];
        int rank2 = pointers[set2];
        if (rank1 < rank2) {
            helpUnion(set2, set1);
        } else if (rank1 > rank2) {
            helpUnion(set1, set2);
        } else {
            pointers[set2]--;
            helpUnion(set1, set2);
        }
    }

    private void helpUnion(int lowRankRoot, int highRankRoot) {
        pointers[lowRankRoot] = highRankRoot;
    }
    
}
