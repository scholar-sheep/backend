package sheep.search.vo;

import lombok.Data;

import java.util.List;

@Data
public class SearchParam {
    /**
     * 页面传递过来的全文匹配关键字
     */
    private String keyword;

    /**
     * 领域id,可以多选
     */
    private List<Long> domainId;


    /**
     * 排序条件：sort=year/index_desc/asc
     */
    private String sort;


    /**
     * 时间区间查询
     */
    private String interval;

    /**
     * 按照属性进行筛选
     */
    private List<String> attrs;

    /**
     * 页码
     */
    private Integer pageNum = 1;

    /**
     * 原生的所有查询条件
     */
    private String _queryString;


}
