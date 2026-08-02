/**
 * LeetCode #877: Stone Game
 * Difficulty: Medium
 * Link: https://leetcode.com/problems/stone-game/
 * Date: 2026-08-02
 *
 * Approach 1 (Mathematical): Alice Always Wins
 *
 * This problem has a beautiful mathematical property: with an even number
 * of piles and an odd total sum, Alice can ALWAYS force a win.
 *
 * Proof:
 * 1. The piles can be partitioned into two groups by index parity:
 *    - Group Even: piles[0], piles[2], piles[4], ...
 *    - Group Odd:  piles[1], piles[3], piles[5], ...
 * 2. Since the total sum is odd, sum(Even) ≠ sum(Odd).
 * 3. Alice, going first, can guarantee she collects all piles from the
 *    larger group. How?
 *    - If she wants Even-indexed piles, she takes piles[0] (an even-indexed
 *      pile). Now both ends (piles[1] and piles[n-1]) are odd-indexed.
 *      Whatever Bob picks, the next turn both ends are even-indexed again.
 *      Alice can always pick an even-indexed pile.
 *    - Similarly, if she wants Odd-indexed piles, she takes piles[n-1]
 *      (which is odd-indexed since n is even), and the same logic applies.
 *
 * Therefore, Alice can always guarantee victory. The answer is always true.
 *
 * Time Complexity:  O(1)
 * Space Complexity: O(1)
 *
 * ------------------------------------------------------------------
 *
 * Approach 2 (DP / Minimax): General Solution
 *
 * Same DP formulation as "Predict the Winner" (#486):
 *   dp[i][j] = max(piles[i] - dp[i+1][j], piles[j] - dp[i][j-1])
 *
 * dp[i][j] represents the maximum score advantage the current player
 * can achieve over the opponent when playing on piles[i..j].
 *
 * Alice wins if dp[0][n-1] > 0 (strictly greater, since total is odd).
 *
 * Time Complexity:  O(n²) where n = piles.length ≤ 500
 * Space Complexity: O(n²) or O(n) with 1D optimization
 */
class Solution {
    // Approach 1: Mathematical — O(1) time, O(1) space
    public boolean stoneGame(int[] piles) {
        // With even number of piles and odd total sum,
        // Alice can always force a win by controlling parity.
        return true;
    }

    // Approach 2: DP (Minimax) — O(n²) time, O(n²) space
    // Uncomment to use:
    // public boolean stoneGame(int[] piles) {
    //     int n = piles.length;
    //     int[][] dp = new int[n][n];
    //
    //     for (int i = 0; i < n; i++) {
    //         dp[i][i] = piles[i];
    //     }
    //
    //     for (int len = 2; len <= n; len++) {
    //         for (int i = 0; i + len - 1 < n; i++) {
    //             int j = i + len - 1;
    //             dp[i][j] = Math.max(
    //                 piles[i] - dp[i + 1][j],
    //                 piles[j] - dp[i][j - 1]
    //             );
    //         }
    //     }
    //
    //     return dp[0][n - 1] > 0;
    // }

    // Approach 3: Space-optimized DP — O(n²) time, O(n) space
    // public boolean stoneGame(int[] piles) {
    //     int n = piles.length;
    //     int[] dp = piles.clone(); // dp[j] for current row i
    //
    //     for (int i = n - 2; i >= 0; i--) {
    //         for (int j = i + 1; j < n; j++) {
    //             dp[j] = Math.max(piles[i] - dp[j], piles[j] - dp[j - 1]);
    //         }
    //     }
    //
    //     return dp[n - 1] > 0;
    // }

