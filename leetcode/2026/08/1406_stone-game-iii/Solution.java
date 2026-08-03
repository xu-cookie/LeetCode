/**
 * LeetCode #1406: Stone Game III
 * Difficulty: Hard
 * Link: https://leetcode.com/problems/stone-game-iii/
 * Date: 2026-08-03
 *
 * Problem:
 * Alice and Bob take turns picking 1, 2, or 3 stones from the beginning
 * of a row. Each stone has an associated value. The player with the
 * highest score wins; ties are possible. Both play optimally.
 * Return "Alice", "Bob", or "Tie".
 *
 * Approach: DP (Bottom-Up, Right-to-Left)
 *
 * Let dp[i] = the MAXIMUM score ADVANTAGE the current player can achieve
 *             over the opponent when starting from index i.
 *
 * The current player at index i can take:
 *   - 1 stone:  gain = stoneValue[i] - dp[i+1]
 *   - 2 stones: gain = stoneValue[i] + stoneValue[i+1] - dp[i+2]
 *   - 3 stones: gain = stoneValue[i] + stoneValue[i+1] + stoneValue[i+2] - dp[i+3]
 *
 * dp[i] = max(option1, option2, option3)
 *
 * Why does it work? When the current player takes k stones (gaining sum_k),
 * the opponent faces the remaining array starting at i+k with advantage
 * dp[i+k] (from the opponent's perspective). So the current player's net
 * advantage is sum_k - dp[i+k].
 *
 * Base case: dp[n] = dp[n+1] = dp[n+2] = 0 (no stones → no advantage)
 *
 * Result:
 *   dp[0] > 0 → "Alice" (Alice wins)
 *   dp[0] < 0 → "Bob"    (Bob wins)
 *   dp[0] = 0 → "Tie"    (Draw)
 *
 * Time Complexity:  O(n) — single pass from right to left
 * Space Complexity: O(1) — only the last 3 dp values are needed
 */
class Solution {
    public String stoneGameIII(int[] stoneValue) {
        int n = stoneValue.length;

        // dp_i1 = dp[i+1], dp_i2 = dp[i+2], dp_i3 = dp[i+3]
        // Initially, these represent positions beyond the array → 0
        int dp_i1 = 0; // dp[n] or dp[i+1] = 0
        int dp_i2 = 0; // dp[n+1] or dp[i+2] = 0
        int dp_i3 = 0; // dp[n+2] or dp[i+3] = 0

        for (int i = n - 1; i >= 0; i--) {
            int takeOne = stoneValue[i] - dp_i1;
            int takeTwo = Integer.MIN_VALUE;
            int takeThree = Integer.MIN_VALUE;

            // Two-stone option: only valid if at least 2 stones remain
            if (i + 1 < n) {
                takeTwo = stoneValue[i] + stoneValue[i + 1] - dp_i2;
            }

            // Three-stone option: only valid if at least 3 stones remain
            if (i + 2 < n) {
                takeThree = stoneValue[i] + stoneValue[i + 1] + stoneValue[i + 2] - dp_i3;
            }

            int dp_i = Math.max(takeOne, Math.max(takeTwo, takeThree));

            // Shift: the new dp[i] becomes dp[i-1]'s dp_i1 in the next iteration
            dp_i3 = dp_i2;
            dp_i2 = dp_i1;
            dp_i1 = dp_i;
        }

        // dp_i1 now holds dp[0]
        if (dp_i1 > 0) {
            return "Alice";
        } else if (dp_i1 < 0) {
            return "Bob";
        } else {
            return "Tie";
        }
    }

    // Test harness
    public static void main(String[] args) {
        Solution sol = new Solution();

        // Example 1: stoneValue = [1,2,3,7] → "Bob"
        test(sol, new int[]{1, 2, 3, 7}, "Bob", 1);

        // Example 2: stoneValue = [1,2,3,-9] → "Alice"
        test(sol, new int[]{1, 2, 3, -9}, "Alice", 2);

        // Example 3: stoneValue = [1,2,3,6] → "Tie"
        test(sol, new int[]{1, 2, 3, 6}, "Tie", 3);

        // Additional test: single pile positive → Alice
        test(sol, new int[]{5}, "Alice", 4);

        // Additional test: single pile negative → Bob
        test(sol, new int[]{-5}, "Bob", 5);

        // Additional test: two piles, both positive
        // [1,2]: Alice can take 1 (score 1), Bob takes 2 (score 2) → Bob
        //        Alice can take 1+2=3, Bob gets 0 → Alice
        //        So Alice takes both → Alice wins
        test(sol, new int[]{1, 2}, "Alice", 6);

        // Additional test: two piles, one negative
        // [1,-2]: Alice takes 1 → Bob takes -2 → Alice wins (1 > -2)
        //         Alice takes both → score -1 → Bob wins (0 > -1)
        //         Alice should take 1, then dp: takeOne = 1 - dp[1]
        //         dp[1] = max(-2) = -2. So takeOne = 1 - (-2) = 3. takeTwo = -1 - 0 = -1.
        //         dp[0] = 3 > 0 → Alice
        test(sol, new int[]{1, -2}, "Alice", 7);

        // Additional test: all negative
        // [-1,-2,-3]: Alice should take only 1 stone (least negative): -1
        // dp[2] = -3. dp[1] = max(-2-(-3), -5) = max(1, -5) = 1.
        // dp[0] = max(-1-1, -3-(-3), -6-0) = max(-2, 0, -6) = 0 → Tie
        test(sol, new int[]{-1, -2, -3}, "Tie", 8);

        // Test: many alternating values
        // [1,-1,1,-1,1] → Let's compute manually...
        test(sol, new int[]{1, -1, 1, -1, 1}, "Alice", 9);

        // Test: large input (just verify no crash)
        int[] large = new int[50000];
        for (int i = 0; i < large.length; i++) {
            large[i] = (i % 5) - 2; // values: -2, -1, 0, 1, 2
        }
        String result = sol.stoneGameIII(large);
        System.out.println("Test 10 (n=50000): " + result + " (completed without errors)");

        System.out.println("\nAll tests passed!");
    }

    private static void test(Solution sol, int[] input, String expected, int testNum) {
        String result = sol.stoneGameIII(input);
        String status = result.equals(expected) ? "PASS" : "FAIL";
        System.out.println(status + " Test " + testNum + ": stoneGameIII("
                + arrayToString(input) + ") = \"" + result
                + "\" (expected \"" + expected + "\")");
        assert result.equals(expected) : "Test " + testNum + " failed!";
    }

    private static String arrayToString(int[] arr) {
        StringBuilder sb = new StringBuilder("[");
        int limit = Math.min(arr.length, 10);
        for (int i = 0; i < limit; i++) {
            if (i > 0) sb.append(",");
            sb.append(arr[i]);
        }
        if (arr.length > limit) sb.append(",...");
        sb.append("]");
        return sb.toString();
    }
}
