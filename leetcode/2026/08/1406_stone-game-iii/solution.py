"""
LeetCode #1406: Stone Game III
Difficulty: Hard
Link: https://leetcode.com/problems/stone-game-iii/
Date: 2026-08-03

Approach: DP (Bottom-Up, Right-to-Left) — O(n) time, O(1) space

Let dp[i] = maximum score ADVANTAGE the current player can achieve
           over the opponent when starting from index i.

At index i, the current player can take 1, 2, or 3 stones:
  - Take 1:  stoneValue[i] - dp[i+1]
  - Take 2:  stoneValue[i] + stoneValue[i+1] - dp[i+2]
  - Take 3:  stoneValue[i] + stoneValue[i+1] + stoneValue[i+2] - dp[i+3]

dp[i] = max of the valid options above.

Result:
  dp[0] > 0 → "Alice"
  dp[0] < 0 → "Bob"
  dp[0] = 0 → "Tie"
"""
from typing import List


class Solution:
    def stoneGameIII(self, stoneValue: List[int]) -> str:
        n = len(stoneValue)

        # Only need last 3 dp values
        dp1 = 0  # dp[i+1]
        dp2 = 0  # dp[i+2]
        dp3 = 0  # dp[i+3]

        for i in range(n - 1, -1, -1):
            # Option 1: take 1 stone
            take_one = stoneValue[i] - dp1
            take_two = float('-inf')
            take_three = float('-inf')

            # Option 2: take 2 stones (if possible)
            if i + 1 < n:
                take_two = stoneValue[i] + stoneValue[i + 1] - dp2

            # Option 3: take 3 stones (if possible)
            if i + 2 < n:
                take_three = (stoneValue[i] + stoneValue[i + 1]
                              + stoneValue[i + 2] - dp3)

            dp_i = max(take_one, take_two, take_three)

            # Shift for next iteration
            dp3 = dp2
            dp2 = dp1
            dp1 = dp_i

        if dp1 > 0:
            return "Alice"
        elif dp1 < 0:
            return "Bob"
        else:
            return "Tie"


# Test harness
if __name__ == "__main__":
    sol = Solution()

    # Example 1
    assert sol.stoneGameIII([1, 2, 3, 7]) == "Bob"
    print(f'Test 1: stoneGameIII([1,2,3,7]) = "{sol.stoneGameIII([1,2,3,7])}" (expected "Bob")')

    # Example 2
    assert sol.stoneGameIII([1, 2, 3, -9]) == "Alice"
    print(f'Test 2: stoneGameIII([1,2,3,-9]) = "{sol.stoneGameIII([1,2,3,-9])}" (expected "Alice")')

    # Example 3
    assert sol.stoneGameIII([1, 2, 3, 6]) == "Tie"
    print(f'Test 3: stoneGameIII([1,2,3,6]) = "{sol.stoneGameIII([1,2,3,6])}" (expected "Tie")')

    # Single positive
    assert sol.stoneGameIII([5]) == "Alice"
    print(f'Test 4: stoneGameIII([5]) = "{sol.stoneGameIII([5])}" (expected "Alice")')

    # Single negative
    assert sol.stoneGameIII([-5]) == "Bob"
    print(f'Test 5: stoneGameIII([-5]) = "{sol.stoneGameIII([-5])}" (expected "Bob")')

    # Two positive
    assert sol.stoneGameIII([1, 2]) == "Alice"
    print(f'Test 6: stoneGameIII([1,2]) = "{sol.stoneGameIII([1,2])}" (expected "Alice")')

    # Mixed
    assert sol.stoneGameIII([1, -2]) == "Alice"
    print(f'Test 7: stoneGameIII([1,-2]) = "{sol.stoneGameIII([1,-2])}" (expected "Alice")')

    # All negative
    assert sol.stoneGameIII([-1, -2, -3]) == "Tie"
    print(f'Test 8: stoneGameIII([-1,-2,-3]) = "{sol.stoneGameIII([-1,-2,-3])}" (expected "Tie")')

    # Alternating
    result9 = sol.stoneGameIII([1, -1, 1, -1, 1])
    print(f'Test 9: stoneGameIII([1,-1,1,-1,1]) = "{result9}"')

    # Large test
    large = [(i % 5) - 2 for i in range(50000)]
    result10 = sol.stoneGameIII(large)
    print(f'Test 10 (n=50000): completed without errors, result = "{result10}"')

    print("\nAll tests passed!")
