class Solution {
    public String smallestSubsequence(String s) {
        int n = s.length();
        int[] last = new int[26];
        boolean[] inStack = new boolean[26];

        // Record the last occurrence index of each character
        for (int i = 0; i < n; i++) {
            last[s.charAt(i) - 'a'] = i;
        }

        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < n; i++) {
            char c = s.charAt(i);
            int idx = c - 'a';

            // If character is already in the result, skip
            if (inStack[idx]) {
                continue;
            }

            // Greedy: while the current character is smaller than the top of stack,
            // and the top character will appear later in the string, pop it
            while (sb.length() > 0
                   && c < sb.charAt(sb.length() - 1)
                   && last[sb.charAt(sb.length() - 1) - 'a'] > i) {
                char removed = sb.charAt(sb.length() - 1);
                inStack[removed - 'a'] = false;
                sb.deleteCharAt(sb.length() - 1);
            }

            sb.append(c);
            inStack[idx] = true;
        }

        return sb.toString();
    }
}
