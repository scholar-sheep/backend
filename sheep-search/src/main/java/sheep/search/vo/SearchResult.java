package sheep.search.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
public class SearchResult<T> {
    private List<T> results;
}
