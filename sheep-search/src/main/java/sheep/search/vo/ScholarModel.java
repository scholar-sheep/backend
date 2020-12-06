package sheep.search.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
public class ScholarModel {
    private String id;
    private String name;
    private String org;
    private int n_pubs;
    private  int n_citation;
    private List<Tag> tags;
    private String head;
    @Data
    @AllArgsConstructor
    public static class Tag
    {
        private String t;
        private int w;
    }

}
