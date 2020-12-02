package sheep.search.vo;

import lombok.Data;

import java.util.List;

@Data
public class SearchParam {
    /**
     * 包含所有关键词
     */
    private String all;
    /**
     * 完全匹配关键词
     */
    private String accurate ;
    /**
     * 包含任意关键词
     */
    private String any ;
    /**
     * 包含任意关键词
     */
    private String exclude ;
    /**
     *  刊物
     */
    private String venue;
    /**
     *  作者
     */
    private String author;
    /**
     *  领域
     */
    private String fos;

    /**
     * 排序条件：sort=year/n_citation-desc/asc
     */
    private String sort;


    /**
     * 时间区间查询
     */
    private String yearRange;

    /**
     * 页码
     */
    private Integer pageNum = 1;

    /**
     * 原生的所有查询条件
     */
    private String _queryString;


}
