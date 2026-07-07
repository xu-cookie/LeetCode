/**
 * LeetCode Daily Challenge - 2026-07-07
 * Problem: 3754. Concatenate Non-Zero Digits and Multiply by Sum I
 * Difficulty: Easy
 * Link: https://leetcode.com/problems/concatenate-non-zero-digits-and-multiply-by-sum-i/
 *
 * Problem Description:
 * Given an integer n, form a new integer x by concatenating all non-zero digits
 * of n in their original order. If there are no non-zero digits, x = 0.
 * Let sum be the sum of digits in x. Return x * sum.
 *
 * Approach: String Iteration (Linear Scan)
 *
 * Key Insight:
 * Converting n to a String allows us to iterate digits in their original
 * left-to-right order. For each character:
 *   - If the digit is '0', skip it.
 *   - Otherwise, accumulate: x = x * 10 + digit, and sum += digit.
 *
 * Since n <= 10^9 (at most 10 digits), the string approach is perfectly
 * efficient and much simpler than manually reversing digit extraction.
 *
 * Algorithm:
 *   1. Convert n to a String.
 *   2. Initialize x = 0, sum = 0.
 *   3. For each character c in the string:
 *        - If c != '0': x = x * 10 + (c - '0'), sum += (c - '0')
 *   4. Return x * sum (handles n = 0 naturally: x=0, sum=0, product=0).
 *
 * Time Complexity:  O(d) where d = number of digits (max 10 for n <= 10^9)
 * Space Complexity: O(d) for the string representation (max 10 chars)
 */

class Solution {
    public long sumAndMultiply(int n) {
        // Convert to string to process digits in original left-to-right order
        String s = String.valueOf(n);

        long x = 0;    // the concatenated non-zero digits
        long sum = 0;  // sum of non-zero digits

        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c != '0') {
                int digit = c - '0';
                x = x * 10 + digit;
                sum += digit;
            }
        }

        // x * sum: if no non-zero digits, both are 0, product is 0 (correct)
        return x * sum;
    }

    // Test harness — run locally to verify correctness
    public static void main(String[] args) {
        Solution sol = new Solution();

        // Example 1: n = 10203004 → non-zero: 1,2,3,4 → x=1234, sum=10 → 12340
        long r1 = sol.sumAndMultiply(10203004);
        System.out.println("Example 1: " + r1 + " (expected 12340) — "
                + (r1 == 12340 ? "PASS" : "FAIL"));

        // Example 2: n = 1000 → non-zero: 1 → x=1, sum=1 → 1
        long r2 = sol.sumAndMultiply(1000);
        System.out.println("Example 2: " + r2 + " (expected 1) — "
                + (r2 == 1 ? "PASS" : "FAIL"));

        // Edge case: n = 0 → no non-zero digits → x=0, sum=0 → 0
        long r3 = sol.sumAndMultiply(0);
        System.out.println("n = 0: " + r3 + " (expected 0) — "
                + (r3 == 0 ? "PASS" : "FAIL"));

        // Edge case: n = 9 → single non-zero digit → x=9, sum=9 → 81
        long r4 = sol.sumAndMultiply(9);
        System.out.println("n = 9: " + r4 + " (expected 81) — "
                + (r4 == 81 ? "PASS" : "FAIL"));

        // Edge case: n = 900 → only 9 → x=9, sum=9 → 81
        long r5 = sol.sumAndMultiply(900);
        System.out.println("n = 900: " + r5 + " (expected 81) — "
                + (r5 == 81 ? "PASS" : "FAIL"));

        // Edge case: n = 101 → non-zero: 1,1 → x=11, sum=2 → 22
        long r6 = sol.sumAndMultiply(101);
        System.out.println("n = 101: " + r6 + " (expected 22) — "
                + (r6 == 22 ? "PASS" : "FAIL"));

        // Edge case: n = 987654321 → x=987654321, sum=45 → 44444444445
        long r7 = sol.sumAndMultiply(987654321);
        System.out.println("n = 987654321: " + r7 + " (expected 44444444445) — "
                + (r7 == 44444444445L ? "PASS" : "FAIL"));

        // Edge case: max constraint n = 1000000000 (10^9) → non-zero: 1 → x=1, sum=1 → 1
        long r8 = sol.sumAndMultiply(1000000000);
        System.out.println("n = 10^9: " + r8 + " (expected 1) — "
                + (r8 == 1 ? "PASS" : "FAIL"));
    }
}
