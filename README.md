# Stacks Lab — DSA Finals Laboratory Activity 1

Java 17+ · Swing · no external libraries.

## Run

```bash
javac -d out src/Main.java src/core/*.java src/ui/*.java
java -cp out Main
```

## Structure

```
src/
├── Main.java                 entry point
├── core/                     pure logic, no GUI code
│   ├── ArrayStack.java       Exercise 1, reused everywhere
│   ├── ArrayQueue.java       Group Exercise (circular array)
│   ├── PostfixEvaluator.java Exercise 2
│   ├── InfixToPostfix.java   Exercise 2 (handout, tabulation method)
│   └── DecimalToBinary.java  Exercise 3
└── ui/                       screens and reusable widgets
    ├── AppFrame, MainMenu, Page          navigation
    ├── Ex1Panel, Ex2Panel, Ex3Panel, GroupPanel   one per exercise
    ├── StructurePanel → StackPanel, QueuePanel    shared operation screen
    ├── TracePanel                        shared "input → trace table" screen
    └── Theme, GameButton, Status, ArrayView, TraceTable, SizePanel
```

Design rule: `core` never imports `ui`. Every exercise's algorithm can be explained
(and tested) without opening a window.

## Exercise write-ups

| Exercise | Explanation |
|---|---|
| 1 — Stack operations | [docs/exercise1.md](docs/exercise1.md) |
| 2 — Postfix evaluation + infix-to-postfix | [docs/exercise2.md](docs/exercise2.md) |
| 3 — Decimal to binary | [docs/exercise3.md](docs/exercise3.md) |
| Group — Stack & Queue menu | [docs/group-exercise.md](docs/group-exercise.md) |
