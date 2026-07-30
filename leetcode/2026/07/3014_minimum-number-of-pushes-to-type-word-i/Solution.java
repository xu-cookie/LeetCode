/**
 * LeetCode #3014: Minimum Number of Pushes to Type Word I
 * Difficulty: Easy
 * Link: https://leetcode.com/problems/minimum-number-of-pushes-to-type-word-i/
 * Date: 2026-07-30
 *
 * Approach:
 * We have 8 keys (2-9) that can be freely remapped to any distinct letters.
 * For each key, pressing it once types the 1st letter, twice types the 2nd, etc.
 * The goal is to minimize total key presses to type the given word.
 *
 * Key insight: Since all letters in the word are distinct (the problem guarantees
 * this), every letter appears exactly once. The optimal strategy is to assign
 * the first 8 letters to position 1 on each of the 8 keys (1 press each),
 * the next 8 letters to position 2 (2 presses each), and so on.
 *
 * Let n = word.length():
 *   - Letters 1..8:   each needs 1 press  → 8 * 1 = 8  presses
 *   - Letters 9..16:  each needs 2 presses → 8 * 2 = 16 presses
 *   - Letters 17..24: each needs 3 presses → 8 * 3 = 24 presses
 *   - Letters 25..26: each needs 4 presses → 2 * 4 = 8  presses (max)
 *
 * Formula derivation:
 *   groups = n / 8,  remainder = n % 8
 *   total = 8 * (1 + 2 + ... + groups) + (groups + 1) * remainder
 *         = 8 * groups * (groups + 1) / 2 + (groups + 1) * remainder
 *         = 4 * groups * (groups + 1) + (groups + 1) * remainder
 *         = (groups + 1) * (4 * groups + remainder)
 *
 * Simplified: for the i-th letter (0-indexed), pushes = i / 8 + 1.
 * So sum from i=0 to n-1 of (i / 8 + 1).
 *
 * Time Complexity:  O(1) — formula-based computation
 * Space Complexity: O(1) — constant space used
 */
class Solution {
    public int minimumPushes(String word) {
        int n = word.length();
        int groups = n / 8;
        int remainder = n % 8;
        return (groups + 1) * (4 * groups + remainder);
    }

    // Test harness
    public static void main(String[] args) {
        Solution sol = new Solution();

        // Example 1
        assert sol.minimumPushes("abcde") == 5 : "Test 1 failed";
        System.out.println("Test 1: minimumPushes(\"abcde\") = "
                + sol.minimumPushes("abcde") + " (expected 5)");

        // Example 2
        assert sol.minimumPushes("xycdefghij") == 12 : "Test 2 failed";
        System.out.println("Test 2: minimumPushes(\"xycdefghij\") = "
                + sol.minimumPushes("xycdefghij") + " (expected 12)");

        // Edge case: single character (n=1)
        assert sol.minimumPushes("a") == 1 : "Test 3 failed";
        System.out.println("Test 3: minimumPushes(\"a\") = "
                + sol.minimumPushes("a") + " (expected 1)");

        // Exactly 8 characters: all get 1 push
        assert sol.minimumPushes("abcdefgh") == 8 : "Test 4 failed";
        System.out.println("Test 4: minimumPushes(\"abcdefgh\") = "
                + sol.minimumPushes("abcdefgh") + " (expected 8)");

        // Exactly 9 characters: 8 get 1 push, 1 gets 2 pushes
        assert sol.minimumPushes("abcdefghi") == 10 : "Test 5 failed";
        System.out.println("Test 5: minimumPushes(\"abcdefghi\") = "
                + sol.minimumPushes("abcdefghi") + " (expected 10)");

        // Exactly 16 characters: 8 get 1, 8 get 2
        assert sol.minimumPushes("abcdefghijklmnop") == 24 : "Test 6 failed";
        System.out.println("Test 6: minimumPushes(\"abcdefghijklmnop\") = "
                + sol.minimumPushes("abcdefghijklmnop") + " (expected 24)");

        // Max case: 26 distinct letters (all a-z)
        // groups=3, rem=2 → (3+1)*(4*3+2) = 4*(12+2) = 4*14 = 56
        assert sol.minimumPushes("abcdefghijklmnopqrstuvwxyz") == 56 : "Test 7 failed";
        System.out.println("Test 7: minimumPushes(\"abcdefghijklmnopqrstuvwxyz\") = "
                + sol.minimumPushes("abcdefghijklmnopqrstuvwxyz") + " (expected 56)");

        System.out.println("\nAll tests passed!");
    }
}
