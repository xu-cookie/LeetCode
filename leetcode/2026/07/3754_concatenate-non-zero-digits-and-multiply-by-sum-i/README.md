# 3754. Concatenate Non-Zero Digits and Multiply by Sum I

- **Difficulty**: Easy
- **Link**: https://leetcode.com/problems/concatenate-non-zero-digits-and-multiply-by-sum-i/
- **Date Solved**: 2026-07-07

## Problem Summary

Given an integer `n` (0 <= n <= 10^9):
1. Extract all **non-zero** digits from `n` in their original order.
2. Concatenate them to form a new integer `x`. If no non-zero digits exist, `x = 0`.
3. Let `sum` be the sum of digits in `x`.
4. Return `x * sum`.

## Examples

| n         | Non-zero digits | x    | sum | Result  |
|-----------|-----------------|------|-----|---------|
| 10203004  | 1, 2, 3, 4      | 1234 | 10  | 12340   |
| 1000      | 1               | 1    | 1   | 1       |
| 0         | (none)          | 0    | 0   | 0       |

## Approach

**String Iteration (Linear Scan)**

Since `n` has at most 10 digits, converting to a `String` and iterating character-by-character is simple and efficient:

1. Convert `n` to a `String`.
2. Initialize `x = 0` and `sum = 0`.
3. For each character `c`:
   - If `c != '0'`: update `x = x * 10 + digit` and `sum += digit`.
4. Return `x * sum`.

This naturally handles the edge case `n = 0` (x = 0, sum = 0, product = 0).

## Complexity

- **Time Complexity**: O(d) where `d` is the number of digits (max 10 for n <= 10^9).
- **Space Complexity**: O(d) for the string representation (max 10 characters).
