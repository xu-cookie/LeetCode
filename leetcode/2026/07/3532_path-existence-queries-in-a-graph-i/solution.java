/**
 * LeetCode Daily Challenge - 2026-07-09
 * Problem: 3532. Path Existence Queries in a Graph I
 * Difficulty: Medium
 * Link: https://leetcode.com/problems/path-existence-queries-in-a-graph-i/
 *
 * Problem Description:
 * You are given an integer n representing the number of nodes in a graph,
 * labeled from 0 to n - 1. You are also given an integer array nums of
 * length n sorted in non-decreasing order, and an integer maxDiff.
 *
 * An undirected edge exists between nodes i and j if the absolute difference
 * between nums[i] and nums[j] is at most maxDiff.
 *
 * You are also given a 2D integer array queries. For each queries[i] = [ui, vi],
 * determine whether there exists a path between nodes ui and vi.
 *
 * Return a boolean array answer.
 *
 * Approach: Contiguous Component Labeling
 *
 * Key Insight:
 * Since nums is sorted in non-decreasing order, the graph's connectivity is
 * determined solely by adjacent differences. If |nums[i] - nums[i-1]| > maxDiff,
 * no edge can cross between the left group and the right group — because for
 * any a ≤ i-1 and b ≥ i, we have nums[b] - nums[a] ≥ nums[i] - nums[i-1] > maxDiff.
 *
 * Therefore, connected components are contiguous intervals. A gap where
 * |nums[i] - nums[i-1]| > maxDiff creates a permanent cut.
 *
 * Algorithm:
 *   1. Assign each node a component ID:
 *      - Start with component 0 for node 0.
 *      - For i from 1 to n-1: if nums[i] - nums[i-1] > maxDiff, start a new
 *        component; otherwise, inherit the previous component ID.
 *   2. For each query [u, v]: answer true iff component[u] == component[v].
 *
 * Time Complexity:  O(n + q)
 * Space Complexity: O(n) for the component array
 */
class Solution {
    public boolean[] pathExistenceQueries(int n, int[] nums, int maxDiff, int[][] queries) {
        // Assign each node to its connected component
        int[] comp = new int[n];
        int compId = 0;
        comp[0] = 0;

        for (int i = 1; i < n; i++) {
            if (nums[i] - nums[i - 1] > maxDiff) {
                compId++;
            }
            comp[i] = compId;
        }

        // Answer each query in O(1)
        int q = queries.length;
        boolean[] answer = new boolean[q];
        for (int i = 0; i < q; i++) {
            answer[i] = comp[queries[i][0]] == comp[queries[i][1]];
        }

        return answer;
    }

    // Test harness — run locally to verify correctness
    public static void main(String[] args) {
        Solution sol = new Solution();

        // Example 1
        boolean[] r1 = sol.pathExistenceQueries(2, new int[]{1, 3}, 1,
                new int[][]{{0, 0}, {0, 1}});
        System.out.print("Example 1: [");
        for (int i = 0; i < r1.length; i++) {
            if (i > 0) System.out.print(", ");
            System.out.print(r1[i]);
        }
        System.out.println("] (expected [true, false]) — "
                + (r1[0] && !r1[1] ? "PASS" : "FAIL"));

        // Example 2
        boolean[] r2 = sol.pathExistenceQueries(4, new int[]{2, 5, 6, 8}, 2,
                new int[][]{{0, 1}, {0, 2}, {1, 3}, {2, 3}});
        System.out.print("Example 2: [");
        for (int i = 0; i < r2.length; i++) {
            if (i > 0) System.out.print(", ");
            System.out.print(r2[i]);
        }
        System.out.println("] (expected [false, false, true, true]) — "
                + (!r2[0] && !r2[1] && r2[2] && r2[3] ? "PASS" : "FAIL"));

        // Edge case: single node
        boolean[] r3 = sol.pathExistenceQueries(1, new int[]{5}, 0,
                new int[][]{{0, 0}});
        System.out.println("Single node [0,0]: " + r3[0] + " (expected true) — "
                + (r3[0] ? "PASS" : "FAIL"));

        // Edge case: all connected (maxDiff is huge)
        boolean[] r4 = sol.pathExistenceQueries(5, new int[]{1, 2, 3, 4, 5}, 100,
                new int[][]{{0, 4}, {1, 3}});
        System.out.print("All connected: [");
        for (int i = 0; i < r4.length; i++) {
            if (i > 0) System.out.print(", ");
            System.out.print(r4[i]);
        }
        System.out.println("] (expected [true, true]) — "
                + (r4[0] && r4[1] ? "PASS" : "FAIL"));

        // Edge case: all disconnected (maxDiff = 0, all distinct)
        boolean[] r5 = sol.pathExistenceQueries(4, new int[]{1, 3, 5, 7}, 0,
                new int[][]{{0, 1}, {0, 2}, {0, 3}});
        System.out.print("All disconnected (maxDiff=0): [");
        for (int i = 0; i < r5.length; i++) {
            if (i > 0) System.out.print(", ");
            System.out.print(r5[i]);
        }
        System.out.println("] (expected [false, false, false]) — "
                + (!r5[0] && !r5[1] && !r5[2] ? "PASS" : "FAIL"));

        // Edge case: equal values connected
        boolean[] r6 = sol.pathExistenceQueries(4, new int[]{2, 2, 2, 5}, 0,
                new int[][]{{0, 2}, {2, 3}});
        System.out.print("Equal values: [");
        for (int i = 0; i < r6.length; i++) {
            if (i > 0) System.out.print(", ");
            System.out.print(r6[i]);
        }
        // [2,2,2,5], maxDiff=0: |2-2|=0≤0 connected, |2-5|=3>0 disconnected
        System.out.println("] (expected [true, false]) — "
                + (r6[0] && !r6[1] ? "PASS" : "FAIL"));

        // Edge case: multiple components
        boolean[] r7 = sol.pathExistenceQueries(6, new int[]{1, 2, 10, 11, 20, 21}, 1,
                new int[][]{{0, 1}, {2, 3}, {4, 5}, {0, 3}, {0, 5}});
        System.out.print("Multi-component: [");
        for (int i = 0; i < r7.length; i++) {
            if (i > 0) System.out.print(", ");
            System.out.print(r7[i]);
        }
        // Components: {0,1}, {2,3}, {4,5}
        System.out.println("] (expected [true, true, true, false, false]) — "
                + (r7[0] && r7[1] && r7[2] && !r7[3] && !r7[4] ? "PASS" : "FAIL"));

        // Large test: stress test with 100k nodes
        int n = 100000;
        int[] nums = new int[n];
        for (int i = 0; i < n; i++) nums[i] = i * 10; // diff=10 between adjacent
        boolean[] r8 = sol.pathExistenceQueries(n, nums, 9,
                new int[][]{{0, n - 1}, {0, 50000}, {50000, n - 1}});
        System.out.print("Large disconnected (diff=10, maxDiff=9): [");
        for (int i = 0; i < r8.length; i++) {
            if (i > 0) System.out.print(", ");
            System.out.print(r8[i]);
        }
        System.out.println("] (expected [false, false, false]) — "
                + (!r8[0] && !r8[1] && !r8[2] ? "PASS" : "FAIL"));

        // Large test: all connected
        boolean[] r9 = sol.pathExistenceQueries(n, nums, 10,
                new int[][]{{0, n - 1}});
        System.out.println("Large connected (diff=10, maxDiff=10): " + r9[0]
                + " (expected true) — " + (r9[0] ? "PASS" : "FAIL"));
    }
}
