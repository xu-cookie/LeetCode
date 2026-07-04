# 2492. Minimum Score of a Path Between Two Cities

- **Difficulty**: Medium
- **Link**: https://leetcode.com/problems/minimum-score-of-a-path-between-two-cities/
- **Date Solved**: 2026-07-04

## Problem Summary

Given `n` cities and a list of bidirectional roads `roads[i] = [ai, bi, distance_i]`, define the **score** of a path as the minimum distance of any road on that path. A path may revisit cities and traverse the same road multiple times.

Return the **minimum possible score** of any path from city 1 to city n.

## Key Insight

Since we can revisit cities and roads arbitrarily, we can always detour from any path to include **any** edge in the same connected component. The score of a path is determined by its smallest edge, so to minimize the score we simply include the absolute smallest edge in the connected component containing both city 1 and city n.

This transforms the problem from a path-finding problem to a connected-component exploration problem.

## Approach

1. Build an undirected adjacency list from the `roads` array.
2. Run DFS (or BFS) starting from city 1 to explore the connected component.
3. While traversing, track the minimum edge distance encountered.
4. Return that minimum — it is the answer.

## Complexity

- **Time Complexity**: O(n + m) — each node is visited once, each edge is traversed twice (once from each endpoint).
- **Space Complexity**: O(n + m) — adjacency list (O(m) edges stored twice) plus visited array (O(n)).
