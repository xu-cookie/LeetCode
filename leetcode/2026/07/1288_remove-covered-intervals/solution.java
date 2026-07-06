/**
 * LeetCode Daily Challenge - 2026-07-06
 * Problem: 1288. Remove Covered Intervals
 * Difficulty: Medium
 * Link: https://leetcode.com/problems/remove-covered-intervals/
 *
 * Problem Description:
 * Given an array intervals where intervals[i] = [li, ri] represent the interval
 * [li, ri), remove all intervals that are covered by another interval in the list.
 *
 * The interval [a, b) is covered by the interval [c, d) if and only if
 * c <= a and b <= d.
 *
 * Return the number of remaining intervals.
 *
 * Approach: Sorting + Greedy One-Pass
 *
 * Key Insight:
 * Sort intervals by start point ascending, and for equal starts, by end point
 * descending. This way, when we iterate, every previous interval has start <=
 * current start. An interval is "covered" iff some previous interval extends
 * at least as far (prev_end >= current_end).
 *
 * By tracking the maximum end seen so far (maxEnd), we can determine coverage
 * in O(1) per interval:
 *   - If current_end > maxEnd → NOT covered (extends further than any previous)
 *   - If current_end <= maxEnd → COVERED (some previous interval fully encompasses it)
 *
 * Sorting by end descending for equal starts ensures that longer intervals
 * appear first when starts are tied, correctly marking shorter same-start
 * intervals as covered.
 *
 * Algorithm:
 *   1. Sort: by start asc, then by end desc.
 *   2. Iterate, maintaining maxEnd (the farthest right boundary seen so far).
 *   3. For each interval [start, end]:
 *        - If end > maxEnd: count++, maxEnd = end
 *        - Else: skip (it is covered)
 *   4. Return count.
 *
 * Time Complexity:  O(n log n) — dominated by sorting
 * Space Complexity: O(log n) — sorting stack space (or O(1) if in-place)
 */

import java.util.*;

class Solution {
    public int removeCoveredIntervals(int[][] intervals) {
        // Sort by start ascending, and for equal starts, by end descending
        // This ensures longer intervals come first when starts are tied
        Arrays.sort(intervals, (a, b) -> {
            if (a[0] != b[0]) {
                return a[0] - b[0];
            }
            return b[1] - a[1];
        });

        int count = 0;
        int maxEnd = -1;

        for (int[] interval : intervals) {
            int end = interval[1];

            // If this interval extends further than any seen so far,
            // it is NOT covered by any previous interval
            if (end > maxEnd) {
                count++;
                maxEnd = end;
            }
            // else: end <= maxEnd → covered by some previous interval → skip
        }

        return count;
    }

    // Test harness — run locally to verify correctness
    public static void main(String[] args) {
        Solution sol = new Solution();

        // Example 1
        int[][] intervals1 = {{1, 4}, {3, 6}, {2, 8}};
        int res1 = sol.removeCoveredIntervals(intervals1);
        System.out.println("Example 1: " + res1 + " (expected 2) — " + (res1 == 2 ? "PASS" : "FAIL"));

        // Example 2
        int[][] intervals2 = {{1, 4}, {2, 3}};
        int res2 = sol.removeCoveredIntervals(intervals2);
        System.out.println("Example 2: " + res2 + " (expected 1) — " + (res2 == 1 ? "PASS" : "FAIL"));

        // Edge case: single interval
        int[][] intervals3 = {{1, 2}};
        int res3 = sol.removeCoveredIntervals(intervals3);
        System.out.println("Single interval: " + res3 + " (expected 1) — " + (res3 == 1 ? "PASS" : "FAIL"));

        // Edge case: no intervals covered (all disjoint in terms of coverage)
        int[][] intervals4 = {{1, 2}, {3, 4}, {5, 6}};
        int res4 = sol.removeCoveredIntervals(intervals4);
        System.out.println("No coverage: " + res4 + " (expected 3) — " + (res4 == 3 ? "PASS" : "FAIL"));

        // Edge case: all intervals cover each other (nested)
        int[][] intervals5 = {{1, 10}, {2, 9}, {3, 8}, {4, 7}};
        int res5 = sol.removeCoveredIntervals(intervals5);
        System.out.println("Fully nested: " + res5 + " (expected 1) — " + (res5 == 1 ? "PASS" : "FAIL"));

        // Edge case: same start, different ends
        int[][] intervals6 = {{1, 4}, {1, 5}, {1, 3}};
        int res6 = sol.removeCoveredIntervals(intervals6);
        System.out.println("Same start: " + res6 + " (expected 1) — " + (res6 == 1 ? "PASS" : "FAIL"));

        // Edge case: chain of partial overlaps (none fully covered)
        int[][] intervals7 = {{1, 3}, {2, 5}, {4, 7}};
        int res7 = sol.removeCoveredIntervals(intervals7);
        System.out.println("Chain partial overlap: " + res7 + " (expected 3) — " + (res7 == 3 ? "PASS" : "FAIL"));

        // Edge case: exact duplicates — not possible per constraints (unique),
        // but same start with one covering the other
        int[][] intervals8 = {{0, 10}, {5, 12}};
        int res8 = sol.removeCoveredIntervals(intervals8);
        System.out.println("Cross overlap: " + res8 + " (expected 2) — " + (res8 == 2 ? "PASS" : "FAIL"));
    }
}
