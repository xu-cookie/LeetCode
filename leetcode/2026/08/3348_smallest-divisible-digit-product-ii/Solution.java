/**
 * LeetCode Daily Challenge - 2026-08-07 (补做)
 * Problem: 3348. Smallest Divisible Digit Product II
 * Difficulty: Hard
 * Link: https://leetcode.com/problems/smallest-divisible-digit-product-ii/
 *
 * Problem Description:
 * Given a string num (positive integer) and an integer t.
 * A number is "zero-free" if none of its digits are 0.
 * Return the smallest zero-free number >= num such that the product of
 * its digits is divisible by t. If none exists, return "-1".
 *
 * Approach:
 * 1. Factorize t — only prime factors 2, 3, 5, 7 are possible (digits 1-9).
 *    Any other factor → impossible, return "-1".
 * 2. DP: minLen[e2][e3][e5][e7] = minimum digits needed to achieve at least
 *    those prime exponents. State space ≈ 48×30×21×17 ≈ 500K.
 * 3. Check if num itself works (zero-free + product divisible by t).
 * 4. Same-length: scan right to left, try to increase one digit, fill suffix
 *    with lexicographically smallest valid digits.
 * 5. Longer length: if same-length fails, try with length+1 (prepend "1" and
 *    construct optimal suffix).
 *
 * Time Complexity: O(STATES * 9 + |num|) ≈ O(|num|)
 * Space Complexity: O(STATES) ≈ 500K ints ≈ 2 MB
 */

class Solution {
    // Prime factor contributions for each digit 1-9
    // indexed by digit: {e2, e3, e5, e7}
    static final int[][] CONTRIB = {
        {0, 0, 0, 0}, // unused (digit 0)
        {0, 0, 0, 0}, // 1
        {1, 0, 0, 0}, // 2
        {0, 1, 0, 0}, // 3
        {2, 0, 0, 0}, // 4
        {0, 0, 1, 0}, // 5
        {1, 1, 0, 0}, // 6
        {0, 0, 0, 1}, // 7
        {3, 0, 0, 0}, // 8
        {0, 2, 0, 0}, // 9
    };

    static final int INF = Integer.MAX_VALUE / 2;

    public String smallestNumber(String num, long t) {
        // ── 1. Factorize t ──
        int[] needs = factorize(t);
        if (needs == null) return "-1";

        int MAX2 = needs[0], MAX3 = needs[1], MAX5 = needs[2], MAX7 = needs[3];
        int D2 = MAX2 + 1, D3 = MAX3 + 1, D5 = MAX5 + 1, D7 = MAX7 + 1;

        // ── 2. Precompute minLen DP ──
        // minLen[e2][e3][e5][e7] = min digits to cover at least those exponents
        int[][][][] minLen = computeMinLen(MAX2, MAX3, MAX5, MAX7);

        // ── 3. Check if num itself works ──
        int m = num.length();
        if (numWorks(num, MAX2, MAX3, MAX5, MAX7)) {
            return num;
        }

        // ── 4. Precompute prefix info ──
        // prefixZero[i] = true if num[0..i-1] contains '0'
        boolean[] prefixZero = new boolean[m + 1];
        // prefixExp[i][p] = exponents of prime p from num[0..i-1], capped at MAX
        int[][] prefixExp = new int[m + 1][4]; // [e2, e3, e5, e7]

        for (int i = 0; i < m; i++) {
            int d = num.charAt(i) - '0';
            prefixZero[i + 1] = prefixZero[i] || (d == 0);
            for (int p = 0; p < 4; p++) {
                int max = (p == 0) ? MAX2 : (p == 1) ? MAX3 : (p == 2) ? MAX5 : MAX7;
                prefixExp[i + 1][p] = Math.min(max,
                    prefixExp[i][p] + (d == 0 ? 0 : CONTRIB[d][p]));
            }
        }

        // ── 5. Try same-length answer ──
        // Scan right to left, try to increase one digit
        for (int i = m - 1; i >= 0; i--) {
            // Prefix must be zero-free
            if (prefixZero[i]) continue;

            int d = num.charAt(i) - '0';

            // Remaining needs after prefix
            int[] pne = new int[4];
            int[] maxArr = {MAX2, MAX3, MAX5, MAX7};
            for (int p = 0; p < 4; p++) {
                pne[p] = Math.max(0, maxArr[p] - prefixExp[i][p]);
            }

            int startDigit = (d == 0) ? 1 : d + 1;

            for (int nd = startDigit; nd <= 9; nd++) {
                int[] sne = new int[4];
                for (int p = 0; p < 4; p++) {
                    sne[p] = Math.max(0, pne[p] - CONTRIB[nd][p]);
                }
                int rem = m - i - 1;

                if (minLen[sne[0]][sne[1]][sne[2]][sne[3]] <= rem) {
                    // Found answer with same length!
                    StringBuilder ans = new StringBuilder();
                    ans.append(num, 0, i);           // prefix
                    ans.append((char) ('0' + nd));   // increased digit
                    // Lexicographically smallest suffix
                    buildSuffix(ans, sne[0], sne[1], sne[2], sne[3], rem, minLen);
                    return ans.toString();
                }
            }
        }

        // ── 6. Try longer length ──
        int minDigits = minLen[MAX2][MAX3][MAX5][MAX7];
        if (minDigits == INF) return "-1";
        int targetLen = Math.max(m + 1, minDigits);

        StringBuilder ans = new StringBuilder();
        buildSuffix(ans, MAX2, MAX3, MAX5, MAX7, targetLen, minLen);
        return ans.toString();
    }

