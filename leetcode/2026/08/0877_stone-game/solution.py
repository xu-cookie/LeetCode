"""
LeetCode #877: Stone Game
Difficulty: Medium
Link: https://leetcode.com/problems/stone-game/
Date: 2026-08-02

Approach 1 (Mathematical): Alice Always Wins

This problem has a beautiful mathematical property: with an even number
of piles and an odd total sum, Alice can ALWAYS force a win.

Proof:
1. The piles can be partitioned into two groups by index parity:
   - Group Even: piles[0], piles[2], piles[4], ...
   - Group Odd:  piles[1], piles[3], piles[5], ...
2. Since the total sum is odd, sum(Even) ≠ sum(Odd).
3. Alice, going first, can guarantee she collects all piles from the
   larger group. How?
   - If she wants Even-indexed piles, she takes piles[0] (an even-indexed
     pile). Now both ends (piles[1] and piles[n-1]) are odd-indexed.
     Whatever Bob picks, the next turn both ends are even-indexed again.
     Alice can always pick an even-indexed pile.
   - Similarly, if she wants Odd-indexed piles, she takes piles[n-1]
     (which is odd-indexed since n is even), and the same logic applies.

Therefore, Alice can always guarantee victory. The answer is always True.

Time Complexity:  O(1)
Space Complexity: O(1)

------------------------------------------------------------------

Approach 2 (DP / Minimax): General Solution

Same DP formulation as "Predict the Winner" (#486):
  dp[i][j] = max(piles[i] - dp[i+1][j], piles[j] - dp[i][j-1])

dp[i][j] represents the maximum score advantage the current player
can achieve over the opponent when playing on piles[i..j].

Alice wins if dp[0][n-1] > 0 (strictly greater, since total is odd).

Time Complexity:  O(n²) where n = piles.length ≤ 500
Space Complexity: O(n²) or O(n) with 1D optimization
"""

from typing import List


class Solution:
    # Approach 1: Mathematical — O(1) time, O(1) space
    def stoneGame(self, piles: List[int]) -> bool:
        """
        With even number of piles and odd total sum,
        Alice can always force a win by controlling parity.
        """
        return True

    # Approach 2: DP (Minimax) — O(n²) time, O(n²) space
    # def stoneGame(self, piles: List[int]) -> bool:
    #     n = len(piles)
    #     dp = [[0] * n for _ in range(n)]
    #
    #     for i in range(n):
    #         dp[i][i] = piles[i]
    #
    #     for length in range(2, n + 1):
    #         for i in range(n - length + 1):
    #             j = i + length - 1
    #             dp[i][j] = max(
    #                 piles[i] - dp[i + 1][j],
    #                 piles[j] - dp[i][j - 1]
    #             )
    #
    #     return dp[0][n - 1] > 0

    # Approach 3: Space-optimized DP — O(n²) time, O(n) space
    # def stoneGame(self, piles: List[int]) -> bool:
    #     n = len(piles)
    #     dp = piles[:]  # dp[j] for current row i
    #
    #     for i in range(n - 2, -1, -1):
    #         for j in range(i + 1, n):
    #             dp[j] = max(piles[i] - dp[j], piles[j] - dp[j - 1])
    #
    #     return dp[n - 1] > 0


class SolutionDP:
    """DP (Minimax) version for verification."""

    def stoneGame(self, piles: List[int]) -> bool:
        n = len(piles)

        # Space-optimized 1D DP
        dp = piles[:]

        for i in range(n - 2, -1, -1):
            for j in range(i + 1, n):
                dp[j] = max(piles[i] - dp[j], piles[j] - dp[j - 1])

        return dp[n - 1] > 0


# Test harness
if __name__ == "__main__":
    sol = Solution()
    sol_dp = SolutionDP()

    # Example 1: piles = [5,3,4,5] → True
    assert sol.stoneGame([5, 3, 4, 5]) == True
    assert sol_dp.stoneGame([5, 3, 4, 5]) == True
    print(f"Test 1: stoneGame([5,3,4,5]) = {sol.stoneGame([5, 3, 4, 5])} (expected True)")

    # Example 2: piles = [3,7,2,3] → True
    assert sol.stoneGame([3, 7, 2, 3]) == True
    assert sol_dp.stoneGame([3, 7, 2, 3]) == True
    print(f"Test 2: stoneGame([3,7,2,3]) = {sol.stoneGame([3, 7, 2, 3])} (expected True)")

    # Test 3: Minimum case (n=2, odd sum)
    assert sol.stoneGame([1, 2]) == True
    assert sol_dp.stoneGame([1, 2]) == True
    print(f"Test 3: stoneGame([1,2]) = {sol.stoneGame([1, 2])} (expected True)")

    # Test 4: Larger case, n=6
    assert sol.stoneGame([1, 3, 5, 7, 9, 11]) == True
    assert sol_dp.stoneGame([1, 3, 5, 7, 9, 11]) == True
    print(f"Test 4: stoneGame([1,3,5,7,9,11]) = {sol.stoneGame([1, 3, 5, 7, 9, 11])} (expected True)")

    # Test 5: Random case, n=8
    assert sol.stoneGame([7, 1, 5, 3, 6, 2, 9, 4]) == True
    assert sol_dp.stoneGame([7, 1, 5, 3, 6, 2, 9, 4]) == True
    print(f"Test 5: stoneGame([7,1,5,3,6,2,9,4]) = {sol.stoneGame([7, 1, 5, 3, 6, 2, 9, 4])} (expected True)")

    # Test 6: Symmetric but odd total, n=4
    assert sol.stoneGame([3, 2, 2, 2]) == True
    assert sol_dp.stoneGame([3, 2, 2, 2]) == True
    print(f"Test 6: stoneGame([3,2,2,2]) = {sol.stoneGame([3, 2, 2, 2])} (expected True)")

    # Test 7: One side heavily weighted
    assert sol.stoneGame([1, 100, 2, 4]) == True
    assert sol_dp.stoneGame([1, 100, 2, 4]) == True
    print(f"Test 7: stoneGame([1,100,2,4]) = {sol.stoneGame([1, 100, 2, 4])} (expected True)")

    # Test 8: Large n, max constraint
    large = [((i % 3) + 1) for i in range(500)]
    assert sol.stoneGame(large) == True
    assert sol_dp.stoneGame(large) == True
    print(f"Test 8: stoneGame([500 elements]) = {sol.stoneGame(large)} (expected True)")

    # Test 9: Various even-length, odd-sum arrays
    test_cases = [
        [7, 8],                    # sum=15 odd
        [1, 1, 1, 2],             # sum=5 odd
        [5, 1, 1, 1, 1, 2],       # sum=11 odd
        [3, 3, 3, 3, 3, 3, 3, 2], # sum=23 odd
    ]
    for t, case in enumerate(test_cases):
        assert sol_dp.stoneGame(case) == True
        print(f"Test 9.{t}: DP result = {sol_dp.stoneGame(case)} (expected True)")

    print("\nAll tests passed! Alice always wins.")
