package sheep.search.Service;

import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sheep.search.vo.SearchParam;
import sheep.search.vo.SearchResult;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class PaperServiceImpl implements PaperService {
    @Autowired
    private RestHighLevelClient restHighLevelClient;
    @Autowired
    private SearchPaperService advancedSearchService;

    @Override
    public SearchResult getRecommendById(String paperIdStr) throws IOException {
        GetRequest getRequest = new GetRequest("sheep-paper", paperIdStr);
        GetResponse response = restHighLevelClient.get(getRequest, RequestOptions.DEFAULT);
        Map<String, Object> res = response.getSourceAsMap();
        List<String> keywords = (List<String>) res.get("keywords");
        List<Map<String, Object>> authorList =  (List<Map<String, Object>>) res.get("authors");
        List<String> authorNameList = new ArrayList<>();
        for (Map author : authorList) {
            String name;
            try {
                name = (String) author.get("name");
            } catch (Exception e) {
                continue;
            }
            if (name!=null && !name.equals("")) {
                authorNameList.add(name);
            }
        }
        StringBuilder sb = new StringBuilder();
        for (String s : keywords) {
            if (!s.equals("")) {
                sb.append(s);
                sb.append(' ');
            }
        }
        sb.deleteCharAt(sb.length()-1);
        SearchParam searchParam = new SearchParam();
        searchParam.setAny(sb.toString());
        return advancedSearchService.getSearchResult(searchParam);
    }
}
