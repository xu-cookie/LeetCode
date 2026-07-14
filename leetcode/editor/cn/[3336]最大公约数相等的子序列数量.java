/**
 * LeetCode #3336 - Find the Number of Subsequences With Equal GCD
 * Difficulty: Hard
 * Link: https://leetcode.com/problems/find-the-number-of-subsequences-with-equal-gcd/
 * Date Solved: 2026-07-14
 *
 * Approach: Dynamic Programming over GCD values
 *
 * Since nums[i] <= 200, possible GCD values are also bounded by 200.
 * We use dp[g1][g2] to represent the number of ways to process elements so far,
 * where g1 is the GCD of seq1 (0 if empty) and g2 is the GCD of seq2 (0 if empty).
 *
 * For each element x, we have three choices:
 *   1. Skip x (don't use it)
 *   2. Add x to seq1: new_g1 = (g1 == 0 ? x : gcd(g1, x))
 *   3. Add x to seq2: new_g2 = (g2 == 0 ? x : gcd(g2, x))
 *
 * At the end, the answer is the sum of dp[g][g] for all g > 0 (both non-empty
 * and GCDs equal).
 *
 * Time Complexity:  O(n * M^2) where M = max(nums) <= 200, n <= 200
 *                   => ~8 million operations, very fast
 * Space Complexity: O(M^2) = O(40000)
 */

import java.util.*;

class Solution {
    private static final int MOD = 1_000_000_007;

    public int subsequencePairCount(int[] nums) {
        // Find the maximum value to bound our DP table
        int M = 0;
        for (int x : nums) {
            if (x > M) M = x;
        }

        // dp[g1][g2]: g=0 means empty subsequence, g>0 means current GCD
        int[][] dp = new int[M + 1][M + 1];
        dp[0][0] = 1; // both empty initially

        for (int x : nums) {
            // Copy current dp for the "skip" case
            int[][] ndp = new int[M + 1][M + 1];
            for (int i = 0; i <= M; i++) {
                System.arraycopy(dp[i], 0, ndp[i], 0, M + 1);
            }

            for (int g1 = 0; g1 <= M; g1++) {
                for (int g2 = 0; g2 <= M; g2++) {
                    int val = dp[g1][g2];
                    if (val == 0) continue;

                    // Add x to seq1
                    int ng1 = (g1 == 0) ? x : gcd(g1, x);
                    ndp[ng1][g2] = (ndp[ng1][g2] + val) % MOD;

                    // Add x to seq2
                    int ng2 = (g2 == 0) ? x : gcd(g2, x);
                    ndp[g1][ng2] = (ndp[g1][ng2] + val) % MOD;
                }
            }
            dp = ndp;
        }

        // Sum up all states where both subsequences are non-empty and GCDs equal
        int ans = 0;
        for (int g = 1; g <= M; g++) {
            ans = (ans + dp[g][g]) % MOD;
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
