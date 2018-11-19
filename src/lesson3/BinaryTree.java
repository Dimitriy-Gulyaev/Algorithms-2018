package lesson3;

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

    private Node<T> current = null;

    private int size = 0;

    @Override
    public boolean add(T t) {
        Node<T> closest = find(t);
        int comparison = closest == null ? -1 : t.compareTo(closest.value);
        if (comparison == 0) {
            return false;
        }
        if (closest == null) {
            current = new Node<>(t);
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
        return current == null || checkInvariant(current);
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

    //Трудоёмкость T = O(n), ресурсоёмкость R = O(1), где n - высота дерева.
    @SuppressWarnings("unchecked")
    @Override
    public boolean remove(Object o) {
        T key = (T) o;
        Node<T> toBeRemoved = find(key);
        if (toBeRemoved == null) return false;
        if (toBeRemoved.value.compareTo(key) != 0) return false;
        if (toBeRemoved == current) {
            if (current.right == null && current.left == null) {
                current = null;
            } else if (current.left == null || current.right == null) {
                if (current.left == null) {
                    current = current.right;
                } else {
                    current = current.left;
                }
            } else {
                Node<T> successor = next(current);
                current.value = successor.value;
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
        if (current == null) return null;
        return find(current, value);
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

        private Node<T> current = null;

        private Stack<Node<T>> list = new Stack<>();

        private BinaryTreeIterator() {
            if (BinaryTree.this.current == null)
                throw new NoSuchElementException();
            createStack(BinaryTree.this.current);
        }

        private void createStack(Node<T> node) {
            if (node.right != null)
                createStack(node.right);
            list.add(node);
            if (node.left != null)
                createStack(node.left);
        }

        /**
         * Поиск следующего элемента
         * Средняя
         */

        // Трудоёмкость T = O(n), Ресурсоёмкость R = О(1), где n - высота дерева
        private Node<T> findNext() {
            current = list.pop();
            return current;
        }

        @Override
        public boolean hasNext() {
            list.clear();
            return list.size() > 0;
        }

        @Override
        public T next() {
            if (findNext() == null) throw new NoSuchElementException();
            return current.value;
        }

        /**
         * Удаление следующего элемента
         * Сложная
         */
        @Override
        public void remove() {

            if (current.parent == null) {
                if (BinaryTree.this.current.right == null && BinaryTree.this.current.left == null) {
                    BinaryTree.this.current = null;
                } else if (BinaryTree.this.current.left == null || BinaryTree.this.current.right == null) {
                    if (BinaryTree.this.current.left == null) {
                        BinaryTree.this.current = BinaryTree.this.current.right;
                    } else {
                        BinaryTree.this.current = BinaryTree.this.current.left;
                    }
                } else {
                    Node<T> successor = list.peek();
                    BinaryTree.this.current.value = successor.value;
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
            } else {
                Node<T> parent = current.parent;
                if (current.left == null && current.right == null) {
                    if (parent.left == current)
                        parent.left = null;
                    if (parent.right == current)
                        parent.right = null;
                } else if (current.left == null || current.right == null) {
                    if (current.left == null) {
                        if (parent.left == current) {
                            parent.left = current.right;
                        } else {
                            parent.right = current.right;
                        }
                        current.right.parent = parent;
                    } else {
                        if (parent.left == current)
                            parent.left = current.left;
                        else {
                            parent.right = current.left;
                        }
                        current.left.parent = parent;
                    }
                } else {
                    Node<T> successor = list.peek();
                    current.value = successor.value;
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

    // Трудоёмкость T = O(n), Ресурсоёмкость R = O(m), где n - высота дерева,
    // m - количество элементов в искомом подмножестве
    @NotNull
    @Override
    public SortedSet<T> subSet(T fromElement, T toElement) {
        if (toElement == null || fromElement == null) throw new NullPointerException("element can't be null");
        SortedSet<T> set = new TreeSet<>();
        if (current != null) traversePreOrderSubSet(current, toElement, fromElement, set);
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
        if (current != null) traversePreOrderHead(current, toElement, set);
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
        if (current != null) traversePreOrderTail(current, fromElement, set);
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
        if (current == null) throw new NoSuchElementException();
        Node<T> current = this.current;
        while (current.left != null) {
            current = current.left;
        }
        return current.value;
    }

    @Override
    public T last() {
        if (current == null) throw new NoSuchElementException();
        Node<T> current = this.current;
        while (current.right != null) {
            current = current.right;
        }
        return current.value;
    }
}
