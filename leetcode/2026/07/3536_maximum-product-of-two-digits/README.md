# [3536. Maximum Product of Two Digits](https://leetcode.com/problems/maximum-product-of-two-digits/)

**Difficulty**: Easy

## Problem Description

You are given a positive integer `n`.

Return the **maximum** product of any two digits in `n`.

**Note:** You may use the **same** digit twice if it appears more than once in `n`.

### Examples

**Example 1:**
```
Input: n = 31
Output: 3
Explanation: The digits of n are [3, 1]. The possible products: 3 * 1 = 3.
```

**Example 2:**
```
Input: n = 22
Output: 4
Explanation: The digits of n are [2, 2]. The possible products: 2 * 2 = 4.
```

**Example 3:**
```
Input: n = 124
Output: 8
Explanation: The digits of n are [1, 2, 4]. Products: 1*2=2, 1*4=4, 2*4=8. Maximum is 8.
```

### Constraints

- `10 <= n <= 10^9`

## Approach

Since all digits are non-negative (0-9) and the product function is monotonic for non-negative numbers, the maximum product of any two digits is always the product of the two largest digits in `n`.

### Algorithm

1. Extract digits one by one from `n` using modulo and integer division.
2. Maintain two variables `max1` and `max2` tracking the largest and second-largest digits.
3. When a new digit is larger than `max1`, shift `max1` to `max2` and set `max1` to the new digit.
4. Otherwise, if the new digit is larger than `max2` (but not larger than `max1`), update `max2`.
5. Return `max1 * max2`.

This approach naturally handles ties: if a digit equals `max1`, it falls into the `else if (digit > max2)` branch and correctly updates `max2`, allowing the same digit value to be used twice.

## Complexity Analysis

- **Time Complexity**: O(d) where d is the number of digits in n (at most 10, since n <= 10^9).
- **Space Complexity**: O(1) — only two integer variables are used regardless of input size.

## Code

See [Solution.java](./Solution.java) for the complete implementation with test cases.
