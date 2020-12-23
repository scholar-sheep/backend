package sheep.search.Service;

import org.elasticsearch.action.get.GetResponse;
import sheep.search.vo.SearchResult;

import java.io.IOException;

public interface PaperService {
    SearchResult getRecommendById(String paperIdStr) throws IOException;
}
