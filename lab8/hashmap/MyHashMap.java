package hashmap;

import java.util.*;


/**
 *  A hash table-backed Map implementation. Provides amortized constant time
 *  access to elements via get(), remove(), and put() in the best case.
 *
 *  Assumes null keys will never be inserted, and does not resize down upon remove().
 *  @author YOUR NAME HERE
 */
public class MyHashMap<K, V> implements Map61B<K, V> {
    /**
     * Protected helper class to store key/value pairs
     * The protected qualifier allows subclass access
     */
    protected class Node {
        K key;
        V value;

        Node(K k, V v) {
            key = k;
            value = v;
        }
    }
    
    private int bucketSize;
    private final double maxLoad;
    private int size;
    private Collection<Node>[] table;


    /* Instance Variables */
    private Collection<Node>[] buckets;
    // You should probably define some more!

    /** Constructors */
    public MyHashMap() {
        bucketSize = 16;
        maxLoad = 0.75;
        table = createTable(this.bucketSize);
    }

    public MyHashMap(int initialSize) { 
        this.bucketSize = initialSize;
        maxLoad = 0.75;
        table = createTable(this.bucketSize);
    }

    /**
     * MyHashMap constructor that creates a backing array of initialSize.
     * The load factor (# items / # buckets) should always be <= loadFactor
     *
     * @param bucketSize initial size of backing array
     * @param maxLoad maximum load factor
     */
    public MyHashMap(int bucketSize, double maxLoad) {
        this.bucketSize = bucketSize;
        this.maxLoad = maxLoad;
        table = createTable(this.bucketSize);
    }

    /**
     * Returns a new node to be placed in a hash table bucket
     */
    private Node createNode(K key, V value) {
        return new Node(key, value);
    }

    /**
     * Returns a data structure to be a hash table bucket
     *
     * The only requirements of a hash table bucket are that we can:
     *  1. Insert items (`add` method)
     *  2. Remove items (`remove` method)
     *  3. Iterate through items (`iterator` method)
     *
     * Each of these methods is supported by java.util.Collection,
     * Most data structures in Java inherit from Collection, so we
     * can use almost any data structure as our buckets.
     *
     * Override this method to use different data structures as
     * the underlying bucket type
     *
     * BE SURE TO CALL THIS FACTORY METHOD INSTEAD OF CREATING YOUR
     * OWN BUCKET DATA STRUCTURES WITH THE NEW OPERATOR!
     */
    protected Collection<Node> createBucket() {
        return new LinkedList<>();
    }

    /**
     * Returns a table to back our hash table. As per the comment
     * above, this table can be an array of Collection objects
     *
     * BE SURE TO CALL THIS FACTORY METHOD WHEN CREATING A TABLE SO
     * THAT ALL BUCKET TYPES ARE OF JAVA.UTIL.COLLECTION
     *
     * @param tableSize the size of the table to create
     */
    private Collection<Node>[] createTable(int tableSize) {
        Collection<Node>[] table = (Collection<Node>[]) new Collection[tableSize];
        for (int i = 0; i < tableSize; i++) {
            table[i] = createBucket();
        }
        return table;
    }

    // TODO: Implement the methods of the Map61B Interface below

    @Override
    public void clear() {
        table = createTable(bucketSize);
        size = 0;
    }

    @Override
    public boolean containsKey(K key) {
        int bucketIndex = hash2Index(key);
        for (Node item : table[bucketIndex]) {
            if (item.key.equals(key)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public V get(K key) {
        int bucketIndex = hash2Index(key);
        for (Node item : table[bucketIndex]) {
            if (item.key.equals(key)) {
                return item.value;
            }
        }
        return null;
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public void put(K key, V value) {
        if ((double) (size + 1) / bucketSize >= maxLoad ) {
            bucketSize *= 2;
            Collection<Node>[] newTable = createTable(bucketSize);
            for (Collection<Node> bucket : table) {
                for (Node item : bucket) {
                    int index = hash2Index(item.key);
                    newTable[index].add(item);
                }
            }
            table = newTable;
        }

        int bucketIndex = hash2Index(key);
        boolean keyExist = false;
        for (Node item : table[bucketIndex]) {
            if (item.key.equals(key)) {
                item.value = value;
                keyExist = true;
            }
        }
        if (!keyExist) {
            Node newNode = new Node(key, value);
            table[bucketIndex].add(newNode);
            size += 1;
        }

    }

    @Override
    public Set<K> keySet() {
        Set<K> keys = new HashSet<>();
        for (Collection<Node> bucket : table) {
            for (Node item : bucket) {
                keys.add(item.key);
            }

        }
        return keys;
    }

    @Override
    public V remove(K key) {
        int bucketIndex = hash2Index(key);
        V value = null;
        for (Node item : table[bucketIndex]) {
            if (item.key.equals(key)) {
                value = item.value;
                table[bucketIndex].remove(item);
            }
        }
        size -= 1;
        return value;
    }

    @Override
    public V remove(K key, V value) {
        int bucketIndex = hash2Index(key);
        V val = null;
        for (Node item : table[bucketIndex]) {
            if (item.key.equals(key) && item.value.equals(value)) {
                val = item.value;
                table[bucketIndex].remove(item);
            }
        }
        return val;
    }

    private class hashMapIterator implements Iterator<K> {
        private int bucketPos;
        private Iterator<Node> curBucket;
        public hashMapIterator() {
            bucketPos = 0;
            curBucket = table[bucketPos].iterator();
        }
        public boolean hasNext() {
            if (bucketPos < bucketSize) {
                return false;
            } else {
                return !curBucket.hasNext();
            }
        }
        public K next() {
            if (curBucket.hasNext()) {
                return curBucket.next().key;
            } else if (bucketPos < bucketSize) {
                bucketPos += 1;
                curBucket = table[bucketPos].iterator();
                return curBucket.next().key;
            }
            return null;
        }
    }

    @Override
    public Iterator<K> iterator() {
        return new hashMapIterator();
    }

    private int hash2Index(K key) {
        int hash = key.hashCode();
        return Math.floorMod(hash, bucketSize);
    }

}
