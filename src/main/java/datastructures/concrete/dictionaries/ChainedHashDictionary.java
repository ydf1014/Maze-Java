package datastructures.concrete.dictionaries;

import datastructures.concrete.KVPair;
import datastructures.interfaces.IDictionary;
import misc.exceptions.NoSuchKeyException;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * See the spec and IDictionary for more details on what each method should do
 */
public class ChainedHashDictionary<K, V> implements IDictionary<K, V> {
    // You may not change or rename this field: we will be inspecting
    // it using our private tests.
    private IDictionary<K, V>[] chains;
    private int numElement;
    // You're encouraged to add extra fields (and helper methods) though!

    public ChainedHashDictionary() {
        chains = makeArrayOfChains(10);
        numElement = 0;      
    }

    /**
     * This method will return a new, empty array of the given size
     * that can contain IDictionary<K, V> objects.
     *
     * Note that each element in the array will initially be null.
     */
    @SuppressWarnings("unchecked")
    private IDictionary<K, V>[] makeArrayOfChains(int size) {
        // Note: You do not need to modify this method.
        // See ArrayDictionary's makeArrayOfPairs(...) method for
        // more background on why we need this method.
        return (IDictionary<K, V>[]) new IDictionary[size];
    }

    private int modifiedKeyHash(K key, int length) {
        if (key == null) {
            return 0;
        }
        int modiKey = Math.abs(key.hashCode());
        return modiKey % length;
    }
    
    @Override
    public V get(K key) {
        int modiKey = modifiedKeyHash(key, chains.length);
        if (chains[modiKey] == null) {
            throw new NoSuchKeyException();
        }
        return(chains[modiKey].get(key));
    }
    
    @Override
    public void put(K key, V value) {
        int modiKey = modifiedKeyHash(key, chains.length);
        if (chains[modiKey] == null) {
            chains[modiKey] = new ArrayDictionary<K, V>();
        }
        int currentNumEle = chains[modiKey].size();
        chains[modiKey].put(key, value);
        if (chains[modiKey].size() > currentNumEle) {
            numElement++;
        }
        if (((double) numElement / chains.length) >= 1) {
            // resize the dictionary
            IDictionary<K, V>[] newDic = makeArrayOfChains(2 * chains.length);
            Iterator<KVPair<K, V>> iter = this.iterator();
            while (iter.hasNext()){
                KVPair<K, V> current = iter.next();
                int mKey = modifiedKeyHash(current.getKey(), chains.length * 2);
                if (newDic[mKey] == null) {
                    newDic[mKey] = new ArrayDictionary<K, V>();
                }
                newDic[mKey].put(current.getKey(), current.getValue());
            }
            chains = newDic;
        }
    }
    

    @Override
    public V remove(K key) {
        if (!containsKey(key)) {
            throw new NoSuchKeyException();
        }
        int modiKey = modifiedKeyHash(key, chains.length);
        numElement--;
        return chains[modiKey].remove(key);
    }

    
    @Override
    public boolean containsKey(K key) {
        int modiKey = modifiedKeyHash(key, chains.length);
        return chains[modiKey] != null && chains[modiKey].containsKey(key);
    }

    @Override
    public int size() {
        return numElement;
    }

    @Override
    public Iterator<KVPair<K, V>> iterator() {
        // Note: you do not need to change this method
        return new ChainedIterator<>(this.chains);
    }

    /**
     * Hints:
     *
     * 1. You should add extra fields to keep track of your iteration
     *    state. You can add as many fields as you want. If it helps,
     *    our reference implementation uses three (including the one we
     *    gave you).
     *
     * 2. Before you try and write code, try designing an algorithm
     *    using pencil and paper and run through a few examples by hand.
     *
     *    We STRONGLY recommend you spend some time doing this before
     *    coding. Getting the invariants correct can be tricky, and
     *    running through your proposed algorithm using pencil and
     *    paper is a good way of helping you iron them out.
     *
     * 3. Think about what exactly your *invariants* are. As a
     *    reminder, an *invariant* is something that must *always* be 
     *    true once the constructor is done setting up the class AND 
     *    must *always* be true both before and after you call any 
     *    method in your class.
     *
     *    Once you've decided, write them down in a comment somewhere to
     *    help you remember.
     *
     *    You may also find it useful to write a helper method that checks
     *    your invariants and throws an exception if they're violated.
     *    You can then call this helper method at the start and end of each
     *    method if you're running into issues while debugging.
     *
     *    (Be sure to delete this method once your iterator is fully working.)
     *
     * Implementation restrictions:
     *
     * 1. You **MAY NOT** create any new data structures. Iterators
     *    are meant to be lightweight and so should not be copying
     *    the data contained in your dictionary to some other data
     *    structure.
     *
     * 2. You **MAY** call the `.iterator()` method on each IDictionary
     *    instance inside your 'chains' array, however.
     */
    private static class ChainedIterator<K, V> implements Iterator<KVPair<K, V>> {
        private IDictionary<K, V>[] chains;
        private int chainIndex;
        private Iterator<KVPair<K, V>> arrayDicIter;

        public ChainedIterator(IDictionary<K, V>[] chains) {
            this.chains = chains;
            chainIndex = 0;
            arrayDicIter = null;
            if (chains[0] != null && chains[0].size() > 0) {
                arrayDicIter = chains[chainIndex].iterator();
            }
        }

        @Override
        public boolean hasNext() {   
            for (int i = chainIndex; i < chains.length; i++) {          
                if (arrayDicIter != null && arrayDicIter.hasNext()) {
                    return true;
                }         
                chainIndex++;
                if (i + 1 >= chains.length) {
                    return false;
                }
                if (chains[i + 1] != null) {
                    arrayDicIter = chains[i + 1].iterator();   
                } else {
                    arrayDicIter = null;
                }
            
            } 
            return false;       
        }

        @Override
        public KVPair<K, V> next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            return arrayDicIter.next();
        }
    }
}
