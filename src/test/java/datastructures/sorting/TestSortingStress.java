package datastructures.sorting;

import misc.BaseTest;
import misc.Searcher;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

import datastructures.concrete.ArrayHeap;
import datastructures.concrete.DoubleLinkedList;
import datastructures.interfaces.IList;
import datastructures.interfaces.IPriorityQueue;


/**
 * See spec for details on what kinds of tests this class should include.
 */
public class TestSortingStress extends BaseTest {
    protected <T extends Comparable<T>> IPriorityQueue<T> makeInstance() {
        return new ArrayHeap<>();
    }
    
    @Test(timeout = 15 * SECOND)
    public void testInverseInsertion() {
    	IPriorityQueue<Integer> heap = this.makeInstance();
    	for (int i = 12456789; i >= 0; i--) {
    		heap.insert(i);
    	}
    	while (!heap.isEmpty()) {
    		heap.removeMin();
    	}
    }

    
    @Test(timeout = 10 * SECOND)
    public void testInsertion() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        for (int i = 0; i < 12456789; i++) {
            heap.insert(i);
        }
    	while (!heap.isEmpty()) {
    		heap.peekMin();
    		heap.removeMin();
    	}
    }
    
    
    @Test(timeout = 10 * SECOND)
    public void testFunkyCalculation() {
        IPriorityQueue<Double> heap = this.makeInstance();
        for (int i = 0; i < 1245678; i++) {
            heap.insert(i * 0.618 % 7);
        }
    	while (!heap.isEmpty()) {
    		heap.peekMin();
    		heap.removeMin();
    	}
    }
    
    
    @Test (timeout = 10 * SECOND)
    public void testArrayHeapSame() {
        IPriorityQueue<Double> heap = this.makeInstance();
        for (int i = 0; i < 12456789; i++) {
            heap.insert(9.124124);
        }
    	while (!heap.isEmpty()) {
    		heap.peekMin();
    		heap.removeMin();
    	}
    }
    
    
    @Test (timeout = 15 * SECOND)
    public void testSorting() {
        List<Integer> data = new ArrayList<>();
        
        for (int i = 0; i < 12456789; i++) {
            data.add(i / 4 % 7);
        }
        Collections.sort(data);
        IPriorityQueue<Integer> heap = this.makeInstance();
        for (int num : data) {
        	heap.insert(num);
        }
        int[] tmp = new int[12456789];
        for (int i = 0; i < 12456789; i++) {
            tmp[i] = heap.removeMin();
        }
        for (int i = 0; i < data.size(); i++) {
            assertEquals(data.get(i), tmp[i]);
        }
    }
    
    @Test (timeout = 10 * SECOND)
    public void testInsertAndRemove() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        for (int i = 1245678; i >= 0; i--) {
            heap.insert(i);
        }
        int counter = 1245678;
        while (counter != 0) {
            heap.insert(heap.removeMin());
            counter -= 1;
        }
    }
    
    @Test (timeout = 10 * SECOND)
    public void testNestedLoop() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        for (int i = 0; i < 124; i++) {
            for (int j = 0; j < 124; j++) {
            	for (int k = 124; k >= 0; k--) {
            		heap.insert(j * k);
            	}
            }
        }
    }
    
    @Test (timeout = 10 * SECOND)
    public void testDifferentK() {
        int k = 10;
        for (int i = 0; i < 10; i++) {
            IList<Integer> list = new DoubleLinkedList<Integer>();
            for (int j = 0; j < 124567; j++) {
                list.add(j % 7);
            }
            Searcher.topKSort(k, list);
            k *= 8;
        }
    }
    
    @Test (timeout = 12 * SECOND)
    public void testK() {
        IList<Integer> list = new DoubleLinkedList<Integer>();
        for (int i = 0; i < 12456789; i++) {
            list.add(i % 2);
        }
        Searcher.topKSort(1245, list);
    }
    

    
    @Test (timeout = 15 * SECOND)
    public void testK2() {
        IList<Integer> list = new DoubleLinkedList<Integer>();
        int count = 12456789;
        while (count != 0) {
        	list.add(count);
        	count--;
        }
        Searcher.topKSort(1245, list);
    }
    
}
   
