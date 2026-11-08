package core;

import java.util.ArrayList;
import java.util.List;

/**
 * Fixed-capacity FIFO queue backed by a circular array using modulo arithmetic.
 * Fields: _queueArray, _front, _rear, _count, _maxSize.
 * Methods: Enqueue, Dequeue, Peek, IsEmpty, IsFull, Display.
 */
public class ArrayQueue<T> {
    private final Object[] _queueArray;
    private int _front = 0, _rear = -1, _count = 0;
    private final int _maxSize;

    public ArrayQueue(int maxSize) {
        if (maxSize <= 0) throw new IllegalArgumentException("Size must be positive");
        this._maxSize = maxSize;
        this._queueArray = new Object[maxSize];
    }

    public void enqueue(T item) {
        if (isFull()) throw new IllegalStateException("Queue overflow: the queue is full");
        _rear = (_rear + 1) % _maxSize;
        _queueArray[_rear] = item;
        _count++;
    }

    public void Enqueue(T item) { enqueue(item); }

    public T dequeue() {
        T item = peek();
        _queueArray[_front] = null;
        _front = (_front + 1) % _maxSize;
        _count--;
        return item;
    }

    public T Dequeue() { return dequeue(); }

    @SuppressWarnings("unchecked")
    public T peek() {
        if (isEmpty()) throw new IllegalStateException("Queue underflow: the queue is empty");
        return (T) _queueArray[_front];
    }

    public T Peek() { return peek(); }

    public boolean isEmpty() { return _count == 0; }

    public boolean IsEmpty() { return isEmpty(); }

    public boolean isFull() { return _count == _maxSize; }

    public boolean IsFull() { return isFull(); }

    public int count() { return _count; }

    public int maxSize() { return _maxSize; }

    /** Index of the front element, or -1 when empty. */
    public int frontIndex() { return isEmpty() ? -1 : _front; }

    /** Index of the rear element, or -1 when empty. */
    public int rearIndex() { return isEmpty() ? -1 : _rear; }

    /** Copy of the backing array (unused slots are null). */
    public Object[] cells() { return _queueArray.clone(); }

    public void clear() {
        for (int i = 0; i < _maxSize; i++) _queueArray[i] = null;
        _front = 0;
        _rear = -1;
        _count = 0;
    }

    /** Elements from front to rear. */
    @SuppressWarnings("unchecked")
    public String display() {
        if (isEmpty()) return "(empty)";
        List<T> items = new ArrayList<>();
        for (int i = 0; i < _count; i++) items.add((T) _queueArray[(_front + i) % _maxSize]);
        return items.toString();
    }

    public String Display() { return display(); }
}

