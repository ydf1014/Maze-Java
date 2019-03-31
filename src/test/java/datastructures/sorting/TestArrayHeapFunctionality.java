package datastructures.sorting;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import misc.BaseTest;
import misc.exceptions.EmptyContainerException;
import datastructures.concrete.ArrayHeap;
import datastructures.interfaces.IPriorityQueue;
import org.junit.Test;

/**
 * See spec for details on what kinds of tests this class should include.
 */
public class TestArrayHeapFunctionality extends BaseTest {
    protected <T extends Comparable<T>> IPriorityQueue<T> makeInstance() {
        return new ArrayHeap<>();
    }

    @Test(timeout=SECOND)
    public void testBasicSize() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        heap.insert(3);
        assertEquals(1, heap.size());
        assertTrue(!heap.isEmpty());
    }
    
    @Test (timeout = SECOND)
    public void testDuplicates() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        heap.insert(1);
        heap.insert(1);
        heap.insert(2);
        heap.insert(3);
        heap.insert(4);
        heap.insert(5);
        heap.insert(5);
        heap.removeMin();
        assertEquals(1, heap.removeMin());
        heap.removeMin();
        heap.removeMin();
        heap.removeMin();
        assertEquals(5, heap.removeMin());
        assertEquals(1, heap.size());
        heap.removeMin(); 
        int k = 15;
        while (k > 0) {
            heap.insert(8);
            k--;
        }
        heap.insert(1);
        heap.insert(9);
        heap.insert(0);
        assertEquals(0, heap.removeMin());
        assertEquals(1, heap.removeMin());
        for (int i = 0; i < 15; i++) {
            assertEquals(8, heap.removeMin());   
        }
        assertEquals(9, heap.peekMin());
    }
    
    @Test(timeout = SECOND)
    public void testReverseInputNoDuplicates() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        int k = 15;
        while (k > 0) {
            heap.insert(k);
            k--;
        }
        assertEquals(1, heap.peekMin());
        heap.insert(0);
        assertEquals(16, heap.size());
        assertEquals(0, heap.peekMin());
        
    }
    
    @Test(timeout = SECOND)
    public void testShortInputNoDuplicates() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        int k = 1;
        while (k < 16) {
            heap.insert(k);
            k++;
        }
        assertEquals(1, heap.peekMin());
        heap.insert(0);
        assertEquals(16, heap.size());
        assertEquals(0, heap.peekMin());
    }
    
    @Test(timeout = SECOND)
    public void testNullInput() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        try {
            heap.insert(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // GOOD
        }
        assertEquals(0, heap.size());
    }
    
    @Test(timeout = SECOND)
    public void testEmptyContainerException() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        try {
            heap.peekMin();
            fail("Expected EmptyContainerException");
        } catch (EmptyContainerException e) {
            // GOOD
        }
        assertEquals(0, heap.size());
        try {
            heap.removeMin();
            fail("Expected EmptyContainerException");
        } catch (EmptyContainerException e) {
            // GOOD
        }
        assertEquals(0, heap.size());
        heap.insert(1);
        assertEquals(1, heap.peekMin());
        assertEquals(1, heap.removeMin());
        assertEquals(0, heap.size());
        try {
            heap.removeMin();
        } catch (EmptyContainerException e) {
            // GOOD
        }

        try {
            int k = 1;
            while (k < 16) {
                heap.insert(k);
                k++;
            }
            while (k > 1) {
                heap.removeMin();
                k--;
            }
            assertEquals(0, heap.size());
            heap.removeMin();
        } catch (EmptyContainerException e) {
            // GOOD
        }
        assertEquals(0, heap.size());
    }

    @Test(timeout = SECOND)
    public void testSortSuccessfully() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        int k = 1;
        while (k < 16) {
            heap.insert(k);
            k++;
        }
        k = 1;
        while (k < 16) {
            assertEquals(k, heap.removeMin());
            k++;
        }
    }
    
    @Test(timeout = SECOND)
    public void testReverseSortSuccessfully() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        int k = 15;
        while (k > 0) {
            heap.insert(k);
            k--;
        }
        k = 1;
        while (k < 16) {
            assertEquals(k, heap.removeMin());
            k++;
        }
    }
    
    @Test(timeout = SECOND)
    public void testRandomNumSort() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        int[] unsorted = new int[] {3, 5, 2, 8, 1, 9, 4, 7, 6, 0};
        int[] sorted = new int[] {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
        int[] sortedPlus = new int[] {-3, -2, -1, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
        for (int i = 0; i < unsorted.length; i++) {
            heap.insert(unsorted[i]);
        }
        for (int i = 0; i < sorted.length; i++) {
            assertEquals(sorted[i], heap.removeMin());
        }

        for (int i = 0; i < unsorted.length; i++) {
            heap.insert(unsorted[i]);
        }
        heap.insert(-2);
        heap.insert(-1);
        heap.insert(-3);
        for (int i = 0; i < sortedPlus.length; i++) {
            assertEquals(sortedPlus[i], heap.removeMin());
        }
    }
    
    @Test(timeout = SECOND)
    public void testSuperRandomNumSort() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        int[] unsorted = new int[] {3, 2, 8, 9, 4, 7, 6, 0, -100};
        int[] sortedPlus = new int[] {-100, -3, -1, 0, 2, 3, 4, 6, 7, 8, 9, 100};
        for (int i = 0; i < unsorted.length; i++) {
            heap.insert(unsorted[i]);
        }
        
        heap.insert(-1);
        heap.insert(-3);
        heap.insert(100);
        for (int i = 0; i < sortedPlus.length; i++) {
            assertEquals(sortedPlus[i], heap.removeMin());
        }
    }
    
    @Test (timeout = SECOND) 
    public void testString() {
        IPriorityQueue<String> heap = this.makeInstance();
        String word = "honorificabilitudinitatibus";
        
        for (int i = 0; i < word.length(); i++) {
            heap.insert(Character.toString(word.charAt(i)));
        }
        
        String removed = "";
        while (!heap.isEmpty()) {
            removed += heap.removeMin(); 
        }
        assertEquals("aabbcdfhiiiiiiilnnoorstttuu", removed);
    }


    @Test (timeout = SECOND)
    public void testNegativeAndPositive() {
        IPriorityQueue<Integer> heap = this.makeInstance();
        for (int i = -5; i < 5; i++) {
            heap.insert(i);
            assertEquals(-5, heap.peekMin());
        }
        assertEquals(10, heap.size());
        heap.removeMin();
        assertEquals(-4, heap.peekMin());
    }

    
    
}
