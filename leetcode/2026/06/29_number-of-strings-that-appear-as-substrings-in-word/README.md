# [1967] Number of Strings That Appear as Substrings in Word

**Difficulty:** Easy
**Date:** 2026-06-29
**Link:** https://leetcode.com/problems/number-of-strings-that-appear-as-substrings-in-word/

## Problem Description

Given an array of strings `patterns` and a string `word`, return *the **number** of strings in* `patterns` *that exist as a **substring** in* `word`.

A **substring** is a contiguous sequence of characters within a string.

### Examples

**Example 1:** `patterns = ["a","abc","bc","d"]`, `word = "abc"` → `3`
- "a" appears in "**a**bc"
- "abc" appears in "**abc**"
- "bc" appears in "a**bc**"
- "d" does not appear

**Example 2:** `patterns = ["a","b","c"]`, `word = "aaaaabbbbb"` → `2`

**Example 3:** `patterns = ["a","a","a"]`, `word = "ab"` → `3`

### Constraints
- `1 <= patterns.length <= 100`
- `1 <= patterns[i].length <= 100`
- `1 <= word.length <= 100`
- `patterns[i]` and `word` consist of lowercase English letters.

## Approach

### Algorithm

1. Initialize a counter `count = 0`
2. For each `pattern` in `patterns`:
   - If `word.contains(pattern)`, increment `count`
3. Return `count`

### Why It Works

Java's `String.contains()` checks if the given string is a contiguous substring — exactly what the problem asks. The constraints are small enough (max 100 each) that the straightforward O(n*m) approach is perfectly efficient.

## Complexity Analysis

- **Time Complexity:** O(n * m) where n = `patterns.length` and m = `word.length`
- **Space Complexity:** O(1) — only a counter variable
