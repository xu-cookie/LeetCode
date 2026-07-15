/**
 * LeetCode #3658 - GCD of Odd and Even Sums
 * Difficulty: Easy
 * Link: https://leetcode.com/problems/gcd-of-odd-and-even-sums/
 * Date Solved: 2026-07-15
 *
 * Approach:
 * Mathematical derivation:
 *   sumOdd  = 1 + 3 + 5 + ... + (2n-1) = n²
 *   sumEven = 2 + 4 + 6 + ... + 2n     = n(n+1)
 *   GCD(n², n(n+1)) = n · GCD(n, n+1)
 * Since n and n+1 are consecutive, GCD(n, n+1) = 1.
 * Therefore, GCD(sumOdd, sumEven) = n.
 *
 * Time Complexity:  O(1)
 * Space Complexity: O(1)
 */

class Solution {
    public int gcdOfOddEvenSums(int n) {
        return n;
    }

    // Test harness
    public static void main(String[] args) {
        Solution sol = new Solution();

        // Test Case 1
        int res1 = sol.gcdOfOddEvenSums(4);
        System.out.println("Test 1 (n=4): " + res1);
        // Expected: 4 (sumOdd=16, sumEven=20, GCD=4)

        // Test Case 2
        int res2 = sol.gcdOfOddEvenSums(5);
        System.out.println("Test 2 (n=5): " + res2);
        // Expected: 5 (sumOdd=25, sumEven=30, GCD=5)

        // Test Case 3
        int res3 = sol.gcdOfOddEvenSums(1);
        System.out.println("Test 3 (n=1): " + res3);
        // Expected: 1 (sumOdd=1, sumEven=2, GCD=1)

        // Test Case 4
        int res4 = sol.gcdOfOddEvenSums(10);
        System.out.println("Test 4 (n=10): " + res4);
        // Expected: 10 (sumOdd=100, sumEven=110, GCD=10)

        // Test Case 5
        int res5 = sol.gcdOfOddEvenSums(2);
        System.out.println("Test 5 (n=2): " + res5);
        // Expected: 2 (sumOdd=4, sumEven=6, GCD=2)
    }
}