    // ── Factorize t into {e2, e3, e5, e7}, or null if impossible ──
    private int[] factorize(long t) {
        int e2 = 0, e3 = 0, e5 = 0, e7 = 0;
        long x = t;
        while (x % 2 == 0) { e2++; x /= 2; }
        while (x % 3 == 0) { e3++; x /= 3; }
        while (x % 5 == 0) { e5++; x /= 5; }
        while (x % 7 == 0) { e7++; x /= 7; }
        if (x != 1) return null; // has prime factor > 7: impossible
        return new int[]{e2, e3, e5, e7};
    }

    // ── DP: min digits to achieve at least (e2,e3,e5,e7) exponents ──
    private int[][][][] computeMinLen(int M2, int M3, int M5, int M7) {
        int D2 = M2 + 1, D3 = M3 + 1, D5 = M5 + 1, D7 = M7 + 1;
        int[][][][] dp = new int[D2][D3][D5][D7];

        // Initialize with INF
        for (int a = 0; a < D2; a++) {
            for (int b = 0; b < D3; b++) {
                for (int c = 0; c < D5; c++) {
                    for (int d = 0; d < D7; d++) {
                        dp[a][b][c][d] = INF;
                    }
                }
            }
        }
        dp[0][0][0][0] = 0;

        // DP: increasing order guarantees optimal substructure
        for (int a = 0; a < D2; a++) {
            for (int b = 0; b < D3; b++) {
                for (int c = 0; c < D5; c++) {
                    for (int d = 0; d < D7; d++) {
                        int cur = dp[a][b][c][d];
                        if (cur == INF) continue;
                        for (int digit = 1; digit <= 9; digit++) {
                            int na = Math.min(M2, a + CONTRIB[digit][0]);
                            int nb = Math.min(M3, b + CONTRIB[digit][1]);
                            int nc = Math.min(M5, c + CONTRIB[digit][2]);
                            int nd = Math.min(M7, d + CONTRIB[digit][3]);
                            if (cur + 1 < dp[na][nb][nc][nd]) {
                                dp[na][nb][nc][nd] = cur + 1;
                            }
                        }
                    }
                }
            }
        }
        return dp;
    }

    // ── Build lexicographically smallest suffix ──
    private void buildSuffix(StringBuilder sb, int e2, int e3, int e5, int e7,
                             int len, int[][][][] minLen) {
        while (len > 0) {
            for (int d = 1; d <= 9; d++) {
                int ne2 = Math.max(0, e2 - CONTRIB[d][0]);
                int ne3 = Math.max(0, e3 - CONTRIB[d][1]);
                int ne5 = Math.max(0, e5 - CONTRIB[d][2]);
                int ne7 = Math.max(0, e7 - CONTRIB[d][3]);
                if (minLen[ne2][ne3][ne5][ne7] <= len - 1) {
                    sb.append((char) ('0' + d));
                    e2 = ne2; e3 = ne3; e5 = ne5; e7 = ne7;
                    break;
                }
            }
            len--;
        }
    }

