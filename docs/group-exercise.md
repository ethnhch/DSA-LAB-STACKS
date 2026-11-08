# Group Exercise — Stack & Queue Menu Program

**Topic:** Stacks and Queues · Lesson 12  
**Faculty / Institution:** Prof. Lester G. Diampoc | UMAK  
**Requirements:**
1. Input first the size of an array (`_maxSize`).
2. Add menu options to choose either: **STACK**, **QUEUE**, or **EXIT**.
3. Add sub-menu options for stack and queue with their corresponding methods:
   - **Stack methods:** `Push`, `Pop`, `IsEmpty`, `IsFull`, `Peek`, `Display`.
   - **Queue methods:** `Enqueue`, `Dequeue`, `Peek`, `IsEmpty`, `IsFull`, `Display`.
4. View array elements based on the selected method.
5. **Note:** The program will not close unless the user chooses EXIT from the menu options.

---

## 1. Architecture & Program Flow

```
Allocate Array Capacity (e.g. 5)
               │
               ▼
     [ STACK | QUEUE | EXIT ]
       │         │        │
       │         │        └── Clean return to Main Menu / Exit
       ▼         ▼
  STACK SUB-MENU  QUEUE SUB-MENU
  • Push          • Enqueue
  • Pop           • Dequeue
  • Peek          • Peek
  • IsEmpty       • IsEmpty
  • IsFull        • IsFull
  • Display       • Display
  • Array View    • Array View
```

Both structures are allocated with the specified shared capacity upon entering the size, preserving their internal state as the user switches between options.

---

## 2. Array Stack Implementation (`core/ArrayStack.java`)
- **Principle:** LIFO (Last-In, First-Out).
- **Pointer:** Single `_top` index (`-1` when empty).
- **Operations:**
  - `Push(item)`: Places item at `_stackArray[++_top]`. Throws `Stack overflow` when full.
  - `Pop()`: Returns and nullifies `_stackArray[_top--]`. Throws `Stack underflow` when empty.
  - `Peek()`: Returns top item without removing it.
  - `IsEmpty()`: `_top == -1`.
  - `IsFull()`: `_top == _maxSize - 1`.

---

## 3. Circular Array Queue Implementation (`core/ArrayQueue.java`)
- **Principle:** FIFO (First-In, First-Out).
- **The "False Overflow" Problem:** In a linear array queue, every `Dequeue` moves `front` forward. Even if earlier slots are vacant, `rear` eventually reaches the end of the array, falsely reporting full. Shifting elements backward takes $O(N)$ time.
- **The Modulo Wrapping Solution:** Using modulo arithmetic (`% _maxSize`), pointers automatically wrap around from index `_maxSize - 1` back to index `0`:
  $$\text{rear} = (\text{rear} + 1) \pmod{\text{maxSize}}$$
  $$\text{front} = (\text{front} + 1) \pmod{\text{maxSize}}$$
- **State Fields:**
  - `_queueArray`: Fixed array of size `_maxSize`.
  - `_front`: Index of the oldest active element.
  - `_rear`: Index of the most recently enqueued element.
  - `_count`: Explicit number of active items (distinguishes empty `_count == 0` from full `_count == _maxSize`).
- **All operations run in strictly $O(1)$ constant time.**

---

## 4. Live Array Memory Visualizer (`ui/ArrayView.java`)
- Draws every memory slot with its index `[0]`, `[1]`, `[2]`, ...
- **Stack:** Purple border and pointer tag **▼ TOP** marks the current top index.
- **Queue:** Sky-blue pointer **▼ FRONT** and emerald-green pointer **▼ REAR**. When both point to the same slot, tagged **▼ FRONT/REAR**.
- Vacated memory slots are cleared (`-`), demonstrating real-time circular wrap-around.

---

## 5. Exit Compliance
Per the lab sheet requirement:
> *"The program will not close unless the user choose EXIT from the menu options."*

The application requires navigating to the menu and selecting the red **EXIT** button to exit the exercise cleanly.

