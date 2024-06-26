package bstmap;

import java.util.*;

public class BSTMap<K extends Comparable<K>, V> implements Map61B<K, V>{

    private Node root;             // root of BST
    private V removeStore = null;

    private class Node {
        private K key;           // sorted by key
        private V val;         // associated data
        private Node left, right;  // left and right subtrees
        private int size;          // number of nodes in subtree

        public Node(K key, V val, int size) {
            this.key = key;
            this.val = val;
            this.size = size;
            this.left = null;
            this.right = null;
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
        if (key == null) throw new IllegalArgumentException("calls remove() with a null key");
        root = remove(key, root);
        return removeStore;
    }

    private Node remove(K key, Node x) {
        if (x == null) {
            removeStore = null;
            return null;
        }
        int cmp = key.compareTo(x.key);
        if      (cmp < 0) x.left = remove(key, x.left);
        else if (cmp > 0) x.right = remove(key, x.right);
        else {
            removeStore = x.val;
            if (x.right == null && x.left == null) {
                x = null;
            } else if (x.right == null && x.left != null) {
                x = x.left;
            } else if (x.right != null && x.left == null) {
                x = x.right;
            } else {
                Node n = successorCopy(x);
                x = successorRemove(x);
                x.key = n.key;
                x.val = n.val;
            }
        }
        if (x != null) {
            x.size = size(x.left) + size(x.right) + 1;
        }
        return x;
    }

    private Node max(Node x) {
        if (x.right == null) {
            return x;
        } else {
            return max(x.right);
        }
    }

    public K successorCopy() {
        return successorCopy(root).key;
    }
    private Node successorCopy(Node x) {
        Node n = max(x.left);
        return new Node(n.key, n.val, 1);
    }

    private Node successorRemove(Node x) {
        Node n = successorCopy(x);
        x = remove(n.key, x);
        return x;
    }

    @Override
    public V remove(K key, V value) {
        if (key == null) throw new IllegalArgumentException("calls remove() with a null key");
        root = remove2(key, value, root);
        return removeStore;
    }

    private Node remove2(K key, V value, Node x) {
        if (x == null) {
            removeStore = null;
            return null;
        }
        int cmp = key.compareTo(x.key);
        if      (cmp < 0) x = remove(key, x.left);
        else if (cmp > 0) x = remove(key, x.right);
        else if (x.val == value){
            removeStore = x.val;
            if (x.right == null && x.left == null) {
                x = null;
            } else if (x.right == null && x.left != null) {
                x = x.left;
            } else if (x.right != null && x.left == null) {
                x = x.right;
            } else {
                Node n = successorCopy(x);
                x = successorRemove(x);
                x.key = n.key;
                x.val = n.val;
            }
        } else {
            removeStore = null;
            return x;
        }
        if (x != null) {
            x.size = size(x.left) + size(x.right) + 1;
        }

        return x;
    }


    @Override
    public Iterator<K> iterator() {
        return new BSTMapIter();
    }

    private class BSTMapIter implements Iterator<K> {
        private Stack<Node> stack = new Stack<>();

        public BSTMapIter() {
            pushLeft(root);
        }

        private void pushLeft(Node node) {
            while (node != null) {
                stack.push(node);
                node = node.left;
            }
        }

        @Override
        public boolean hasNext() {
            return !stack.isEmpty();
        }

        @Override
        public K next() {
            if (!hasNext()) throw new NoSuchElementException();
            Node topNode = stack.pop();
            pushLeft(topNode.right);
            return topNode.key;
        }
    }
}
