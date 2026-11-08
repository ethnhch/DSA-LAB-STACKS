package ui;

import core.ArrayStack;
import java.awt.Color;

/** Interactive array stack: Push, Pop, Peek, IsEmpty, IsFull, Display, Clear. */
public class StackPanel extends StructurePanel {
    private final ArrayStack<Integer> stack;

    public StackPanel(int size) {
        this(size, null);
    }

    public StackPanel(int size, Runnable onChangeSize) {
        super(onChangeSize);
        stack = new ArrayStack<>(size);
        triggerOnEnter(op("Push", () -> {
            int item = input();
            stack.push(item);
            return "Pushed " + item + " onto stack (top index = " + stack.top() + ", size = " + stack.size() + "/" + stack.maxSize() + ")";
        }, Theme.ACCENT));
        op("Pop", () -> "Popped " + stack.pop() + " from stack (new top index = " + stack.top() + ")", Theme.CELL);
        op("Peek", () -> "Top element = " + stack.peek() + " (at index " + stack.top() + ")", Theme.CELL);
        op("IsEmpty", () -> "isEmpty() -> " + stack.isEmpty() + " (count = " + stack.size() + ")", Theme.CELL);
        op("IsFull", () -> "isFull() -> " + stack.isFull() + " (capacity = " + stack.maxSize() + ")", Theme.CELL);
        op("Display", () -> "Stack elements (top -> bottom): " + stack.display(), Theme.CELL);
        op("Clear", () -> {
            stack.clear();
            return "Cleared all elements from the stack.";
        }, new Color(0x331822));
        refresh();
    }

    @Override protected Object[] cells() { return stack.cells(); }

    @Override protected void mark(String[] tags) {
        if (!stack.isEmpty()) tags[stack.top()] = "TOP";
    }

    @Override protected void updateCount() {
        String state = stack.isFull() ? " [FULL]" : (stack.isEmpty() ? " [EMPTY]" : "");
        setCountText("Capacity: " + stack.maxSize() + "   |   Stack Items: " + stack.size() + "   |   _top Index: " + stack.top() + state);
    }
}


