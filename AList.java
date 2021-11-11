import java.io.Serial;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.Arrays;
import java.io.Serializable;

/**
 * An array-based list data structure. For this implementation, please use the
 * inculded List.java interface, and <b>not</b> java.util.List.
 * @author Stephen J. Weierman
 * @author YOUR NAME HERE
 */
public class AList<T> implements List<T>, Serializable {
    private int size;
    private Object[] list;
    private int capacity;
    public static final int DEFAULT_CAPACITY = 25;

    public AList() {
        this(DEFAULT_CAPACITY);
        this.capacity = DEFAULT_CAPACITY;
    }

    public AList(int initialCapacity) {
        this.capacity = initialCapacity;
        list = new Object[capacity];
        size = 0;
    }

    // Checks to see if size is == to capacity, if so resize array.
    // Then checks to see if index is larger than size, if it is then
    // an IndexOutOfBoundsException is thrown. If index is valid then
    // iterates through list and shifts values until index is reached
    // and adds object to that index.
    public boolean add(int index, T obj) {
        if (size == capacity) {
            resizeArray(capacity * 2);
        }
        if (index > size) {
            throw new IndexOutOfBoundsException();
        }
        for (int i = size - 1; i > index; i--) {
            list[i + 1] = list[i];
        }
        list[index + 1] = list[index];
        list[index] = obj;
        size++;
        return true;
    }

    // Checks to see if size is == to capacity, if so resize array.
    // Then object is added to size as size will be th next available index.
    public boolean add(T obj) {
        if (size == capacity) {
            resizeArray(capacity * 2);
        }
        list[size] = obj;
        size++;
        return true;
    }

    // Checks if index of value to grab is out of bounds,
    // if it is bound then return value.
    @SuppressWarnings("unchecked")
    public T get(int index) {
        if (index > size || index < 0) {
            throw new IndexOutOfBoundsException();
        }
        return (T) list[index];
    }

    // Starts at the beginning of list and iterates until
    // object is found, if object is not found return -1.
    public int indexOf(T obj) {
        for (int i = 0; i < size; i++) {
            if (list[i].equals(obj)) {
                return i;
            }
        }
        return -1;
    }

    // Gets the last occurrence of an object in the list,
    // if object is not found then -1 is returned.
    public int lastIndexOf(T obj) {
        for (int i = size - 1; i > 0; i--) {
            if (list[i].equals(obj)) {
                return i;
            }
        }
        return -1;
    }
    
    // DONE ************** DO NOT MODIFY LIST ITERATOR CODE *************** //
    public ListIterator<T> listIterator() {
        return new ListIterator<T>() {
            static final int PREVIOUS = 1;
            static final int NEXT = 2;
            int lastCalled = 0;
            //LastCalled indicates if next or previous have been called.
            int nextIndex = 0;

            @Override
            public boolean hasNext() {
                return nextIndex < size;
            }

            @Override
            @SuppressWarnings("unchecked")
            public T next() {
                if (!hasNext())
                    throw new NoSuchElementException("End of collection");
                lastCalled = NEXT;
                return (T)(list[nextIndex++]);
            }

            @Override
            public boolean hasPrevious() {
                return nextIndex > 0;
            }

            @Override
            @SuppressWarnings("unchecked")
            public T previous() {
                if (!hasPrevious())
                    throw new NoSuchElementException("Start of collection");
                lastCalled = PREVIOUS;
                return (T)list[--nextIndex];
            }

            @Override
            public int nextIndex() {
                return nextIndex;
            }

            @Override
            public int previousIndex() {
                return nextIndex-1;
            }

            @Override
            public void remove() {
                if(lastCalled == PREVIOUS) { //Remove current position
                    AList.this.remove(nextIndex);
                } else if (lastCalled == NEXT) { //Remove last position
                    AList.this.remove(--nextIndex);
                } else {
                    throw new IllegalStateException("removed called without next or previous");
                }
                lastCalled = 0; //Reset last called to prevent repeated calls.
            }

            @Override
            public void set(T e) {
                if (lastCalled == PREVIOUS) {
                    list[nextIndex] = e;
                } else if (lastCalled == NEXT) {
                    list[nextIndex-1] = e;
                } else {
                    throw new IllegalStateException("set called without next or previous");
                }

            }

            @Override
            public void add(T e) {
                //Insert before next
                AList.this.add(nextIndex++, e);
            }

        };
    }
    // ************** DO NOT MODIFY ABOVE CODE *************** //

    
    public T remove(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException();
        }
        T obj = get(index);
        for (int i = index; i < size-1; i++) {
            list[i] = list[i+1];
        }
        list[--size] = null;
        return obj;
    }

   
    public T set(int index, T obj) {
        if (index > size || index < 0) {
            throw new IndexOutOfBoundsException();
        }
        T returnVal = (T) list[index];
        list[index] = obj;
        return returnVal;
    }

    // Uses indexOf method to check if an object is contained
    // within array.
    public boolean contains(T obj) {
        return indexOf(obj) > -1;
    }

    // Returns current size of array.
    public int size() {
        return size;
    }

    // Makes new list and sets size = 0;
    public void clear() {
        Object[] list = new Object[capacity];
        size = 0;
    }

    // If size == 0, list is empty.
    public boolean isEmpty() {
        return size == 0;
    }

    // Creates an Object array of elements in list.
    public Object[] toArray() {
        return Arrays.copyOf(list, size);
    }

    // Creates a generic array of elements in list.
    @SuppressWarnings("unchecked")
    public T[] toArray(T[] a) {
        return (T[])Arrays.copyOf(list, size, a.getClass());
    }

    // Resizes array using Arrays.copyOf and a new capacity
    // specified by user.
    private void resizeArray(int newCapacity) {
        list = Arrays.copyOf(list, newCapacity);
        capacity = newCapacity;
    }
}
