# [1846] Maximum Element After Decreasing and Rearranging

**Difficulty:** Medium
**Date:** 2026-06-28
**Link:** https://leetcode.com/problems/maximum-element-after-decreasing-and-rearranging/

## Problem Description

You are given an array of positive integers `arr`. Perform some operations (possibly none) on `arr` so that it satisfies these conditions:

- The value of the **first** element in `arr` must be `1`.
- The absolute difference between any 2 adjacent elements must be **less than or equal to** `1`. In other words, `abs(arr[i] - arr[i - 1]) <= 1` for each `i` where `1 <= i < arr.length` (**0-indexed**). `abs(x)` is the absolute value of `x`.

There are 2 types of operations that you can perform any number of times:

- **Decrease** the value of any element of `arr` to a **smaller positive integer**.
- **Rearrange** the elements of `arr` to be in any order.

Return *the **maximum** possible value of an element in* `arr` *after performing the operations to satisfy the conditions*.

## Approach

### Key Insight

Since we can freely rearrange and decrease elements, the problem reduces to:

1. **Sort** the array (rearrange for free)
2. **Greedily build** the maximum possible sequence `[1, 2, 3, ..., k]`

### Algorithm

1. Sort `arr` in ascending order
2. Initialize `cur = 0` (the maximum value we've built so far)
3. For each element `x` in sorted `arr`:
   - `cur = min(x, cur + 1)`
   - If `x` is large enough, extend the sequence by 1
   - If `x` is too small, we're limited by what's available
4. Return `cur`

### Why It Works

- Sorting puts elements in non-decreasing order — optimal for building ascending adjacency
- `cur + 1` is the greedy choice: we always want the largest possible next value
- `min(x, cur + 1)` ensures we never claim a value larger than what's actually available
- The first element becomes 1 automatically: `cur` starts at 0, so `min(arr[0], 0+1)` ensures max is at least 1

## Complexity Analysis

- **Time Complexity:** O(n log n) — dominated by sorting
- **Space Complexity:** O(1) — in-place sorting (or O(n) if the language creates a copy)
