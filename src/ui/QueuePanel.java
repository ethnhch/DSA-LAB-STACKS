package ui;

import core.ArrayQueue;
import java.awt.Color;

/** Interactive circular-array queue: Enqueue, Dequeue, Peek, IsEmpty, IsFull, Display, Clear. */
public class QueuePanel extends StructurePanel {
    private final ArrayQueue<Integer> queue;

    public QueuePanel(int size) {
        this(size, null);
    }

    public QueuePanel(int size, Runnable onChangeSize) {
        super(onChangeSize);
        queue = new ArrayQueue<>(size);
        triggerOnEnter(op("Enqueue", () -> {
            int item = input();
            queue.enqueue(item);
            return "Enqueued " + item + " at rear index " + queue.rearIndex() + " (items = " + queue.count() + "/" + queue.maxSize() + ")";
        }, Theme.ACCENT));
        op("Dequeue", () -> "Dequeued " + queue.dequeue() + " from front (new front index = " + queue.frontIndex() + ")", Theme.CELL);
        op("Peek", () -> "Front element = " + queue.peek() + " (at front index " + queue.frontIndex() + ")", Theme.CELL);
        op("IsEmpty", () -> "isEmpty() -> " + queue.isEmpty() + " (count = " + queue.count() + ")", Theme.CELL);
        op("IsFull", () -> "isFull() -> " + queue.isFull() + " (capacity = " + queue.maxSize() + ")", Theme.CELL);
        op("Display", () -> "Queue elements (front -> rear): " + queue.display(), Theme.CELL);
        op("Clear", () -> {
            queue.clear();
            return "Cleared all elements from the queue.";
        }, new Color(0x331822));
        refresh();
    }

    @Override protected Object[] cells() { return queue.cells(); }

    @Override protected void mark(String[] tags) {
        if (queue.isEmpty()) return;
        int front = queue.frontIndex();
        int rear = queue.rearIndex();
        if (front == rear) {
            tags[front] = "FRONT/REAR";
        } else {
            tags[front] = "FRONT";
            tags[rear] = "REAR";
        }
    }

    @Override protected void updateCount() {
        String state = queue.isFull() ? " [FULL]" : (queue.isEmpty() ? " [EMPTY]" : "");
        setCountText("Capacity: " + queue.maxSize() + "   |   Queue Items: " + queue.count() + "   |   _front: " + queue.frontIndex() + "   |   _rear: " + queue.rearIndex() + state);
    }
}


