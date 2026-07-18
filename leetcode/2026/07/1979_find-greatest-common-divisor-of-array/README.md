# 1979. Find Greatest Common Divisor of Array

- **Difficulty**: Easy
- **Link**: https://leetcode.com/problems/find-greatest-common-divisor-of-array/
- **Date Solved**: 2026-07-18

## Problem Summary

Given an integer array `nums`, return the GCD of the smallest and largest numbers in `nums`.

The GCD (Greatest Common Divisor) of two numbers is the largest positive integer that evenly divides both numbers.

## Examples

| nums            | Min | Max | GCD | Result |
|-----------------|-----|-----|-----|--------|
| [2,5,6,9,10]   | 2   | 10  | 2   | 2      |
| [7,5,6,8,3]    | 3   | 8   | 1   | 1      |
| [3,3]           | 3   | 3   | 3   | 3      |

## Approach

**One-Pass Min/Max + Euclidean GCD**

The problem has two simple sub-problems:

1. **Find min and max** — a single linear scan: `O(n)`.
2. **Compute GCD** — Euclidean algorithm: `O(log M)`.

Extremely straightforward — no edge cases beyond standard GCD behavior.

### Algorithm

1. Initialize `min = nums[0]`, `max = nums[0]`.
2. Iterate `nums`: update `min` and `max` as needed.
3. Return `gcd(max, min)` via Euclidean algorithm.

## Complexity

- **Time Complexity**: `O(n + log M)` where `n = nums.length`, `M = max(nums) <= 1000`.
- **Space Complexity**: `O(1)` — only scalar variables.
