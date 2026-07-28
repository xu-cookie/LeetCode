# LeetCode #3517: Smallest Palindromic Rearrangement I

- **Difficulty**: Medium
- **Link**: https://leetcode.com/problems/smallest-palindromic-rearrangement-i/
- **Date**: 2026-07-28

## Problem

Given a **palindromic** string `s`, return the **lexicographically smallest** palindromic permutation of `s`.

## Approach

Since `s` is guaranteed to be a palindrome, at most one character has an odd frequency (the middle character). All other characters appear in even counts.

To get the lexicographically smallest palindrome:
1. Count character frequencies in an array of size 26
2. Build the first half by iterating characters 'a' to 'z', appending `freq[c] / 2` copies of each
3. Find the middle character: the smallest character with odd frequency
4. Result = firstHalf + middle + reverse(firstHalf)

## Complexity

- **Time**: O(n) — single pass over the string + 26 iterations
- **Space**: O(n) — StringBuilder for result

## Examples

| Input | Output |
|-------|--------|
| `"z"` | `"z"` |
| `"babab"` | `"abbba"` |
| `"daccad"` | `"acddca"` |
