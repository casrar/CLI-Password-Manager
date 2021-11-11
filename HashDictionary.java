import java.util.Iterator;
import java.util.NoSuchElementException;
import java.io.Serializable;

/**
 * This is an implementation of a hash table using separate chaining. Entries in
 * the array contain Nodes, which form a chain of all key-value pairs with the
 * same key hash index.
 *
 * There's no mechanism to resize the array here. There's also no real need, as
 * a table entry will contain a linked chain which may have multiple nodes.
 *
 * Some things to think about: - How would you implement the iterators for this
 * collection? - What is the runtime complexity of the methods?
 *
 * @author Stephen J. Sarma-Weierman
 * @author Casey Morar
 */
public class HashDictionary<K, V> implements Dictionary<K, V>, Serializable {

    private Object[] entries; // array of Nodes
    private int size;
    private static final int DEFAULT_CAPACITY = 17;
    
    public HashDictionary() {
        this(DEFAULT_CAPACITY);
    }
    
    public HashDictionary(int initialCapacity) {
        entries = new Object[initialCapacity];
        size = 0;
    }

    @Override
    public Iterator<K> keys() {
        return new Iterator<K>() {
            private int count = 0;
            private int currentIndex = 0;
            private Node currentNode = null;

            public boolean hasNext() {
                return count < size; // If count < size, implicitly there is a next Node
            }
            // If there is no next, throw NoSuchElementException otherwise while node is null iterate through index
            // Otherwise grab key stored at node and increment through chain.
            public K next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                while (currentNode == null) {
                    currentNode = (Node) entries[currentIndex++];
                }
                K tempKey = currentNode.getKey(); // Stores the previous key in currentNode
                currentNode = currentNode.getNext();
                count++;
                return tempKey;
            }
        };
    }
    
    @Override
    public Iterator<V> elements( ) {
        return new Iterator<V>() {
            private int count = 0;
            private int currentIndex = 0;
            private Node currentNode = null;

            public boolean hasNext() {
                return count < size; // If count < size, implicitly there is a next Node
            }

            // If there is no next, throw NoSuchElementException otherwise while node is null iterate through index
            // Otherwise grab value stored at node and increment through chain.
            public V next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                while (currentNode == null) {
                    currentNode = (Node) entries[currentIndex++];
                }
                V tempValue = currentNode.getValue(); // Stores the previous value in currentNode
                currentNode = currentNode.getNext();
                count++;
                return tempValue;
            }
        };
    }

    /**
     * This returns an index based on the hashCode for the key object. The index
     * must be in the bounds of the array.
     *
     * @param key
     * @return
     */
    private int getHashIndex(K key) {
        int capacity = entries.length;
        int index = key.hashCode() % capacity;
        if (index < 0) {
            index += capacity;
        }
        return index;
    }

    @SuppressWarnings("unchecked")
    @Override
    public V get(K key) {
        int index = getHashIndex(key);
        Node n = (Node)entries[index];
        while (n != null && !n.getKey().equals(key)) {
            n = n.getNext();
        }
        if (n == null) {
            return null;
        }
        return n.getValue();
    }

    // Checks if key is null, if it is return null. Else hash the key
    // and check if the index is null. If index is null then put node indo index.
    // If position is not null then traverse through the list and check if key
    // is already contained. If it is then replace value and return previous value.
    // Otherwise add node onto the end of the chain.
    @Override
    public V put(K key, V value) {
        if (key == null) {
            return null;
        }
        int hashedIndex = getHashIndex(key);
        Node newNode = new Node(key, value);
        if (entries[hashedIndex] == null) {
            entries[hashedIndex] = newNode;
            size++;
            return value;
        }
        Node previousNode = null;
        Node traversalNode = (Node) entries[hashedIndex];
        while (traversalNode != null) {
            previousNode = traversalNode;
            if (traversalNode.getKey().equals(key)) {
                V returnValue = traversalNode.getValue();
                traversalNode.setValue(value);
                return returnValue;
            }
            traversalNode = traversalNode.getNext();
        }
        previousNode.setNext(newNode);
        size++;
        return null;
    }

    /**
     * If the key is in the collection of keys, return the associated
     * value and remove both key and value. Otherwise, return null and leave
     * collections unaltered.
     * Precondition: key != null
     * Postcondition: key, and associated value (if any), are not in dictionary
     * @param key
     * @return value associated with key
     */
    @Override
    public V remove(K key) {
        if (key == null) {
            return null;
        }
        int hashedIndex = getHashIndex(key);
        if (entries[hashedIndex] == null) {
            return null;
        }

        Node previousNode = null;
        Node traversalNode = (Node) entries[hashedIndex];
        while (traversalNode != null) {
            previousNode = traversalNode;
            if (traversalNode.getKey().equals(key)) {
                V returnValue = traversalNode.getValue();
                previousNode.setNext(traversalNode.getNext());
                traversalNode.setNext(null);
                size--;
                return returnValue;
            }
            traversalNode = traversalNode.getNext();
        }
        V returnValue = previousNode.getValue();
        previousNode = null;
        size--;
        return returnValue;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public int size() {
        return size;
    }
    
        private boolean isPrime(int n) {
        if (n < 2) {
            return false;
        }
        if (n == 2 || n == 3) {
            return true;
        }
        if (n % 2 == 0) {
            return false;
        }

        for (int i = 3; i < (int) Math.sqrt(n) + 1; i += 2) {
            if (n % i == 0) {
                return false;
            }
        }

        return true;
    }

    private int nextPrime(int n) {
        int p = n + 1;
        while (!isPrime(p)) {
            p++;
        }
        return p;
    }


    private class Node implements Serializable {

        private K key;
        private V value;
        private Node next;

        public Node(K key, V value) {
            this.key = key;
            this.value = value;
        }

        public K getKey() {
            return key;
        }

        public void setKey(K key) {
            this.key = key;
        }

        public V getValue() {
            return value;
        }

        public void setValue(V value) {
            this.value = value;
        }

        public Node getNext() {
            return next;
        }

        public void setNext(Node next) {
            this.next = next;
        }

    }
}