    // ── Check if num itself is zero-free and has required divisibility ──
    private boolean numWorks(String num, int M2, int M3, int M5, int M7) {
        int e2 = 0, e3 = 0, e5 = 0, e7 = 0;
        for (int i = 0; i < num.length(); i++) {
            int d = num.charAt(i) - '0';
            if (d == 0) return false; // zero not allowed
            e2 += CONTRIB[d][0];
            e3 += CONTRIB[d][1];
            e5 += CONTRIB[d][2];
            e7 += CONTRIB[d][3];
        }
        return e2 >= M2 && e3 >= M3 && e5 >= M5 && e7 >= M7;
    }

    // ═══════════════════════════════════════════════════════════════
    // Test harness
    // ═══════════════════════════════════════════════════════════════
    public static void main(String[] args) {
        Solution sol = new Solution();

        // Example 1
        test(sol, "1234", 256, "1488");

        // Example 2
        test(sol, "12355", 50, "12355");

        // Example 3
        test(sol, "11111", 26, "-1");

        // Additional: t has prime factor 11 (impossible)
        test(sol, "123", 11, "-1");

        // Additional: t=1 (always divisible)
        test(sol, "100", 1, "111");

        // Additional: need to increase first digit
        test(sol, "99", 10, "125"); // 2*5=10, need digits with 2 and 5

        // Additional: t needs more digits (2^8=256, min 3 digits: 4*8*8=256)
        test(sol, "5", 256, "488");

        // Additional: num with zero, need product divisible by 2
        test(sol, "105", 2, "112");

        // Edge: t = large
        // 2^40 ≈ 1.1e12, 3^0, 5^0, 7^0
        // 8 contributes 2^3 per digit, need ceil(40/3)=14 digits of 8
        test(sol, "88888888888888", 1L << 40, "88888888888888");

        System.out.println("\nAll tests completed.");
    }

    private static void test(Solution sol, String num, long t, String expected) {
        String result = sol.smallestNumber(num, t);
        boolean pass = result.equals(expected);

        // Also verify the result satisfies constraints
        if (!result.equals("-1")) {
            pass = pass && verify(result, num, t);
        }

        System.out.printf("num=\"%s\", t=%d => \"%s\" (expected \"%s\") [%s]%n",
            num.length() > 20 ? num.substring(0, 17) + "..." : num,
            t,
            result.length() > 20 ? result.substring(0, 17) + "..." : result,
            expected.length() > 20 ? expected.substring(0, 17) + "..." : expected,
            pass ? "PASS" : "FAIL");
    }

    private static boolean verify(String result, String originalNum, long t) {
        // Must be >= originalNum (as numeric string comparison)
        if (result.length() < originalNum.length()) return false;
        if (result.length() == originalNum.length() && result.compareTo(originalNum) < 0)
            return false;
        if (result.length() > originalNum.length() && result.charAt(0) == '0')
            return false;

        // Must be zero-free
        long product = 1;
        for (int i = 0; i < result.length(); i++) {
            char c = result.charAt(i);
            if (c < '1' || c > '9') return false;
            int d = c - '0';
            product = (product * d) % t; // avoid overflow with modular reduction?
            // Actually product might overflow long. Let's use modular arithmetic:
            // product % t == 0 is equivalent to checking prime factors.
        }
        // Different verification: check prime factors
        int e2 = 0, e3 = 0, e5 = 0, e7 = 0;
        for (int i = 0; i < result.length(); i++) {
            int d = result.charAt(i) - '0';
            e2 += CONTRIB[d][0];
            e3 += CONTRIB[d][1];
            e5 += CONTRIB[d][2];
            e7 += CONTRIB[d][3];
        }
        // Factorize t
        long x = t;
        int ne2 = 0; while (x % 2 == 0) { ne2++; x /= 2; }
        int ne3 = 0; while (x % 3 == 0) { ne3++; x /= 3; }
        int ne5 = 0; while (x % 5 == 0) { ne5++; x /= 5; }
        int ne7 = 0; while (x % 7 == 0) { ne7++; x /= 7; }

        return e2 >= ne2 && e3 >= ne3 && e5 >= ne5 && e7 >= ne7;
    }
}
