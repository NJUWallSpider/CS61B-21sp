package bstmap;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class BSTMap<K extends Comparable<K>, V> implements Map61B<K, V>{

    private Node root;             // root of BST

    private class Node {
        private K key;           // sorted by key
        private V val;         // associated data
        private Node left, right;  // left and right subtrees
        private int size;          // number of nodes in subtree

        public Node(K key, V val, int size) {
            this.key = key;
            this.val = val;
            this.size = size;
        }
    }

    public BSTMap() {
        root = null;
    }

    @Override
    public void clear() {
        root = null;
    }

    public boolean containsKey(K key) {
        return containsKey(key, root);
    }

    private boolean containsKey(K key, Node x) {
        return getNode(key, x) != null;
    }

    @Override
    public V get(K key) {
        Node hasKey = getNode(key, root);
        if (hasKey == null) {
            return null;
        } else {
            return hasKey.val;
        }
    }

    private Node getNode(K key, Node x) {
        if (x == null) {
            return null;
        } else if (x.key.compareTo(key) > 0) {
            return getNode(key, x.left);
        } else if (x.key.compareTo(key) < 0) {
            return getNode(key, x.right);
        }
        return x;
    }

    @Override
    public int size() {
        return size(root);
    }

    private int size(Node x) {
        if (x == null) return 0;
        else return x.size;
    }

    @Override
    public void put(K key, V value) {
        if (key == null) throw new IllegalArgumentException("calls put() with a null key");
        root = put(key, value, root);
    }

    private Node put(K key, V value, Node x) {
        if (x == null) {
            return new Node(key, value, 1);
        }
        int cmp = key.compareTo(x.key);
        if (cmp < 0) {
            x.left = put(key, value, x.left);
        } else if (cmp > 0) {
            x.right = put(key, value, x.right);
        } else {
            x.val = value;
        }
        x.size = 1 + size(x.left) + size(x.right);
        return x;
    }

    @Override
    public Set<K> keySet() {
        return keySet(root);
    }

    private Set<K> keySet(Node x) {
        HashSet<K> set = new HashSet<>();
        if (x != null) {
            set.add(x.key);
            set.addAll(keySet(x.left));
            set.addAll(keySet(x.right));
        }
        return set;
    }


    @Override
    public V remove(K key) {
        if (key == null) throw new IllegalArgumentException("calls delete() with a null key");
        return remove(key, root);
    }

    private V remove(K key, Node x) {
        if (x == null) return null;
        int cmp = key.compareTo(x.key);
        if      (cmp < 0) remove(key, x.left);
        else if (cmp > 0) remove(key, x.right);
        else {
        }
        x.size = size(x.left) + size(x.right) + 1;
    }

    @Override
    public V remove(K key, V value) {
        throw new UnsupportedOperationException("Unsupported operation");
    }

    @Override
    public Iterator<K> iterator() {
        throw new UnsupportedOperationException("Unsupported operation");
    }
}
