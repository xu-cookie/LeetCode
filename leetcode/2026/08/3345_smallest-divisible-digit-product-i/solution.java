/**
 * LeetCode #3345: Smallest Divisible Digit Product I
 * Difficulty: Easy
 * Link: https://leetcode.com/problems/smallest-divisible-digit-product-i/
 * Date: 2026-08-06
 *
 * Problem:
 * Given two integers n and t, return the smallest number greater than or equal
 * to n such that the product of its digits is divisible by t.
 *
 * Approach: Brute Force Iteration
 *
 * Since the constraints are small (n <= 100, t <= 10), we can simply iterate
 * from n upward until we find a number whose digit product is divisible by t.
 *
 * Key observations:
 * - Any number containing digit 0 has product 0, which is divisible by any t.
 * - If t = 1, the answer is always n (1 divides everything).
 * - For n <= 100, at most a few hundred iterations are needed in the worst case.
 *
 * Time Complexity:  O(k * log m) where k is the number of candidates checked
 *                   (at most ~200) and m is the candidate value (at most ~200)
 * Space Complexity: O(1)
 */
class Solution {
    public int smallestNumber(int n, int t) {
        // Iterate from n upward until we find a valid number
        for (int candidate = n; ; candidate++) {
            if (digitProduct(candidate) % t == 0) {
                return candidate;
            }
        }
    }

    /**
     * Computes the product of all digits of the given number.
     * Returns 0 immediately if any digit is 0, since the product
     * will be 0 regardless of remaining digits.
     */
    private int digitProduct(int num) {
        int product = 1;
        while (num > 0) {
            int digit = num % 10;
            if (digit == 0) {
                return 0; // 0 * anything = 0, divisible by any t
            }
            product *= digit;
            num /= 10;
        }
        return product;
    }

    // Test harness
    public static void main(String[] args) {
        Solution sol = new Solution();
        int passed = 0;
        int total = 0;

        // Example 1: n = 10, t = 2
        total++;
        int res1 = sol.smallestNumber(10, 2);
        if (res1 == 10) {
            System.out.println("Example 1: PASS (10, t=2 -> " + res1 + ")");
            passed++;
        } else {
            System.out.println("Example 1: FAIL - got " + res1 + " expected 10");
        }

        // Example 2: n = 15, t = 3
        total++;
        int res2 = sol.smallestNumber(15, 3);
        if (res2 == 16) {
            System.out.println("Example 2: PASS (15, t=3 -> " + res2 + ")");
            passed++;
        } else {
            System.out.println("Example 2: FAIL - got " + res2 + " expected 16");
        }

        // Edge case: t = 1, should return n immediately
        total++;
        if (sol.smallestNumber(7, 1) == 7 &&
            sol.smallestNumber(100, 1) == 100) {
            System.out.println("Test t=1: PASS");
            passed++;
        } else {
            System.out.println("Test t=1: FAIL");
        }

        // Edge case: n contains digit 0 (product = 0, divisible by any t)
        total++;
        if (sol.smallestNumber(10, 7) == 10 &&
            sol.smallestNumber(20, 9) == 20 &&
            sol.smallestNumber(100, 8) == 100) {
            System.out.println("Test digit-zero: PASS");
            passed++;
        } else {
            System.out.println("Test digit-zero: FAIL");
        }

        // Edge case: need to go beyond n
        // n = 11, t = 9 -> 11(1), 12(2), ..., 19(9) -> 19
        total++;
        if (sol.smallestNumber(11, 9) == 19) {
            System.out.println("Test n=11 t=9: PASS -> 19");
            passed++;
        } else {
            System.out.println("Test n=11 t=9: FAIL - got " + sol.smallestNumber(11, 9));
        }

        // n = 33, t = 10 -> 33(9), 34(12), ..., 40(0) -> 40 (digit 0)
        total++;
        if (sol.smallestNumber(33, 10) == 40) {
            System.out.println("Test n=33 t=10: PASS -> 40");
            passed++;
        } else {
            System.out.println("Test n=33 t=10: FAIL - got " + sol.smallestNumber(33, 10));
        }

        // n = 99, t = 2 -> 99(81), 100(0) -> 100
        total++;
        if (sol.smallestNumber(99, 2) == 99) {
            System.out.println("Test n=99 t=2: PASS -> 99");
            passed++;
        } else {
            System.out.println("Test n=99 t=2: FAIL - got " + sol.smallestNumber(99, 2));
        }

        // n = 1, t = 7 -> iterate until we find digit product divisible by 7
        // 1(1),2(2),3(3),4(4),5(5),6(6),7(7) -> 7
        total++;
        if (sol.smallestNumber(1, 7) == 7) {
            System.out.println("Test n=1 t=7: PASS -> 7");
            passed++;
        } else {
            System.out.println("Test n=1 t=7: FAIL - got " + sol.smallestNumber(1, 7));
        }

        System.out.println("\n" + passed + "/" + total + " tests passed.");
    }
}
