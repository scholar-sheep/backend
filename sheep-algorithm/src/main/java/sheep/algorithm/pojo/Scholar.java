package sheep.algorithm.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Scholar {
    private String id;
    //学者姓名
    private String name;
    //记录该作者的所属机构
    private String org;
    //学者发布的学术成果数量
    private int n_pubs;
    //学者的学术成果被引用次数
    private int n_citation;

    private List<Tag> tags;
    private List<Pub>pubs;
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Tag
    {
        private String t;
        private int w;
    }
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Pub
    {
        private String i;
        private int r;
    }
}