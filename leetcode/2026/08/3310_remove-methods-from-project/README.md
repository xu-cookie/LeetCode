# LeetCode #3310: Remove Methods From Project

**Difficulty**: Medium | **Date**: 2026-08-05 | **Source**: leetcode.cn

## Problem Summary

We have `n` methods (0 to n-1) and a directed graph of invocations. Method `k` is buggy. All methods reachable from `k` (directly or indirectly via outgoing edges) are **suspicious**.

Remove all suspicious methods **iff** no non-suspicious method invokes any suspicious method. Otherwise, remove nothing.

Return the list of remaining methods (any order).

## Approach

**Graph Traversal + Incoming Edge Check**

1. **Build adjacency lists**: both outgoing edges (for DFS) and incoming edges (for verification)
2. **DFS from `k`**: mark all reachable nodes as suspicious (following outgoing edges)
3. **Validate removal**: for each suspicious method, check if any incoming edge comes from a non-suspicious caller
4. **Build result**: if removable → return non-suspicious methods; otherwise → return all methods

### Complexity

- **Time**: O(n + m) where m = invocations.length
- **Space**: O(n + m) for adjacency lists

## Key Insight

The problem separates into two concerns:
1. **What is suspicious?** → DFS/BFS reachability from k
2. **Can we remove it?** → No external dependencies (incoming edges from outside)

The "all or nothing" constraint simplifies the decision: we just need to find ONE external caller into the suspicious set to block removal.

## Solution

See `Solution.java` — DFS-based implementation with explicit adjacency list construction for both forward and reverse edges.
