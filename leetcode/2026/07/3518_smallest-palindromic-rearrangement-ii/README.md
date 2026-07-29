# LeetCode #3518: Smallest Palindromic Rearrangement II

- **Difficulty**: Hard
- **Link**: https://leetcode.com/problems/smallest-palindromic-rearrangement-ii/
- **Date**: 2026-07-29

## Problem

Given a **palindromic** string `s` and an integer `k`, return the **k-th lexicographically smallest** palindromic permutation of `s`. If there are fewer than `k` distinct palindromic permutations, return an empty string.

Different rearrangements that yield the same palindromic string are considered identical and counted once.

## Approach

**Key insight**: A palindrome is determined entirely by its first half. The middle character (if string length is odd) is fixed. So the problem reduces to finding the k-th lexicographically smallest permutation of the multiset forming the first half.

### Algorithm

1. Count character frequencies; half-freq[c] = freq[c] / 2
2. Find the middle character (one with odd frequency, if any)
3. Compute total distinct permutations of the first half using multinomial:
   `total = (totalHalf)! / ∏ (halfFreq[c])!`
4. If `k > total`, return `""`
5. Greedily build the first half position by position:
   - For each position, try characters 'a' to 'z' in order
   - `count = curTotal × remFreq[c] / remaining` (exact formula)
   - If `k > count`: skip this character (`k -= count`)
   - If `k ≤ count`: place this character, update state

### Optimization (avoids BigInteger)

- Use bounded multinomial with `cap = k × totalHalf` (≤ 5×10⁹, fits in 64-bit long)
- If exact total > `k × totalHalf`, ALL subtree sizes exceed k, so the k-th permutation = 1st permutation = lexicographically smallest ("fast mode")
- Only compute exact counts when total is within range of long arithmetic

## Complexity

- **Time**: O(n + 26·totalHalf) = O(n) — single pass over string + character tries per position
- **Space**: O(n) — for the result string and frequency arrays

## Examples

| Input | k | Output |
|-------|---|--------|
| `"abba"` | 2 | `"baab"` |
| `"aa"` | 2 | `""` |
| `"bacab"` | 1 | `"abcba"` |
| `"bacab"` | 2 | `"bacab"` |
| `"aabbcc"` | 1 | `"abccba"` |
| `"aabbcc"` | 6 | `"cbaabc"` |
