# [3658] GCD of Odd and Even Sums

**Difficulty:** Easy
**Date:** 2026-07-15
**Link:** https://leetcode.com/problems/gcd-of-odd-and-even-sums/

## Problem Description

Given an integer `n`, compute the GCD of:
- `sumOdd`: the sum of the smallest `n` positive odd numbers
- `sumEven`: the sum of the smallest `n` positive even numbers

## Approach

Mathematical derivation — O(1) solution.

### Key Observations

- Sum of first n odd numbers = 1 + 3 + 5 + ... + (2n-1) = **n²**
- Sum of first n even numbers = 2 + 4 + 6 + ... + 2n = **n(n+1)**
- GCD(n², n(n+1)) = n · GCD(n, n+1) = n · 1 = **n**
- Since `n` and `n+1` are always coprime, the answer is simply `n`.

## Complexity Analysis

- **Time Complexity:** O(1)
- **Space Complexity:** O(1)
