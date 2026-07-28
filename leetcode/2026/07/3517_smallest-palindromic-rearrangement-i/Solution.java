/**
 * LeetCode #3517: Smallest Palindromic Rearrangement I
 * Difficulty: Medium
 * Link: https://leetcode.com/problems/smallest-palindromic-rearrangement-i/
 * Date: 2026-07-28
 *
 * Approach:
 * The input s is guaranteed to be a palindrome. We need to find the
 * lexicographically smallest palindrome that is a permutation of s.
 *
 * Since s is a palindrome, at most one character has an odd frequency
 * (the middle character). All other characters appear in even counts.
 *
 * To get the lexicographically smallest palindrome:
 * 1. Count character frequencies
 * 2. Build the first half by iterating 'a' to 'z', adding freq[c]/2 of each
 * 3. Find the middle character: the smallest char with odd frequency
 * 4. Result = firstHalf + middle + reverse(firstHalf)
 *
 * Time Complexity:  O(n + 26) = O(n)
 * Space Complexity: O(n) for the result string
 */
class Solution {
    public String smallestPalindrome(String s) {
        // Count character frequencies
        int[] freq = new int[26];
        for (int i = 0; i < s.length(); i++) {
            freq[s.charAt(i) - 'a']++;
        }

        // Build first half in lexicographic order
        StringBuilder firstHalf = new StringBuilder();
        char middleChar = 0; // 0 means no middle char yet (null character)

        for (int i = 0; i < 26; i++) {
            if (freq[i] % 2 == 1 && middleChar == 0) {
                middleChar = (char) (i + 'a');
            }
            // Add half of this character's occurrences to the first half
            for (int j = 0; j < freq[i] / 2; j++) {
                firstHalf.append((char) (i + 'a'));
            }
        }

        StringBuilder result = new StringBuilder();
        result.append(firstHalf);
        if (middleChar != 0) {
            result.append(middleChar);
        }
        // Append the reversed first half
        result.append(firstHalf.reverse());

        return result.toString();
    }

    // Test harness
    public static void main(String[] args) {
        Solution sol = new Solution();

        // Example 1: single character
        assert sol.smallestPalindrome("z").equals("z") : "Test 1 failed";
        System.out.println("Test 1: smallestPalindrome(\"z\") = \"" + sol.smallestPalindrome("z") + "\" (expected \"z\")");

        // Example 2: odd length, multiple characters
        assert sol.smallestPalindrome("babab").equals("abbba") : "Test 2 failed";
        System.out.println("Test 2: smallestPalindrome(\"babab\") = \"" + sol.smallestPalindrome("babab") + "\" (expected \"abbba\")");

        // Example 3: even length
        assert sol.smallestPalindrome("daccad").equals("acddca") : "Test 3 failed";
        System.out.println("Test 3: smallestPalindrome(\"daccad\") = \"" + sol.smallestPalindrome("daccad") + "\" (expected \"acddca\")");

        // Edge case: already sorted palindrome
        assert sol.smallestPalindrome("abcba").equals("abcba") : "Test 4 failed";
        System.out.println("Test 4: smallestPalindrome(\"abcba\") = \"" + sol.smallestPalindrome("abcba") + "\" (expected \"abcba\")");

        // Edge case: all same characters
        assert sol.smallestPalindrome("aaaa").equals("aaaa") : "Test 5 failed";
        System.out.println("Test 5: smallestPalindrome(\"aaaa\") = \"" + sol.smallestPalindrome("aaaa") + "\" (expected \"aaaa\")");

        // Edge case: two characters, even length
        assert sol.smallestPalindrome("baab").equals("abba") : "Test 6 failed";
        System.out.println("Test 6: smallestPalindrome(\"baab\") = \"" + sol.smallestPalindrome("baab") + "\" (expected \"abba\")");

        // Edge case: longer palindrome with single odd char
        // "zzccaaacczz" has freq: a=3, c=4, z=4 → smallest palindrome: "acczzazzcca"
        String t7input = "zzccaaacczz";
        String t7expected = "acczzazzcca";
        String t7result = sol.smallestPalindrome(t7input);
        assert t7result.equals(t7expected) : "Test 7 failed: got " + t7result;
        System.out.println("Test 7: smallestPalindrome(\"" + t7input + "\") = \""
                + t7result + "\" (expected \"" + t7expected + "\")");

        // Edge case: already lexicographically smallest palindrome
        String t8result = sol.smallestPalindrome("aabbaa");
        System.out.println("Test 8: smallestPalindrome(\"aabbaa\") = \"" + t8result + "\"");

        // Verify all results are palindromes
        String[] tests = {"z", "babab", "daccad", "abcba", "aaaa", "baab", t7input};
        for (String t : tests) {
            String r = sol.smallestPalindrome(t);
            assert isPalindrome(r) : "Result not palindrome: " + r;
        }

        System.out.println("\nAll tests passed!");
    }

    private static boolean isPalindrome(String s) {
        int l = 0, r = s.length() - 1;
        while (l < r) {
            if (s.charAt(l++) != s.charAt(r--)) return false;
        }
        return true;
    }
}