    // Test harness
    public static void main(String[] args) {
        Solution sol = new Solution();
        SolutionDP solDP = new SolutionDP();

        // Example 1: piles = [5,3,4,5] → true
        assert sol.stoneGame(new int[]{5, 3, 4, 5}) : "Test 1 failed";
        assert solDP.stoneGame(new int[]{5, 3, 4, 5}) : "Test 1 DP failed";
        System.out.println("Test 1: stoneGame([5,3,4,5]) = "
                + sol.stoneGame(new int[]{5, 3, 4, 5}) + " (expected true)");

        // Example 2: piles = [3,7,2,3] → true
        assert sol.stoneGame(new int[]{3, 7, 2, 3}) : "Test 2 failed";
        assert solDP.stoneGame(new int[]{3, 7, 2, 3}) : "Test 2 DP failed";
        System.out.println("Test 2: stoneGame([3,7,2,3]) = "
                + sol.stoneGame(new int[]{3, 7, 2, 3}) + " (expected true)");

        // Test 3: Minimum case (n=2, odd sum)
        assert sol.stoneGame(new int[]{1, 2}) : "Test 3 failed";
        assert solDP.stoneGame(new int[]{1, 2}) : "Test 3 DP failed";
        System.out.println("Test 3: stoneGame([1,2]) = "
                + sol.stoneGame(new int[]{1, 2}) + " (expected true)");

        // Test 4: Larger case, n=6
        assert sol.stoneGame(new int[]{1, 3, 5, 7, 9, 11}) : "Test 4 failed";
        assert solDP.stoneGame(new int[]{1, 3, 5, 7, 9, 11}) : "Test 4 DP failed";
        System.out.println("Test 4: stoneGame([1,3,5,7,9,11]) = "
                + sol.stoneGame(new int[]{1, 3, 5, 7, 9, 11}) + " (expected true)");

        // Test 5: Random case, n=8
        assert sol.stoneGame(new int[]{7, 1, 5, 3, 6, 2, 9, 4}) : "Test 5 failed";
        assert solDP.stoneGame(new int[]{7, 1, 5, 3, 6, 2, 9, 4}) : "Test 5 DP failed";
        System.out.println("Test 5: stoneGame([7,1,5,3,6,2,9,4]) = "
                + sol.stoneGame(new int[]{7, 1, 5, 3, 6, 2, 9, 4}) + " (expected true)");

        // Test 6: Symmetric but odd total, n=4
        // piles = [3,2,2,2], sum=9(odd). Even:3+2=5, Odd:2+2=4, Alice wins
        assert sol.stoneGame(new int[]{3, 2, 2, 2}) : "Test 6 failed";
        assert solDP.stoneGame(new int[]{3, 2, 2, 2}) : "Test 6 DP failed";
        System.out.println("Test 6: stoneGame([3,2,2,2]) = "
                + sol.stoneGame(new int[]{3, 2, 2, 2}) + " (expected true)");

        // Test 7: One side heavily weighted, n=4
        // piles = [1,100,2,3], sum=106(even→must be odd per constraints, but let's try [1,100,2,4])
        // sum=107(odd). Even:1+2=3, Odd:100+4=104. Alice takes piles[3]=4 (odd group)...
        assert sol.stoneGame(new int[]{1, 100, 2, 4}) : "Test 7 failed";
        assert solDP.stoneGame(new int[]{1, 100, 2, 4}) : "Test 7 DP failed";
        System.out.println("Test 7: stoneGame([1,100,2,4]) = "
                + sol.stoneGame(new int[]{1, 100, 2, 4}) + " (expected true)");

        // Test 8: Large n, max constraint
        int[] large = new int[500];
        for (int i = 0; i < 500; i++) {
            large[i] = (i % 3) + 1; // values 1,2,3 repeating; sum ≈ 1000, odd? Let's verify
        }
        // Sum of 1,2,3 repeating 166 full cycles (498 elements) + 2 more = odd check
        // Each cycle sum = 6. 166*6 = 996. + last 2: 1+2=3. Total = 999 (odd) ✓
        assert sol.stoneGame(large) : "Test 8 failed (mathematical)";
        assert solDP.stoneGame(large) : "Test 8 failed (DP)";
        System.out.println("Test 8: stoneGame([500 elements]) = "
                + sol.stoneGame(large) + " (expected true)");

        // Test 9: Verify DP returns true for all even-length, odd-sum arrays
        int[][] testCases = {
            {7, 8},                    // sum=15 odd
            {1, 1, 1, 2},             // sum=5 odd
            {5, 1, 1, 1, 1, 2},       // sum=11 odd
            {3, 3, 3, 3, 3, 3, 3, 2}, // sum=23 odd
        };
        for (int t = 0; t < testCases.length; t++) {
            assert solDP.stoneGame(testCases[t]) : "Test 9." + t + " DP failed";
            System.out.println("Test 9." + t + ": DP result = "
                    + solDP.stoneGame(testCases[t]) + " (expected true)");
        }

        System.out.println("\nAll tests passed! Alice always wins.");
    }
}

/**
 * DP (Minimax) version for verification.
 *
 * This is the general solution that works for ANY game of this type,
 * not just the special case handled by the mathematical solution above.
 *
 * dp[i][j] = maximum score advantage the CURRENT player can achieve
 *            over the opponent when playing on piles[i..j].
 *
 * Transition:
 *   Pick left:  piles[i] - dp[i+1][j]
 *   Pick right: piles[j] - dp[i][j-1]
 *   dp[i][j] = max(pickLeft, pickRight)
 */
class SolutionDP {
    public boolean stoneGame(int[] piles) {
        int n = piles.length;

        // Space-optimized 1D DP
        int[] dp = piles.clone();

        for (int i = n - 2; i >= 0; i--) {
            for (int j = i + 1; j < n; j++) {
                dp[j] = Math.max(piles[i] - dp[j], piles[j] - dp[j - 1]);
            }
        }

        return dp[n - 1] > 0;
    }
}
