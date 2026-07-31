/**
 * LeetCode #3016: Minimum Number of Pushes to Type Word II
 * Difficulty: Medium
 * Link: https://leetcode.com/problems/minimum-number-of-pushes-to-type-word-ii/
 * Date: 2026-07-31
 *
 * Approach:
 * This is the follow-up to #3014 where letters can now repeat (no longer distinct).
 * We have 8 keys (2-9) that can be freely remapped to any distinct letters.
 *
 * Key insight: Letters with higher frequency should be assigned to position 1
 * (1 push per occurrence) on their key, the next most frequent letters to position 2
 * (2 pushes per occurrence), etc. Each key can hold multiple letters at different
 * positions.
 *
 * Algorithm:
 * 1. Count frequency of each of the 26 lowercase letters in the word
 * 2. Sort frequencies in descending order
 * 3. Assign the top 8 frequencies to position 1 (each occurrence costs 1 push),
 *    the next 8 to position 2 (costs 2 pushes per occurrence), etc.
 * 4. Formula for letter at sorted index i: cost = freq[i] * (i / 8 + 1)
 * 5. Sum all costs
 *
 * Why greedy works: To minimize total pushes, letters typed more frequently
 * should require fewer pushes per occurrence. Since we have exactly 8 keys,
 * there are 8 "slots" at position 1 (best), 8 slots at position 2 (second best),
 * etc. Sorting by frequency and assigning to best available slot is optimal
 * by the rearrangement inequality.
 *
 * Time Complexity:  O(n + 26 log 26) ≈ O(n) where n = word.length()
 * Space Complexity: O(1) — fixed-size frequency array of 26
 */
class Solution {
    public int minimumPushes(String word) {
        // Count frequency of each lowercase letter
        int[] freq = new int[26];
        for (int i = 0; i < word.length(); i++) {
            freq[word.charAt(i) - 'a']++;
        }

        // Sort frequencies in descending order (largest first)
        java.util.Arrays.sort(freq);

        int total = 0;
        // Iterate from largest frequency to smallest
        for (int i = 25; i >= 0; i--) {
            if (freq[i] == 0) break; // no more letters to process
            // Index from largest: (25 - i) gives 0, 1, 2, ...
            // pushes per occurrence = (25 - i) / 8 + 1
            int pushesPerLetter = (25 - i) / 8 + 1;
            total += freq[i] * pushesPerLetter;
        }

        return total;
    }

    // Test harness
    public static void main(String[] args) {
        Solution sol = new Solution();

        // Example 1: "abcde" — all distinct, same as Part I
        assert sol.minimumPushes("abcde") == 5 : "Test 1 failed";
        System.out.println("Test 1: minimumPushes(\"abcde\") = "
                + sol.minimumPushes("abcde") + " (expected 5)");

        // Example 2: "xyzxyzxyzxyz" — 3 letters, each appears 4 times
        assert sol.minimumPushes("xyzxyzxyzxyz") == 12 : "Test 2 failed";
        System.out.println("Test 2: minimumPushes(\"xyzxyzxyzxyz\") = "
                + sol.minimumPushes("xyzxyzxyzxyz") + " (expected 12)");

        // Example 3: "aabbccddeeffgghhiiiiii" — 9 letters, i appears 6 times
        assert sol.minimumPushes("aabbccddeeffgghhiiiiii") == 24 : "Test 3 failed";
        System.out.println("Test 3: minimumPushes(\"aabbccddeeffgghhiiiiii\") = "
                + sol.minimumPushes("aabbccddeeffgghhiiiiii") + " (expected 24)");

        // Edge case: single character
        assert sol.minimumPushes("a") == 1 : "Test 4 failed";
        System.out.println("Test 4: minimumPushes(\"a\") = "
                + sol.minimumPushes("a") + " (expected 1)");

        // Edge case: 8 distinct letters, all freq 1
        assert sol.minimumPushes("abcdefgh") == 8 : "Test 5 failed";
        System.out.println("Test 5: minimumPushes(\"abcdefgh\") = "
                + sol.minimumPushes("abcdefgh") + " (expected 8)");

        // Edge case: 9 distinct letters — 8 get 1 push, 1 gets 2 pushes
        assert sol.minimumPushes("abcdefghi") == 10 : "Test 6 failed";
        System.out.println("Test 6: minimumPushes(\"abcdefghi\") = "
                + sol.minimumPushes("abcdefghi") + " (expected 10)");

        // 26 distinct letters (all a-z), same as Part I max case
        assert sol.minimumPushes("abcdefghijklmnopqrstuvwxyz") == 56 : "Test 7 failed";
        System.out.println("Test 7: minimumPushes(\"abcdefghijklmnopqrstuvwxyz\") = "
                + sol.minimumPushes("abcdefghijklmnopqrstuvwxyz") + " (expected 56)");

        // Test with highly skewed frequency: one letter dominates
        // "aaaaabbbccd" → a:5, b:3, c:2, d:1
        // sorted: [5,3,2,1,0,...]
        // i=0: 5*1=5, i=1: 3*1=3, i=2: 2*1=2, i=3: 1*1=1 → total=11
        assert sol.minimumPushes("aaaaabbbccd") == 11 : "Test 8 failed";
        System.out.println("Test 8: minimumPushes(\"aaaaabbbccd\") = "
                + sol.minimumPushes("aaaaabbbccd") + " (expected 11)");

        // Test with >8 unique letters with varying frequencies
        // 9 most frequent letters occupy positions 1-8 (1 push) and position 9 (2 pushes)
        // "a"*10 + "b"*9 + ... : let's do a simpler test
        // 9 letters each appearing once → 8*1 + 1*2 = 10
        assert sol.minimumPushes("abcdefghi") == 10 : "Test 9 failed (duplicate of test 6)";
        System.out.println("Test 9: minimumPushes(\"abcdefghi\") = "
                + sol.minimumPushes("abcdefghi") + " (expected 10)");

        // 17 letters each once → 8*1 + 8*2 + 1*3 = 8+16+3 = 27
        // "abcdefghijklmnopq"  17 distinct
        assert sol.minimumPushes("abcdefghijklmnopq") == 27 : "Test 10 failed";
        System.out.println("Test 10: minimumPushes(\"abcdefghijklmnopq\") = "
                + sol.minimumPushes("abcdefghijklmnopq") + " (expected 27)");

        System.out.println("\nAll tests passed!");
    }
}
