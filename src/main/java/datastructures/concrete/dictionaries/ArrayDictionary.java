package datastructures.concrete.dictionaries;

import datastructures.concrete.KVPair;
import datastructures.interfaces.IDictionary;
import misc.exceptions.NoSuchKeyException;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class ArrayDictionary<K, V> implements IDictionary<K, V> {
    private Pair<K, V>[] pairs;
    private int size;
    

    public ArrayDictionary() {
        pairs = makeArrayOfPairs(5);
        size = 0;
    }

    @SuppressWarnings("unchecked")
    private Pair<K, V>[] makeArrayOfPairs(int arraySize) {
        return (Pair<K, V>[]) (new Pair[arraySize]);
    }

    
    private int keyIndex(K key) {
        for (int i = 0; i < size; i++) {
            if (equalsKey(pairs[i].key, key)) {
                return i;
            }
        }
        return -1;
    }

    public V get(K key) {
        int index = keyIndex(key);
        if (index == -1) {
            throw new NoSuchKeyException();
        }
        return pairs[index].value;
    }
    

    @Override
    public void put(K key, V value) {
        int index = keyIndex(key);
        if (index != -1) { 
            pairs[index].value = value;
        } else {
            if (size >= pairs.length) {
                Pair<K, V>[] newPairs = makeArrayOfPairs(2 * pairs.length);
                for (int i = 0; i < size; i++) {
                    newPairs[i] = pairs[i];
                }
                pairs = newPairs;
            }
            pairs[size] = new Pair<K, V>(key, value);
            size++;
        }   
    }

    @Override
    public V remove(K key) {
        int index = keyIndex(key);
        if (index == -1) { 
            throw new NoSuchKeyException();
        }
        V value = pairs[index].value;
        pairs[index] = pairs[size - 1];
        pairs[size - 1] = null;
        size--;
        return value;
    }

    @Override
    public boolean containsKey(K key) {
        for (int i = 0; i < size; i++) {
            if (equalsKey(pairs[i].key, key)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int size() {
        return size;
    }

    // returns true if the given two keys are equaled and false otherwise
    private boolean equalsKey(K key1, K key2) {
        return ((key1 == null && key2 == null) || (key2 != null && key2.equals(key1)));
    }
    
    public Iterator<KVPair<K, V>> iterator() {
        return new ArrayDictionaryIterator<K, V>(size, pairs);
    }
    
    private static class ArrayDictionaryIterator<K, V> implements Iterator<KVPair<K, V>> {
        private int currentIndex;
        private int size;
        private Pair<K, V>[] pairs;
        public ArrayDictionaryIterator(int size, Pair<K, V>[] pairs) {
            currentIndex = 0;
            this.size = size;
            this.pairs = pairs;
        }
        
        public boolean hasNext() {
            return currentIndex < size;
        }
        
        public KVPair<K, V> next(){
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            currentIndex++;
            return new KVPair<K, V>(pairs[currentIndex - 1].key, pairs[currentIndex - 1].value);
        }
    }
    

    private static class Pair<K, V> {
        public K key;
        public V value;

        // You may add constructors and methods to this class as necessary.
        public Pair(K key, V value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public String toString() {
            return this.key + "=" + this.value;
        }
    }
}
