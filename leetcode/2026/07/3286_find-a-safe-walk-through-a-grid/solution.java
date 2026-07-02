/**
 * LeetCode Daily Challenge - 2026-07-02
 * Problem: 3286. Find a Safe Walk Through a Grid
 * Difficulty: Medium
 * Link: https://leetcode.com/problems/find-a-safe-walk-through-a-grid/
 *
 * Problem Description:
 * You are given an m x n binary matrix grid and an integer health.
 * You start on the upper-left corner (0, 0) and would like to get to
 * the lower-right corner (m-1, n-1).
 * You can move up, down, left, or right from one cell to another adjacent
 * cell as long as your health remains positive.
 * Cells (i, j) with grid[i][j] = 1 are considered unsafe and reduce your
 * health by 1.
 * Return true if you can reach the final cell with a health value of 1 or
 * more, and false otherwise.
 *
 * Approach: 0-1 BFS (Deque)
 * - Treat grid[i][j] = 1 as edge weight 1, grid[i][j] = 0 as edge weight 0.
 * - Use a deque to perform 0-1 BFS: push 0-weight cells to front,
 *   1-weight cells to back.
 * - dist[r][c] = minimum health cost to reach (r, c) from (0, 0).
 * - Return true if dist[m-1][n-1] < health (need health >= 1 at end).
 *
 * Time Complexity: O(m * n) — each cell processed at most once per
 *                  improvement (bounded by constant due to 0/1 weights)
 * Space Complexity: O(m * n) — distance array + deque
 */

import java.util.*;

class Solution {
    public boolean findSafeWalk(List<List<Integer>> grid, int health) {
        int m = grid.size();
        int n = grid.get(0).size();

        // dist[i][j] = min unsafe cells encountered from (0,0) to (i,j)
        int[][] dist = new int[m][n];
        for (int i = 0; i < m; i++) {
            Arrays.fill(dist[i], Integer.MAX_VALUE);
        }

        Deque<int[]> deque = new ArrayDeque<>();
        int startCost = grid.get(0).get(0);
        dist[0][0] = startCost;
        deque.offerFirst(new int[]{0, 0});

        int[][] dirs = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};

        while (!deque.isEmpty()) {
            int[] cur = deque.pollFirst();
            int r = cur[0], c = cur[1];

            // Early exit: if we've reached the target
            if (r == m - 1 && c == n - 1) {
                return dist[r][c] < health;
            }

            for (int[] dir : dirs) {
                int nr = r + dir[0];
                int nc = c + dir[1];

                if (nr < 0 || nr >= m || nc < 0 || nc >= n) {
                    continue;
                }

                int newDist = dist[r][c] + grid.get(nr).get(nc);
                if (newDist < dist[nr][nc]) {
                    dist[nr][nc] = newDist;
                    // 0-weight: push front, 1-weight: push back
                    if (grid.get(nr).get(nc) == 0) {
                        deque.offerFirst(new int[]{nr, nc});
                    } else {
                        deque.offerLast(new int[]{nr, nc});
                    }
                }
            }
        }

        return false; // unreachable per constraints
    }
}
