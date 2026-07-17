/**
 * LeetCode Daily Challenge - 2026-07-17
 * Problem: 3312. Sorted GCD Pair Queries
 * Difficulty: Hard
 * Link: https://leetcode.com/problems/sorted-gcd-pair-queries/
 *
 * Problem Description:
 * You are given an integer array nums of length n and an integer array queries.
 * Let gcdPairs denote an array obtained by calculating the GCD of all possible
 * pairs (nums[i], nums[j]), where 0 <= i < j < n, and then sorting these values
 * in ascending order.
 * For each query queries[i], return the element at index queries[i] in gcdPairs.
 *
 * Approach: Counting + Inclusion-Exclusion + Binary Search
 *
 * Key Observations:
 * - nums[i] <= 5 * 10^4 (small value range), but n <= 10^5 (large array)
 * - Total pairs = n*(n-1)/2 can be ~5e9 — impossible to enumerate all GCDs
 * - Instead, count how many pairs have each possible GCD value
 *
 * Algorithm:
 * 1. Count frequency of each value in nums
 * 2. For each d from 1 to maxVal, count numbers that are multiples of d
 *    → countMultiples[d] * (countMultiples[d] - 1) / 2 = pairs with GCD divisible by d
 * 3. Inclusion-exclusion from maxVal down to 1:
 *    exactPairs[d] = pairsDivisible[d] - sum(exactPairs[2d], exactPairs[3d], ...)
 * 4. Build prefix sums over exactPairs
 * 5. For each query, binary search to find the GCD value at that index
 *
 * Time Complexity: O(M * log M + Q * log M) where M = max(nums) <= 50000
 * Space Complexity: O(M)
 */
class Solution {
    public int[] gcdValues(int[] nums, long[] queries) {
        // Find maximum value in nums
        int maxVal = 0;
        for (int num : nums) {
            if (num > maxVal) maxVal = num;
        }

        // Count frequency of each value
        int[] freq = new int[maxVal + 1];
        for (int num : nums) {
            freq[num]++;
        }

        // For each divisor d, count how many numbers are multiples of d
        long[] countMultiples = new long[maxVal + 1];
        for (int d = 1; d <= maxVal; d++) {
            long cnt = 0;
            for (int multiple = d; multiple <= maxVal; multiple += d) {
                cnt += freq[multiple];
            }
            countMultiples[d] = cnt;
        }

        // Number of pairs whose GCD is divisible by d
        long[] pairsDivisible = new long[maxVal + 1];
        for (int d = 1; d <= maxVal; d++) {
            long c = countMultiples[d];
            pairsDivisible[d] = c * (c - 1) / 2;
        }

        // Inclusion-exclusion: exact number of pairs with each GCD value
        long[] exactPairs = new long[maxVal + 1];
        for (int d = maxVal; d >= 1; d--) {
            long total = pairsDivisible[d];
            for (int multiple = 2 * d; multiple <= maxVal; multiple += d) {
                total -= exactPairs[multiple];
            }
            exactPairs[d] = total;
        }

        // Build prefix sums: prefix[d] = number of pairs with GCD <= d
        long[] prefix = new long[maxVal + 1];
        for (int d = 1; d <= maxVal; d++) {
            prefix[d] = prefix[d - 1] + exactPairs[d];
        }

        // Answer each query via binary search
        int[] ans = new int[queries.length];
        for (int i = 0; i < queries.length; i++) {
            long q = queries[i]; // 0-indexed position in sorted gcdPairs
            int left = 1, right = maxVal;
            while (left < right) {
                int mid = (left + right) / 2;
                if (prefix[mid] > q) {
                    right = mid;
                } else {
                    left = mid + 1;
                }
            }
            ans[i] = left;
        }

        return ans;
    }
}
