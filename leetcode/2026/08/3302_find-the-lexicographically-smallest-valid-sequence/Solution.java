/**
 * LeetCode Daily Challenge - 2026-08-08
 * Problem: 3302. Find the Lexicographically Smallest Valid Sequence
 * Difficulty: Medium
 * Link: https://leetcode.com/problems/find-the-lexicographically-smallest-valid-sequence/
 *
 * Problem Description:
 * You are given two strings word1 and word2.
 * A string x is called almost equal to y if you can change at most one character
 * in x to make it identical to y.
 * A sequence of indices seq is valid if:
 * - The indices are sorted in ascending order.
 * - Concatenating the characters at these indices in word1 in the same order
 *   results in a string that is almost equal to word2.
 * Return an array of size word2.length representing the lexicographically smallest
 * valid sequence of indices. If no such sequence exists, return an empty array.
 *
 * Approach:
 * 1. Precompute next[pos][c]: smallest index >= pos where word1[index] == c
 * 2. Greedy left-to-right prefix matching: pref[i] = index in word1 for word2[i]
 * 3. Greedy right-to-left suffix matching: suff[i] = index in word1 for word2[i]
 * 4. Precompute sufLen[p]: for each position p in word1, max length of word2's
 *    suffix that can be matched from p (right-to-left scan).
 * 5. Check exact match (no changes needed). Return pref if works.
 * 6. For each possible change position k (0 to m-1):
 *    - leftEnd = position after matching word2[0..k-1]
 *    - rightStart = position of word2[k+1] in right-to-left greedy match
 *    - If leftEnd < rightStart AND sufLen[ans[k]+1] >= m-k-1:
 *      Reconstruct: pref[0..k-1] + (leftEnd+1) + greedy suffix from leftEnd+2
 *
 * Key Insight:
 * - Lexicographically smallest index array → pick smallest possible index at
 *   each position. Greedy left-to-right with nxt array achieves this.
 * - For one-change case, try changes at the earliest possible position k.
 *   The first feasible k yields the lexicographically smallest result because
 *   prefix indices are identical (from greedy) and the change position is the
 *   earliest possible.
 *
 * Time Complexity: O(n * 26 + m) = O(n)  where n = word1.length, m = word2.length
 * Space Complexity: O(n * 26) = O(n) for the nxt array
 */

class Solution {
    public int[] validSequence(String word1, String word2) {
        int n = word1.length(), m = word2.length();

        // ── 1. Precompute next[pos][c] ──
        // next[i][c] = smallest index >= i where word1[index] == c (or n if none)
        int[][] nxt = new int[n + 1][26];
        for (int c = 0; c < 26; c++) {
            nxt[n][c] = n; // sentinel: beyond end
        }
        for (int i = n - 1; i >= 0; i--) {
            for (int c = 0; c < 26; c++) {
                nxt[i][c] = nxt[i + 1][c];
            }
            nxt[i][word1.charAt(i) - 'a'] = i;
        }

        // ── 2. Greedy left-to-right prefix matching ──
        int[] pref = new int[m];
        int pos = -1;
        for (int i = 0; i < m; i++) {
            if (pos >= n) {
                pref[i] = n; // cannot match further
                continue;
            }
            int c = word2.charAt(i) - 'a';
            pos = nxt[pos + 1][c];
            pref[i] = pos;
        }

        // Exact match check: if all prefix positions are valid
        if (pref[m - 1] < n) {
            return pref;
        }

        // ── 3. Greedy right-to-left suffix matching ──
        int[] suff = new int[m];
        pos = n - 1;
        for (int i = m - 1; i >= 0; i--) {
            while (pos >= 0 && word1.charAt(pos) != word2.charAt(i)) {
                pos--;
            }
            suff[i] = pos; // -1 if not found
            if (pos >= 0) {
                pos--; // move left for next character
            }
        }

        // ── 4. Precompute sufLen[p] ──
        // sufLen[p] = max length of word2 suffix matchable from word1[p...]
        int[] sufLen = new int[n + 1];
        int w2p = m - 1;
        for (int i = n - 1; i >= 0; i--) {
            if (w2p >= 0 && word1.charAt(i) == word2.charAt(w2p)) {
                w2p--;
            }
            sufLen[i] = m - 1 - w2p; // characters of suffix matched from position i
        }
        sufLen[n] = 0; // beyond end, nothing matchable

        // ── 5. Try one-change solution ──
        for (int k = 0; k < m; k++) {
            int leftEnd = (k == 0) ? -1 : pref[k - 1];
            if (leftEnd == n) continue; // prefix not fully matchable

            int rightStart = (k == m - 1) ? n : suff[k + 1];
            if (rightStart == -1) continue; // suffix not fully matchable

            // Need a position between leftEnd and rightStart for the changed char
            if (leftEnd < rightStart) {
                int changePos = leftEnd + 1; // smallest possible index for changed char

                // changePos must be within word1 bounds
                if (changePos >= n) continue;

                // Check if suffix can be matched from changePos + 1
                int suffixNeeded = m - k - 1;
                if (sufLen[changePos + 1] >= suffixNeeded) {
                    // ── Reconstruct answer ──
                    int[] ans = new int[m];

                    // Copy greedy prefix indices
                    for (int i = 0; i < k; i++) {
                        ans[i] = pref[i];
                    }

                    // Changed position: pick smallest valid index
                    ans[k] = changePos;

                    // Greedy suffix matching from changePos onwards
                    int cur = changePos;
                    for (int i = k + 1; i < m; i++) {
                        cur = nxt[cur + 1][word2.charAt(i) - 'a'];
                        ans[i] = cur;
                    }

                    return ans;
                }
            }
        }

        // No valid sequence found
        return new int[0];
    }

