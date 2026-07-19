/**
 * LeetCode Daily Challenge - 2026-07-20
 * Problem: 1260. Shift 2D Grid
 * Difficulty: Easy
 * Link: https://leetcode.com/problems/shift-2d-grid/
 *
 * Problem Description:
 * Given a 2D grid of size m x n and an integer k, shift the grid k times.
 * In one shift operation:
 *   - grid[i][j] moves to grid[i][j + 1]
 *   - grid[i][n - 1] moves to grid[i + 1][0]
 *   - grid[m - 1][n - 1] moves to grid[0][0]
 * Return the 2D grid after applying shift operation k times.
 *
 * Approach: 1D Flattening + Index Mapping (O(m*n) time, O(m*n) space)
 *
 * Key Insight:
 * The grid can be conceptualized as a 1D array of size total = m * n.
 * Each shift operation moves every element one position to the right
 * in this flattened representation, with wraparound. After k shifts,
 * an element originally at 1D index p moves to 1D index (p + k) % total.
 *
 * Algorithm:
 *   1. Compute total = m * n. Since k may be large, use k %= total
 *      (full rotations bring us back to the original configuration).
 *   2. Create a result list of size m x n, initially all zeros.
 *   3. For each cell (i, j) in the original grid:
 *      - Compute 1D index:      p = i * n + j
 *      - Compute new 1D index:  newP = (p + k) % total
 *      - Compute new 2D coords: newRow = newP / n, newCol = newP % n
 *      - result.get(newRow).set(newCol, grid[i][j])
 *   4. Return the result.
 *
 * Time Complexity:  O(m * n) — each cell visited exactly once
 * Space Complexity: O(m * n) — for the result grid (required output;
 *                    O(1) extra space beyond the output)
 */

import java.util.ArrayList;
import java.util.List;

class Solution {
    public List<List<Integer>> shiftGrid(int[][] grid, int k) {
        int m = grid.length;
        int n = grid[0].length;
        int total = m * n;

        // Reduce k modulo total: full rotations bring grid back to original
        k %= total;

        // If k == 0, the grid is unchanged — but we still create a proper copy
        // because the result should be a new List<List<Integer>>

        // Initialize result grid filled with 0
        List<List<Integer>> result = new ArrayList<>(m);
        for (int i = 0; i < m; i++) {
            List<Integer> row = new ArrayList<>(n);
            for (int j = 0; j < n; j++) {
                row.add(0);
            }
            result.add(row);
        }

        // Map each original cell to its new position after k shifts
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                int oldPos = i * n + j;          // 1D index before shifting
                int newPos = (oldPos + k) % total; // 1D index after shifting
                int newRow = newPos / n;          // corresponding 2D row
                int newCol = newPos % n;          // corresponding 2D column

                result.get(newRow).set(newCol, grid[i][j]);
            }
        }

        return result;
    }

    // Test harness — run locally to verify correctness
    public static void main(String[] args) {
        Solution sol = new Solution();

        // Example 1:
        // grid = [[1,2,3],[4,5,6],[7,8,9]], k = 1
        // After 1 shift: 9→[0][0], 1→[0][1], 2→[0][2],
        //                 3→[1][0], 4→[1][1], 5→[1][2],
        //                 6→[2][0], 7→[2][1], 8→[2][2]
        // Result: [[9,1,2],[3,4,5],[6,7,8]]
        int[][] grid1 = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        List<List<Integer>> r1 = sol.shiftGrid(grid1, 1);
        System.out.println("Example 1: " + r1);
        System.out.println("Expected:  [[9, 1, 2], [3, 4, 5], [6, 7, 8]]");
        System.out.println("PASS: " + r1.toString().equals("[[9, 1, 2], [3, 4, 5], [6, 7, 8]]"));
        System.out.println();

        // Example 2:
        // grid = [[3,8,1,9],[19,7,2,5],[4,6,11,10],[12,0,21,13]], k = 4
        // Result: [[12,0,21,13],[3,8,1,9],[19,7,2,5],[4,6,11,10]]
        int[][] grid2 = {{3, 8, 1, 9}, {19, 7, 2, 5}, {4, 6, 11, 10}, {12, 0, 21, 13}};
        List<List<Integer>> r2 = sol.shiftGrid(grid2, 4);
        System.out.println("Example 2: " + r2);
        System.out.println("Expected:  [[12, 0, 21, 13], [3, 8, 1, 9], [19, 7, 2, 5], [4, 6, 11, 10]]");
        System.out.println("PASS: " + r2.toString().equals("[[12, 0, 21, 13], [3, 8, 1, 9], [19, 7, 2, 5], [4, 6, 11, 10]]"));
        System.out.println();

        // Example 3:
        // grid = [[1,2,3],[4,5,6],[7,8,9]], k = 9
        // k = 9 = m * n = full rotation → same as original
        int[][] grid3 = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        List<List<Integer>> r3 = sol.shiftGrid(grid3, 9);
        System.out.println("Example 3 (k=9, full rotation): " + r3);
        System.out.println("Expected:  [[1, 2, 3], [4, 5, 6], [7, 8, 9]]");
        System.out.println("PASS: " + r3.toString().equals("[[1, 2, 3], [4, 5, 6], [7, 8, 9]]"));
        System.out.println();

        // Edge case: k = 0 (no shift)
        int[][] grid4 = {{1, 2}, {3, 4}};
        List<List<Integer>> r4 = sol.shiftGrid(grid4, 0);
        System.out.println("k=0 (no shift): " + r4);
        System.out.println("PASS: " + r4.toString().equals("[[1, 2], [3, 4]]"));
        System.out.println();

        // Edge case: single row grid
        int[][] grid5 = {{1, 2, 3, 4}};
        List<List<Integer>> r5 = sol.shiftGrid(grid5, 2);
        System.out.println("Single row, k=2: " + r5);
        System.out.println("Expected:  [[3, 4, 1, 2]]");
        System.out.println("PASS: " + r5.toString().equals("[[3, 4, 1, 2]]"));
        System.out.println();

        // Edge case: single column grid
        int[][] grid6 = {{1}, {2}, {3}};
        List<List<Integer>> r6 = sol.shiftGrid(grid6, 1);
        System.out.println("Single column, k=1: " + r6);
        System.out.println("Expected:  [[3], [1], [2]]");
        System.out.println("PASS: " + r6.toString().equals("[[3], [1], [2]]"));
    }
}
