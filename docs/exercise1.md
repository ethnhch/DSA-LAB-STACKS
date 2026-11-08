
# Exercise 1 — Implementation of Stack Operations

**Topic:** Stacks · Lesson 12  
**Faculty / Institution:** Prof. Lester G. Diampoc | UMAK  
**Objective:** Create a stack data structure from scratch using an array. Implement basic stack operations: `Push(int item)`, `Pop()`, `Peek()`, `IsEmpty()`, `IsFull()`, and `Display()`.

---

## 1. Concept & Background
A stack is a linear data structure following the **LIFO (Last-In, First-Out)** principle: the last element inserted is the first element removed. All operations happen at a single designated end: the **top**.

Common real-world applications:
- Function call runtime stack (activation records & recursion)
- Undo/Redo operations in text editors
- Expression evaluation and syntax parsing
- Depth-First Search (DFS) and backtracking algorithms

---

## 2. Specification & Implementation (`core/ArrayStack.java`)

As mandated by the laboratory sheet, the `ArrayStack` class defines the following fields and methods:

| Member | Type | Role |
|---|---|---|
| `_stackArray` | `Object[]` | Contiguous array storage for stack elements |
| `_top` | `int` | Zero-based index of top element (`-1` when empty) |
| `_maxSize` | `int` | Maximum capacity of the allocated array |
| `Push(item)` | `void` | Adds item to `_stackArray[++_top]`; throws on overflow |
| `Pop()` | `T` | Reads and clears `_stackArray[_top--]`; throws on underflow |
| `Peek()` | `T` | Returns top element `_stackArray[_top]` without removal |
| `IsEmpty()` | `boolean` | Returns `true` if `_top == -1` |
| `IsFull()` | `boolean` | Returns `true` if `_top == _maxSize - 1` |
| `Display()` | `String` | Formats all elements from top to bottom |

---

## 3. Complexity & Architectural Highlights
- **Time Complexity:** Every operation (`Push`, `Pop`, `Peek`, `IsEmpty`, `IsFull`) runs in strictly **$O(1)$ constant time**.
- **Space Complexity:** Fixed contiguous **$O(N)$ memory**, eliminating pointer overhead.
- **Defensive Error Handling:** Overflow and underflow conditions throw typed `IllegalStateException` instances rather than silently printing to standard output. The GUI cleanly catches these exceptions to display responsive error notifications.
- **Memory Hygiene:** On `Pop()`, the vacated slot in `_stackArray` is explicitly set to `null` so the live visualizer precisely reflects memory state without ghost references.

---

## 4. UI/UX & Live Demonstration (`ui/Ex1Panel`, `ui/StackPanel`)
1. **Capacity Setup:** The user inputs `_maxSize` (e.g. 5) on the initial screen.
2. **Interactive Controls:** Individual buttons for `Push`, `Pop`, `Peek`, `IsEmpty`, `IsFull`, `Display`, and `Clear`.
3. **Live Memory Visualizer:** Custom `ArrayView` renders each memory cell with its zero-based index `[0]`, `[1]`, etc. The current `_top` index is highlighted with a cyan border and tagged with a **▼ TOP** pointer badge.
4. **Resizing:** A "Change Size" button allows returning to the configuration screen without restarting the app.

---

## 5. Defense Demo Script
1. Enter size **5** → Allocate.
2. Push values: `10`, `20`, `30`. Note pointer `▼ TOP` moving to index `2`.
3. Click `Peek` → returns `30` at index `2`.
4. Click `Pop` → returns `30`, `▼ TOP` retreats to index `1`.
5. Check `IsEmpty` (`false`) and `IsFull` (`false`).
6. Push until full (`40`, `50`, `60`) → slot 4 tagged `TOP`.
7. Attempt an extra `Push` → triggers red **Stack overflow** warning.
8. Pop until empty → attempting an extra `Pop` triggers **Stack underflow** warning.

