# LeetCode #1406: Stone Game III

- **Difficulty**: Hard
- **Date**: 2026-08-03
- **Link**: https://leetcode.com/problems/stone-game-iii/
- **Tags**: `DP` `Game Theory` `Minimax`

## Problem

Alice and Bob take turns picking 1, 2, or 3 stones from the **beginning** of a row. Each stone has an associated integer value. The player with the highest total score wins (ties possible). Both play optimally.

Return `"Alice"`, `"Bob"`, or `"Tie"`.

## Approach: DP (Bottom-Up, Right-to-Left)

### Key Insight

Define `dp[i]` = the **maximum score advantage** the current player can achieve over the opponent when starting from index `i`.

At position `i`, the current player has up to 3 choices:

| Choice | Gain (current sum) | Opponent's advantage | Net advantage |
|--------|-------------------|---------------------|--------------|
| 1 stone | `stoneValue[i]` | `dp[i+1]` | `stoneValue[i] - dp[i+1]` |
| 2 stones | `stoneValue[i] + stoneValue[i+1]` | `dp[i+2]` | `stoneValue[i] + stoneValue[i+1] - dp[i+2]` |
| 3 stones | `stoneValue[i] + stoneValue[i+1] + stoneValue[i+2]` | `dp[i+3]` | `stoneValue[i] + stoneValue[i+1] + stoneValue[i+2] - dp[i+3]` |

`dp[i] = max(option1, option2, option3)`

### Why It Works

When a player takes `k` stones (gaining score `S`), the opponent faces the remaining array starting at `i+k`. The opponent's maximal advantage from that position is `dp[i+k]`. So the current player's net gain is `S - dp[i+k]`.

### Result

- `dp[0] > 0` → Alice wins
- `dp[0] < 0` → Bob wins
- `dp[0] == 0` → Tie

### Complexity

| | Time | Space |
|---|---|---|
| Java | O(n) | O(1) |
| Python | O(n) | O(1) |

Only the last 3 DP values are needed at each step, so the algorithm uses constant space.

## Edge Cases

- **Single element**: Alice takes it → advantage = stoneValue[0]
- **Negative values**: Players minimize losses (pick the least negative option)
- **All negative**: Both try to lose less; ties are common

## Related Problems

- [#877 Stone Game](https://leetcode.com/problems/stone-game/) (Medium) — Alice always wins
- [#486 Predict the Winner](https://leetcode.com/problems/predict-the-winner/) (Medium) — Similar DP, pick from either end
- [#1140 Stone Game II](https://leetcode.com/problems/stone-game-ii/) (Medium) — M ranging picks
- [#1510 Stone Game IV](https://leetcode.com/problems/stone-game-iv/) (Hard) — Take square numbers
