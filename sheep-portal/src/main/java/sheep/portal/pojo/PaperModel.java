package sheep.portal.pojo;

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
    private String venue;
    private int year;
    private int n_citation;
    private String authorNames;
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Author
    {
        private String name;
        private String org;
    }
    public PaperModel(PaperParam paperParam)
    {
        this.id= UUID.randomUUID().toString();
        this.title=paperParam.getTitle();
        this.Abstract=paperParam.getAbstract();
        this.venue=paperParam.getVenue();
        this.year=paperParam.getYear();
        this.n_citation= paperParam.getN_citation();
        this.authorNames=paperParam.getAuthors();
    }

}