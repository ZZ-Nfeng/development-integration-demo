package top.bulk.search.es.constant;

/**
 * 分词器常量
 * 关于这两个枚举怎么来的，查看 https://github.com/medcl/elasticsearch-analysis-ik ==> ik_max_word 和 ik_smart 什么区别?
 */
public interface AnalyzerConstant {
    /**
     * IK 最大化分词
     * <p>
     * 会将文本做最细粒度的拆分
     */
    String IK_MAX_WORD = "ik_max_word";
    /**
     * IK 智能分词
     * <p>
     * 会做最粗粒度的拆分
     */
    String IK_SMART = "ik_smart";
}
