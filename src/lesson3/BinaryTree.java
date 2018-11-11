package lesson3;

import kotlin.NotImplementedError;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

// Attention: comparable supported but comparator is not
@SuppressWarnings("WeakerAccess")
public class BinaryTree<T extends Comparable<T>> extends AbstractSet<T> implements CheckableSortedSet<T> {

    private static class Node<T> {
        final T value;

        Node<T> left = null;

        Node<T> right = null;

        Node(T value) {
            this.value = value;
        }
    }

    private Node<T> root = null;

    private int size = 0;

    private SortedSet<T> treeSubSet;

    @Override
    public boolean add(T t) {
        Node<T> closest = find(t);
        int comparison = closest == null ? -1 : t.compareTo(closest.value);
        if (comparison == 0) {
            return false;
        }
        Node<T> newNode = new Node<>(t);
        if (closest == null) {
            root = newNode;
        } else if (comparison < 0) {
            assert closest.left == null;
            closest.left = newNode;
            if (treeSubSet != null) ; //TODO
        } else {
            assert closest.right == null;
            closest.right = newNode;
        }
        size++;
        return true;
    }

    public boolean checkInvariant() {
        return root == null || checkInvariant(root);
    }

    private boolean checkInvariant(Node<T> node) {
        Node<T> left = node.left;
        if (left != null && (left.value.compareTo(node.value) >= 0 || !checkInvariant(left))) return false;
        Node<T> right = node.right;
        return right == null || right.value.compareTo(node.value) > 0 && checkInvariant(right);
    }

    /**
     * Удаление элемента в дереве
     * Средняя
     */
    @SuppressWarnings("unchecked")
    @Override
    public boolean remove(Object o) { //TODO
        if (o == null) throw new IllegalArgumentException("can't remove null");
        if (this.treeSubSet != null) this.treeSubSet.remove(o);
        T t = (T) o;
        Node<T> closest = find(t);
        int comparison = closest.value == null ? -1 : t.compareTo(closest.value);
        if (comparison != 0) return false;
        else {
            if (t.compareTo(root.value) > 0) {
                closest = closest.left;
                return true;
            } else {
                closest = closest.right;
                return true;
            }
        }
    }

    @Override
    public boolean contains(Object o) {
        @SuppressWarnings("unchecked")
        T t = (T) o;
        Node<T> closest = find(t);
        return closest != null && t.compareTo(closest.value) == 0;
    }

    private Node<T> find(T value) {
        if (root == null) return null;
        return find(root, value);
    }

    private Node<T> find(Node<T> start, T value) {
        int comparison = value.compareTo(start.value);
        if (comparison == 0) {
            return start;
        } else if (comparison < 0) {
            if (start.left == null) return start;
            return find(start.left, value);
        } else {
            if (start.right == null) return start;
            return find(start.right, value);
        }
    }

    public class BinaryTreeIterator implements Iterator<T> { //TODO

        private Node<T> current;

        private BinaryTreeIterator() {
            current = root;
        }

        private Node<T> iteratorRoot;

        /**
         * Поиск следующего элемента
         * Средняя
         */
        private Node<T> findNext() {
            if (current.value == null) throw new NullPointerException();
            if (current.right != null) {
                return leftMostTreeNode(current.right);
            }
            Node<T> successor = null;
            while (iteratorRoot != null) {
                if (iteratorRoot.value.compareTo(current.value) > 0) {
                    successor = iteratorRoot;
                    iteratorRoot = iteratorRoot.left;
                } else if (iteratorRoot.value.compareTo(current.value) < 0) {
                    iteratorRoot = iteratorRoot.right;
                } else {
                    return successor;
                }
            }
            return null;
        }

        public Node<T> leftMostTreeNode(Node<T> current) {
            while (current.left != null) {
                current = current.left;
            }
            return current;
        }


        @Override
        public boolean hasNext() {
            return current != null;
        }

