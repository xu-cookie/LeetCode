/**
 * LeetCode 1840 - 最高建筑高度 (Maximum Building Height)
 * 难度: Hard
 * 链接: https://leetcode.cn/problems/maximum-building-height/
 * 日期: 2026-06-20
 *
 * 题目描述:
 * 你打算在城市里建造 n 栋新建筑。新建筑将建造在一条线上，标记为 1 到 n。
 * 城市对新建筑的高度有以下限制：
 *   - 每栋建筑的高度必须是 非负整数
 *   - 第一栋建筑的高度 必须为 0
 *   - 相邻建筑之间的高度差不能超过 1
 * 另外，城市限制某些特定建筑的最大高度，以 2D 整数数组 restrictions 给出，
 * restrictions[i] = [id_i, maxHeight_i] 表示建筑 id_i 的高度必须 ≤ maxHeight_i。
 * 返回 最高建筑能达到的 最大可能高度。
 *
 * 示例 1:
 *   输入: n = 5, restrictions = [[2,1],[4,1]]
 *   输出: 2
 *   解释: 可以建造高度为 [0,1,2,1,2] 的建筑，最高为 2。
 *
 * 示例 2:
 *   输入: n = 6, restrictions = []
 *   输出: 5
 *   解释: 可以建造高度为 [0,1,2,3,4,5]，最高为 5。
 *
 * 示例 3:
 *   输入: n = 10, restrictions = [[5,3],[2,5],[7,4],[10,3]]
 *   输出: 5
 *
 * 约束:
 *   - 2 <= n <= 10^9
 *   - 0 <= restrictions.length <= min(n-1, 10^5)
 *   - 2 <= id_i <= n
 *   - id_i 唯一
 *   - 0 <= maxHeight_i <= 10^9
 *
 * -------------------------------------------
 * 解题思路: 约束传播 + 贪心
 *
 * 核心洞察:
 *   - 限制条件会向两侧传播：如果一个建筑高度受限，相邻建筑也间接受限
 *   - 两栋受限建筑之间，最大高度可以通过"爬坡再下坡"达到
 *   - 关键公式：pos_i (高 h_i) 和 pos_j (高 h_j) 之间，距离 dist，
 *     峰值可达 (h_i + h_j + dist) / 2
 *
 * 算法步骤:
 *   1. 处理空限制：n-1 直接返回
 *   2. 将建筑1（pos=1, h=0）加入限制列表，按位置排序
 *   3. 正向传播（左→右）：右侧建筑高度 ≤ 左侧高度 + 距离
 *   4. 反向传播（右→左）：左侧建筑高度 ≤ 右侧高度 + 距离
 *   5. 计算相邻限制之间的峰值 + 最后建筑到 n 的延伸
 *   6. 返回全局最大值
 *
 * 时间复杂度: O(m log m)，m = restrictions.length，排序主导
 * 空间复杂度: O(m)，存储限制数组
 *
 * 相关话题: 数组, 贪心, 排序
 */
import java.util.Arrays;

class Solution {
    public int maxBuilding(int n, int[][] restrictions) {
        int m = restrictions.length;

        // 无限制：一路递增
        if (m == 0) {
            return n - 1;
        }

        // 按建筑编号排序
        Arrays.sort(restrictions, (a, b) -> Integer.compare(a[0], b[0]));

        // 将建筑1（位置=1，高度=0）加入限制
        int[][] all = new int[m + 1][2];
        all[0][0] = 1;
        all[0][1] = 0;
        for (int i = 0; i < m; i++) {
            all[i + 1][0] = restrictions[i][0];
            all[i + 1][1] = restrictions[i][1];
        }
        m++;

        // 正向传播：从左边收紧
        for (int i = 1; i < m; i++) {
            int dist = all[i][0] - all[i - 1][0];
            all[i][1] = Math.min(all[i][1], all[i - 1][1] + dist);
        }

        // 反向传播：从右边收紧
        for (int i = m - 2; i >= 0; i--) {
            int dist = all[i + 1][0] - all[i][0];
            all[i][1] = Math.min(all[i][1], all[i + 1][1] + dist);
        }

        long maxHeight = 0;

        // 相邻限制之间的峰值
        for (int i = 0; i < m - 1; i++) {
            long pos1 = all[i][0], h1 = all[i][1];
            long pos2 = all[i + 1][0], h2 = all[i + 1][1];
            long dist = pos2 - pos1;
            long peak = (h1 + h2 + dist) / 2;
            maxHeight = Math.max(maxHeight, peak);
        }

        // 最后一个限制到建筑n之间的延伸
        long lastPos = all[m - 1][0];
        long lastHeight = all[m - 1][1];
        long endHeight = lastHeight + (n - lastPos);
        maxHeight = Math.max(maxHeight, endHeight);

        return (int) maxHeight;
    }
}

//leetcode submit region end(Prohibit modification and deletion)

// 测试代码
class Test {
    public static void main(String[] args) {
        Solution sol = new Solution();

        // 示例1
        int[][] r1 = {{2, 1}, {4, 1}};
        int result1 = sol.maxBuilding(5, r1);
        System.out.println("测试1: " + result1 + " (期望: 2) "
            + (result1 == 2 ? "✓" : "✗"));

        // 示例2: 无限制
        int[][] r2 = {};
        int result2 = sol.maxBuilding(6, r2);
        System.out.println("测试2: " + result2 + " (期望: 5) "
            + (result2 == 5 ? "✓" : "✗"));

        // 示例3
        int[][] r3 = {{5, 3}, {2, 5}, {7, 4}, {10, 3}};
        int result3 = sol.maxBuilding(10, r3);
        System.out.println("测试3: " + result3 + " (期望: 5) "
            + (result3 == 5 ? "✓" : "✗"));

        // 边界测试: 最小n
        int[][] r4 = {};
        int result4 = sol.maxBuilding(2, r4);
        System.out.println("测试4: " + result4 + " (期望: 1) "
            + (result4 == 1 ? "✓" : "✗"));

        // 边界测试: 严格的中间限制
        int[][] r5 = {{3, 0}};
        int result5 = sol.maxBuilding(5, r5);
        System.out.println("测试5: " + result5 + " (期望: 1) "
            + (result5 == 1 ? "✓" : "✗"));
    }
}
