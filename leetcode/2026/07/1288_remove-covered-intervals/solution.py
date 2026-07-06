"""
LeetCode Daily Challenge - 2026-07-06
Problem: 1288. Remove Covered Intervals
Difficulty: Medium
Link: https://leetcode.com/problems/remove-covered-intervals/

Approach: Sorting + Greedy One-Pass

1. Sort intervals by start ascending, and for equal starts, end descending.
2. Track maxEnd; an interval is NOT covered iff its end > maxEnd seen so far.
3. Return count of uncovered intervals.

Time: O(n log n)  Space: O(log n) / O(1)
"""
from typing import List


class Solution:
    def removeCoveredIntervals(self, intervals: List[List[int]]) -> int:
        # Sort: start asc, then end desc (longer intervals first for same start)
        intervals.sort(key=lambda x: (x[0], -x[1]))

        count = 0
        max_end = -1

        for start, end in intervals:
            if end > max_end:
                count += 1
                max_end = end
            # else: covered, skip

        return count


# Test harness
if __name__ == "__main__":
    sol = Solution()

    # Example 1
    res1 = sol.removeCoveredIntervals([[1, 4], [3, 6], [2, 8]])
    print(f"Example 1: {res1} (expected 2) — {'PASS' if res1 == 2 else 'FAIL'}")

    # Example 2
    res2 = sol.removeCoveredIntervals([[1, 4], [2, 3]])
    print(f"Example 2: {res2} (expected 1) — {'PASS' if res2 == 1 else 'FAIL'}")

    # Single interval
    res3 = sol.removeCoveredIntervals([[1, 2]])
    print(f"Single: {res3} (expected 1) — {'PASS' if res3 == 1 else 'FAIL'}")

    # No coverage
    res4 = sol.removeCoveredIntervals([[1, 2], [3, 4], [5, 6]])
    print(f"No coverage: {res4} (expected 3) — {'PASS' if res4 == 3 else 'FAIL'}")

    # Fully nested
    res5 = sol.removeCoveredIntervals([[1, 10], [2, 9], [3, 8], [4, 7]])
    print(f"Fully nested: {res5} (expected 1) — {'PASS' if res5 == 1 else 'FAIL'}")

    # Same start
    res6 = sol.removeCoveredIntervals([[1, 4], [1, 5], [1, 3]])
    print(f"Same start: {res6} (expected 1) — {'PASS' if res6 == 1 else 'FAIL'}")

    # Chain partial overlap
    res7 = sol.removeCoveredIntervals([[1, 3], [2, 5], [4, 7]])
    print(f"Chain partial: {res7} (expected 3) — {'PASS' if res7 == 3 else 'FAIL'}")

    # Cross overlap
    res8 = sol.removeCoveredIntervals([[0, 10], [5, 12]])
    print(f"Cross overlap: {res8} (expected 2) — {'PASS' if res8 == 2 else 'FAIL'}")
