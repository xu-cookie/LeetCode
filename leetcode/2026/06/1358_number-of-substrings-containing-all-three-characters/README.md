# 1358. Number of Substrings Containing All Three Characters

- **Difficulty**: Medium
- **Link**: https://leetcode.com/problems/number-of-substrings-containing-all-three-characters/
- **Date Solved**: 2026-06-30

## Problem Description

Given a string `s` consisting only of characters _a_, _b_ and _c_, return the number of substrings containing **at least** one occurrence of all these characters _a_, _b_ and _c_.

### Constraints
- `3 <= s.length <= 5 * 10^4`
- `s` only consists of _a_, _b_ or _c_ characters.

## Approach

**Last Occurrence Tracking (Sliding Window Variation)**

The key insight is that for each position `i` (the right end of a substring), we need to know the earliest start position such that the substring `s[start..i]` contains all three characters.

1. Track `last[0]`, `last[1]`, `last[2]` — the most recent index where 'a', 'b', 'c' appear, all initialized to -1.
2. For each position `i`:
   - Update `last[s.charAt(i) - 'a'] = i`
   - If all three characters have appeared (min of last[] ≠ -1), then any start position from `0` to `min(last[0], last[1], last[2])` is valid. The count of valid substrings ending at `i` is `min(last[0], last[1], last[2]) + 1`.
3. Sum these counts for all `i`.

**Why this works**: For a substring to contain all three characters, the start position must be at or before the position where the "least recently seen" character appeared. If the minimum last-position is at index `k`, we can start at `0, 1, ..., k` — that's `k + 1` choices.

## Complexity Analysis

- **Time Complexity**: O(N) — single pass through the string.
- **Space Complexity**: O(1) — only three integer variables for tracking positions.
