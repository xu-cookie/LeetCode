//给定一个仅包含数字 2-9 的字符串，返回所有它能表示的字母组合。答案可以按 任意顺序 返回。
//
// 给出数字到字母的映射如下（与电话按键相同）。注意 1 不对应任何字母。
//
//
//
//
//
// 示例 1：
//
//
//输入：digits = "23"
//输出：["ad","ae","af","bd","be","bf","cd","ce","cf"]
//
//
// 示例 2：
//
//
//输入：digits = "2"
//输出：["a","b","c"]
//
//
//
//
// 提示：
//
//
// 1 <= digits.length <= 4
// digits[i] 是范围 ['2', '9'] 的一个数字。
//
//
// Related Topics 哈希表 字符串 回溯 👍 3269 👎 0


//leetcode submit region begin(Prohibit modification and deletion)
import java.util.ArrayList;
import java.util.List;

class Solution {
    // 数字到字母的映射表，下标对应数字
    private static final String[] MAPPING = {
        "",     // 0 - 无映射
        "",     // 1 - 无映射
        "abc",  // 2
        "def",  // 3
        "ghi",  // 4
        "jkl",  // 5
        "mno",  // 6
        "pqrs", // 7
        "tuv",  // 8
        "wxyz"  // 9
    };

    public List<String> letterCombinations(String digits) {
        List<String> result = new ArrayList<>();
        // 边界情况：空输入
        if (digits == null || digits.isEmpty()) {
            return result;
        }
        backtrack(result, new StringBuilder(), digits, 0);
        return result;
    }

    /**
     * 回溯法：从 digits[index] 开始，构建所有可能的字母组合
     * @param result  结果列表
     * @param current 当前构建中的字符串
     * @param digits  输入的数字字符串
     * @param index   当前处理的数字下标
     */
    private void backtrack(List<String> result, StringBuilder current,
                           String digits, int index) {
        // 终止条件：已处理完所有数字
        if (index == digits.length()) {
            result.add(current.toString());
            return;
        }

        // 获取当前数字对应的字母集合
        String letters = MAPPING[digits.charAt(index) - '0'];
        for (char c : letters.toCharArray()) {
            current.append(c);                              // 做选择
            backtrack(result, current, digits, index + 1); // 递归下一层
            current.deleteCharAt(current.length() - 1);     // 撤销选择（回溯）
        }
    }

    // 测试
    public static void main(String[] args) {
        Solution sol = new Solution();
        System.out.println(sol.letterCombinations("23")); // [ad, ae, af, bd, be, bf, cd, ce, cf]
        System.out.println(sol.letterCombinations("2"));  // [a, b, c]
        System.out.println(sol.letterCombinations(""));   // []
    }
}
//leetcode submit region end(Prohibit modification and deletion)
