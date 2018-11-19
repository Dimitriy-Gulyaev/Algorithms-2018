package lesson3;

import kotlin.NotImplementedError;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

// Attention: comparable supported but comparator is not
@SuppressWarnings("WeakerAccess")
public class BinaryTree<T extends Comparable<T>> extends AbstractSet<T> implements CheckableSortedSet<T> {

    private static class Node<T> {
        T value;

        Node<T> left = null;

        Node<T> right = null;

        Node<T> parent = null;

        Node(T value) {
            this.value = value;
        }

        Node(T value, Node<T> parent) {
            this.value = value;
            this.parent = parent;
        }
    }

    private Node<T> root = null;

    private int size = 0;

    @Override
    public boolean add(T t) {
        Node<T> closest = find(t);
        int comparison = closest == null ? -1 : t.compareTo(closest.value);
        if (comparison == 0) {
            return false;
        }
        if (closest == null) {
            root = new Node<>(t);
        } else if (comparison < 0) {
            assert closest.left == null;
            closest.left = new Node<>(t, closest);
        } else {
            assert closest.right == null;
            closest.right = new Node<>(t, closest);
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
    public boolean remove(Object o) {
        T key = (T) o;
        return remove(key);
    }

    private boolean remove(T key) {
        Node<T> toBeRemoved = find(key);
        if (toBeRemoved == null) return false;
        if (toBeRemoved.value.compareTo(key) != 0) return false;
        if (toBeRemoved == root) {
            if (root.right == null && root.left == null) {
                root = null;
            } else if (root.left == null || root.right == null) {
                if (root.left == null) {
                    root = root.right;
                } else {
                    root = root.left;
                }
            } else {
                Node<T> successor = next(root);
                root.value = successor.value;
                if (successor.parent.left == successor) {
                    successor.parent.left = successor.right;
                    if (successor.right != null)
                        successor.right.parent = successor.parent;
                } else {
                    successor.parent.right = successor.right;
                    if (successor.right != null)
                        successor.right.parent = successor.parent;
                }
            }
            size--;
            return true;
        }
        Node<T> parent = toBeRemoved.parent;
        if (toBeRemoved.left == null && toBeRemoved.right == null) {
            if (parent.left == toBeRemoved)
                parent.left = null;
            if (parent.right == toBeRemoved)
                parent.right = null;
        } else if (toBeRemoved.left == null || toBeRemoved.right == null) {
            if (toBeRemoved.left == null) {
                if (parent.left == toBeRemoved) {
                    parent.left = toBeRemoved.right;
                } else {
                    parent.right = toBeRemoved.right;
                }
                toBeRemoved.right.parent = parent;
            } else {
                if (parent.left == toBeRemoved)
                    parent.left = toBeRemoved.left;
                else {
                    parent.right = toBeRemoved.left;
                }
                toBeRemoved.left.parent = parent;
            }
        } else {
            Node<T> successor = next(toBeRemoved);
            toBeRemoved.value = successor.value;
            if (successor.parent.left == successor) {
                successor.parent.left = successor.right;
                if (successor.right != null)
                    successor.right.parent = successor.parent;
            } else {
                successor.parent.right = successor.right;
                if (successor.right != null)
                    successor.right.parent = successor.parent;
            }
        }
        size--;
        return true;
    }

    private Node<T> next(Node<T> current) {
        if (current.right != null)
            return minimum(current.right);
        Node<T> parent = current.parent;
        while (parent != null && current == parent.right) {
            current = parent;
            parent = parent.parent;
        }
        return parent;
    }

    private Node<T> minimum(Node<T> current) {
        if (current == null) return null;
        while (current.left != null)
            current = current.left;
        return current;
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

    public class BinaryTreeIterator implements Iterator<T> {

        private Node<T> current;

        boolean isFirstIteration;

        private BinaryTreeIterator() {
            if (root == null)
                return;
            current = mostLeftElement(root);
            isFirstIteration = true;
        }

        private Node<T> mostLeftElement(Node<T> node) {
            while (node.left != null) node = node.left;
            return node;
        }

        /**
         * Поиск следующего элемента
         * Средняя
         */

        // Трудоёмкость Т = О(n), где n - высота дерева; Ресурсоёмкость R = O(1)
        private Node<T> findNext() {
            if (isFirstIteration) {
                isFirstIteration = false;
                return current;
            }
            if (current == root && root.right == null) return null;
            if (current.right != null) {
                current = current.right;
                while (current.left != null)
                    current = current.left;
                return current;
            } else {
                while (true) {
                    if (current.parent.left == current) {
                        current = current.parent;
                        return current;
                    } else {
                        current = current.parent;
                    }
                }
            }
        }

        @Override
        public boolean hasNext() {
            return findNext() != null;
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
            if (current == null)
                throw new NoSuchElementException();
            BinaryTree.this.remove(current.value);
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

    // Трудоёмкость T = O(n), Ресурсоёмкость R = O(m), где n - высота дерева,
    // m - количество элементов в искомом подмножестве
    @NotNull
    @Override
    public SortedSet<T> subSet(T fromElement, T toElement) {
        if (toElement == null || fromElement == null) throw new NullPointerException("element can't be null");
        SortedSet<T> set = new TreeSet<>();
        if (root != null) traversePreOrderSubSet(root, toElement, fromElement, set);
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

    // Трудоёмкость T = O(n), Ресурсоёмкость R = O(m), где n - высота дерева,
    // m - количество элементов в искомом подмножестве
    @NotNull
    @Override
    public SortedSet<T> headSet(T toElement) {
        if (toElement == null) throw new NullPointerException("element can't be null");
        SortedSet<T> set = new TreeSet<>();
        if (root != null) traversePreOrderHead(root, toElement, set);
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

    // Трудоёмкость T = O(n), Ресурсоёмкость R = O(m), где n - высота дерева,
    // m - количество элементов в искомом подмножестве
    @NotNull
    @Override
    public SortedSet<T> tailSet(T fromElement) {
        if (fromElement == null) throw new NullPointerException("element can't be null");
        SortedSet<T> set = new TreeSet<>();
        if (root != null) traversePreOrderTail(root, fromElement, set);
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
