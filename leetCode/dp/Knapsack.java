package leetCode.dp;

public class Knapsack {

    public static void main(String[] args) {
        // 动态规划的两种解法：1，状态转移表法； 2，状态转移方程法
        // 划分多阶段后，如果最后阶段的节点是不固定的，求的是节点的位置和到达节点的路径，就用状态转移表法，如0-1背包问题
        // 如果最后阶段的节点是固定的，求的是到达节点的路径，则可以用状态转移表也可以用状态转移方程，如果是状态类型大于2（高维），则使用状态转移方程
        // 状态转移表法：写出回溯算法 》 定义状态空间 》 画状态转移表 》 根据递推关系填表 》 将填表过程翻译为代码
        // 状态转移方程法：找最优子结构 》 写状态转移方程 》 通过状态转移方程反向递归；


        // 基础版的0-1背包问题，物品数n，物品重量weight，背包承载上限w，求背包中物品重量最大值
        int[] weight = new int[]{2, 2, 4, 6, 3};
        int n = 5;
        int w = 9;
        System.out.println(new Knapsack().knapsack(weight, n, w));
        System.out.println(new Knapsack().knapsackPro(weight, n, w));

        // 进阶版的0-1背包问题，每个物品包含不同的价值(正整数)，求最大价值
        int[] value = new int[]{3, 4, 8, 9, 6};
        System.out.println(new Knapsack().knapsack1(weight, value, n, w));

        // 实战训练：有币值为1、3、5的三种硬币，如果用来支付9元商品，最少需要多少硬币
        int[] coins = new int[]{1, 3, 5};
        Knapsack knapsack = new Knapsack();
        // 回溯法
        knapsack.minNum(9, coins, 0);
        System.out.println(knapsack.minCoinNum);
        // 状态转移表法
        System.out.println(knapsack.minNum(9, coins));
        // 状态转移方程法
        System.out.println(knapsack.minNum1(9, coins));


    }

    // 物品数n，物品重量weight，背包承载上限w，求背包中物品重量最大值
    // 其他变种，背包装满时的最大或者最小价值，背包装满时最小或者最大物品数
    public int knapsack(int[] weight, int n, int w) {
        // 1,创建保存状态的空间，一维坐标是物品数（阶段数），二维坐标是背包上限+1（限制值+1）
        boolean[][] states = new boolean[n][w + 1];
        // 2,初始化第一物品的状态
        states[0][0] = true; // 不选第一个物品
        states[0][weight[0]] = true; // 选第一物品

        // 3,循环操作状态转移方程，将第一个物品（阶段）的状态推导至最后一个物品（阶段）
        for (int i = 1; i < n; i++) {
            // 不选第i个物品
            for (int j = 0; j < w + 1; j++) {
                if (states[i - 1][j]) {
                    states[i][j] = true;
                }
            }

            // 选第i个物品, 注意j的上限是w + 1 - weight[i], 否则循环体中的j + weight[i]就可能大于w, 导致数组越界
            for (int j = 0; j < w + 1 - weight[i]; j++) {
                if (states[i - 1][j]) {
                    states[i][j + weight[i]] = true;
                }
            }
        }

        // 4, 遍历最后一个物品（阶段）的状态，输出背包中物品的最大重量
        for (int i = w; i > 0; i--) {
            if (states[n - 1][i]) {
                // 5扩展：求背包最大重量时，背包中存放的物品
                printSelect(states, weight, n, i);
                return i;
            }
        }
        return 0;
    }

    public void printSelect(boolean[][] states, int[] weight, int n, int max) {
        for (int i = n - 1; i >= 0; i--) {
            // 如果物品i被选了，就打印其重量, 没选就不管
            if (max - weight[i] >= 0 && states[i - 1][max - weight[i]]) {
                System.out.println(weight[i] + "");
                max = max - weight[i];
            }
        }
        if (max > 0) {
            System.out.println(weight[max] + "");
        }
    }

