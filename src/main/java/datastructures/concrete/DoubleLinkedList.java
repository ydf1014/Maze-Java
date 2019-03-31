package datastructures.concrete;

import datastructures.interfaces.IList;
import misc.exceptions.EmptyContainerException;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Note: For more info on the expected behavior of your methods, see
 * the source code for IList.
 */
public class DoubleLinkedList<T> implements IList<T> {
    // You may not rename these fields or change their types.
    // We will be inspecting these in our private tests.
    // You also may not add any additional fields.
    private Node<T> front;
    private Node<T> back;
    private int size;

    public DoubleLinkedList() {
        this.front = null;
        this.back = null;
        this.size = 0;
    }

    @Override
    public void add(T item) {
        if (size() == 0) {
            front = new Node<T>(item);
            back = front;
        } else {
            back.next = new Node<T>(back, item, null);
            back = back.next;
        }
        size++;
    }

    @Override
    public T remove() {
        if (size() == 0) {
            throw new EmptyContainerException();
        } else {
            T node = back.data;
            if (size() == 1) {
                front = null;
                back = null;
            } else {
                back = back.prev;
                
                back.next = null;
            }
          size--;
          return node;
        }
    }

    @Override
    public T get(int index) {
        if (index < 0 || index >= size()) {
            throw new IndexOutOfBoundsException();
        }
        return getIndex(index).data;
    }

    @Override
    public void set(int index, T item) {
        if (index < 0 || index >= size()) {
            throw new IndexOutOfBoundsException();
        }
        if (front == null && index == 0) {
            front = new Node<T>(item);
        }
        if (index == 0 && front != null) {
            front = new Node<T>(null, item, front.next);
        } else {
            Node<T> temp = getIndex(index - 1);
            Node<T> node = temp.next;
            temp.next = new Node<T>(temp, item, node.next);
            if (temp.next.next != null) { 
                temp.next.next.prev = temp.next;
                node.next = null;
            } else {
                back = temp.next;
            }
            node.prev = null;
            
        
        }
    }

    @Override
    public void insert(int index, T item) {
        if (index < 0 || index >= size() + 1) {
            throw new IndexOutOfBoundsException();
        }
        if (index == size()) {
            add(item);
        } else if (index == 0) {
              Node<T> newFront = new Node<T>(null, item, front);
              front.prev = newFront;
              front = front.prev;
              size++;
        } else {
            Node<T> temp = getIndex(index - 1);
            temp.next = new Node<T>(temp, item, temp.next);
            temp.next.next.prev = temp.next;
            size++;
        }
    }

    @Override
    public T delete(int index) {
        if (index < 0 || index >= size()) {
            throw new IndexOutOfBoundsException();
        }
        if (size == 1 || index == size - 1) {
            return remove();
        } 
        T node = front.data;
        if (index == 0) {
            front = front.next;
            front.prev = null;
        } else {
            Node<T> cur = front;
            for (int i = 0; i < index; i++) {
                cur = cur.next;
            }
            cur.prev.next = cur.next;
            cur.next.prev = cur.prev;
            node = cur.data;
        }
        size--;
        return node;
    }

    @Override
    public int indexOf(T item) {
        Node<T> cur = front;
        for (int i = 0; i < size(); i++) {
            if (cur.data == item || (cur.data != null && cur.data.equals(item))) {
                return i;
            }
            cur = cur.next;
        }
        return -1;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean contains(T other) {
        return indexOf(other) >= 0;
    }
    
    private Node<T> getIndex(int index) {
        Node<T> cur = front;
        if (index <= size/2) {
            for (int i = 0; i < index; i++) {
                cur = cur.next;
            }
        } else {
            cur = back;
            for (int i = size - 1; i > index; i--) {
                cur = cur.prev;
            }
        }
        return cur;
    }
    
    @Override
    public Iterator<T> iterator() {
        // Note: we have provided a part of the implementation of
        // an iterator for you. You should complete the methods stubs
        // in the DoubleLinkedListIterator inner class at the bottom
        // of this file. You do not need to change this method.
        return new DoubleLinkedListIterator<>(this.front);
    }

    private static class Node<E> {
        // You may not change the fields in this node or add any new fields.
        public final E data;
        public Node<E> prev;
        public Node<E> next;

        public Node(Node<E> prev, E data, Node<E> next) {
            this.data = data;
            this.prev = prev;
            this.next = next;
        }

        public Node(E data) {
            this(null, data, null);
        }

        // Feel free to add additional constructors or methods to this class.
    }

    private static class DoubleLinkedListIterator<T> implements Iterator<T> {
        // You should not need to change this field, or add any new fields.
        private Node<T> current;

        public DoubleLinkedListIterator(Node<T> current) {
            // You do not need to make any changes to this constructor.
            this.current = current;
        }

        /**
         * Returns 'true' if the iterator still has elements to look at;
         * returns 'false' otherwise.
         */
        public boolean hasNext() {
            return this.current != null;
        }

        /**
         * Returns the next item in the iteration and internally updates the
         * iterator to advance one element forward.
         *
         * @throws NoSuchElementException if we have reached the end of the iteration and
         *         there are no more elements to look at.
         */
        public T next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            T node = current.data;
            current = current.next;
            return node;
        }
        
    }
}
