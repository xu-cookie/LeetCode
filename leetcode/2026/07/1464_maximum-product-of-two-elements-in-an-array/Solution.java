/**
 * LeetCode #1464: Maximum Product of Two Elements in an Array
 * Difficulty: Easy
 * Link: https://leetcode.com/problems/maximum-product-of-two-elements-in-an-array/
 * Date: 2026-07-27
 *
 * Approach:
 * The problem asks for max((nums[i]-1) * (nums[j]-1)) where i != j.
 * Since (a-1)*(b-1) is monotonic in a and b for a,b >= 1, the maximum
 * product is achieved by picking the two largest elements in the array.
 *
 * Algorithm:
 * 1. Find the two largest elements in a single pass
 * 2. Return (max1 - 1) * (max2 - 1)
 *
 * Time Complexity:  O(n) — single pass
 * Space Complexity: O(1) — only two integer variables
 */
class Solution {
    public int maxProduct(int[] nums) {
        // Track the two largest elements
        int max1 = Integer.MIN_VALUE; // largest
        int max2 = Integer.MIN_VALUE; // second largest

        for (int num : nums) {
            if (num > max1) {
                max2 = max1;
                max1 = num;
            } else if (num > max2) {
                max2 = num;
            }
        }

        return (max1 - 1) * (max2 - 1);
    }

    // Test harness
    public static void main(String[] args) {
        Solution sol = new Solution();

        // Example 1
        assert sol.maxProduct(new int[]{3, 4, 5, 2}) == 12 : "Test 1 failed";
        System.out.println("Test 1: maxProduct([3,4,5,2]) = " + sol.maxProduct(new int[]{3, 4, 5, 2}) + " (expected 12)");

        // Example 2
        assert sol.maxProduct(new int[]{1, 5, 4, 5}) == 16 : "Test 2 failed";
        System.out.println("Test 2: maxProduct([1,5,4,5]) = " + sol.maxProduct(new int[]{1, 5, 4, 5}) + " (expected 16)");

        // Example 3
        assert sol.maxProduct(new int[]{3, 7}) == 12 : "Test 3 failed";
        System.out.println("Test 3: maxProduct([3,7]) = " + sol.maxProduct(new int[]{3, 7}) + " (expected 12)");

        // Additional edge cases
        // Minimum length with all 1s
        assert sol.maxProduct(new int[]{1, 1}) == 0 : "Test 4 failed";
        System.out.println("Test 4: maxProduct([1,1]) = " + sol.maxProduct(new int[]{1, 1}) + " (expected 0)");

        // Max at constraint boundary
        assert sol.maxProduct(new int[]{1000, 1000}) == 999 * 999 : "Test 5 failed";
        System.out.println("Test 5: maxProduct([1000,1000]) = " + sol.maxProduct(new int[]{1000, 1000}) + " (expected " + (999 * 999) + ")");

        // Larger array, max values at different positions
        assert sol.maxProduct(new int[]{10, 2, 5, 2}) == 36 : "Test 6 failed";
        System.out.println("Test 6: maxProduct([10,2,5,2]) = " + sol.maxProduct(new int[]{10, 2, 5, 2}) + " (expected 36)");

        // Descending order
        assert sol.maxProduct(new int[]{9, 8, 7, 6}) == 56 : "Test 7 failed";
        System.out.println("Test 7: maxProduct([9,8,7,6]) = " + sol.maxProduct(new int[]{9, 8, 7, 6}) + " (expected 56)");

        System.out.println("\nAll tests passed!");
    }
}