        @Override
        public T next() {
            current = findNext();
            if (current == null) throw new NoSuchElementException();
            return current.value;
        }

        /**
         * Удаление следующего элемента
         * Сложная
         */
        @Override
        public void remove() {
            if (this.hasNext()) {
                Node<T> current = this.findNext();
                int comparison = current.value.compareTo(root.value);
                if (comparison < 0) {
                    current = current.right;
                } else current = current.left;
            }
        }
    }

    @NotNull
    @Override
    public Iterator<T> iterator() {
        return new BinaryTreeIterator();
    }

    @Override
    public int size() {
        return size;
    }


    @Nullable
    @Override
    public Comparator<? super T> comparator() {
        return null;
    }

    /**
     * Для этой задачи нет тестов (есть только заготовка subSetTest), но её тоже можно решить и их написать
     * Очень сложная
     */

    // Трудоёмкость T = O(n), Ресурсоёмкость R = O(m), где n - количество элементов в дереве,
    // m - количество элементов в искомом подмножестве
    @NotNull
    @Override
    public SortedSet<T> subSet(T fromElement, T toElement) {
        if (toElement == null || fromElement == null) throw new NullPointerException("element can't be null");
        SortedSet<T> set = new TreeSet<>();
        if (root != null) traversePreOrderSubSet(root, toElement, fromElement, set);
        this.treeSubSet = set;
        return set;
    }

    public void traversePreOrderSubSet(Node<T> node, T toElement, T fromElement, SortedSet<T> set) {
        if (node != null) {
            if (node.value.compareTo(toElement) < 0 && node.value.compareTo(fromElement) >= 0) set.add(node.value);
            traversePreOrderSubSet(node.left, toElement, fromElement, set);
            traversePreOrderSubSet(node.right, toElement, fromElement, set);
        }
    }

    /**
     * Найти множество всех элементов меньше заданного
     * Сложная
     */

    // Трудоёмкость T = O(n), Ресурсоёмкость R = O(m), где n - количество элементов в дереве,
    // m - количество элементов в искомом подмножестве
    @NotNull
    @Override
    public SortedSet<T> headSet(T toElement) {
        if (toElement == null) throw new NullPointerException("element can't be null");
        SortedSet<T> set = new TreeSet<>();
        if (root != null) traversePreOrderHead(root, toElement, set);
        this.treeSubSet = set;
        return set;
    }

    public void traversePreOrderHead(Node<T> node, T toElement, SortedSet<T> set) {
        if (node != null) {
            if (node.value.compareTo(toElement) < 0) set.add(node.value);
            traversePreOrderHead(node.left, toElement, set);
            traversePreOrderHead(node.right, toElement, set);
        }
    }

    /**
     * Найти множество всех элементов больше или равных заданного
     * Сложная
     */

    // Трудоёмкость T = O(n), Ресурсоёмкость R = O(m), где n - количество элементов в дереве,
    // m - количество элементов в искомом подмножестве
    @NotNull
    @Override
    public SortedSet<T> tailSet(T fromElement) {
        if (fromElement == null) throw new NullPointerException("element can't be null");
        SortedSet<T> set = new TreeSet<>();
        if (root != null) traversePreOrderTail(root, fromElement, set);
        this.treeSubSet = set;
        return set;
    }

    public void traversePreOrderTail(Node<T> node, T fromElement, SortedSet<T> set) {
        if (node != null) {
            if (node.value.compareTo(fromElement) >= 0) set.add(node.value);
            traversePreOrderTail(node.left, fromElement, set);
            traversePreOrderTail(node.right, fromElement, set);
        }
    }


    @Override
    public T first() {
        if (root == null) throw new NoSuchElementException();
        Node<T> current = root;
        while (current.left != null) {
            current = current.left;
        }
        return current.value;
    }

    @Override
    public T last() {
        if (root == null) throw new NoSuchElementException();
        Node<T> current = root;
        while (current.right != null) {
            current = current.right;
        }
        return current.value;
    }
}
