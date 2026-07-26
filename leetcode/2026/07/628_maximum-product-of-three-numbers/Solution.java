/**
 * LeetCode #628: Maximum Product of Three Numbers
 * Difficulty: Easy
 * Link: https://leetcode.com/problems/maximum-product-of-three-numbers/
 * Date: 2026-07-26
 *
 * Approach:
 * The maximum product of three numbers in an array is either:
 *   1. The product of the three largest numbers (max1 * max2 * max3)
 *   2. The product of the two smallest numbers and the largest number (min1 * min2 * max1)
 *      — this handles the case where two negative numbers multiply to a large positive.
 *
 * We find these five values in a single pass through the array (O(n) time, O(1) space),
 * avoiding the O(n log n) cost of sorting.
 *
 * Algorithm:
 * 1. Initialize max1, max2, max3 = Integer.MIN_VALUE and min1, min2 = Integer.MAX_VALUE
 * 2. Iterate through nums: update the three largest and two smallest values
 * 3. Return max(max1 * max2 * max3, min1 * min2 * max1)
 *
 * Time Complexity:  O(n) — single pass
 * Space Complexity: O(1) — only five integer variables
 */
class Solution {
    public int maximumProduct(int[] nums) {
        // Three largest values
        int max1 = Integer.MIN_VALUE; // largest
        int max2 = Integer.MIN_VALUE; // second largest
        int max3 = Integer.MIN_VALUE; // third largest

        // Two smallest values (for negative * negative = positive case)
        int min1 = Integer.MAX_VALUE; // smallest
        int min2 = Integer.MAX_VALUE; // second smallest

        for (int num : nums) {
            // Update three largest
            if (num > max1) {
                max3 = max2;
                max2 = max1;
                max1 = num;
            } else if (num > max2) {
                max3 = max2;
                max2 = num;
            } else if (num > max3) {
                max3 = num;
            }

            // Update two smallest
            if (num < min1) {
                min2 = min1;
                min1 = num;
            } else if (num < min2) {
                min2 = num;
            }
        }

        return Math.max(max1 * max2 * max3, min1 * min2 * max1);
    }

    // Test harness
    public static void main(String[] args) {
        Solution sol = new Solution();

        // Example 1
        assert sol.maximumProduct(new int[]{1, 2, 3}) == 6 : "Test 1 failed";
        System.out.println("Test 1: maximumProduct([1,2,3]) = " + sol.maximumProduct(new int[]{1, 2, 3}) + " (expected 6)");

        // Example 2
        assert sol.maximumProduct(new int[]{1, 2, 3, 4}) == 24 : "Test 2 failed";
        System.out.println("Test 2: maximumProduct([1,2,3,4]) = " + sol.maximumProduct(new int[]{1, 2, 3, 4}) + " (expected 24)");

        // Example 3
        assert sol.maximumProduct(new int[]{-1, -2, -3}) == -6 : "Test 3 failed";
        System.out.println("Test 3: maximumProduct([-1,-2,-3]) = " + sol.maximumProduct(new int[]{-1, -2, -3}) + " (expected -6)");

        // Additional edge cases
        // Two negatives produce larger positive product
        assert sol.maximumProduct(new int[]{-100, -98, 1, 2, 3}) == 29400 : "Test 4 failed";
        System.out.println("Test 4: maximumProduct([-100,-98,1,2,3]) = " + sol.maximumProduct(new int[]{-100, -98, 1, 2, 3}) + " (expected 29400)");

        // All negative — pick three largest (closest to zero)
        assert sol.maximumProduct(new int[]{-5, -4, -3, -2, -1}) == -6 : "Test 5 failed";
        System.out.println("Test 5: maximumProduct([-5,-4,-3,-2,-1]) = " + sol.maximumProduct(new int[]{-5, -4, -3, -2, -1}) + " (expected -6)");

        // Mixed with zero
        assert sol.maximumProduct(new int[]{-1, 0, 1, 2}) == 0 : "Test 6 failed";
        System.out.println("Test 6: maximumProduct([-1,0,1,2]) = " + sol.maximumProduct(new int[]{-1, 0, 1, 2}) + " (expected 0)");

        // Large values at constraint boundary
        assert sol.maximumProduct(new int[]{-1000, -1000, 1000, 999}) == 1000000000 : "Test 7 failed";
        System.out.println("Test 7: maximumProduct([-1000,-1000,1000,999]) = " + sol.maximumProduct(new int[]{-1000, -1000, 1000, 999}) + " (expected 1000000000)");

        System.out.println("\nAll tests passed!");
    }
}
