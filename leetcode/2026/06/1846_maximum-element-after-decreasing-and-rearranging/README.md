# [1846] Maximum Element After Decreasing and Rearranging

**Difficulty:** Medium
**Date:** 2026-06-29
**Link:** https://leetcode.com/problems/maximum-element-after-decreasing-and-rearranging/

## Problem Description

You are given an array of positive integers `arr`. Perform some operations (possibly none) on `arr` so that it satisfies these conditions:

- The value of the **first** element in `arr` must be `1`.
- The absolute difference between any 2 adjacent elements must be **less than or equal to** `1`. In other words, `abs(arr[i] - arr[i - 1]) <= 1` for each `i` where `1 <= i < arr.length` (**0-indexed**). `abs(x)` is the absolute value of `x`.

There are 2 types of operations that you can perform any number of times:

- **Decrease** the value of any element of `arr` to a **smaller positive integer**.
- **Rearrange** the elements of `arr` to be in any order.

Return *the **maximum** possible value of an element in* `arr` *after performing the operations to satisfy the conditions*.

### Examples

**Example 1:** `arr = [2,2,1,2,1]` -> `2` (rearrange to `[1,2,2,2,1]`)
**Example 2:** `arr = [100,1,1000]` -> `3` (rearrange, decrease 100->2, 1000->3 -> `[1,2,3]`)
**Example 3:** `arr = [1,2,3,4,5]` -> `5` (already satisfies)

### Constraints
- `1 <= arr.length <= 10^5`
- `1 <= arr[i] <= 10^9`

## Approach

### Key Insight

Since we can freely rearrange and decrease elements, the problem reduces to:
1. **Sort** the array (rearrange for free to put values in optimal order)
2. **Greedily build** the maximum possible sequence `[1, 2, 3, ..., k]`

### Algorithm

```
1. Sort arr in ascending order
2. Initialize cur = 0 (maximum value built so far)
3. For each element x in sorted arr:
   - cur = min(x, cur + 1)
   - If x is large enough, extend the sequence by 1
   - If x is too small, we're limited by what's available
4. Return cur
```

### Why It Works

- Sorting puts elements in non-decreasing order -- optimal for building ascending adjacency
- `cur + 1` is the greedy choice: we always want the largest possible next value
- `min(x, cur + 1)` ensures we never claim a value larger than what's available
- The first element becomes 1 automatically: `cur` starts at 0, so `min(arr[0], 1)` ensures max >= 1

## Complexity Analysis

- **Time Complexity:** O(n log n) -- dominated by sorting
- **Space Complexity:** O(1) -- in-place algorithm (or O(n) if sorting implementation creates a copy)
