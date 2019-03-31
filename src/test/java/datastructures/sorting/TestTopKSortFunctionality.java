package datastructures.sorting;

import misc.BaseTest;
import datastructures.concrete.DoubleLinkedList;
import datastructures.interfaces.IList;
import misc.Searcher;
import static org.junit.Assert.fail;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.junit.Test;

/**
 * See spec for details on what kinds of tests this class should include.
 */
public class TestTopKSortFunctionality extends BaseTest {
    
    protected <T> List<T> convertList(IList<T> list) {
        List<T> nList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            nList.add(list.get(i));
        }
        return nList;
    }
    
    @Test (timeout = SECOND)
    public void testDifferentK() {
        IList<Integer> list = new DoubleLinkedList<>();
        int count = 0;
        while (count < 50) {
        	list.add(count);
        	count++;
        }
        list = Searcher.topKSort(124, list);
        assertEquals(0, list.get(0));
        list = Searcher.topKSort(15, list);
        assertEquals(15, list.size());
        list = Searcher.topKSort(0, list);
        assertEquals(0, list.size());
    }
    

    
    @Test (timeout = SECOND)
    public void testNegativeValue() {
        IList<Integer> list = new DoubleLinkedList<Integer>();
        int count = 0;
        while (count < 50) {
        	list.add(-1 * count);
        	count++;
        }
        IList<Integer> data = Searcher.topKSort(5, list);
        assertEquals(5, data.size());
        for (int i = 0; i < 5; i++) {
            assertEquals(true, data.get(i) >= -5);
        }
    }
    
    @Test (timeout = SECOND)
    public void testFunkyNum() {
        IList<Double> list = new DoubleLinkedList<Double>();
        int count = 0;
        while (count < 50) {
            if (count % 7 >= 2) {
                list.add(-1.8 * count);
            } else {
                list.add(-1.8 + count);
            }
            count++;
        }
        List<Double> perfect = convertList(list);
        Collections.sort(perfect);
        Collections.reverse(perfect);
        list = Searcher.topKSort(7, list);
        for (int i = 0; i < 7; i++) {
            assertEquals(list.get(i), perfect.get(6 - i));
        }
    }
    
    @Test (timeout = SECOND)
    public void testExceptions() {
        try {
            IList<Integer> list = Searcher.topKSort(-1, new DoubleLinkedList<Integer>());
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            System.out.println("success");
        }
    }
   
    
    @Test (timeout = SECOND)
    public void testDifferentManipulations() {
        IList<Integer> list = new DoubleLinkedList<>();
        int count = 0;
        while (count < 100) {
        	list.add(1);
        	count++;
        }
        list.add(-1);
        list.add(2);
        list = Searcher.topKSort(10, list);
        assertEquals(list.get(list.size()-1), 2);
        assertEquals(list.get(0), 1);
    }
    
}
