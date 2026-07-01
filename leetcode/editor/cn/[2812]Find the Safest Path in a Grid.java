/**
 * LeetCode #2812: Find the Safest Path in a Grid
 * Difficulty: Medium
 *
 * Approach: Multi-source BFS + Binary Search + BFS
 *
 * Step 1: Multi-source BFS to compute the minimum Manhattan distance from
 *         each cell to the nearest thief (dist[r][c]).
 * Step 2: Binary search on the safeness factor k. For each k, use BFS to
 *         check if there exists a path from (0,0) to (n-1,n-1) where every
 *         cell on the path has dist >= k.
 *
 * Time Complexity:  O(n^2 * log n)
 * Space Complexity: O(n^2)
 */
class Solution {
    public int maximumSafenessFactor(List<List<Integer>> grid) {
        int n = grid.size();

        // Step 1: Multi-source BFS to compute distance to nearest thief
        int[][] dist = new int[n][n];
        Queue<int[]> queue = new LinkedList<>();

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (grid.get(i).get(j) == 1) {
                    dist[i][j] = 0;
                    queue.offer(new int[]{i, j});
                } else {
                    dist[i][j] = Integer.MAX_VALUE;
                }
            }
        }

        int[][] dirs = {{0, 1}, {0, -1}, {1, 0}, {-1, 0}};
        while (!queue.isEmpty()) {
            int[] cur = queue.poll();
            int r = cur[0], c = cur[1];
            for (int[] d : dirs) {
                int nr = r + d[0], nc = c + d[1];
                if (nr >= 0 && nr < n && nc >= 0 && nc < n
                        && dist[nr][nc] == Integer.MAX_VALUE) {
                    dist[nr][nc] = dist[r][c] + 1;
                    queue.offer(new int[]{nr, nc});
                }
            }
        }

        // Step 2: Binary search on the answer
        // Maximum possible Manhattan distance in n*n grid is 2*(n-1)
        int lo = 0, hi = 2 * n;
        int ans = 0;

        while (lo <= hi) {
            int mid = lo + (hi - lo) / 2;
            if (canReach(dist, mid, n)) {
                ans = mid;
                lo = mid + 1;
            } else {
                hi = mid - 1;
            }
        }

        return ans;
    }

    /**
     * Check if there exists a path from (0,0) to (n-1,n-1) where
     * every cell on the path has distance >= k to the nearest thief.
     */
    private boolean canReach(int[][] dist, int k, int n) {
        // Start or end cell has insufficient safety factor
        if (dist[0][0] < k || dist[n - 1][n - 1] < k) return false;

        boolean[][] visited = new boolean[n][n];
        Queue<int[]> queue = new LinkedList<>();
        queue.offer(new int[]{0, 0});
        visited[0][0] = true;

        int[][] dirs = {{0, 1}, {0, -1}, {1, 0}, {-1, 0}};
        while (!queue.isEmpty()) {
            int[] cur = queue.poll();
            int r = cur[0], c = cur[1];

            if (r == n - 1 && c == n - 1) return true;

            for (int[] d : dirs) {
                int nr = r + d[0], nc = c + d[1];
                if (nr >= 0 && nr < n && nc >= 0 && nc < n
                        && !visited[nr][nc] && dist[nr][nc] >= k) {
                    visited[nr][nc] = true;
                    queue.offer(new int[]{nr, nc});
                }
            }
        }

        return false;
    }
}
