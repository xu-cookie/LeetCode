/**
 * LeetCode #3536: Maximum Product of Two Digits
 * Difficulty: Easy
 * Link: https://leetcode.com/problems/maximum-product-of-two-digits/
 * Date: 2026-07-25
 *
 * Approach:
 * Since all digits are non-negative integers (0-9) and the product function
 * is monotonic for non-negative numbers, the maximum product of any two
 * digits is simply the product of the two largest digits in n.
 *
 * We iterate through the digits of n, tracking the two largest values seen.
 * Special care is taken for ties: if a digit equals max1, it can still
 * qualify as max2 (allowing the same digit to be used twice).
 *
 * Algorithm:
 * 1. Initialize max1 = 0, max2 = 0
 * 2. While n > 0: extract the last digit via n % 10, reduce n via n /= 10
 * 3. If digit > max1: shift max1 to max2, update max1 to digit
 * 4. Else if digit > max2: update max2 to digit
 * 5. Return max1 * max2
 *
 * Time Complexity:  O(d) where d is the number of digits in n (at most 10)
 * Space Complexity: O(1) — only two integer variables used
 */
class Solution {
    public int maxProduct(int n) {
        int max1 = 0; // largest digit seen so far
        int max2 = 0; // second largest digit seen so far

        // Extract digits from right to left
        while (n > 0) {
            int digit = n % 10;
            n /= 10;

            if (digit > max1) {
                // digit is new maximum: cascade previous max1 to max2
                max2 = max1;
                max1 = digit;
            } else if (digit > max2) {
                // digit fits as second maximum
                max2 = digit;
            }
        }

        return max1 * max2;
    }

    // Test harness
    public static void main(String[] args) {
        Solution sol = new Solution();

        // Example 1
        assert sol.maxProduct(31) == 3 : "Test 1 failed";
        System.out.println("Test 1: maxProduct(31) = " + sol.maxProduct(31) + " (expected 3)");

        // Example 2
        assert sol.maxProduct(22) == 4 : "Test 2 failed";
        System.out.println("Test 2: maxProduct(22) = " + sol.maxProduct(22) + " (expected 4)");

        // Example 3
        assert sol.maxProduct(124) == 8 : "Test 3 failed";
        System.out.println("Test 3: maxProduct(124) = " + sol.maxProduct(124) + " (expected 8)");

        // Additional edge cases
        assert sol.maxProduct(10) == 0 : "Test 4 failed";
        System.out.println("Test 4: maxProduct(10) = " + sol.maxProduct(10) + " (expected 0)");

        assert sol.maxProduct(99) == 81 : "Test 5 failed";
        System.out.println("Test 5: maxProduct(99) = " + sol.maxProduct(99) + " (expected 81)");

        assert sol.maxProduct(91) == 9 : "Test 6 failed";
        System.out.println("Test 6: maxProduct(91) = " + sol.maxProduct(91) + " (expected 9)");

        assert sol.maxProduct(1000000000) == 1 : "Test 7 failed";
        System.out.println("Test 7: maxProduct(1000000000) = " + sol.maxProduct(1000000000) + " (expected 1)");

        System.out.println("\nAll tests passed!");
    }
}
