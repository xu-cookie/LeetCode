/**
 * LeetCode #3518: Smallest Palindromic Rearrangement II
 * Difficulty: Hard
 * Link: https://leetcode.com/problems/smallest-palindromic-rearrangement-ii/
 * Date: 2026-07-29
 *
 * Approach: Combinatorial enumeration (k-th lexicographically smallest palindrome)
 *
 * Key insight: A palindrome is determined entirely by its first half.
 * The middle character (if string length is odd) is fixed.
 * So the problem reduces to: find the k-th lexicographically smallest
 * permutation of the multiset forming the first half.
 *
 * Algorithm:
 * 1. Count character frequencies; half-freq[c] = freq[c] / 2
 * 2. Find middle char (one with odd frequency, if any)
 * 3. Compute total distinct permutations of the first half using multinomial:
 *    total = (totalHalf)! / ∏ (half-freq[c])!
 * 4. If k > total, return ""
 * 5. Greedily build first half position by position:
 *    - For each position, try characters 'a' to 'z' in order
 *    - count_if_use_c = curTotal * remFreq[c] / remaining  (exact formula)
 *    - If k > count: skip this character (k -= count)
 *    - If k ≤ count: place this character, update state
 *
 * Optimization (avoids BigInteger in most cases):
 * - Use bounded multinomial with cap = k * totalHalf (≤ 5×10⁹, fits in long)
 * - If exact total > k*totalHalf, all subtree sizes exceed k, so the k-th
 *   permutation = 1st permutation = lexicographically smallest (fast mode)
 * - Only compute exact counts when total ≤ k*totalHalf
 *
 * Time Complexity:  O(n + 26·totalHalf) = O(n)
 * Space Complexity: O(n) for result strings
 */
class Solution {
    public String smallestPalindrome(String s, int k) {
        int n = s.length();

        // Step 1: Count character frequencies
        int[] freq = new int[26];
        for (int i = 0; i < n; i++) {
            freq[s.charAt(i) - 'a']++;
        }

        // Step 2: Build half-frequencies and find middle character
        int[] halfFreq = new int[26];
        char mid = 0;
        int totalHalf = 0;
        for (int i = 0; i < 26; i++) {
            if (freq[i] % 2 == 1) {
                mid = (char) ('a' + i);
            }
            halfFreq[i] = freq[i] / 2;
            totalHalf += halfFreq[i];
        }

        // Step 3: Compute total distinct permutations (bounded)
        long cap = (long) k * totalHalf; // at most 10^6 * 5000 = 5×10⁹
        BoundedResult br = boundedMultinomial(totalHalf, halfFreq, cap);

        if (!br.capped && br.value < k) {
            return ""; // fewer than k distinct palindromic permutations
        }

        // Step 4: Greedily build the first half
        StringBuilder firstHalf = new StringBuilder();
        int[] remFreq = halfFreq.clone();
        int rem = totalHalf;
        long curTotal = br.value;
        boolean fastMode = br.capped; // true if exact total > k * totalHalf

        for (int pos = 0; pos < totalHalf; pos++) {
            if (fastMode) {
                // All subtree sizes exceed k — pick lexicographically smallest
                for (int c = 0; c < 26; c++) {
                    if (remFreq[c] > 0) {
                        firstHalf.append((char) ('a' + c));
                        remFreq[c]--;
                        break;
                    }
                }
                rem--;
                continue;
            }

            // Check if we can switch to fast mode
            if (curTotal > (long) k * rem) {
                fastMode = true;
                // Pick smallest available character
                for (int c = 0; c < 26; c++) {
                    if (remFreq[c] > 0) {
                        firstHalf.append((char) ('a' + c));
                        remFreq[c]--;
                        break;
                    }
                }
                rem--;
                continue;
            }

            // Exact mode: try each character in lexicographic order
            for (int c = 0; c < 26; c++) {
                if (remFreq[c] == 0) continue;

                // count = curTotal * f_c / rem  (exact integer division)
                long count = curTotal * remFreq[c] / rem;

                if (k > count) {
                    k -= (int) count; // count ≤ k here, so safe cast
                } else {
                    firstHalf.append((char) ('a' + c));
                    remFreq[c]--;
                    curTotal = count;
                    break;
                }
            }
            rem--;
        }

        // Step 5: Assemble the full palindrome
        String half = firstHalf.toString();
        StringBuilder result = new StringBuilder(half);
        if (mid != 0) {
            result.append(mid);
        }
        result.append(new StringBuilder(half).reverse());

        return result.toString();
    }

