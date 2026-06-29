# 1967. Number of Strings That Appear as Substrings in Word

- **Difficulty**: Easy
- **Link**: https://leetcode.com/problems/number-of-strings-that-appear-as-substrings-in-word/
- **Date Solved**: 2026-06-29

## Problem Description

Given an array of strings `patterns` and a string `word`, return the number of strings in `patterns` that exist as a substring in `word`.

A substring is a contiguous sequence of characters within a string.

### Constraints
- `1 <= patterns.length <= 100`
- `1 <= patterns[i].length <= 100`
- `1 <= word.length <= 100`
- `patterns[i]` and `word` consist of lowercase English letters.

## Approach

**Direct Substring Matching (Brute Force)**

Since the constraints are small (at most 100 patterns, each up to length 100, word up to length 100), a straightforward approach works well:

1. Initialize a counter `count = 0`.
2. For each `pattern` in `patterns`:
   - Use Java's built-in `String.contains()` to check if `pattern` is a substring of `word`.
   - If yes, increment `count`.
3. Return `count`.

Java's `String.contains()` internally implements a naive O(n*m) substring search for the small-string case, or a more advanced algorithm (like boyer-moore or KMP variations) for longer strings in modern JDK versions.

## Complexity Analysis

- **Time Complexity**: O(P * W) where P is the total length of all pattern strings and W is the length of `word`. In the worst case, each `contains()` call scans the whole word.
- **Space Complexity**: O(1) — only an integer counter variable is used.
