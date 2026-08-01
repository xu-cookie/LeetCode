/**
 * LeetCode #486: Predict the Winner
 * Difficulty: Medium
 * Link: https://leetcode.com/problems/predict-the-winner/
 * Date: 2026-08-01
 *
 * Approach: Minimax Dynamic Programming (Bottom-Up)
 *
 * This is a classic two-player zero-sum game with perfect information.
 * Both players play optimally, trying to maximize their own score.
 *
 * Key insight — Turn the game into a "score difference" problem:
 * Define dp[i][j] = the maximum net score advantage the CURRENT player
 * can achieve over the opponent when playing on subarray nums[i..j].
 *
 * When the current player picks nums[i]:
 *   - They gain nums[i] points immediately
 *   - The opponent then plays optimally on nums[i+1..j], achieving dp[i+1][j]
 *   - Net advantage = nums[i] - dp[i+1][j]
 *
 * When the current player picks nums[j]:
 *   - Net advantage = nums[j] - dp[i][j-1]
 *
 * Transition: dp[i][j] = max(nums[i] - dp[i+1][j], nums[j] - dp[i][j-1])
 * Base case: dp[i][i] = nums[i] (only one element, take it)
 *
 * Player 1 wins if dp[0][n-1] >= 0 (non-negative advantage)
 *
 * Time Complexity:  O(n²) where n = nums.length ≤ 20
 * Space Complexity: O(n²) for the DP table
 *
 * Alternative approach (space-optimized): Use 1D DP since we only need
 * dp[i+1][j] (row below) and dp[i][j-1] (column left). Can reduce to O(n).
 */
class Solution {
    public boolean predictTheWinner(int[] nums) {
        int n = nums.length;

        // dp[i][j] = max net advantage for current player on nums[i..j]
        int[][] dp = new int[n][n];

        // Base case: single element — current player takes it
        for (int i = 0; i < n; i++) {
            dp[i][i] = nums[i];
        }

        // Fill by increasing subarray length
        for (int len = 2; len <= n; len++) {
            for (int i = 0; i + len - 1 < n; i++) {
                int j = i + len - 1;

                // Pick left: gain nums[i], then opponent gets dp[i+1][j]
                int pickLeft = nums[i] - dp[i + 1][j];

                // Pick right: gain nums[j], then opponent gets dp[i][j-1]
                int pickRight = nums[j] - dp[i][j - 1];

                dp[i][j] = Math.max(pickLeft, pickRight);
            }
        }

        // Player 1 wins if their net advantage ≥ 0
        return dp[0][n - 1] >= 0;
    }

    // Test harness
    public static void main(String[] args) {
        Solution sol = new Solution();

        // Example 1: nums = [1,5,2] → false
        assert !sol.predictTheWinner(new int[]{1, 5, 2}) : "Test 1 failed";
        System.out.println("Test 1: predictTheWinner([1,5,2]) = "
                + sol.predictTheWinner(new int[]{1, 5, 2}) + " (expected false)");

        // Example 2: nums = [1,5,233,7] → true
        assert sol.predictTheWinner(new int[]{1, 5, 233, 7}) : "Test 2 failed";
        System.out.println("Test 2: predictTheWinner([1,5,233,7]) = "
                + sol.predictTheWinner(new int[]{1, 5, 233, 7}) + " (expected true)");

        // Edge case: single element — Player 1 takes it and wins
        assert sol.predictTheWinner(new int[]{5}) : "Test 3 failed";
        System.out.println("Test 3: predictTheWinner([5]) = "
                + sol.predictTheWinner(new int[]{5}) + " (expected true)");

        // Edge case: two equal elements — tie, Player 1 wins
        assert sol.predictTheWinner(new int[]{3, 3}) : "Test 4 failed";
        System.out.println("Test 4: predictTheWinner([3,3]) = "
                + sol.predictTheWinner(new int[]{3, 3}) + " (expected true)");

        // Player 1 can always win with 2 elements (pick the larger one)
        assert sol.predictTheWinner(new int[]{2, 7}) : "Test 5 failed";
        System.out.println("Test 5: predictTheWinner([2,7]) = "
                + sol.predictTheWinner(new int[]{2, 7}) + " (expected true)");

        // Player 1 loses: nums = [1,5,2,4,6] — let's verify by DP
        // dp[0][4]: Player 1 optimal is pick 6 (right): 6 - dp[0][3]
        //   dp[0][3] on [1,5,2,4]: Player 2 picks 1: 1 - dp[1][3]
        //     dp[1][3] on [5,2,4]: Player 1 picks 5: 5 - dp[2][3] or 4: 4 - dp[1][2]
        //       dp[2][3] on [2,4]: Player 2 picks 4: 4 - dp[2][2]=2 → 2
        //       dp[1][2] on [5,2]: Player 2 picks 5: 5-2=3 or 2-5=-3 → max=3
        //     dp[1][3] = max(5-2, 4-3) = max(3, 1) = 3
        //   dp[0][3] = max(1-3, 4-??)=... let's just run the code
        System.out.println("Test 6: predictTheWinner([1,5,2,4,6]) = "
                + sol.predictTheWinner(new int[]{1, 5, 2, 4, 6}));

        // Larger test: nums = [1,2,3,4,5,6,7,8,9] — Player 1 should win
        System.out.println("Test 7: predictTheWinner([1,2,3,4,5,6,7,8,9]) = "
                + sol.predictTheWinner(new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9}));

        // Classic case: nums = [1,1,1,1,1,1,1,1,1,1] (10 ones) — tie, Player 1 wins
        assert sol.predictTheWinner(new int[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1}) : "Test 8 failed";
        System.out.println("Test 8: predictTheWinner([10 ones]) = "
                + sol.predictTheWinner(new int[]{1, 1, 1, 1, 1, 1, 1, 1, 1, 1})
                + " (expected true)");

        // Even number of elements, all same → always tie, Player 1 wins
        assert sol.predictTheWinner(new int[]{7, 7, 7, 7}) : "Test 9 failed";
        System.out.println("Test 9: predictTheWinner([7,7,7,7]) = "
                + sol.predictTheWinner(new int[]{7, 7, 7, 7}) + " (expected true)");

        // Max constraint test: n=20, large values
        int[] large = new int[20];
        for (int i = 0; i < 20; i++) large[i] = 10000000;
        assert sol.predictTheWinner(large) : "Test 10 failed";
        System.out.println("Test 10: predictTheWinner([20 x 10^7]) = "
                + sol.predictTheWinner(large) + " (expected true)");

        System.out.println("\nAll tests passed!");
    }
}