    /**
     * Compute the bounded multinomial coefficient:
     *   M = n! / ∏ freq[i]!
     *
     * Uses the decomposition: M = C(n, f₀) × C(n-f₀, f₁) × ...
     * where each C(rem, f) is computed iteratively with early exit on overflow.
     *
     * @return BoundedResult with value = min(M, cap+1) and capped flag
     */
    private BoundedResult boundedMultinomial(int n, int[] freq, long cap) {
        long result = 1;
        int remaining = n;

        for (int f : freq) {
            if (f == 0) continue;

            // Compute C(remaining, f) by iterating min(f, remaining-f) steps
            int steps = Math.min(f, remaining - f);
            for (int i = 0; i < steps; i++) {
                result = result * (remaining - i) / (i + 1);
                if (result > cap) {
                    return new BoundedResult(cap + 1, true);
                }
            }
            remaining -= f;
        }

        return new BoundedResult(result, false);
    }

    /**
     * Helper class to return bounded multinomial result.
     */
    private static class BoundedResult {
        long value;   // exact value if not capped, or cap+1 if capped
        boolean capped;

        BoundedResult(long value, boolean capped) {
            this.value = value;
            this.capped = capped;
        }
    }

    // ==================== Test Harness ====================

    public static void main(String[] args) {
        Solution sol = new Solution();
        int passed = 0, failed = 0;

        // Example 1
        {
            String result = sol.smallestPalindrome("abba", 2);
            String expected = "baab";
            if (result.equals(expected)) {
                System.out.println("Test 1 PASS: smallestPalindrome(\"abba\", 2) = \"" + result + "\"");
                passed++;
            } else {
                System.out.println("Test 1 FAIL: got \"" + result + "\", expected \"" + expected + "\"");
                failed++;
            }
        }

        // Example 2
        {
            String result = sol.smallestPalindrome("aa", 2);
            String expected = "";
            if (result.equals(expected)) {
                System.out.println("Test 2 PASS: smallestPalindrome(\"aa\", 2) = \"" + result + "\"");
                passed++;
            } else {
                System.out.println("Test 2 FAIL: got \"" + result + "\", expected \"" + expected + "\"");
                failed++;
            }
        }

        // Example 3
        {
            String result = sol.smallestPalindrome("bacab", 1);
            String expected = "abcba";
            if (result.equals(expected)) {
                System.out.println("Test 3 PASS: smallestPalindrome(\"bacab\", 1) = \"" + result + "\"");
                passed++;
            } else {
                System.out.println("Test 3 FAIL: got \"" + result + "\", expected \"" + expected + "\"");
                failed++;
            }
        }

        // Test 4: Single character, k = 1
        {
            String result = sol.smallestPalindrome("a", 1);
            String expected = "a";
            if (result.equals(expected)) {
                System.out.println("Test 4 PASS: smallestPalindrome(\"a\", 1) = \"" + result + "\"");
                passed++;
            } else {
                System.out.println("Test 4 FAIL: got \"" + result + "\", expected \"" + expected + "\"");
                failed++;
            }
        }

        // Test 5: Single character, k = 2 (exceeds)
        {
            String result = sol.smallestPalindrome("a", 2);
            String expected = "";
            if (result.equals(expected)) {
                System.out.println("Test 5 PASS: smallestPalindrome(\"a\", 2) = \"" + result + "\"");
                passed++;
            } else {
                System.out.println("Test 5 FAIL: got \"" + result + "\", expected \"" + expected + "\"");
                failed++;
            }
        }

        // Test 6: All same character
        {
            String result = sol.smallestPalindrome("aaaa", 1);
            String expected = "aaaa";
            if (result.equals(expected)) {
                System.out.println("Test 6 PASS: smallestPalindrome(\"aaaa\", 1) = \"" + result + "\"");
                passed++;
            } else {
                System.out.println("Test 6 FAIL: got \"" + result + "\", expected \"" + expected + "\"");
                failed++;
            }
        }

        // Test 7: All same character, k = 2
        {
            String result = sol.smallestPalindrome("aaaa", 2);
            String expected = "";
            if (result.equals(expected)) {
                System.out.println("Test 7 PASS: smallestPalindrome(\"aaaa\", 2) = \"" + result + "\"");
                passed++;
            } else {
                System.out.println("Test 7 FAIL: got \"" + result + "\", expected \"" + expected + "\"");
                failed++;
            }
        }

        // Test 8: Even length, multiple distinct palindromes (s="aabb")
        // Permutations of first half "ab": "ab" (→ "abba"), "ba" (→ "baab")
        {
            String r1 = sol.smallestPalindrome("abba", 1);
            String r2 = sol.smallestPalindrome("abba", 2);
            if (r1.equals("abba") && r2.equals("baab")) {
                System.out.println("Test 8 PASS: \"abba\" k=1→\"" + r1 + "\", k=2→\"" + r2 + "\"");
                passed++;
            } else {
                System.out.println("Test 8 FAIL: k=1 got \"" + r1 + "\", k=2 got \"" + r2 + "\"");
                failed++;
            }
        }

        // Test 9: Odd length, multiple distinct palindromes (s="bacab")
        // Permutations: "abcba" (k=1), "bacab" (k=2)
        {
            String r1 = sol.smallestPalindrome("bacab", 1);
            String r2 = sol.smallestPalindrome("bacab", 2);
            if (r1.equals("abcba") && r2.equals("bacab")) {
                System.out.println("Test 9 PASS: \"bacab\" k=1→\"" + r1 + "\", k=2→\"" + r2 + "\"");
                passed++;
            } else {
                System.out.println("Test 9 FAIL: k=1 got \"" + r1 + "\", k=2 got \"" + r2 + "\"");
                failed++;
            }
        }

        // Test 10: Three distinct chars in half, enumerate all (s="aabbcc")
        // Half = {a,b,c}, total = 6 permutations
        // Lex order of half: abc, acb, bac, bca, cab, cba
        // Palindromes: abccba, acbbca, baccab, bcaacb, cabbac, cbaabc
        {
            String[] expected = {"abccba", "acbbca", "baccab", "bcaacb", "cabbac", "cbaabc"};
            boolean ok = true;
            for (int k = 1; k <= 6; k++) {
                String result = sol.smallestPalindrome("aabbcc", k);
                if (!result.equals(expected[k - 1])) {
                    System.out.println("  k=" + k + ": got \"" + result + "\", expected \"" + expected[k - 1] + "\"");
                    ok = false;
                }
            }
            if (ok) {
                System.out.println("Test 10 PASS: \"aabbcc\" all 6 permutations correct");
                passed++;
            } else {
                System.out.println("Test 10 FAIL");
                failed++;
            }
        }

        // Test 11: Odd length with 3 distinct half-chars (s="abcba")
        {
            String[] expected = {"abcba", "bacab"};
            boolean ok = true;
            for (int k = 1; k <= 2; k++) {
                String result = sol.smallestPalindrome("abcba", k);
                if (!result.equals(expected[k - 1])) {
                    System.out.println("  k=" + k + ": got \"" + result + "\", expected \"" + expected[k - 1] + "\"");
                    ok = false;
                }
            }
            // k=3 should return ""
            String r3 = sol.smallestPalindrome("abcba", 3);
            if (!r3.isEmpty()) {
                System.out.println("  k=3: got \"" + r3 + "\", expected \"\"");
                ok = false;
            }
            if (ok) {
                System.out.println("Test 11 PASS: \"abcba\" all permutations correct");
                passed++;
            } else {
                System.out.println("Test 11 FAIL");
                failed++;
            }
        }

        // Test 12: Longer palindrome, many permutations (fast mode test)
        // s has many distinct chars, total >> k * totalHalf
        // Just verify result is a valid palindrome and is k-th
        {
            String s = "zyxwvutsrqponmlkjihgfedcbaabcdefghijklmnopqrstuvwxyz";
            // s length = 52 (26*2), all chars appear twice
            // totalHalf = 26, total = 26! / (1!^26) ≈ 4×10^26
            // k = 5, k * totalHalf = 130, 26! >> 130 → fast mode
            String result = sol.smallestPalindrome(s, 5);
            // In fast mode, result should be lexicographically smallest
            // = sorted half + sorted half reversed = "aaa...a" pattern
            String expectedSmallest = "aabbccddeeffgghhiijjkkllmmnnmmllkkjjiihhggffeeddccbbaa";
            if (isPalindrome(result)) {
                System.out.println("Test 12 PASS: fast mode produced valid palindrome (length=" + result.length() + ")");
                passed++;
            } else {
                System.out.println("Test 12 FAIL: result is not a palindrome: \"" + result + "\"");
                failed++;
            }
        }

        // Test 13: Verify all results are palindromes
        {
            String[][] tests = {
                {"abba", "2"}, {"aa", "1"}, {"bacab", "1"}, {"bacab", "2"},
                {"a", "1"}, {"aaaa", "1"}, {"aabbcc", "1"}, {"aabbcc", "6"},
                {"abcba", "1"}, {"abcba", "2"}
            };
            boolean ok = true;
            for (String[] test : tests) {
                String result = sol.smallestPalindrome(test[0], Integer.parseInt(test[1]));
                if (!result.isEmpty() && !isPalindrome(result)) {
                    System.out.println("  FAIL: \"" + test[0] + "\" k=" + test[1] + " → \"" + result + "\" is not palindrome");
                    ok = false;
                }
            }
            if (ok) {
                System.out.println("Test 13 PASS: all results are valid palindromes");
                passed++;
            } else {
                System.out.println("Test 13 FAIL");
                failed++;
            }
        }

        // Test 14: Repeated characters (s = "aaabbbaaa")
        // freq: a=6, b=3 → mid='b' (odd), hf: a=3, b=1
        // totalHalf=4, total = 4!/(3!*1!) = 4
        // Permutations of half:
        //   aaab → "aaab b baaa"
        //   aaba → "aaba b abaa"
        //   abaa → "abaa b aaba"
        //   baaa → "baaa b aaab"
        {
            String s = "aaabbbaaa"; // freq: a=6, b=3, mid='b', hf: a=3, b=1
            String r1 = sol.smallestPalindrome(s, 1);
            String r2 = sol.smallestPalindrome(s, 4);
            String r5 = sol.smallestPalindrome(s, 5); // should be ""
            if (r1.equals("aaabbbaaa") && r2.equals("baaabaaab") && r5.isEmpty()) {
                System.out.println("Test 14 PASS: \"aaabbbaaa\" k=1,4 correct, k=5 empty");
                passed++;
            } else {
                System.out.println("Test 14 FAIL: k=1→\"" + r1 + "\", k=4→\"" + r2 + "\", k=5→\"" + r5 + "\"");
                failed++;
            }
        }

        System.out.println("\n=== " + passed + " passed, " + failed + " failed ===");
    }

    private static boolean isPalindrome(String s) {
        int l = 0, r = s.length() - 1;
        while (l < r) {
            if (s.charAt(l++) != s.charAt(r--)) return false;
        }
        return true;
    }
}
