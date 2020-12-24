package sheep.paper.Entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Transient;

import java.util.List;

@Data
public class PaperData {
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
    @Data
    @AllArgsConstructor
    public static class Author
    {
        private String id;
        private String name;
        private String org;
    }
    @Data
    @AllArgsConstructor
    public static class Venue
    {
        private  String id;
        private String raw;
    }

    @Transient
    Boolean favored;
}