    // 优化1：将保存状态的二维数组精简成一维数组，节省空间
    public int knapsackPro(int[] weight, int n, int w) {
        // 1,创建保存状态的空间
        boolean[] states = new boolean[w + 1];
        // 2,初始化第一物品的状态
        states[0] = true; // 不选第一个物品
        states[weight[0]] = true; // 选第一物品

        // 3,循环操作状态转移方程，将第一个物品（阶段）的状态推导至最后一个物品（阶段）
        for (int i = 1; i < n; i++) {
            // 因为每个物品状态使用同样的空间，所以不选第i个物品时不需要做处理

            // 选第i个物品时，需要从后往前遍历，从前往后会导致重复操作同一个状态
            for (int j = w - weight[i]; j >= 0; j--) {
                if (states[j]) {
                    states[j + weight[i]] = true;
                }
            }
        }

        // 4, 遍历最后一个物品（阶段）的状态，输出背包中物品的最大重量
        for (int i = w; i > 0; i--) {
            if (states[i]) {
                return i;
            }
        }
        return 0;
    }

    public int knapsack1(int[] weight, int[] value, int n, int w) {
        // 1,创建保存状态的空间，一维坐标是物品数（阶段数），二维坐标是背包上限+1（限制值+1），值是物品价值
        int[][] states = new int[n][w + 1];
        // 1.1, 初始化状态空间
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < w + 1; j++) {
                states[i][j] = -1;
            }
        }

        // 2,初始化第一物品的状态
        states[0][0] = 0;
        states[0][weight[0]] = value[0]; // 选第一物品

        // 3,循环操作状态转移方程，将第一个物品（阶段）的状态推导至最后一个物品（阶段）
        for (int i = 1; i < n; i++) {
            // 不选第i个物品
            for (int j = 0; j < w + 1; j++) {
                if (states[i - 1][j] > -1) {
                    states[i][j] = states[i - 1][j];
                }
            }

            // 选第i个物品, 注意j的上限是w + 1 - weight[i], 否则循环体中的j + weight[i]就可能大于w, 导致数组越界
            for (int j = 0; j < w + 1 - weight[i]; j++) {
                if (states[i - 1][j] > -1) {
                    // 上一个for循环中可能已经对states[i][j + weight[i]]赋予了更大的值，所以需要防止更大的物品价值被覆盖
                    if (states[i - 1][j] + value[i] > states[i][j + weight[i]]) {
                        states[i][j + weight[i]] = states[i - 1][j] + value[i];
                    }
                }
            }
        }

        // 4, 遍历最后一个物品（阶段）的状态，输出背包中物品的最大价值
        int maxValue = 0;
        for (int i = w; i > 0; i--) {
            if (states[n - 1][i] > maxValue) {
                maxValue = states[n - 1][i];
            }
        }
        return maxValue;
    }

    public int minCoinNum = Integer.MAX_VALUE;
    public void minNum(int p, int[] coins, int currentNum) {
        if (p < 0) {
            return;
        }
        if (p == 0) {
            minCoinNum = Math.min(currentNum, minCoinNum);
            return;
        }

        for (int coin : coins) {
            minNum(p - coin, coins, currentNum + 1);
        }
    }

    public int minNum(int p, int[] coins) {
        boolean[][] dp = new boolean[p][p +1];

        for (int i = 0; i < coins.length; i++) {
            dp[0][i] = true;
        }

        for (int i = 1; i < p; i++) {
            if (dp[i - 1][p]) {
                return i;
            }

            for (int coin : coins) {
                for (int k = 0; k < p + 1; k++) {
                    if (dp[i - 1][k] && (k + coin <= p)) {
                        dp[i][k + coin] = true;
                    }
                }
            }
        }
        return 0;
    }

    public int minNum1(int p, int[] coins) {
        // 状态转移方程：f(p) = Math.min(f(p)-coins[0]...) + 1
        if (p == 0) {
            return 0;
        }

        for (int coin : coins) {
            if (p == coin) {
               return 1;
            }
        }
        int min = Integer.MAX_VALUE;
        for (int coin : coins) {
            if (p - coin >= 0) {
                int cc = minNum1(p - coin, coins);
                if (cc < min) {
                    min = cc;
                }
            }
        }
        return 1 + min;
    }
}
