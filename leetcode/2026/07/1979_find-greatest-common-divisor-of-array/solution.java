/**
 * LeetCode Daily Challenge - 2026-07-18
 * Problem: 1979. Find Greatest Common Divisor of Array
 * Difficulty: Easy
 * Link: https://leetcode.com/problems/find-greatest-common-divisor-of-array/
 *
 * Problem Description:
 * Given an integer array nums, return the greatest common divisor of the
 * smallest number and largest number in nums.
 *
 * Approach: One-Pass Min/Max + Euclidean GCD
 *
 * Key Insight:
 * The problem reduces to two simple sub-problems:
 *   1. Find the minimum and maximum values in the array — a single linear scan.
 *   2. Compute GCD(min, max) using the Euclidean algorithm.
 *
 * Algorithm:
 *   1. Initialize min = nums[0], max = nums[0].
 *   2. Iterate nums: update min and max as needed (O(n)).
 *   3. Compute gcd(max, min) via Euclidean algorithm (O(log min)).
 *   4. Return the result.
 *
 * Euclidean Algorithm: gcd(a, b) = gcd(b, a % b), terminating when b == 0.
 *
 * Time Complexity:  O(n + log M) where n = nums.length, M = max(nums) <= 1000
 * Space Complexity: O(1) — only a few scalar variables
 */
class Solution {
    public int findGCD(int[] nums) {
        // Step 1: Find min and max in a single pass
        int min = nums[0];
        int max = nums[0];

        for (int i = 1; i < nums.length; i++) {
            if (nums[i] < min) {
                min = nums[i];
            } else if (nums[i] > max) {
                max = nums[i];
            }
        }

        // Step 2: Compute GCD of max and min using Euclidean algorithm
        return gcd(max, min);
    }

    /**
     * Euclidean algorithm for greatest common divisor.
     * Iterative version: O(log min(a,b)) time, O(1) space.
     */
    private int gcd(int a, int b) {
        while (b != 0) {
            int temp = b;
            b = a % b;
            a = temp;
        }
        return a;
    }

    // Test harness — run locally to verify correctness
    public static void main(String[] args) {
        Solution sol = new Solution();

        // Example 1: nums = [2,5,6,9,10] → min=2, max=10 → gcd(10,2)=2
        int r1 = sol.findGCD(new int[]{2, 5, 6, 9, 10});
        System.out.println("Example 1: " + r1 + " (expected 2) — "
                + (r1 == 2 ? "PASS" : "FAIL"));

        // Example 2: nums = [7,5,6,8,3] → min=3, max=8 → gcd(8,3)=1
        int r2 = sol.findGCD(new int[]{7, 5, 6, 8, 3});
        System.out.println("Example 2: " + r2 + " (expected 1) — "
                + (r2 == 1 ? "PASS" : "FAIL"));

        // Example 3: nums = [3,3] → min=3, max=3 → gcd(3,3)=3
        int r3 = sol.findGCD(new int[]{3, 3});
        System.out.println("Example 3: " + r3 + " (expected 3) — "
                + (r3 == 3 ? "PASS" : "FAIL"));

        // Edge case: nums = [1,1000] → min=1, max=1000 → gcd(1000,1)=1
        int r4 = sol.findGCD(new int[]{1, 1000});
        System.out.println("nums = [1,1000]: " + r4 + " (expected 1) — "
                + (r4 == 1 ? "PASS" : "FAIL"));

        // Edge case: nums = [2,2,2,2] → min=2, max=2 → gcd(2,2)=2
        int r5 = sol.findGCD(new int[]{2, 2, 2, 2});
        System.out.println("nums = [2,2,2,2]: " + r5 + " (expected 2) — "
                + (r5 == 2 ? "PASS" : "FAIL"));

        // Edge case: nums = [1000,999] → min=999, max=1000 → gcd(1000,999)=1
        int r6 = sol.findGCD(new int[]{1000, 999});
        System.out.println("nums = [1000,999]: " + r6 + " (expected 1) — "
                + (r6 == 1 ? "PASS" : "FAIL"));

        // Edge case: nums = [12,18,24] → min=12, max=24 → gcd(24,12)=12
        int r7 = sol.findGCD(new int[]{12, 18, 24});
        System.out.println("nums = [12,18,24]: " + r7 + " (expected 12) — "
                + (r7 == 12 ? "PASS" : "FAIL"));

        // Edge case: nums = [17,34,51] → min=17, max=51 → gcd(51,17)=17
        int r8 = sol.findGCD(new int[]{17, 34, 51});
        System.out.println("nums = [17,34,51]: " + r8 + " (expected 17) — "
                + (r8 == 17 ? "PASS" : "FAIL"));

        // Edge case: min constraint n=2, max nums
        int r9 = sol.findGCD(new int[]{4, 8});
        System.out.println("nums = [4,8]: " + r9 + " (expected 4) — "
                + (r9 == 4 ? "PASS" : "FAIL"));
    }
}
