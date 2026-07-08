/**
 * LeetCode Daily Challenge - 2026-07-08
 * Problem: 3756. Concatenate Non-Zero Digits and Multiply by Sum II
 * Difficulty: Medium
 * Link: https://leetcode.com/problems/concatenate-non-zero-digits-and-multiply-by-sum-ii/
 *
 * Problem Description:
 * Given a string s of length m consisting of digits and a 2D integer array queries
 * where queries[i] = [l_i, r_i]. For each query, extract substring s[l_i..r_i],
 * form x by concatenating all non-zero digits, let sum be the sum of digits in x,
 * and return (x * sum) % (10^9 + 7).
 *
 * Approach: Prefix Sums + Modular Arithmetic
 *
 * Key Insight:
 * For a substring [l, r], let:
 *   - A = x-value of non-zero digits in s[0..l-1] (the prefix before the query)
 *   - B = x-value of non-zero digits in s[l..r] (what we want)
 *   - |B| = count of non-zero digits in s[l..r]
 *
 * Then prefixX[r] represents (A followed by B):
 *   prefixX[r] = A * 10^|B| + B
 *   => B = prefixX[r] - A * 10^|B|
 *
 * Similarly, the sum of non-zero digits in [l, r] is:
 *   sum = prefixSum[r] - prefixSum[l-1]
 *
 * Algorithm:
 *   1. Precompute prefix arrays:
 *      - prefixX[i]:    x-value formed by non-zero digits in s[0..i]  (mod MOD)
 *      - prefixCnt[i]:  count of non-zero digits in s[0..i]
 *      - prefixSum[i]:  sum of non-zero digits in s[0..i]
 *      - pow10[i]:      10^i % MOD
 *   2. For each query [l, r]:
 *      - cnt = prefixCnt[r] - prefixCnt[l-1]
 *      - x = (prefixX[r] - prefixX[l-1] * pow10[cnt]) % MOD
 *      - sum = prefixSum[r] - prefixSum[l-1]
 *      - answer[i] = (x * sum) % MOD
 *
 * Time Complexity:  O(m + q) where m = s.length(), q = queries.length
 * Space Complexity: O(m) for prefix arrays
 */

class Solution {
    private static final int MOD = 1_000_000_007;

    public int[] sumAndMultiply(String s, int[][] queries) {
        int m = s.length();
        int q = queries.length;

        // Prefix arrays: store values including current position
        long[] prefixX = new long[m];     // x-value of non-zero digits in s[0..i]
        int[] prefixCnt = new int[m];     // count of non-zero digits in s[0..i]
        int[] prefixSum = new int[m];     // sum of non-zero digits in s[0..i]

        // Build prefix arrays
        long curX = 0;
        int curCnt = 0;
        int curSum = 0;
        for (int i = 0; i < m; i++) {
            char c = s.charAt(i);
            if (c != '0') {
                int digit = c - '0';
                curX = (curX * 10 + digit) % MOD;
                curCnt++;
                curSum += digit;
            }
            prefixX[i] = curX;
            prefixCnt[i] = curCnt;
            prefixSum[i] = curSum;
        }

        // Precompute powers of 10 up to m (max possible non-zero count)
        long[] pow10 = new long[m + 1];
        pow10[0] = 1;
        for (int i = 1; i <= m; i++) {
            pow10[i] = (pow10[i - 1] * 10) % MOD;
        }

        // Answer each query
        int[] answer = new int[q];
        for (int i = 0; i < q; i++) {
            int l = queries[i][0];
            int r = queries[i][1];

            int cnt = prefixCnt[r] - (l > 0 ? prefixCnt[l - 1] : 0);
            int sum = prefixSum[r] - (l > 0 ? prefixSum[l - 1] : 0);

            long prevX = l > 0 ? prefixX[l - 1] : 0;
            long x = (prefixX[r] - prevX * pow10[cnt] % MOD + MOD) % MOD;

            answer[i] = (int) ((x * sum) % MOD);
        }

        return answer;
    }

