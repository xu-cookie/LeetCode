/**
 * LeetCode 1833 - Maximum Ice Cream Bars (最大冰淇淋数量)
 * 难度: Medium
 * 链接: https://leetcode.com/problems/maximum-ice-cream-bars/
 * 日期: 2026-06-21
 *
 * 题目描述:
 * 夏日炎炎，一个小男孩想买一些冰淇淋棒。
 * 商店里有 n 个冰淇淋棒。给定一个长度为 n 的数组 costs，其中 costs[i] 是第 i 个冰淇淋棒的硬币价格。
 * 男孩最初有 coins 个硬币，他想买尽可能多的冰淇淋棒。
 * 注意：男孩可以按任意顺序购买。
 * 返回男孩用 coins 个硬币能买到的最大冰淇淋棒数量。
 * 必须使用计数排序解决此题。
 *
 * 示例 1:
 *   输入: costs = [1,3,2,4,1], coins = 7
 *   输出: 4
 *   解释: 可以买下标 0,1,2,4 的冰淇淋，总价 1+3+2+1=7
 *
 * 示例 2:
 *   输入: costs = [10,6,8,7,7,8], coins = 5
 *   输出: 0
 *   解释: 买不起任何冰淇淋
 *
 * 示例 3:
 *   输入: costs = [1,6,3,1,2,5], coins = 20
 *   输出: 6
 *   解释: 可以买全部冰淇淋，总价 1+6+3+1+2+5=18
 *
 * 约束:
 *   - costs.length == n
 *   - 1 <= n <= 10^5
 *   - 1 <= costs[i] <= 10^5
 *   - 1 <= coins <= 10^8
 *
 * -------------------------------------------
 * 解题思路: 计数排序 + 贪心
 *
 * 核心洞察:
 *   - 要买尽可能多，显然应该从最便宜的买起（贪心）
 *   - 题目要求用计数排序，因为 costs[i] 范围有限（1 ~ 10^5）
 *   - 计数排序比 O(n log n) 比较排序更适合这种取值范围有限的场景
 *
 * 算法步骤:
 *   1. 找到 costs 中的最大值 maxCost
 *   2. 创建计数数组 freq[maxCost + 1]，统计每个价格的出现次数
 *   3. 从价格 1 遍历到 maxCost：
 *      - 贪心地尽可能多买当前价格的冰淇淋
 *      - 如果硬币不够买全部，就买尽可能多的数量然后结束
 *      - 否则全部买下，继续下一价格
 *   4. 返回总数
 *
 * 时间复杂度: O(n + maxCost)，n 是数组长度，maxCost <= 10^5
 * 空间复杂度: O(maxCost)
 *
 * 相关话题: 数组, 计数排序, 贪心
 */
class Solution {
    public int maxIceCream(int[] costs, int coins) {
        // 1. 找最大值，确定计数数组大小
        int maxCost = 0;
        for (int cost : costs) {
            if (cost > maxCost) {
                maxCost = cost;
            }
        }

        // 2. 计数排序：统计每个价格的出现次数
        int[] freq = new int[maxCost + 1];
        for (int cost : costs) {
            freq[cost]++;
        }

        // 3. 贪心：从最低价格开始买
        int count = 0;
        for (int price = 1; price <= maxCost; price++) {
            if (freq[price] == 0) {
                continue;
            }
            if (coins < price) {
                // 最便宜的也买不起了，直接结束
                break;
            }
            // 当前价格最多能买几个
            int canBuy = Math.min(freq[price], coins / price);
            count += canBuy;
            coins -= canBuy * price;
        }

        return count;
    }
}

//leetcode submit region end(Prohibit modification and deletion)

// 测试代码
class Test {
    public static void main(String[] args) {
        Solution sol = new Solution();

        // 测试用例1: costs = [1,3,2,4,1], coins = 7, 期望: 4
        int[] costs1 = {1, 3, 2, 4, 1};
        int result1 = sol.maxIceCream(costs1, 7);
        System.out.println("测试1: " + result1 + " (期望: 4) "
            + (result1 == 4 ? "✓" : "✗"));

        // 测试用例2: costs = [10,6,8,7,7,8], coins = 5, 期望: 0
        int[] costs2 = {10, 6, 8, 7, 7, 8};
        int result2 = sol.maxIceCream(costs2, 5);
        System.out.println("测试2: " + result2 + " (期望: 0) "
            + (result2 == 0 ? "✓" : "✗"));

        // 测试用例3: costs = [1,6,3,1,2,5], coins = 20, 期望: 6
        int[] costs3 = {1, 6, 3, 1, 2, 5};
        int result3 = sol.maxIceCream(costs3, 20);
        System.out.println("测试3: " + result3 + " (期望: 6) "
            + (result3 == 6 ? "✓" : "✗"));

        // 额外测试: 刚好用完所有硬币
        int[] costs4 = {1, 2, 3, 4};
        int result4 = sol.maxIceCream(costs4, 10);
        System.out.println("测试4: " + result4 + " (期望: 4) "
            + (result4 == 4 ? "✓" : "✗"));

        // 额外测试: 大范围价格
        int[] costs5 = {1, 1, 1, 1, 1};
        int result5 = sol.maxIceCream(costs5, 3);
        System.out.println("测试5: " + result5 + " (期望: 3) "
            + (result5 == 3 ? "✓" : "✗"));
    }
}
