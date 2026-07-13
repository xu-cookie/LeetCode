/**
 * LeetCode #1291 - Sequential Digits
 * Difficulty: Medium
 * Link: https://leetcode.com/problems/sequential-digits/
 * Date Solved: 2026-07-13
 *
 * Approach:
 * Generate all possible sequential digit numbers (there are only 45 total).
 * A sequential digit number has each digit exactly one more than the previous.
 *
 * We iterate over each possible starting digit (1 through 9) and each possible
 * length (up to 9 digits), building the number incrementally. If the number
 * falls within [low, high], it's added to the result.
 *
 * The result is naturally sorted because we generate in increasing order
 * (by length first, then by starting digit).
 *
 * Time Complexity:  O(1) - we generate at most 45 numbers regardless of input
 * Space Complexity: O(1) - at most 45 numbers in the result list
 */

import java.util.*;

class Solution {
    public List<Integer> sequentialDigits(int low, int high) {
        List<Integer> result = new ArrayList<>();

        // Try every possible starting digit (1-9)
        for (int start = 1; start <= 9; start++) {
            int num = start;

            // Extend with sequential digits
            for (int next = start + 1; next <= 9; next++) {
                num = num * 10 + next;

                if (num > high) {
                    break;
                }

                if (num >= low) {
                    result.add(num);
                }
            }
        }

        // Sort the result (natural order from our generation, but sort to be safe)
        Collections.sort(result);
        return result;
    }

    // Test harness
    public static void main(String[] args) {
        Solution sol = new Solution();

        // Test Case 1
        List<Integer> res1 = sol.sequentialDigits(100, 300);
        System.out.println("Test 1: " + res1);
        // Expected: [123, 234]

        // Test Case 2
        List<Integer> res2 = sol.sequentialDigits(1000, 13000);
        System.out.println("Test 2: " + res2);
        // Expected: [1234, 2345, 3456, 4567, 5678, 6789, 12345]

        // Test Case 3: small range
        List<Integer> res3 = sol.sequentialDigits(10, 20);
        System.out.println("Test 3: " + res3);
        // Expected: [12]

        // Test Case 4: no sequential digits in range
        List<Integer> res4 = sol.sequentialDigits(10000, 11000);
        System.out.println("Test 4: " + res4);
        // Expected: []

        // Test Case 5: full range including all
        List<Integer> res5 = sol.sequentialDigits(10, (int)1e9);
        System.out.println("Test 5 (count): " + res5.size());
        // Expected: 36 numbers (2-digit through 9-digit sequential numbers)
    }
}
