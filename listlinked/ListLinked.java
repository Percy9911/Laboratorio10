package listlinked;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

public class ListLinked<E> implements Iterable<E> {
    private Node<E> first;
    private Node<E> last;
    private int size;

    public ListLinked() {
        first = null;
        last = null;
        size = 0;
    }

    public boolean isEmpty() {
        return first == null;
    }

    public int size() {
        return size;
    }

    public Node<E> getFirst() {
        return first;
    }

    public Node<E> getLast() {
        return last;
    }

    public E get(int index) {
        return getNode(index).getData();
    }

    public E set(int index, E data) {
        Node<E> node = getNode(index);
        E oldData = node.getData();
        node.setData(data);
        return oldData;
    }

    public void insertFirst(E data) {
        Node<E> newNode = new Node<>(data, first);
        first = newNode;

        if (last == null) {
            last = newNode;
        }

        size++;
    }

    public void addFirst(E data) {
        insertFirst(data);
    }

    public void insertLast(E data) {
        Node<E> newNode = new Node<>(data);

        if (isEmpty()) {
            first = newNode;
        } else {
            last.setNext(newNode);
        }

        last = newNode;
        size++;
    }

    public void addLast(E data) {
        insertLast(data);
    }

    public void insert(int index, E data) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }

        if (index == 0) {
            insertFirst(data);
            return;
        }

        if (index == size) {
            insertLast(data);
            return;
        }

        Node<E> previous = getNode(index - 1);
        previous.setNext(new Node<>(data, previous.getNext()));
        size++;
    }

    public E removeFirst() {
        if (isEmpty()) {
            throw new NoSuchElementException("The list is empty");
        }

        E data = first.getData();
        first = first.getNext();

        if (first == null) {
            last = null;
        }

        size--;
        return data;
    }

    public E removeLast() {
        if (isEmpty()) {
            throw new NoSuchElementException("The list is empty");
        }

        if (first == last) {
            return removeFirst();
        }

        Node<E> previous = first;

        while (previous.getNext() != last) {
            previous = previous.getNext();
        }

        E data = last.getData();
        previous.setNext(null);
        last = previous;
        size--;
        return data;
    }

    public boolean remove(E data) {
        if (isEmpty()) {
            return false;
        }

        if (Objects.equals(first.getData(), data)) {
            first = first.getNext();

            if (first == null) {
                last = null;
            }

            size--;
            return true;
        }

        Node<E> previous = first;
        Node<E> current = first.getNext();

        while (current != null) {
            if (Objects.equals(current.getData(), data)) {
                previous.setNext(current.getNext());

                if (current == last) {
                    last = previous;
                }

                size--;
                return true;
            }

            previous = current;
            current = current.getNext();
        }

        return false;
    }

    public E removeAt(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }

        if (index == 0) {
            return removeFirst();
        }

        Node<E> previous = getNode(index - 1);
        Node<E> current = previous.getNext();
        E data = current.getData();

        previous.setNext(current.getNext());

        if (current == last) {
            last = previous;
        }

        size--;
        return data;
    }

    public E search(E data) {
        Node<E> node = searchNode(data);
        return node == null ? null : node.getData();
    }

    public boolean contains(E data) {
        return searchNode(data) != null;
    }

    public int indexOf(E data) {
        Node<E> current = first;
        int index = 0;

        while (current != null) {
            if (Objects.equals(current.getData(), data)) {
                return index;
            }

            current = current.getNext();
            index++;
        }

        return -1;
    }

    private Node<E> searchNode(E data) {
        Node<E> current = first;

        while (current != null) {
            if (Objects.equals(current.getData(), data)) {
                return current;
            }

            current = current.getNext();
        }

        return null;
    }

    public void clear() {
        first = null;
        last = null;
        size = 0;
    }

    private Node<E> getNode(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }

        Node<E> current = first;

        for (int i = 0; i < index; i++) {
            current = current.getNext();
        }

        return current;
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            private Node<E> current = first;

            @Override
            public boolean hasNext() {
                return current != null;
            }

            @Override
            public E next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }

                E data = current.getData();
                current = current.getNext();
                return data;
            }
        };
    }

    @Override
    public String toString() {
        StringBuilder text = new StringBuilder("[");
        Node<E> current = first;

        while (current != null) {
            text.append(current.getData());
            current = current.getNext();

            if (current != null) {
                text.append(", ");
            }
        }

        text.append("]");
        return text.toString();
    }
}
