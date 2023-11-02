package leetCode.dp;

public class Solution45 {
    public static void main(String[] args) {
        int[] test = new int[]{5, 6, 4, 4, 6, 9, 4, 4, 7, 4, 4, 8, 2, 6, 8, 1, 5, 9, 6, 5, 2, 7, 9, 7, 9, 6, 9, 4, 1, 6, 8, 8, 4, 4, 2, 0, 3, 8, 5};
        //int[] test = new int[]{2, 0, 2, 0, 1};
        System.out.println(new Solution45().jump(test));
        System.out.println(new Solution45().jumpPro2(test));

        //System.out.println(new Solution45().jump1(0, test.length, test, 0));
    }

    // 状态转移表法
    public int jump(int[] nums) {
        int n = nums.length;
        if (n == 1) {
            return 0;
        }
        // 最多n个阶段，每个阶段nums[i]种状态
        boolean[] dp = new boolean[n];

        // 初始化第一步
        dp[0] = true;

        // 循环操作状态转移方程
        for (int i = 1; i < n; i++) {
            // 遍历上一步状态
            for (int j = n - 1; j >= 0; j--) {
                if (dp[j]) {
                    // 如果最大步数为0则跳过
                    if (nums[j] == 0) {
                        continue;
                    }
                    // 推导下一个状态
                    for (int k = Math.min(nums[j], n - 1 - j); k >= 0; k--) {
                        dp[j + k] = true;
                        if (dp[n - 1]) {
                            return i;
                        }
                    }
                }
            }
        }
        return 0;
    }

    // 通过在dp数组中记录路径长度，可以减少一次循环
    public int jumpPro(int[] nums) {
        int n = nums.length;
        if (n == 1) {
            return 0;
        }
        // 最多n个阶段，每个阶段nums[i]种状态
        int[] dp = new int[n];

        // 初始化第一步
        dp[0] = 0;

        // 循环操作状态转移方程
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j <= Math.min(i + nums[i], n - 1); j++) {
                dp[j] = dp[j] == 0 ? dp[i] + 1 : Math.min(dp[i] + 1, dp[j]);
                if (j == n - 1) {
                    break;
                }
            }
        }
        return dp[n - 1];
    }

    // step为跳跃次数，maxStepDist为当前跳跃的最远距离，nextMaxStepDist为下一次跳跃的最远距离

    public int jumpPro2(int[] nums) {
        int n = nums.length;
        if (n == 1 || nums[0] == 0) {
            return 0;
        }
        int step = 0;
        int maxStepDist = 0;
        int nextMaxStepDist = 0;
        for (int i = 0; i < n; i++) {
            nextMaxStepDist = Math.max(nextMaxStepDist, i + nums[i]);
            // 如果下一次跳跃的最远距离超过了n-1，就返回step + 1
            if (nextMaxStepDist >= n - 1) {
                return step + 1;
            }
            // 达到当前跳跃的最远距离后，step就可以+1，然后将下一跳的最远距离设置为当前跳的最远距离
            if (i == maxStepDist) {
                step++;
                maxStepDist = nextMaxStepDist;
            }
        }
        return step;
    }

    // 回溯法 极慢
    public int jump1(int start, int n, int[] nums, int step) {
        // 设置递归出口
        if (nums[start] == 0) {
            return 0;
        }
        if (nums[start] + start >= n - 1) {
            return step + 1;
        }
        // 遍历处理所有状态
        int minStep = Integer.MAX_VALUE;
        for (int i = 1; i < nums[start] + 1; i++) {
            int tempStep = jump1(start + i, n, nums, step + 1);
            if (tempStep > 0 && minStep > tempStep) {
                minStep = tempStep;
            }
        }
        return minStep;
    }
}
