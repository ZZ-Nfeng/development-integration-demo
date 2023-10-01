package top.bulk.bloom.bloom;

import java.util.BitSet;

/**
 * 自定义一个布隆过滤器 （精简版）
 */
public class BulkBloomFilter {
    /**
     * 一个长度为 10 亿的比特位
     */
    private static final int DEFAULT_SIZE = 256 << 22;

    /**
     * 不同哈希函数的种子，一般应取质数
     * 为了降低错误率，使用加法 hash 算法，所以定义一个8个元素的质数数组
     */
    private static final int[] SEEDS = {3, 5, 7, 11, 13, 31, 37, 61};

    /**
     * 相当于构建 8 个不同的 hash 算法 HashFunction 越多，误判率越低，也越慢
     */
    private static final HashFunction[] FUNCTIONS = new HashFunction[SEEDS.length];

    /**
     * 初始化布隆过滤器的 BitSet
     * BitSet 即“位图”，是一个很长的 “0/1”序列，他的功能就是存储0或者1
     */
    private static final BitSet BIT_SET = new BitSet(DEFAULT_SIZE);

    /**
     * 添加数据
     *
     * @param value 需要加入的值
     */
    public static void add(String value) {
        if (value != null) {
            for (HashFunction f : FUNCTIONS) {
                //计算 hash 值并修改 bitmap 中相应位置为 true
                BIT_SET.set(f.hash(value), true);
            }
        }
    }

    /**
     * 判断相应元素是否存在
     *
     * @param value 需要判断的元素
     * @return 结果
     */
    public static boolean contains(String value) {
        if (value == null) {
            return false;
        }
        boolean ret = true;
        for (HashFunction f : FUNCTIONS) {
            ret = BIT_SET.get(f.hash(value));
            // 一个 hash 函数返回 false 就说明这个数据不存在， 则跳出循环
            if (!ret) {
                break;
            }
        }
        return ret;
    }

    /**
     * 测试
     */
    public static void main(String[] args) {
        // 初始化 FUNCTIONS
        for (int i = 0; i < SEEDS.length; i++) {
            FUNCTIONS[i] = new HashFunction(DEFAULT_SIZE, SEEDS[i]);
        }

        // 添加0到1亿数据
        for (int i = 0; i < 100000000; i++) {
            add(String.valueOf(i));
        }
        String id = "123456789";
        // 将这个id 放入进去
        add(id);
        // 查询已经存在的 id 返回 true
        System.out.println(contains(id));
        // 查询不存在的id 则返回 false
        System.out.println("" + contains("100000000"));
    }

    /**
     * 用于计算 hash
     */
    static class HashFunction {
        /**
         * 数组长度 用于限制 hash 生成值的 最大值
         */
        private final int size;
        /**
         * 不同哈希函数的种子，一般应取质数
         */
        private final int seed;

        public HashFunction(int size, int seed) {
            this.size = size;
            this.seed = seed;
        }

        public int hash(String value) {
            int result = 0;
            int len = value.length();
            for (int i = 0; i < len; i++) {
                result = seed * result + value.charAt(i);
            }
            final int i = (size - 1) & result;
            return i;
        }
    }
}