    // Test harness — run locally to verify correctness
    public static void main(String[] args) {
        Solution sol = new Solution();

        // Example 1
        int[] r1 = sol.sumAndMultiply("10203004", new int[][]{{0, 7}, {1, 3}, {4, 6}});
        System.out.print("Example 1: [");
        for (int i = 0; i < r1.length; i++) {
            if (i > 0) System.out.print(", ");
            System.out.print(r1[i]);
        }
        System.out.println("] (expected [12340, 4, 9]) — "
                + (r1[0] == 12340 && r1[1] == 4 && r1[2] == 9 ? "PASS" : "FAIL"));

        // Example 2
        int[] r2 = sol.sumAndMultiply("1000", new int[][]{{0, 3}, {1, 1}});
        System.out.print("Example 2: [");
        for (int i = 0; i < r2.length; i++) {
            if (i > 0) System.out.print(", ");
            System.out.print(r2[i]);
        }
        System.out.println("] (expected [1, 0]) — "
                + (r2[0] == 1 && r2[1] == 0 ? "PASS" : "FAIL"));

        // Example 3: verifies modulo
        int[] r3 = sol.sumAndMultiply("9876543210", new int[][]{{0, 9}});
        System.out.println("Example 3: " + r3[0] + " (expected 444444137) — "
                + (r3[0] == 444444137 ? "PASS" : "FAIL"));

        // Edge case: single character, non-zero
        int[] r4 = sol.sumAndMultiply("5", new int[][]{{0, 0}});
        System.out.println("\"5\": " + r4[0] + " (expected 25) — "
                + (r4[0] == 25 ? "PASS" : "FAIL"));

        // Edge case: single character, zero
        int[] r5 = sol.sumAndMultiply("0", new int[][]{{0, 0}});
        System.out.println("\"0\": " + r5[0] + " (expected 0) — "
                + (r5[0] == 0 ? "PASS" : "FAIL"));

        // Edge case: all zeros
        int[] r6 = sol.sumAndMultiply("0000", new int[][]{{0, 3}, {1, 2}});
        System.out.print("all zeros: [");
        for (int i = 0; i < r6.length; i++) {
            if (i > 0) System.out.print(", ");
            System.out.print(r6[i]);
        }
        System.out.println("] (expected [0, 0]) — "
                + (r6[0] == 0 && r6[1] == 0 ? "PASS" : "FAIL"));

        // Edge case: no zeros in string
        int[] r7 = sol.sumAndMultiply("123", new int[][]{{0, 2}});
        System.out.println("\"123\": " + r7[0] + " (expected 738) — "
                + (r7[0] == 738 ? "PASS" : "FAIL"));
        // x=123, sum=6, 123*6=738

        // Stress: large range overlapping
        int[] r8 = sol.sumAndMultiply("10203", new int[][]{{0, 4}, {0, 2}, {1, 3}});
        System.out.print("\"10203\" multi-query: [");
        for (int i = 0; i < r8.length; i++) {
            if (i > 0) System.out.print(", ");
            System.out.print(r8[i]);
        }
        // [0,4]: digits 1,2,3 -> x=123, sum=6 -> 738
        // [0,2]: digits 1,2 -> x=12, sum=3 -> 36
        // [1,3]: digits 0,2,0 -> x=2, sum=2 -> 4
        System.out.println("] (expected [738, 36, 4]) — "
                + (r8[0] == 738 && r8[1] == 36 && r8[2] == 4 ? "PASS" : "FAIL"));

        // Large value test
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 100000; i++) {
            sb.append((char) ('1' + (i % 9)));  // digits 1-9 only, no zeros
        }
        int[] r9 = sol.sumAndMultiply(sb.toString(), new int[][]{{0, 99999}});
        System.out.println("100k non-zero digits: " + r9[0] + " (should be non-negative) — "
                + (r9[0] >= 0 ? "PASS" : "FAIL"));
    }
}
