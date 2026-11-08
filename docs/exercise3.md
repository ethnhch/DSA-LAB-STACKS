# Exercise 3 — Convert Decimal to Binary Using a Stack

**Topic:** Stacks · Lesson 12  
**Faculty / Institution:** Prof. Lester G. Diampoc | UMAK  
**Objective:** Convert any non-negative decimal (base-10) integer into its binary (base-2) equivalent using a stack.

---

## 1. The Core Question: Why is a Stack Required?
When performing the classical **division-by-2 algorithm**:
- The **first division** produces the remainder corresponding to $2^0$ — this is the **Least Significant Bit (LSB)**, the rightmost digit of the binary number.
- The **last division** produces the remainder corresponding to the highest power of 2 — this is the **Most Significant Bit (MSB)**, the leftmost digit.

Thus, repeated division produces bits in **reverse order** from standard binary notation. Because a Stack is **LIFO (Last-In, First-Out)**, pushing each remainder onto the stack as calculated and subsequently popping them off when division concludes **automatically reverses the sequence** into correct MSB-to-LSB order without requiring string reversing or array manipulation.

---

## 2. Two-Phase Algorithm (`core/DecimalToBinary.java`)

### Phase 1: Division & Pushing Remainders
1. Take current dividend $N$.
2. Compute quotient $Q = \lfloor N / 2 \rfloor$ and remainder $R = N \pmod 2$ (either `0` or `1`).
3. **Push $R$ onto the stack**.
4. Set $N = Q$ and repeat while $N > 0$.

### Phase 2: Popping to Construct Binary String
1. While the stack is not empty:
   - $B = \text{stack.pop()}$
   - Append $B$ to the binary result string.
2. The resulting string is the final binary equivalent.

### Edge Case: Decimal `0`
Input `0` does not enter the $N > 0$ loop. Per the lab sheet specifications:
- Push `0` explicitly onto the stack.
- Pop `0` from the stack $\to$ output `"0"`.

---

## 3. Worked Example: Decimal `13`

### Phase 1: Division & Pushing
1. $13 / 2 = 6$, remainder **`1`** $\to$ Push `1` onto stack. *(Stack: `[1]`)*
2. $6 / 2 = 3$, remainder **`0`** $\to$ Push `0` onto stack. *(Stack: `[1, 0]`)*
3. $3 / 2 = 1$, remainder **`1`** $\to$ Push `1` onto stack. *(Stack: `[1, 0, 1]`)*
4. $1 / 2 = 0$, remainder **`1`** $\to$ Push `1` onto stack. *(Stack: `[1, 0, 1, 1]`)*

*Division halts because quotient reached 0. Stack top holds MSB (`1`).*

### Phase 2: Popping & Output Construction
5. Pop `1` $\to$ binary string: `"1"`. *(Stack: `[1, 0, 1]`)*
6. Pop `1` $\to$ binary string: `"11"`. *(Stack: `[1, 0]`)*
7. Pop `0` $\to$ binary string: `"110"`. *(Stack: `[1]`)*
8. Pop `1` $\to$ binary string: `"1101"`. *(Stack: `[]` empty)*

**Final Binary Equivalent:** **`1101`**  
Verification: $1(2^3) + 1(2^2) + 0(2^1) + 1(2^0) = 8 + 4 + 0 + 1 = 13_{10}$.

---

## 4. UI/UX & Interactive Stepper
- **Non-automatic Preset Loading:** Clicking preset numbers (`13`, `0`, `1`, `8`, `255`, `1024`) loads the value without auto-running.
- **Phase-by-Phase Stepping:**
  - `Step >` button advances division-by-division during Phase 1 (watching the stack grow with remainders).
  - Continues step-by-step through Phase 2 (watching remainders pop off the stack as the binary string forms).
- **Live Memory Visualizer:** Shows the bits currently in stack memory with the **▼ TOP** pointer moving in real time.
- **Trace Table:** Records Step, Phase, Arithmetic, Remainder/Bit, Progressive Binary Output, and Stack State.

