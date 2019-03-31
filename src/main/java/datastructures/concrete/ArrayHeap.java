package datastructures.concrete;

import datastructures.interfaces.IPriorityQueue;
import misc.exceptions.EmptyContainerException;

/**
 * See IPriorityQueue for details on what each method must do.
 */
public class ArrayHeap<T extends Comparable<T>> implements IPriorityQueue<T> {
    // See spec: you must implement a implement a 4-heap.
    private static final int NUM_CHILDREN = 4;
    private static final int DEFAULT_CAPA = 10;
    // You MUST use this field to store the contents of your heap.
    // You may NOT rename this field: we will be inspecting it within
    // our private tests.
    
    private T[] heap;
    private int length;

    // Feel free to add more fields and constants.

    public ArrayHeap() {
        heap = makeArrayOfT(DEFAULT_CAPA);
        length = -1;
    }

    
    /**
     * This method will return a new, empty array of the given size
     * that can contain elements of type T.
     *
     * Note that each element in the array will initially be null.
     */
    @SuppressWarnings("unchecked")
    private T[] makeArrayOfT(int size) {
        // This helper method is basically the same one we gave you
        // in ArrayDictionary and ChainedHashDictionary.
        //
        // As before, you do not need to understand how this method
        // works, and should not modify it in any way.
        return (T[]) (new Comparable[size]);
    }

    @Override
    public T removeMin() {
        if (length == -1) {
            throw new EmptyContainerException();
        }
        T min = heap[0];
        length--;
        heap[0] = heap[length + 1];
        if (length > 0) {
            percolateDown(0);
        }
        
        return min;
    }

    private void percolateDown(int position) {
        T temp = heap[position];
        int minPosi = minChildPosition(position);
        if (heap[position].compareTo(heap[minPosi]) > 0) {
            heap[position] = heap[minPosi];
            heap[minPosi] = temp;
            if (position * NUM_CHILDREN + 1 <= length) {
                percolateDown(minPosi);
            }
            
        }
      
    }

    private int minChildPosition(int position) {
        int k = 1;
        int minPosition = position;

        while (k <= NUM_CHILDREN && position * NUM_CHILDREN + k <= length) {
            if (heap[minPosition].compareTo(heap[position * NUM_CHILDREN + k]) > 0) {
                minPosition = position * NUM_CHILDREN + k;
            }
            k++;
        }
        return minPosition;
    }
    
    @Override
    public T peekMin() {
        if (length == -1) {
            throw new EmptyContainerException();
        }
        return heap[0];
    }

    @Override
    public void insert(T item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }
        heap[length + 1] = item;
        length++;
        if (length > 0) {
            percolateUp(length);
        }

        if (length == heap.length - 1) {
            T[] newHeap = makeArrayOfT(heap.length * 2);
            for (int i = 0; i < heap.length; i++) {
                newHeap[i] = heap[i];
            }
            heap = newHeap;
        }
        
    }
    
    private void percolateUp(int position) {
        T temp = heap[(position - 1) / NUM_CHILDREN];
        if (temp.compareTo(heap[position]) > 0) {
            heap[(position - 1) / NUM_CHILDREN] = heap[position];
            heap[position] = temp;
            if ((position - 1) / NUM_CHILDREN > 0) {
                percolateUp((position - 1) / NUM_CHILDREN);
            }
            
        }
        
    }


    @Override
    public int size() {
        return length + 1;
    }
}
