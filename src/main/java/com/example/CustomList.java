package com.example;

import java.util.*;

public class CustomList<T> implements List<T> {
    private static final int DEFAULT_CAPACITY = 10;
    private T[] elements;
    private int size;
    private int capacity;

    public CustomList() {
        defaultConstructor(DEFAULT_CAPACITY);
    }

    public CustomList(int initCapacity) {
        defaultConstructor(initCapacity);
    }


    private void defaultConstructor(int initCapacity) {
        if (initCapacity <= 0) {
            throw new IllegalArgumentException("Capacity must be greater than 0");
        }
        size = 0;
        capacity = initCapacity;
        elements = (T[]) new Object[initCapacity];
    }

    private void checkCapacity(int targetCapacity) {
        if (targetCapacity > capacity) {
            increaseCapacity();
        }
    }

    private void increaseCapacity() {
        if (capacity < DEFAULT_CAPACITY) {
            capacity = DEFAULT_CAPACITY;
        } else {
            capacity = capacity + (capacity >> 1);
        }

        elements = Arrays.copyOf(elements, capacity);
    }

    private void checkIndex(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException(String.format("Invalid index: %d. List size: %d", index, size));
        }
    }

    @Override
    public boolean add(T obj) {
        checkCapacity(size + 1);
        elements[size] = obj;
        size++;
        return true;
    }

    @Override
    public void add(int index, T element) {
        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException(String.format("Invalid index: %d. List size: %d", index, size));
        }
        checkCapacity(size + 1);

        for (int i = size; i > index; i--) {
            elements[i] = elements[i - 1];
        }
        elements[index] = element;
        size++;
    }

    @Override
    public T get(int index) {
        checkIndex(index);
        return elements[index];
    }

    @Override
    public T set(int index, T element) {
        checkIndex(index);
        T prevElement = elements[index];
        elements[index] = element;
        return prevElement;
    }

    @Override
    public T remove(int index) {
        checkIndex(index);
        T removed = elements[index];

        for (int i = index; i < size - 1; i++) {
            elements[i] = elements[i + 1];
        }
        elements[size-1] = null;
        size--;

        return removed;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public void clear() {
        Arrays.fill(elements, 0, size, null);
        size = 0;
    }

    @Override
    public boolean contains(Object o) {
        return false;
    }

    @Override
    public Iterator iterator() {
        return null;
    }

    @Override
    public Object[] toArray() {
        return new Object[0];
    }

    @Override
    public Object[] toArray(Object[] a) {
        return new Object[0];
    }

    @Override
    public boolean remove(Object o) {
        return false;
    }

    @Override
    public boolean containsAll(Collection c) {
        return false;
    }

    @Override
    public boolean addAll(Collection c) {
        return false;
    }

    @Override
    public boolean addAll(int index, Collection c) {
        return false;
    }

    @Override
    public boolean removeAll(Collection c) {
        return false;
    }

    @Override
    public boolean retainAll(Collection c) {
        return false;
    }

    @Override
    public int indexOf(Object o) {
        return 0;
    }

    @Override
    public int lastIndexOf(Object o) {
        return 0;
    }

    @Override
    public ListIterator listIterator() {
        return null;
    }

    @Override
    public ListIterator listIterator(int index) {
        return null;
    }

    @Override
    public List subList(int fromIndex, int toIndex) {
        return List.of();
    }
}
