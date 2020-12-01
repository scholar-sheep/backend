package sheep.search.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
public class SearchResult<T> {
    private List<T> results;
    /**
     * 当前页码
     */
    private Integer pageNum;

    /**
     * 总记录数
     */
    private Long total;

    /**
     * 总页码
     */
    private Integer totalPages;
    /**
     * 领域聚合
     */
    private List<fieldVo> fileds;
    /**
     * 作者聚合
     */
    private List<String> authorNames;
    @Data
    @AllArgsConstructor
    public static class fieldVo
    {
        private String field;
        private Long num;
    }
}


