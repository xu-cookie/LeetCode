# 1288. Remove Covered Intervals

- **Difficulty**: Medium
- **Link**: https://leetcode.com/problems/remove-covered-intervals/
- **Date Solved**: 2026-07-06

## Problem Summary

Given an array of intervals `[li, ri)`, remove all intervals that are **covered** by another interval. An interval `[a, b)` is covered by `[c, d)` iff `c <= a` and `b <= d`. Return the number of remaining intervals.

## Key Insight

After sorting by start ascending (and end descending for ties), every interval that appears before the current one has `start <= current_start`. Thus, to check if the current interval is covered, we only need to know the **maximum end** seen so far — if `current_end <= maxEnd`, the interval is covered.

## Approach

1. **Sort**: by `start` ascending; for equal `start`, by `end` descending (so longer intervals come first).
2. **One-pass greedy**: track `maxEnd` (the farthest right boundary seen so far). For each interval:
   - If `end > maxEnd` → not covered, increment `count`, update `maxEnd = end`.
   - Otherwise → covered, skip.
3. Return `count`.

## Complexity

- **Time Complexity**: O(n log n) — sorting dominates.
- **Space Complexity**: O(log n) — sorting recursion stack (or O(1) for in-place sort).
