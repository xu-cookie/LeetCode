/**
 * LeetCode #1331 - Rank Transform of an Array
 * Difficulty: Easy
 * Link: https://leetcode.com/problems/rank-transform-of-an-array/
 * Date Solved: 2026-07-12
 *
 * Approach:
 * 1. Create a sorted array of unique elements from the input array.
 * 2. Build a HashMap that maps each unique value to its rank (1-indexed position in sorted order).
 * 3. Iterate through the original array, replacing each element with its rank from the map.
 *
 * Time Complexity:  O(n log n) - dominated by sorting the unique elements
 * Space Complexity: O(n)     - for the HashMap and sorted unique array
 */

import java.util.*;

class Solution {
    public int[] arrayRankTransform(int[] arr) {
        int n = arr.length;

        // Edge case: empty array
        if (n == 0) {
            return new int[0];
        }

        // Step 1: Extract unique elements and sort them
        // Use a TreeSet to automatically sort and deduplicate
        Set<Integer> uniqueSet = new TreeSet<>();
        for (int num : arr) {
            uniqueSet.add(num);
        }

        // Step 2: Map each unique value to its rank
        // Iterate through sorted unique values directly (no intermediate array needed)
        Map<Integer, Integer> rankMap = new HashMap<>();
        int rank = 1;
        for (int val : uniqueSet) {
            rankMap.put(val, rank++);
        }

        // Step 3: Build the result array
        int[] result = new int[n];
        for (int i = 0; i < n; i++) {
            result[i] = rankMap.get(arr[i]);
        }

        return result;
    }

    // Test harness
    public static void main(String[] args) {
        Solution sol = new Solution();

        // Test Case 1
        int[] arr1 = {40, 10, 20, 30};
        int[] res1 = sol.arrayRankTransform(arr1);
        System.out.println("Test 1: " + Arrays.toString(res1));
        // Expected: [4, 1, 2, 3]

        // Test Case 2
        int[] arr2 = {100, 100, 100};
        int[] res2 = sol.arrayRankTransform(arr2);
        System.out.println("Test 2: " + Arrays.toString(res2));
        // Expected: [1, 1, 1]

        // Test Case 3
        int[] arr3 = {37, 12, 28, 9, 100, 56, 80, 5, 12};
        int[] res3 = sol.arrayRankTransform(arr3);
        System.out.println("Test 3: " + Arrays.toString(res3));
        // Expected: [5, 3, 4, 2, 8, 6, 7, 1, 3]

        // Edge Case: empty array
        int[] arr4 = {};
        int[] res4 = sol.arrayRankTransform(arr4);
        System.out.println("Test 4 (empty): " + Arrays.toString(res4));
        // Expected: []

        // Edge Case: single element
        int[] arr5 = {42};
        int[] res5 = sol.arrayRankTransform(arr5);
        System.out.println("Test 5 (single): " + Arrays.toString(res5));
        // Expected: [1]
    }
}
