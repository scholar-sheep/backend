package sheep.portal.pojo;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
public class PaperParam {
    private String title;
    private String Abstract;
    private String authors;
    private String venue;
    private int year;
    private int n_citation;
}
