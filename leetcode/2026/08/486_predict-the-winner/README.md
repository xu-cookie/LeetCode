# [486. Predict the Winner](https://leetcode.com/problems/predict-the-winner/)

**Difficulty**: Medium

## Problem Description

You are given an integer array `nums`. Two players are playing a game with this array: player 1 and player 2.

Player 1 and player 2 take turns, with player 1 starting first. Both players start the game with a score of `0`. At each turn, the player takes one of the numbers from either end of the array (i.e., `nums[0]` or `nums[nums.length - 1]`) which reduces the size of the array by `1`. The player adds the chosen number to their score. The game ends when there are no more elements in the array.

Return `true` if Player 1 can win the game. If the scores of both players are equal, then player 1 is still the winner, and you should also return `true`. You may assume that both players are playing optimally.

### Examples

**Example 1:**
```
Input: nums = [1,5,2]
Output: false
Explanation: Initially, player 1 can choose between 1 and 2.
If he chooses 2 (or 1), then player 2 can choose from 1 (or 2) and 5. If player 2 chooses 5, then player 1 will be left with 1 (or 2).
So, final score of player 1 is 1 + 2 = 3, and player 2 is 5.
Hence, player 1 will never be the winner and you need to return false.
```

**Example 2:**
```
Input: nums = [1,5,233,7]
Output: true
Explanation: Player 1 first chooses 1. Then player 2 has to choose between 5 and 7. No matter which number player 2 choose, player 1 can choose 233.
Finally, player 1 has more score (234) than player 2 (12), so you need to return True representing player1 can win.
```

### Constraints

- `1 <= nums.length <= 20`
- `0 <= nums[i] <= 10^7`

## Approach

### Key Insight: Score Difference DP

This is a classic **minimax** problem — a two-player, zero-sum game with perfect information. Both players play optimally, meaning each player maximizes their own score assuming the opponent does the same.

Instead of tracking two separate scores, we transform the problem into a **score difference** game:

> Define `dp[i][j]` = the **maximum net score advantage** the current player can achieve over the opponent when playing on subarray `nums[i..j]`.

When the current player picks `nums[i]`:
- They immediately gain `nums[i]` points
- The opponent then plays optimally on the remaining `nums[i+1..j]`, achieving advantage `dp[i+1][j]`
- **Net advantage** = `nums[i] - dp[i+1][j]`

Similarly, picking `nums[j]` yields net advantage = `nums[j] - dp[i][j-1]`.

### DP Formulation

**State**: `dp[i][j]` = max net advantage for current player on `nums[i..j]`

**Base Case**: `dp[i][i] = nums[i]` (only one element — take it)

**Transition**:
```
dp[i][j] = max(nums[i] - dp[i+1][j], nums[j] - dp[i][j-1])
```

**Answer**: `dp[0][n-1] >= 0` (Player 1 has non-negative advantage)

### Why This Works

The beauty of this formulation is that "the opponent's optimal play" is naturally expressed as `-dp[remaining]`. When the opponent faces the remaining subarray, they become the "current player" and will achieve `dp[remaining]` advantage over our original player. So our net gain after their optimal play is `our_pick - their_best_advantage`.

This is the essence of minimax: **my gain minus your best possible future gain**.

### Example Walkthrough: `nums = [1,5,2]`

```
DP table initialization:
  dp[0][0]=1  dp[1][1]=5  dp[2][2]=2

len=2:
  i=0,j=1: max(1-dp[1][1], 5-dp[0][0]) = max(1-5, 5-1) = max(-4,4) = 4
  i=1,j=2: max(5-dp[2][2], 2-dp[1][1]) = max(5-2, 2-5) = max(3,-3) = 3

len=3:
  i=0,j=2: max(1-dp[1][2], 2-dp[0][1]) = max(1-3, 2-4) = max(-2,-2) = -2

dp[0][2] = -2 < 0 → Player 1 CANNOT win → false ✓
```

### Example Walkthrough: `nums = [1,5,233,7]`

```
Base: dp[0][0]=1, dp[1][1]=5, dp[2][2]=233, dp[3][3]=7

len=2: dp[0][1]=4, dp[1][2]=228, dp[2][3]=226
len=3: dp[0][2]=max(1-228, 233-4)=max(-227,229)=229
       dp[1][3]=max(5-226, 7-228)=max(-221,-221)=-221
len=4: dp[0][3]=max(1-(-221), 7-229)=max(222,-222)=222

dp[0][3] = 222 ≥ 0 → Player 1 CAN win → true ✓
```

## Complexity Analysis

- **Time Complexity**: O(n²) — we fill an n×n DP table, each cell O(1)
- **Space Complexity**: O(n²) for the DP table. Can be optimized to O(n) by observing that we only need `dp[i+1][j]` (row below) and `dp[i][j-1]` (column left), so a 1D array suffices.

## Related Problems

- **#877 Stone Game** — Same game, but with the mathematical twist that Player 1 can always win when there are an even number of piles. This problem is the generalization without that guarantee.
- **#1140 Stone Game II** — Extension with variable take size (1 to 2M).
- **#1406 Stone Game III** — Players take 1-3 stones from one end.

## Code

See [Solution.java](./Solution.java) for the Java implementation and [solution.py](./solution.py) for the Python implementation, both with test harnesses.
