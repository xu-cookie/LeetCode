/**
 * LeetCode Daily Challenge - 2026-07-16
 * Problem: 3867. Sum of GCD of Formed Pairs (数对的最大公约数之和)
 * Difficulty: Medium
 * Link: https://leetcode.com/problems/sum-of-gcd-of-formed-pairs/
 *
 * Approach:
 * 1. Build prefixGcd array by tracking running maximum
 *    - For each i: if nums[i] > runningMax, update runningMax
 *    - prefixGcd[i] = gcd(nums[i], runningMax)
 * 2. Sort prefixGcd
 * 3. Pair smallest with largest: gcd(sorted[i], sorted[n-1-i]) for i < n/2
 * 4. Sum all pair GCDs
 *
 * Time Complexity: O(n log n) - dominated by sorting
 * Space Complexity: O(n) for prefixGcd array
 */

import java.util.Arrays;

class Solution {
    public long gcdSum(int[] nums) {
        int n = nums.length;

        // Step 1: Build prefixGcd array
        int[] prefixGcd = new int[n];
        int runningMax = 0;
        for (int i = 0; i < n; i++) {
            if (nums[i] > runningMax) {
                runningMax = nums[i];
            }
            prefixGcd[i] = gcd(nums[i], runningMax);
        }

        // Step 2: Sort non-decreasing
        Arrays.sort(prefixGcd);

        // Step 3: Pair smallest with largest, sum gcd of each pair
        long ans = 0;
        int half = n / 2;
        for (int i = 0; i < half; i++) {
            ans += gcd(prefixGcd[i], prefixGcd[n - 1 - i]);
        }

        return ans;
    }

    private int gcd(int a, int b) {
        while (b != 0) {
            int t = b;
            b = a % b;
            a = t;
        }
        return a;
    }
}
