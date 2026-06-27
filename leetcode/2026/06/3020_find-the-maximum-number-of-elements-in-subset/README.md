# [3020] Find the Maximum Number of Elements in Subset

**Difficulty:** Medium
**Date:** 2026-06-27
**Link:** https://leetcode.com/problems/find-the-maximum-number-of-elements-in-subset/

## Problem Description

You are given an array of **positive** integers `nums`.

You need to select a subset of `nums` which satisfies the following condition:

- You can place the selected elements in a **0-indexed** array such that it follows the pattern: `[x, x², x⁴, ..., x^(k/2), x^k, x^(k/2), ..., x⁴, x², x]` (**Note** that `k` can be any **non-negative** power of `2`). For example, `[2, 4, 16, 4, 2]` and `[3, 9, 3]` follow the pattern while `[2, 4, 8, 4, 2]` does not.

Return _the **maximum** number of elements in a subset that satisfies these conditions._

## Approach

The pattern is a palindrome centered at `x^k`. Since `k = 2^m` for some `m ≥ 0`, the exponents in the pattern are:
`2^0, 2^1, 2^2, ..., 2^m, ..., 2^2, 2^1, 2^0`

The total length is `2m + 1`.

### Key Insights

1. **x = 1 is special**: Since `1^(anything) = 1`, any odd-length sequence of 1's satisfies the pattern. The maximum length for x=1 is the largest odd number ≤ count[1].

2. **For x > 1**: Build a chain `x → x² → x⁴ → x⁸ → ...` while each value exists in the frequency map. For a pattern with center at `chain[i]` (depth `i`):
   - Need at least **2 copies** of each `chain[j]` for `j < i` (one per side)
   - The center uses exactly 1 copy
   - Pattern length = `2*i + 1`

3. **Chain length is small**: Since `nums[i] ≤ 10^9`, squaring quickly exceeds this bound. Maximum chain length is ~5.

### Algorithm

1. Count frequencies of all elements
2. Handle x=1: set ans = largest odd ≤ count[1]
3. For each unique value x > 1:
   - Build chain of x, x², x⁴, ...
   - ans = max(ans, 1) (single element always works)
   - For each depth i ≥ 1: if all intermediate values have count ≥ 2, update ans = max(ans, 2i+1)
4. Return ans

## Complexity Analysis

- **Time Complexity:** O(n + u · log(maxVal)) ≈ O(n), where u = number of unique values. Each chain is at most ~5 elements.
- **Space Complexity:** O(n) for the frequency map.
