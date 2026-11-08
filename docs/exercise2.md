# Exercise 2 — Postfix Expressions

**Topic:** Stacks · Lesson 12  
**Faculty / Institution:** Prof. Lester G. Diampoc | UMAK  
**Objectives:**
1. **Primary Lab Activity:** Evaluate a postfix (Reverse Polish Notation) mathematical expression using a stack.
2. **Tabulation Method:** Convert infix expressions to postfix following the exact rules from Prof. Diampoc's handout.

---

## Part A — Evaluate a Postfix Expression (`core/PostfixEvaluator.java`)

### 1. Algorithm & Rules
Scan the expression strictly from left to right. Tokens are delimited by commas and/or spaces:
1. **Operand (Number):** Recognizes 1, 2, or more digits (e.g. `10` is recognized as a single number `10`, not two separate digits `1` and `0`). Push onto the stack.
2. **Operator (`+ - * / $ ^`):** Pop two operands from the stack:
   - First popped is the **right** operand ($b$).
   - Second popped is the **left** operand ($a$).
   - Apply operator: $r = a \text{ op } b$ (for exponentiation `$` or `^`, $r = a^b = \text{Math.pow}(a, b)$).
   - Push result $r$ back onto the stack.
3. **Termination:** When all tokens are scanned, the stack contains exactly one value: the evaluated answer.

### 2. Multi-Digit & Comma-Separated Examples
- `10, 5, +` $\to$ Push `10`, push `5`, pop `5` and `10`, calculate $10 + 5 = 15$ $\to$ **`15`**
- `10, 2, $` $\to$ Push `10`, push `2`, calculate $10^2 = 100$ $\to$ **`100`**
- `2, 10, $` $\to$ Push `2`, push `10`, calculate $2^{10} = 1024$ $\to$ **`1024`**
- `100, 20, /, 3, +` $\to$ $100 / 20 = 5$; $5 + 3 = 8$ $\to$ **`8`**
- `10, 5, +, 2, $` $\to$ $(10 + 5)^2 = 15^2$ $\to$ **`225`**
- `5, 3, +, 8, 2, -, *` $\to$ $(5 + 3) \times (8 - 2) = 8 \times 6$ $\to$ **`48`**

### 3. Trace of `10, 5, +, 2, $`

| Step | Symbol | Symbol Type | Action / Evaluation | Stack (bottom -> top) |
|:---:|:---:|:---:|---|:---:|
| 1 | `10` | Operand (10) | Push `10` onto stack | `10` |
| 2 | `5` | Operand (5) | Push `5` onto stack | `10, 5` |
| 3 | `+` | Operator (+) | Pop `5` and `10`; calculate $10 + 5 = 15$; push `15` | `15` |
| 4 | `2` | Operand (2) | Push `2` onto stack | `15, 2` |
| 5 | `$` | Operator (Exponent $) | Pop `2` and `15`; calculate $15^2 = 225$; push `225` | `225` |

**Final Evaluated Result:** **`225`**

### 4. Error Handling
- **Division by zero:** e.g. `10, 0, /` throws `IllegalArgumentException: Division by zero`.
- **Insufficient operands:** e.g. `+` when stack has $< 2$ items throws `Malformed expression: operator '+' requires two operands`.
- **Leftover operands:** e.g. `10, 5, 2, +` leaves two items on stack; flagged as malformed (too many operands).
- **Invalid symbols:** Any non-number and non-supported operator is rejected.

---

## UI/UX & Interactive Stepper
- **Non-automatic Preset Loading:** Clicking any preset loads the expression into the text field and initializes the stepper without immediately executing.
- **Step-by-Step Navigation:** 
  - `Step >`: Advances one symbol at a time.
  - `< Prev`: Steps backward to inspect previous states.
  - `Run All >>`: Completes remaining steps.
  - `Reset`: Clears grid and memory visualizer back to start.
- **Live Visual Stack:** At every step, the `ArrayView` renders the exact current contents of the stack with the **▼ TOP** pointer.
- **Active Row Highlight:** The current row in the trace table is highlighted with a distinct purple background.

