# [3336] Find the Number of Subsequences With Equal GCD

**Difficulty:** Hard
**Date:** 2026-07-14
**Link:** https://leetcode.com/problems/find-the-number-of-subsequences-with-equal-gcd/

## Problem Description

You are given an integer array `nums`.

Your task is to find the number of pairs of **non-empty** subsequences `(seq1, seq2)` of `nums` that satisfy the following conditions:

- The subsequences `seq1` and `seq2` are **disjoint**, meaning **no index** of `nums` is common between them.
- The GCD of the elements of `seq1` is equal to the GCD of the elements of `seq2`.

Return the total number of such pairs, modulo `10^9 + 7`.

## Approach

**Dynamic Programming over GCD values**

Since `nums[i] <= 200`, all possible GCD values are bounded by 200. We define `dp[g1][g2]` as the number of ways to process elements so far, where:
- `g1` = GCD of seq1 (0 means seq1 is empty)
- `g2` = GCD of seq2 (0 means seq2 is empty)

For each element `x`, we have three choices:
1. **Skip**: all states carry over unchanged
2. **Add to seq1**: `new_g1 = (g1 == 0 ? x : gcd(g1, x))`
3. **Add to seq2**: `new_g2 = (g2 == 0 ? x : gcd(g2, x))`

The answer is the sum of `dp[g][g]` for all `g > 0` (both non-empty and GCDs equal).

## Key Observations

- `1 <= nums.length <= 200`
- `1 <= nums[i] <= 200`
- Maximum GCD value is bounded by 200, making the DP state space small (~40k states)
- Sequential processing of elements with careful state transitions

## Complexity Analysis

- **Time Complexity:** O(n * M²) where M = max(nums) ≤ 200. With n ≤ 200, this is approximately 8 million operations.
- **Space Complexity:** O(M²) = O(40000) for the DP table.
