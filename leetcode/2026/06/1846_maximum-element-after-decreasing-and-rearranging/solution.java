/**
 * LeetCode Daily Challenge - 2026-06-29
 * Problem: 1846. Maximum Element After Decreasing and Rearranging
 * Difficulty: Medium
 * Link: https://leetcode.com/problems/maximum-element-after-decreasing-and-rearranging/
 *
 * Problem Description:
 * You are given an array of positive integers arr. Perform some operations
 * (possibly none) on arr so that it satisfies these conditions:
 * - The value of the first element in arr must be 1.
 * - The absolute difference between any 2 adjacent elements must be <= 1.
 *   i.e., abs(arr[i] - arr[i - 1]) <= 1 for each i where 1 <= i < arr.length.
 *
 * There are 2 types of operations:
 * - Decrease the value of any element of arr to a smaller positive integer.
 * - Rearrange the elements of arr to be in any order.
 *
 * Return the maximum possible value of an element in arr after performing
 * the operations to satisfy the conditions.
 *
 * Approach (Sorting + Greedy):
 * 1. Sort the array. Since rearrangement is free, sorting gives us the
 *    optimal order to maximize the final values.
 * 2. Initialize cur = 0 (tracks the maximum value we can achieve so far).
 * 3. Iterate through sorted array:
 *    - For each element x, the best we can do is cur + 1 (increment by 1
 *      from previous to satisfy adjacent diff <= 1).
 *    - But we cannot exceed x itself (since we can only decrease values).
 *    - So: cur = min(x, cur + 1).
 * 4. Return cur as the final maximum element.
 *
 * Key Insight:
 * After sorting, we greedily try to build [1, 2, 3, ..., k] as long as
 * the sorted array supplies large enough values. Each position's value
 * is capped by the available element at that position.
 *
 * Time Complexity: O(n log n) - dominated by sorting
 * Space Complexity: O(1) - no extra space beyond sorting (or O(n) if
 *                    sorting implementation creates a copy)
 */

import java.util.Arrays;

class Solution {
    public int maximumElementAfterDecrementingAndRearranging(int[] arr) {
        // Sort to arrange elements in non-decreasing order
        Arrays.sort(arr);

        // cur tracks the maximum achievable value so far
        // Start at 0 because the first element must be 1 (cur + 1 = 1)
        int cur = 0;

        for (int x : arr) {
            // We can at most achieve cur + 1 from the previous value,
            // but we are capped by the actual value x (can only decrease)
            cur = Math.min(x, cur + 1);
        }

        return cur;
    }

    // Test harness
    public static void main(String[] args) {
        Solution sol = new Solution();

        // Example 1
        int[] arr1 = {2, 2, 1, 2, 1};
        int result1 = sol.maximumElementAfterDecrementingAndRearranging(arr1);
        System.out.println("Example 1: " + result1 + " (expected: 2) -> "
                + (result1 == 2 ? "PASS" : "FAIL"));

        // Example 2
        int[] arr2 = {100, 1, 1000};
        int result2 = sol.maximumElementAfterDecrementingAndRearranging(arr2);
        System.out.println("Example 2: " + result2 + " (expected: 3) -> "
                + (result2 == 3 ? "PASS" : "FAIL"));

        // Example 3
        int[] arr3 = {1, 2, 3, 4, 5};
        int result3 = sol.maximumElementAfterDecrementingAndRearranging(arr3);
        System.out.println("Example 3: " + result3 + " (expected: 5) -> "
                + (result3 == 5 ? "PASS" : "FAIL"));

        // Edge case: single element
        int[] arr4 = {1};
        int result4 = sol.maximumElementAfterDecrementingAndRearranging(arr4);
        System.out.println("Test 4 (single): " + result4 + " (expected: 1) -> "
                + (result4 == 1 ? "PASS" : "FAIL"));

        // Edge case: all same values
        int[] arr5 = {5, 5, 5, 5};
        int result5 = sol.maximumElementAfterDecrementingAndRearranging(arr5);
        System.out.println("Test 5 (all 5s): " + result5 + " (expected: 4) -> "
                + (result5 == 4 ? "PASS" : "FAIL"));

        // Edge case: all ones
        int[] arr6 = {1, 1, 1, 1};
        int result6 = sol.maximumElementAfterDecrementingAndRearranging(arr6);
        System.out.println("Test 6 (all 1s): " + result6 + " (expected: 1) -> "
                + (result6 == 1 ? "PASS" : "FAIL"));

        // Edge case: large input with gaps
        int[] arr7 = {73, 98, 9};
        int result7 = sol.maximumElementAfterDecrementingAndRearranging(arr7);
        System.out.println("Test 7: " + result7 + " (expected: 3) -> "
                + (result7 == 3 ? "PASS" : "FAIL"));

        // Edge case: already satisfying
        int[] arr8 = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        int result8 = sol.maximumElementAfterDecrementingAndRearranging(arr8);
        System.out.println("Test 8 (already perfect): " + result8
                + " (expected: 10) -> " + (result8 == 10 ? "PASS" : "FAIL"));
    }
}
