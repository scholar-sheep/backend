package sheep.search.vo;

import lombok.Data;

@Data
public class ScholarParam {
    /**
     * 姓名
     */
    private String name;
    /**
     * 机构
     */
    private String org;
    /**
     * 页码
     */
    private Integer pageNum = 1;

}
