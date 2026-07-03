# [3620] Network Recovery Pathways

**Difficulty:** Hard
**Date:** 2026-07-03
**Link:** https://leetcode.com/problems/network-recovery-pathways/

## Problem Description

You are given a directed acyclic graph of `n` nodes numbered from `0` to `n - 1`. This is represented by a 2D array `edges` of length `m`, where `edges[i] = [ui, vi, costi]` indicates a one-way communication from node `ui` to node `vi` with a recovery cost of `costi`.

Some nodes may be offline. You are given a boolean array `online` where `online[i] = true` means node `i` is online. Nodes `0` and `n - 1` are always online.

A path from `0` to `n - 1` is **valid** if:

- All intermediate nodes on the path are online.
- The total recovery cost of all edges on the path does not exceed `k`.

For each valid path, define its **score** as the minimum edge-cost along that path.

Return the **maximum** path score (i.e., the largest minimum-edge cost) among all valid paths. If no valid path exists, return `-1`.

## Approach

**Binary Search + DAG Dynamic Programming**

This is a "maximin" path problem — maximize the minimum edge cost on a path, subject to total cost ≤ k and online node constraints.

### Key Insight

Binary search on the answer `X`: can we achieve a valid path where every edge has cost ≥ X?

For a fixed `X`:
1. Filter edges: only keep edges with cost ≥ X
2. Run DP in topological order to compute the **minimum total cost** from 0 to each node
3. If `dp[n-1] ≤ k`, then X is achievable

Since the graph is a DAG, one topological sort works for all edge subsets (filtered subgraphs respect the same order).

### DP Details

- `dp[i]` = minimum total cost from node 0 to node i, using only edges with cost ≥ X and valid nodes
- Process nodes in topological order, relaxing outgoing edges
- Skip offline intermediate nodes (source 0 and target n-1 are always online)

## Complexity Analysis

- **Time Complexity:** O((n + m) · log C) where C = max edge cost (≤ 10^9, log C ≤ 30)
  - Each `canAchieve` call: O(n + m)
  - Binary search: ~30 iterations
  - Total: ~3 × 10^6 operations
- **Space Complexity:** O(n + m) — adjacency list, DP array, topological order

## Examples

### Example 1
```
Input: edges = [[0,1,5],[1,3,10],[0,2,3],[2,3,4]], online = [true,true,true,true], k = 10
Output: 3
```
- Path 0→1→3: total cost = 5+10 = 15 > 10, invalid
- Path 0→2→3: total cost = 3+4 = 7 ≤ 10, min edge = min(3,4) = 3 ✓

### Example 2
```
Input: edges = [[0,1,7],[1,4,5],[0,2,6],[2,3,6],[3,4,2],[2,4,6]], online = [true,true,true,false,true], k = 12
Output: 6
```
- Node 3 is offline, paths through it are invalid
- Path 0→1→4: total = 12, min edge = 5
- Path 0→2→4: total = 12, min edge = 6 ← maximum
