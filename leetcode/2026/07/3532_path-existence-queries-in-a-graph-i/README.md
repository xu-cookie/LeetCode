# 3532. Path Existence Queries in a Graph I

- **Difficulty**: Medium
- **Link**: https://leetcode.com/problems/path-existence-queries-in-a-graph-i/
- **Date Solved**: 2026-07-09

## Problem Summary

Given:
- `n` nodes labeled 0 to n-1
- A sorted array `nums` of length `n`
- An integer `maxDiff`

An undirected edge exists between nodes `i` and `j` if `|nums[i] - nums[j]| ≤ maxDiff`.

For each query `[u, v]`, determine if there exists a path between nodes `u` and `v`.

## Examples

| n | nums | maxDiff | queries | Output |
|---|------|---------|---------|--------|
| 2 | [1,3] | 1 | [[0,0],[0,1]] | [true, false] |
| 4 | [2,5,6,8] | 2 | [[0,1],[0,2],[1,3],[2,3]] | [false, false, true, true] |

**Example 1**: Node 0 connects to itself. `|1-3|=2 > 1` → no edge between 0 and 1.

**Example 2**: Adjacent diffs: `|2-5|=3>2` (cut), `|5-6|=1≤2`, `|6-8|=2≤2`. Components: {0}, {1,2,3}.

## Approach

**Contiguous Component Labeling** — O(n + q)

### Key Insight

Since `nums` is sorted, connected components are **contiguous intervals**. If `|nums[i] - nums[i-1]| > maxDiff`, no edge can cross from the left side to the right side. This is because for any `a ≤ i-1` and `b ≥ i`:

```
nums[b] - nums[a] ≥ nums[i] - nums[i-1] > maxDiff
```

So a gap where adjacent diff exceeds `maxDiff` creates a permanent cut in the graph.

### Algorithm

1. **Label components** in one pass:
   - Node 0 belongs to component 0
   - For i from 1 to n-1: if `nums[i] - nums[i-1] > maxDiff`, start a new component; otherwise, same component
2. **Answer queries** in O(1): `comp[u] == comp[v]`

### Why This Works

The sorted property ensures that if any two nodes have a path between them, all adjacent gaps along the way are ≤ maxDiff. Conversely, if a gap exceeds maxDiff, the graph splits into disconnected components. Since edges are based solely on value differences (not arbitrary graph edges), the transitive closure is simply the "connected by ≤ maxDiff adjacent gaps" relation.

## Complexity

- **Time Complexity**: O(n + q) — one pass to label components, one pass for queries
- **Space Complexity**: O(n) for the component ID array
