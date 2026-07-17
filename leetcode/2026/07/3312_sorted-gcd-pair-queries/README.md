# [3312] Sorted GCD Pair Queries

**Difficulty:** Hard
**Date:** 2026-07-17
**Link:** https://leetcode.com/problems/sorted-gcd-pair-queries/

## Problem Description

You are given an integer array `nums` of length `n` and an integer array `queries`.

Let `gcdPairs` denote an array obtained by calculating the GCD of all possible pairs `(nums[i], nums[j])`, where `0 <= i < j < n`, and then sorting these values in ascending order.

For each query `queries[i]`, you need to find the element at index `queries[i]` in `gcdPairs`.

Return an integer array `answer`, where `answer[i]` is the value at `gcdPairs[queries[i]]` for each query.

## Approach

### Key Insight

The value range of `nums[i]` is small (≤ 50000), but `n` can be up to 10^5. Total pairs = n*(n-1)/2 ≈ 5×10^9 — far too many to enumerate.

Instead, we count how many pairs produce each possible GCD value using **divisor enumeration + inclusion-exclusion**.

### Algorithm

1. **Frequency count**: Count occurrences of each value in `nums`
2. **Multiple counting**: For each divisor `d` (1 to maxVal), count how many numbers are multiples of `d`. Then `count * (count-1) / 2` = number of pairs whose GCD is **divisible by** `d`
3. **Inclusion-exclusion** (top-down): `exactPairs[d] = pairsDivisibleBy[d] - sum(exactPairs[2d], exactPairs[3d], ...)`
4. **Prefix sums**: Build cumulative counts for O(log M) query answering
5. **Binary search**: For each query index, find the smallest GCD value whose prefix sum exceeds it

## Complexity Analysis

- **Time Complexity:** O(M·log M + Q·log M) where M = max(nums) ≤ 50000
  - Divisor enumeration: M/1 + M/2 + ... + M/M ≈ M·ln(M) ≈ 5×10^4 × 11 ≈ 5.5×10^5
  - Inclusion-exclusion: same complexity
  - Queries: Q × log(M) ≈ 10^5 × 16 ≈ 1.6×10^6
- **Space Complexity:** O(M)
