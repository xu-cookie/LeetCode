# 1260. Shift 2D Grid

- **Difficulty**: Easy
- **Link**: https://leetcode.com/problems/shift-2d-grid/
- **Date Solved**: 2026-07-20

## Problem Summary

Given a 2D `grid` of size `m x n` and an integer `k`, shift the grid `k` times. In one shift operation:
- `grid[i][j]` moves to `grid[i][j + 1]`
- `grid[i][n - 1]` moves to `grid[i + 1][0]`
- `grid[m - 1][n - 1]` moves to `grid[0][0]`

Return the 2D grid after applying the shift operation `k` times.

## Examples

| grid | k | Output |
|------|---|--------|
| `[[1,2,3],[4,5,6],[7,8,9]]` | 1 | `[[9,1,2],[3,4,5],[6,7,8]]` |
| `[[3,8,1,9],[19,7,2,5],[4,6,11,10],[12,0,21,13]]` | 4 | `[[12,0,21,13],[3,8,1,9],[19,7,2,5],[4,6,11,10]]` |
| `[[1,2,3],[4,5,6],[7,8,9]]` | 9 | `[[1,2,3],[4,5,6],[7,8,9]]` |

## Approach: 1D Flattening + Index Mapping

### Key Insight

The shift operation is equivalent to taking the flattened 1D representation of the grid and rotating it right by `k` positions. Each element's new position can be computed directly from its original coordinates without simulating each shift step-by-step.

For an element at `grid[i][j]`:
- Flattened 1D index: `p = i * n + j`
- New 1D index after k shifts: `p' = (p + k) % (m * n)`
- New 2D coordinates: `newRow = p' / n`, `newCol = p' % n`

### Algorithm

1. Compute `total = m * n`. Reduce `k` modulo `total` since full rotations bring the grid back to its original state.
2. Create a result grid of size `m x n`, initially filled with zeros.
3. For each cell `(i, j)` in the original grid:
   - Compute `newRow = (i * n + j + k) / n`, `newCol = (i * n + j + k) % n`
   - Place `grid[i][j]` at `result[newRow][newCol]`
4. Return the result.

### Visualization: `grid = [[1,2,3],[4,5,6],[7,8,9]]`, k = 1

Flattened array: `[1, 2, 3, 4, 5, 6, 7, 8, 9]`

| Original (i,j) | 1D index p | New 1D index (p+1)%9 | New (row,col) | Value |
|----------------|------------|----------------------|----------------|-------|
| (0,0) | 0 | 1 | (0,1) | 1 |
| (0,1) | 1 | 2 | (0,2) | 2 |
| (0,2) | 2 | 3 | (1,0) | 3 |
| (1,0) | 3 | 4 | (1,1) | 4 |
| (1,1) | 4 | 5 | (1,2) | 5 |
| (1,2) | 5 | 6 | (2,0) | 6 |
| (2,0) | 6 | 7 | (2,1) | 7 |
| (2,1) | 7 | 8 | (2,2) | 8 |
| (2,2) | 8 | 0 | (0,0) | 9 |

Result: `[[9,1,2],[3,4,5],[6,7,8]]`

## Complexity

- **Time Complexity**: `O(m * n)` — each cell is visited exactly once.
- **Space Complexity**: `O(m * n)` — for the result grid (required output; `O(1)` extra space beyond the output).

## Tags

`array`, `matrix`, `simulation`, `index mapping`
