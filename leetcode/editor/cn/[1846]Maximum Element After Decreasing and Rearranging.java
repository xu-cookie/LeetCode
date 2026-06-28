/**
 * LeetCode Daily Challenge - 2026-06-28
 * Problem: 1846. Maximum Element After Decreasing and Rearranging
 * Difficulty: Medium
 * Link: https://leetcode.com/problems/maximum-element-after-decreasing-and-rearranging/
 *
 * Problem Description:
 * Given an array of positive integers arr, perform operations so that:
 * 1. The first element must be 1.
 * 2. abs(arr[i] - arr[i-1]) <= 1 for all i >= 1.
 * Operations allowed: decrease any element to a smaller positive integer,
 * and rearrange elements in any order. Return the maximum possible value.
 *
 * Approach:
 * Since rearrangement is free, sort the array first.
 * Then greedily build the sequence: start with cur = 0.
 * For each element x (in sorted order):
 *   cur = min(x, cur + 1)
 * This means: if x is large enough, we can extend the sequence by 1;
 * if x is too small, we're capped by x itself.
 * The final value of cur is the answer.
 *
 * Intuition: we want to build [1, 2, 3, ..., k], taking the next number
 * from the sorted array. If the next available number is at least cur+1,
 * we can achieve cur+1. Otherwise, the best we can do is the number itself.
 *
 * Time Complexity: O(n log n) - sorting dominates
 * Space Complexity: O(1) - in-place sorting
 */
class Solution {
    public int maximumElementAfterDecrementingAndRearranging(int[] arr) {
        // Step 1: Sort the array (rearrangement is free)
        java.util.Arrays.sort(arr);

        // Step 2: Greedily build the maximum possible value
        int cur = 0; // current maximum value in the constructed sequence
        for (int x : arr) {
            // We can achieve at most cur+1, but if x < cur+1 we're limited by x
            cur = Math.min(x, cur + 1);
        }

        return cur;
    }
}
