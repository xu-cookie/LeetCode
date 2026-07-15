/**
 * LeetCode #3658 - GCD of Odd and Even Sums
 * Difficulty: Easy
 * Link: https://leetcode.cn/problems/gcd-of-odd-and-even-sums/
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
}