    // ── Test harness ──
    public static void main(String[] args) {
        Solution sol = new Solution();

        // Example 1
        test(sol, "vbcca", "abc", new int[]{0, 1, 2});

        // Example 2
        test(sol, "bacdc", "abc", new int[]{1, 2, 4});

        // Example 3: no possible sequence
        test(sol, "aaaaaa", "aaabc", new int[]{});

        // Example 4: word2 shorter than word1, exact match
        test(sol, "abc", "ab", new int[]{0, 1});

        // Additional test: need change at position 0
        test(sol, "xbc", "abc", new int[]{0, 1, 2});

        // Additional test: exact match works
        test(sol, "abcdef", "ace", new int[]{0, 2, 4});

        // Additional test: change at last character
        test(sol, "abx", "abc", new int[]{0, 1, 2});

        // Large test: ensure no TLE
        StringBuilder sb1 = new StringBuilder();
        StringBuilder sb2 = new StringBuilder();
        for (int i = 0; i < 100000; i++) sb1.append('a');
        for (int i = 0; i < 50000; i++) sb2.append('a');
        int[] res = sol.validSequence(sb1.toString(), sb2.toString());
        System.out.println("Large test (exact match): " + (res.length == 50000 ? "PASS" : "FAIL"));

        System.out.println("\nAll tests completed.");
    }

    private static void test(Solution sol, String word1, String word2, int[] expected) {
        int[] result = sol.validSequence(word1, word2);
        boolean pass = java.util.Arrays.equals(result, expected);

        // For result arrays, also verify validity
        if (result.length > 0) {
            pass = pass && isValid(word1, word2, result);
        } else if (expected.length == 0) {
            pass = true;
        }

        System.out.printf("word1=\"%s\", word2=\"%s\" => %s (expected %s) [%s]%n",
            word1, word2,
            java.util.Arrays.toString(result),
            java.util.Arrays.toString(expected),
            pass ? "PASS" : "FAIL");
    }

    /**
     * Verify that the index sequence is valid:
     * - Indices are strictly increasing
     * - Concatenated string is almost equal to word2 (at most 1 diff)
     */
    private static boolean isValid(String word1, String word2, int[] seq) {
        if (seq.length != word2.length()) return false;
        int prev = -1;
        int mismatches = 0;
        for (int i = 0; i < seq.length; i++) {
            if (seq[i] <= prev) return false;
            if (seq[i] < 0 || seq[i] >= word1.length()) return false;
            if (word1.charAt(seq[i]) != word2.charAt(i)) {
                mismatches++;
            }
            prev = seq[i];
        }
        return mismatches <= 1;
    }
}
