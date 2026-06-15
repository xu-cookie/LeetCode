//给你一个长度为 n 的整数数组 nums 和 一个目标值 target。请你从 nums 中选出三个在 不同下标位置 的整数，使它们的和与 target 最
//接近。 
//
// 返回这三个数的和。 
//
// 假定每组输入只存在恰好一个解。 
//
// 
//
// 示例 1： 
//
// 
//输入：nums = [-1,2,1,-4], target = 1
//输出：2
//解释：与 target 最接近的和是 2 (-1 + 2 + 1 = 2)。
// 
//
// 示例 2： 
//
// 
//输入：nums = [0,0,0], target = 1
//输出：0
//解释：与 target 最接近的和是 0（0 + 0 + 0 = 0）。 
//
// 
//
// 提示： 
//
// 
// 3 <= nums.length <= 1000 
// -1000 <= nums[i] <= 1000 
// -10⁴ <= target <= 10⁴ 
// 
//
// Related Topics 数组 双指针 排序 👍 1813 👎 0


//leetcode submit region begin(Prohibit modification and deletion)
import java.util.Arrays;

public class Solution {
    public int threeSumClosest(int[] nums, int target) {
        Arrays.sort(nums);
        int n = nums.length;
        int best = nums[0] + nums[1] + nums[2]; // 初始值

        for (int i = 0; i < n - 2; i++) {
            int left = i + 1, right = n - 1;
            while (left < right) {
                int sum = nums[i] + nums[left] + nums[right];
                // 更新最接近的和
                if (Math.abs(sum - target) < Math.abs(best - target)) {
                    best = sum;
                }
                if (sum == target) {
                    return target; // 完全相等直接返回
                } else if (sum < target) {
                    left++;
                } else {
                    right--;
                }
            }
        }
        return best;
    }

    // 测试
    public static void main(String[] args) {
        Solution sol = new Solution();
        System.out.println(sol.threeSumClosest(new int[]{-1, 2, 1, -4}, 1)); // 2
        System.out.println(sol.threeSumClosest(new int[]{0, 0, 0}, 1));       // 0
    }
}
//leetcode submit region end(Prohibit modification and deletion)
