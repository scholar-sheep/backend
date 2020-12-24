package sheep.paper.Entity;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaperModel {
    private String id;
    private String title;
    @JSONField(name = "abstract")
    private String Abstract;
    private List<Author> authors;
    private Venue venue;
    private int year;
    private int n_citation;
    private String authorNames;
    private String venueName;
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Author
    {
        private String name;
        private String org;
    }
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Venue
    {
        private  String id;
        private String raw;
    }

}