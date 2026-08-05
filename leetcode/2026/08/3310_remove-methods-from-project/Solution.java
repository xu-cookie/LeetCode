import java.util.*;

/**
 * LeetCode #3310: Remove Methods From Project
 * Difficulty: Medium
 *
 * Problem:
 * Given n methods (0 to n-1), a buggy method k, and invocations (directed edges),
 * find all "suspicious" methods (k and everything reachable from k via outgoing edges).
 *
 * Suspicious methods can only be removed if NO method outside the suspicious group
 * invokes any method inside it. If not possible, no methods are removed.
 *
 * Algorithm: DFS from k to find suspicious set, then check incoming edges.
 * Time: O(n + m), Space: O(n + m)
 */
class Solution {
    public List<Integer> remainingMethods(int n, int k, int[][] invocations) {
        // Build adjacency lists
        List<Integer>[] outEdges = new ArrayList[n];
        List<Integer>[] inEdges = new ArrayList[n];
        for (int i = 0; i < n; i++) {
            outEdges[i] = new ArrayList<>();
            inEdges[i] = new ArrayList<>();
        }
        for (int[] inv : invocations) {
            int from = inv[0], to = inv[1];
            outEdges[from].add(to);
            inEdges[to].add(from);
        }

        // Step 1: Find all suspicious methods (DFS from k following outgoing edges)
        boolean[] suspicious = new boolean[n];
        dfs(k, outEdges, suspicious);

        // Step 2: Check if any non-suspicious method invokes a suspicious one
        boolean canRemove = true;
        for (int v = 0; v < n && canRemove; v++) {
            if (suspicious[v]) {
                for (int caller : inEdges[v]) {
                    if (!suspicious[caller]) {
                        canRemove = false;
                        break;
                    }
                }
            }
        }

        // Step 3: Build result
        List<Integer> result = new ArrayList<>();
        if (canRemove) {
            for (int i = 0; i < n; i++) {
                if (!suspicious[i]) {
                    result.add(i);
                }
            }
        } else {
            for (int i = 0; i < n; i++) {
                result.add(i);
            }
        }
        return result;
    }

    private void dfs(int node, List<Integer>[] outEdges, boolean[] suspicious) {
        if (suspicious[node]) return;
        suspicious[node] = true;
        for (int next : outEdges[node]) {
            dfs(next, outEdges, suspicious);
        }
    }

    // Test harness
    public static void main(String[] args) {
        Solution sol = new Solution();
        int passed = 0, total = 0;

        // Example 1
        total++;
        List<Integer> res1 = sol.remainingMethods(4, 1,
            new int[][]{{1, 2}, {0, 1}, {3, 2}});
        List<Integer> exp1 = Arrays.asList(0, 1, 2, 3);
        if (new HashSet<>(res1).equals(new HashSet<>(exp1))) {
            System.out.println("Example 1: PASS");
            passed++;
        } else {
            System.out.println("Example 1: FAIL - got " + res1 + " expected " + exp1);
        }

        // Example 2
        total++;
        List<Integer> res2 = sol.remainingMethods(5, 0,
            new int[][]{{1, 2}, {0, 2}, {0, 1}, {3, 4}});
        List<Integer> exp2 = Arrays.asList(3, 4);
        if (new HashSet<>(res2).equals(new HashSet<>(exp2))) {
            System.out.println("Example 2: PASS");
            passed++;
        } else {
            System.out.println("Example 2: FAIL - got " + res2 + " expected " + exp2);
        }

        // Example 3
        total++;
        List<Integer> res3 = sol.remainingMethods(3, 2,
            new int[][]{{1, 2}, {0, 1}, {2, 0}});
        List<Integer> exp3 = Arrays.asList();
        if (new HashSet<>(res3).equals(new HashSet<>(exp3))) {
            System.out.println("Example 3: PASS");
            passed++;
        } else {
            System.out.println("Example 3: FAIL - got " + res3 + " expected " + exp3);
        }

        // Additional test: single node, k=0, no invocations
        total++;
        List<Integer> res4 = sol.remainingMethods(1, 0, new int[][]{});
        List<Integer> exp4 = Arrays.asList();
        if (res4.equals(exp4)) {
            System.out.println("Test 4 (single node, removable): PASS");
            passed++;
        } else {
            System.out.println("Test 4: FAIL - got " + res4 + " expected " + exp4);
        }

        // Additional test: external caller blocks removal
        total++;
        List<Integer> res5 = sol.remainingMethods(3, 0,
            new int[][]{{0, 1}, {2, 1}});
        List<Integer> exp5 = Arrays.asList(0, 1, 2);
        if (new HashSet<>(res5).equals(new HashSet<>(exp5))) {
            System.out.println("Test 5 (external caller blocks): PASS");
            passed++;
        } else {
            System.out.println("Test 5: FAIL - got " + res5 + " expected " + exp5);
        }

        // Additional test: suspicious set is isolated (removable)
        total++;
        List<Integer> res6 = sol.remainingMethods(4, 0,
            new int[][]{{0, 1}, {1, 0}, {2, 3}});
        // suspicious: {0,1}, external: {2,3}, and 2/3 don't invoke 0/1
        List<Integer> exp6 = Arrays.asList(2, 3);
        if (new HashSet<>(res6).equals(new HashSet<>(exp6))) {
            System.out.println("Test 6 (isolated SCC): PASS");
            passed++;
        } else {
            System.out.println("Test 6: FAIL - got " + res6 + " expected " + exp6);
        }

        System.out.println("\n" + passed + "/" + total + " tests passed.");
    }
}
