package sheep.algorithm.pojo;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

//show in search results
@Data
public class PaperModel {
    private String id;
    private String title;
    @JSONField(name = "abstract")
    private String Abstract;
    private List<Author> authors;
    private String authorNames;
    private Venue venue;
    private int year;
    private int n_citation;
    private List<String> url;
    private List<String> keywords;
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Author
    {
        private String id;
        private String name;
        private String org;
    }
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Venue
    {
        private  String id;
        private String raw;
    }


}



