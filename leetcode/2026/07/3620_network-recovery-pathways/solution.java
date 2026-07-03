/**
 * LeetCode Daily Challenge - 2026-07-03
 * Problem: 3620. Network Recovery Pathways
 * Difficulty: Hard
 * Link: https://leetcode.com/problems/network-recovery-pathways/
 *
 * Problem Description:
 * You are given a directed acyclic graph of n nodes numbered from 0 to n-1.
 * This is represented by a 2D array edges of length m, where
 * edges[i] = [ui, vi, costi] indicates a one-way communication from node ui
 * to node vi with a recovery cost of costi.
 *
 * Some nodes may be offline. You are given a boolean array online where
 * online[i] = true means node i is online. Nodes 0 and n-1 are always online.
 *
 * A path from 0 to n-1 is valid if:
 * - All intermediate nodes on the path are online.
 * - The total recovery cost of all edges on the path does not exceed k.
 *
 * For each valid path, define its score as the minimum edge-cost along that path.
 * Return the maximum path score among all valid paths. If no valid path exists,
 * return -1.
 *
 * Approach: Binary Search + DP on DAG (Topological Order)
 *
 * This is a "maximin" path problem (maximize the minimum edge on a path).
 * Key insight: binary search on the answer X = "can we achieve a path where
 * every edge has cost >= X and total cost <= k?"
 *
 * For each candidate X:
 *   1. Filter edges: only keep edges with cost >= X
 *   2. Run DP on the DAG's topological order to find the minimum total cost
 *      from 0 to n-1, using only online intermediate nodes
 *   3. If dp[n-1] <= k, then X is achievable
 *
 * Since the graph is a DAG, topological order is valid for any edge subset.
 * The DP computes shortest path in O(n+m) per binary search iteration.
 *
 * Time Complexity: O((n + m) * log(maxCost))
 *   - n <= 5*10^4, m <= 10^5, log(maxCost) <= 30
 *   - ~3 million operations, well within limits
 * Space Complexity: O(n + m) — adjacency list + DP array + topo order
 */

import java.util.*;

class Solution {
    public int findMaxPathScore(int[][] edges, boolean[] online, long k) {
        int n = online.length;

        // Build adjacency list and compute in-degree for topological sort
        List<int[]>[] adj = new List[n];
        for (int i = 0; i < n; i++) {
            adj[i] = new ArrayList<>();
        }

        int[] indegree = new int[n];
        int maxCost = 0;
        for (int[] e : edges) {
            int u = e[0], v = e[1], c = e[2];
            adj[u].add(new int[]{v, c});
            indegree[v]++;
            if (c > maxCost) maxCost = c;
        }

        // Topological sort (Kahn's algorithm) — computed once and reused
        int[] topo = new int[n];
        int topoIdx = 0;
        Deque<Integer> queue = new ArrayDeque<>();
        for (int i = 0; i < n; i++) {
            if (indegree[i] == 0) {
                queue.offer(i);
            }
        }
        // Working copy of indegree since Kahn's mutates it
        int[] indeg = indegree.clone();
        while (!queue.isEmpty()) {
            int u = queue.poll();
            topo[topoIdx++] = u;
            for (int[] e : adj[u]) {
                int v = e[0];
                if (--indeg[v] == 0) {
                    queue.offer(v);
                }
            }
        }

        // First check: is there any valid path at all (minCost = 0)?
        if (!canAchieve(0, n, adj, topo, online, k)) {
            return -1;
        }

        // Binary search for the maximum achievable minimum edge cost
        int lo = 0, hi = maxCost;
        while (lo < hi) {
            int mid = (lo + hi + 1) >>> 1;
            if (canAchieve(mid, n, adj, topo, online, k)) {
                lo = mid;
            } else {
                hi = mid - 1;
            }
        }
        return lo;
    }

    /**
     * Checks whether there exists a path from 0 to n-1 satisfying:
     * - Every edge on the path has cost >= minCost
     * - All intermediate nodes are online
     * - Total cost <= k
     *
     * Uses DP on the precomputed topological order to find the minimum
     * total cost path under these constraints.
     */
    private boolean canAchieve(int minCost, int n, List<int[]>[] adj,
                               int[] topo, boolean[] online, long k) {
        final long INF = Long.MAX_VALUE / 2;
        long[] dp = new long[n];
        Arrays.fill(dp, INF);
        dp[0] = 0;

        for (int u : topo) {
            if (dp[u] == INF) continue;
            // Only extend from online nodes (source 0 and target n-1 are always online)
            if (u != 0 && u != n - 1 && !online[u]) continue;

            for (int[] edge : adj[u]) {
                int v = edge[0];
                int cost = edge[1];

                if (cost < minCost) continue;
                // Only go to online nodes (or the target n-1)
                if (v != n - 1 && !online[v]) continue;

                long newDist = dp[u] + cost;
                if (newDist < dp[v]) {
                    dp[v] = newDist;
                }
            }
        }

        return dp[n - 1] <= k;
    }
}
