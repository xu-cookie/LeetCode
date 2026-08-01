"""
LeetCode #486: Predict the Winner
Difficulty: Medium
Link: https://leetcode.com/problems/predict-the-winner/
Date: 2026-08-01

Approach: Minimax Dynamic Programming (Bottom-Up)

This is a classic two-player zero-sum game with perfect information.
Both players play optimally, trying to maximize their own score.

Key insight — Turn the game into a "score difference" problem:
Define dp[i][j] = the maximum net score advantage the CURRENT player
can achieve over the opponent when playing on subarray nums[i..j].

When the current player picks nums[i]:
  - They gain nums[i] points immediately
  - The opponent then plays optimally on nums[i+1..j], achieving dp[i+1][j]
  - Net advantage = nums[i] - dp[i+1][j]

Transition: dp[i][j] = max(nums[i] - dp[i+1][j], nums[j] - dp[i][j-1])
Base case: dp[i][i] = nums[i] (only one element, take it)

Player 1 wins if dp[0][n-1] >= 0 (non-negative advantage)

Time Complexity:  O(n²) where n = nums.length ≤ 20
Space Complexity: O(n²) for the DP table
"""

from typing import List


class Solution:
    def predictTheWinner(self, nums: List[int]) -> bool:
        n = len(nums)

        # dp[i][j] = max net advantage for current player on nums[i..j]
        dp = [[0] * n for _ in range(n)]

        # Base case: single element — current player takes it
        for i in range(n):
            dp[i][i] = nums[i]

        # Fill by increasing subarray length
        for length in range(2, n + 1):
            for i in range(n - length + 1):
                j = i + length - 1

                # Pick left: gain nums[i], then opponent gets dp[i+1][j]
                pick_left = nums[i] - dp[i + 1][j]

                # Pick right: gain nums[j], then opponent gets dp[i][j-1]
                pick_right = nums[j] - dp[i][j - 1]

                dp[i][j] = max(pick_left, pick_right)

        # Player 1 wins if their net advantage ≥ 0
        return dp[0][n - 1] >= 0


# Test harness
if __name__ == "__main__":
    sol = Solution()

    # Example 1: nums = [1,5,2] → false
    assert sol.predictTheWinner([1, 5, 2]) == False
    print(f"Test 1: predictTheWinner([1,5,2]) = {sol.predictTheWinner([1, 5, 2])} (expected False)")

    # Example 2: nums = [1,5,233,7] → true
    assert sol.predictTheWinner([1, 5, 233, 7]) == True
    print(f"Test 2: predictTheWinner([1,5,233,7]) = {sol.predictTheWinner([1, 5, 233, 7])} (expected True)")

    # Edge case: single element
    assert sol.predictTheWinner([5]) == True
    print(f"Test 3: predictTheWinner([5]) = {sol.predictTheWinner([5])} (expected True)")

    # Edge case: two equal elements — tie, Player 1 wins
    assert sol.predictTheWinner([3, 3]) == True
    print(f"Test 4: predictTheWinner([3,3]) = {sol.predictTheWinner([3, 3])} (expected True)")

    # Player 1 can always win with 2 elements (pick the larger one)
    assert sol.predictTheWinner([2, 7]) == True
    print(f"Test 5: predictTheWinner([2,7]) = {sol.predictTheWinner([2, 7])} (expected True)")

    # 10 ones — tie, Player 1 wins
    assert sol.predictTheWinner([1] * 10) == True
    print(f"Test 6: predictTheWinner([10 ones]) = {sol.predictTheWinner([1] * 10)} (expected True)")

    # Even number, all same — tie, Player 1 wins
    assert sol.predictTheWinner([7, 7, 7, 7]) == True
    print(f"Test 7: predictTheWinner([7,7,7,7]) = {sol.predictTheWinner([7, 7, 7, 7])} (expected True)")

    # Max constraint: n=20, large values
    large = [10000000] * 20
    assert sol.predictTheWinner(large) == True
    print(f"Test 8: predictTheWinner([20 x 10^7]) = {sol.predictTheWinner(large)} (expected True)")

    print("\nAll tests passed!")
