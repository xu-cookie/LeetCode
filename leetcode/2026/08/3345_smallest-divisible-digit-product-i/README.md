# LeetCode #3345: Smallest Divisible Digit Product I

**Difficulty**: Easy | **Date**: 2026-08-06 | **Source**: leetcode.cn

## Problem Summary

Given two integers `n` and `t`, return the **smallest** number greater than or equal to `n` such that the **product of its digits** is divisible by `t`.

### Constraints
- `1 <= n <= 100`
- `1 <= t <= 10`

## Approach

**Brute Force Iteration**

Since the constraints are extremely small (n <= 100), we can simply iterate from `n` upward until we find a number whose digit product is divisible by `t`.

### Digit Product Computation
For each candidate number, compute the product of all its digits:
- If any digit is 0, the product is 0 (which is divisible by any `t`), so we return immediately.
- Otherwise, multiply all digits together and check divisibility by `t`.

### Key Observations
1. **Zero digit**: Any number containing a zero digit (e.g., 10, 20, 100, 101) has digit product 0, which is divisible by all `t`. This is an immediate match.
2. **t = 1**: The answer is always `n` since 1 divides everything.
3. **Worst case search**: Even in the worst case (e.g., n = 1, t = 7), we only need to check about 7 numbers. For larger values close to 100, a zero-digit number (100+) will be found very quickly.

### Complexity

- **Time**: O(k * log m) where k is the number of candidates checked (at most ~200 in the absolute worst case) and m is the candidate value (at most ~200). Effectively O(1).
- **Space**: O(1) — only a few integer variables are used.

## Solution

See `solution.java` — simple iterative brute force with early exit on zero-digit detection.
