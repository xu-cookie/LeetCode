# [877. Stone Game](https://leetcode.com/problems/stone-game/)

**Difficulty**: Medium

## Problem Description

Alice and Bob play a game with piles of stones. There are an **even** number of piles arranged in a row, and each pile has a **positive** integer number of stones `piles[i]`.

The objective of the game is to end with the most stones. The **total** number of stones across all the piles is **odd**, so there are no ties.

Alice and Bob take turns, with **Alice starting first**. Each turn, a player takes the entire pile of stones either from the **beginning** or from the **end** of the row. This continues until there are no more piles left, at which point the person with the **most stones wins**.

Assuming Alice and Bob play optimally, return `true` if Alice wins the game, or `false` if Bob wins.

### Examples

**Example 1:**
```
Input: piles = [5,3,4,5]
Output: true
Explanation:
Alice starts first, and can only take the first 5 or the last 5.
Say she takes the first 5, so that the row becomes [3, 4, 5].
If Bob takes 3, then the board is [4, 5], and Alice takes 5 to win with 10 points.
If Bob takes the last 5, then the board is [3, 4], and Alice takes 4 to win with 9 points.
This demonstrated that taking the first 5 was a winning move for Alice, so we return true.
```

**Example 2:**
```
Input: piles = [3,7,2,3]
Output: true
```

### Constraints

- `2 <= piles.length <= 500`
- `piles.length` is **even**.
- `1 <= piles[i] <= 500`
- `sum(piles[i])` is **odd**.

## Approach

### Approach 1: Mathematical Insight — Alice Always Wins 🎉

This problem has a beautiful mathematical property. Given the constraints:

1. **Even number of piles**
2. **Odd total sum** (no ties)

**Alice can always force a win.**

#### Proof

The piles can be partitioned into two groups by index parity:

- **Group Even**: `piles[0], piles[2], piles[4], ...`
- **Group Odd**: `piles[1], piles[3], piles[5], ...`

Since the total sum is odd, `sum(Even) ≠ sum(Odd)`. One group is strictly larger than the other.

Alice, going first, can guarantee she collects ALL piles from the larger group:

- **If she wants Even-indexed piles**: She takes `piles[0]` (even-indexed). After Bob's turn, both ends are odd-indexed. When Bob picks one, the remaining ends become even-indexed again. Alice can always pick an even-indexed pile.
- **If she wants Odd-indexed piles**: She takes `piles[n-1]` (odd-indexed since n is even). Same logic applies — she can always reach an odd-indexed pile on her turn.

Since one parity group is strictly larger, Alice picks the strategy targeting that group and wins.

**Complexity**: O(1) time, O(1) space.

### Approach 2: Dynamic Programming (Minimax) — General Solution

This is the solution to the more general problem **#486 Predict the Winner**. It works for any number of piles, with or without the parity/odd-sum constraints.

#### Key Insight: Score Difference

Define `dp[i][j]` = the **maximum net score advantage** the current player can achieve over the opponent when playing on `piles[i..j]`.

When the current player picks `piles[i]`:
- They gain `piles[i]` immediately
- The opponent then plays optimally on `piles[i+1..j]`, achieving `dp[i+1][j]`
- Net advantage = `piles[i] - dp[i+1][j]`

Similarly, picking `piles[j]` yields `piles[j] - dp[i][j-1]`.

#### DP Formulation

**State**: `dp[i][j]` = max net advantage for current player on `piles[i..j]`

**Base Case**: `dp[i][i] = piles[i]` (only one element — take it)

**Transition**:
```
dp[i][j] = max(piles[i] - dp[i+1][j], piles[j] - dp[i][j-1])
```

**Answer**: `dp[0][n-1] > 0` (Alice has positive advantage; strict since total is odd)

#### Example Walkthrough: `piles = [5,3,4,5]`

```
DP table initialization:
  dp[0][0]=5  dp[1][1]=3  dp[2][2]=4  dp[3][3]=5

len=2:
  i=0,j=1: max(5-dp[1][1], 3-dp[0][0]) = max(5-3, 3-5) = max(2,-2) = 2
  i=1,j=2: max(3-dp[2][2], 4-dp[1][1]) = max(3-4, 4-3) = max(-1,1) = 1
  i=2,j=3: max(4-dp[3][3], 5-dp[2][2]) = max(4-5, 5-4) = max(-1,1) = 1

len=3:
  i=0,j=2: max(5-dp[1][2], 4-dp[0][1]) = max(5-1, 4-2) = max(4,2) = 4
  i=1,j=3: max(3-dp[2][3], 5-dp[1][2]) = max(3-1, 5-1) = max(2,4) = 4

len=4:
  i=0,j=3: max(5-dp[1][3], 5-dp[0][2]) = max(5-4, 5-4) = max(1,1) = 1

dp[0][3] = 1 > 0 → Alice wins → true ✓
```

## Complexity Analysis

| Approach | Time | Space |
|----------|------|-------|
| Mathematical | O(1) | O(1) |
| Minimax DP (2D) | O(n²) | O(n²) |
| Minimax DP (1D) | O(n²) | O(n) |

## Related Problems

- **#486 Predict the Winner** — The general version without the "even number of piles + odd sum" guarantees. Uses the same DP formulation; answer is `dp[0][n-1] >= 0`.
- **#1140 Stone Game II** — Extension with variable take size (1 to 2M).
- **#1406 Stone Game III** — Players take 1-3 stones from one end.
- **#1510 Stone Game IV** — Alice and Bob take square numbers (game theory / DP on game state).
- **#1563 Stone Game V** — More complex variant with splitting piles.

## Code

See [Solution.java](./Solution.java) for the Java implementation and [solution.py](./solution.py) for the Python implementation, both with test harnesses and both DP verifications.
