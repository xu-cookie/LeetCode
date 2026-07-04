/**
 * LeetCode Daily Challenge - 2026-07-04
 * Problem: 2492. Minimum Score of a Path Between Two Cities
 * Difficulty: Medium
 * Link: https://leetcode.com/problems/minimum-score-of-a-path-between-two-cities/
 *
 * Problem Description:
 * You are given a positive integer n representing n cities numbered from 1 to n.
 * You are also given a 2D array roads where roads[i] = [ai, bi, distancei]
 * indicates that there is a bidirectional road between cities ai and bi with a
 * distance equal to distancei. The cities graph is not necessarily connected.
 *
 * The score of a path between two cities is defined as the minimum distance of
 * a road in this path.
 *
 * Return the minimum possible score of a path between cities 1 and n.
 *
 * Note: A path is allowed to contain the same road multiple times, and you can
 * visit cities 1 and n multiple times along the path. The test cases are
 * generated such that there is at least one path between 1 and n.
 *
 * Approach: DFS on Connected Component
 *
 * Key Insight: Since we can revisit cities and traverse the same road multiple
 * times, we can always make a detour from the main path to include ANY edge in
 * the connected component. The path score is the minimum edge on the path, so
 * to minimize the score, we simply include the absolute smallest edge in the
 * entire connected component containing cities 1 and n.
 *
 * Therefore, the problem reduces to: find the minimum edge weight in the
 * connected component that contains both city 1 and city n.
 *
 * Algorithm:
 *   1. Build an undirected adjacency list from roads.
 *   2. Run DFS (or BFS) starting from city 1 to explore the component.
 *   3. While traversing, track the minimum edge distance seen.
 *   4. Since city n is guaranteed reachable from city 1, they are in the same
 *      component — return the tracked minimum.
 *
 * Time Complexity:  O(n + m) — each node visited once, each edge visited twice
 * Space Complexity: O(n + m) — adjacency list and visited array
 */

import java.util.*;

class Solution {
    public int minScore(int n, int[][] roads) {
        // Build adjacency list: graph[u] = list of [v, distance]
        List<int[]>[] graph = new List[n + 1];
        for (int i = 1; i <= n; i++) {
            graph[i] = new ArrayList<>();
        }
        for (int[] road : roads) {
            int u = road[0], v = road[1], d = road[2];
            graph[u].add(new int[]{v, d});
            graph[v].add(new int[]{u, d});
        }

        // Track the minimum edge weight in the component containing city 1
        int[] minEdge = new int[]{Integer.MAX_VALUE};
        boolean[] visited = new boolean[n + 1];

        // DFS from city 1
        dfs(1, graph, visited, minEdge);

        return minEdge[0];
    }

    /**
     * Depth-first search to explore the connected component and track the
     * minimum edge weight encountered.
     */
    private void dfs(int node, List<int[]>[] graph, boolean[] visited, int[] minEdge) {
        visited[node] = true;
        for (int[] edge : graph[node]) {
            int neighbor = edge[0];
            int distance = edge[1];

            // Update minimum edge regardless of whether neighbor is visited,
            // since every edge in the component contributes to the possible path
            if (distance < minEdge[0]) {
                minEdge[0] = distance;
            }

            if (!visited[neighbor]) {
                dfs(neighbor, graph, visited, minEdge);
            }
        }
    }

    // Test harness — run locally to verify correctness
    public static void main(String[] args) {
        Solution sol = new Solution();

        // Example 1
        int[][] roads1 = {{1, 2, 9}, {2, 3, 6}, {2, 4, 5}, {1, 4, 7}};
        int res1 = sol.minScore(4, roads1);
        System.out.println("Example 1: " + res1 + " (expected 5) — " + (res1 == 5 ? "PASS" : "FAIL"));

        // Example 2
        int[][] roads2 = {{1, 2, 2}, {1, 3, 4}, {3, 4, 7}};
        int res2 = sol.minScore(4, roads2);
        System.out.println("Example 2: " + res2 + " (expected 2) — " + (res2 == 2 ? "PASS" : "FAIL"));

        // Edge case: direct edge between 1 and n
        int[][] roads3 = {{1, 3, 5}};
        int res3 = sol.minScore(3, roads3);
        System.out.println("Direct 1-n: " + res3 + " (expected 5) — " + (res3 == 5 ? "PASS" : "FAIL"));

        // Edge case: minimum edge not on the most direct path
        int[][] roads4 = {{1, 2, 10}, {2, 3, 10}, {1, 3, 1}};
        int res4 = sol.minScore(3, roads4);
        System.out.println("Triangle min edge off path: " + res4 + " (expected 1) — " + (res4 == 1 ? "PASS" : "FAIL"));

        // Edge case: all equal distances
        int[][] roads5 = {{1, 2, 7}, {2, 3, 7}, {3, 4, 7}};
        int res5 = sol.minScore(4, roads5);
        System.out.println("All equal: " + res5 + " (expected 7) — " + (res5 == 7 ? "PASS" : "FAIL"));

        // Edge case: large component with small distant edge
        int[][] roads6 = {{1, 2, 100}, {2, 3, 1}, {3, 4, 100}, {4, 5, 100}, {5, 1, 100}};
        int res6 = sol.minScore(5, roads6);
        System.out.println("Cycle with small edge: " + res6 + " (expected 1) — " + (res6 == 1 ? "PASS" : "FAIL"));
    }
}
