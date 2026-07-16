# [3867] Sum of GCD of Formed Pairs

**Difficulty:** Medium
**Date:** 2026-07-16
**Link:** https://leetcode.com/problems/sum-of-gcd-of-formed-pairs/

## Problem Description

You are given an integer array `nums` of length `n`.

Construct an array `prefixGcd` where for each index `i`:
- Let `mx_i = max(nums[0], nums[1], ..., nums[i])`.
- `prefixGcd[i] = gcd(nums[i], mx_i)`.

After constructing `prefixGcd`:
- Sort `prefixGcd` in non-decreasing order.
- Form pairs by taking the smallest unpaired element and the largest unpaired element.
- Repeat this process until no more pairs can be formed.
- For each formed pair, compute the `gcd` of the two elements.
- If `n` is odd, the middle element in the `prefixGcd` array remains unpaired and should be ignored.

Return an integer denoting the sum of the GCD values of all formed pairs.

## Approach

1. Build `prefixGcd` array by tracking running maximum:
   - For each element, update `running_max` if current element is larger
   - `prefixGcd[i] = gcd(nums[i], running_max)`
2. Sort `prefixGcd` in non-decreasing order
3. Pair smallest with largest: `gcd(sorted[i], sorted[n-1-i])` for `i < n/2`
4. Sum all pair GCDs and return

## Complexity Analysis

- **Time Complexity:** O(n log n) — dominated by sorting the prefixGcd array
- **Space Complexity:** O(n) — for the prefixGcd array
