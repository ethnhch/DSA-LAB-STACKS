package core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Fixed-capacity LIFO stack backed by an array.
 * Matches specifications from Prof. Lester G. Diampoc (UMAK) Lab Sheet:
 * Fields: _stackArray, _top, _maxSize.
 * Methods: Push, Pop, Peek, IsEmpty, IsFull, Display.
 */
public class ArrayStack<T> {
    private final Object[] _stackArray;
    private int _top = -1;
    private final int _maxSize;

    public ArrayStack(int maxSize) {
        if (maxSize <= 0) throw new IllegalArgumentException("Size must be positive");
        this._maxSize = maxSize;
        this._stackArray = new Object[maxSize];
    }

    public void push(T item) {
        if (isFull()) throw new IllegalStateException("Stack overflow: the stack is full");
        _stackArray[++_top] = item;
    }

    public void Push(T item) { push(item); }

    public T pop() {
        T item = peek();
        _stackArray[_top--] = null;
        return item;
    }

    public T Pop() { return pop(); }

    @SuppressWarnings("unchecked")
    public T peek() {
        if (isEmpty()) throw new IllegalStateException("Stack underflow: the stack is empty");
        return (T) _stackArray[_top];
    }

    public T Peek() { return peek(); }

    public boolean isEmpty() { return _top == -1; }

    public boolean IsEmpty() { return isEmpty(); }

    public boolean isFull() { return _top == _maxSize - 1; }

    public boolean IsFull() { return isFull(); }

    public int size() { return _top + 1; }

    public int maxSize() { return _maxSize; }

    /** Index of the top element, or -1 when empty. */
    public int top() { return _top; }

    /** Copy of the backing array (unused slots are null). */
    public Object[] cells() { return _stackArray.clone(); }

    public void clear() {
        for (int i = 0; i <= _top; i++) _stackArray[i] = null;
        _top = -1;
    }

    /** Elements from bottom to top. */
    @SuppressWarnings("unchecked")
    public List<T> toList() {
        List<T> items = new ArrayList<>();
        for (int i = 0; i <= _top; i++) items.add((T) _stackArray[i]);
        return items;
    }

    /** Elements from top to bottom. */
    public String display() {
        if (isEmpty()) return "(empty)";
        List<T> items = toList();
        Collections.reverse(items);
        return items.toString();
    }

    public String Display() { return display(); }
}

