package sheep.algorithm.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import java.awt.print.Paper;
import java.util.List;

@Data
public class FieldResult {

    private List<Field> fields;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Field {
    private String field;
    private List<PaperModel> papers;}
}
