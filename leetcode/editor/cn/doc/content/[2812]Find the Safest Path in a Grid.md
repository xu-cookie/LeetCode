# 2812. Find the Safest Path in a Grid

**Difficulty**: Medium

## Problem

You are given a 0-indexed 2D matrix `grid` of size `n x n`, where:
- `grid[r][c] = 1` means a cell containing a thief
- `grid[r][c] = 0` means an empty cell

You start at `(0, 0)`. In one move, you can move to any adjacent cell (including those with thieves).

The **safeness factor** of a path is the **minimum** Manhattan distance from any cell in the path to any thief.

Return the **maximum** safeness factor of all paths from `(0, 0)` to `(n - 1, n - 1)`.

## Approach

### Two-Step Solution

1. **Multi-source BFS** — Compute the minimum Manhattan distance from each cell to the nearest thief
2. **Binary Search + BFS** — Search for the maximum safeness factor k, verifying path existence via BFS

### Complexity

- **Time**: O(n² log n)
- **Space**: O(n²)
