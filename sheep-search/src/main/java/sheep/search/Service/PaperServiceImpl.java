package sheep.search.Service;

import com.alibaba.fastjson.JSON;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sheep.search.config.EsConfig;
import sheep.search.vo.PaperModel;
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

    private int pagesize=10;

    @Override
    public SearchResult getRecommendById(String paperIdStr) throws IOException {
        GetRequest getRequest = new GetRequest("sheep-paper", paperIdStr);
        GetResponse response = restHighLevelClient.get(getRequest, RequestOptions.DEFAULT);
        Map<String, Object> res = response.getSourceAsMap();
        List<String> keywords = (List<String>) res.get("keywords");
//        List<Map<String, Object>> authorList =  (List<Map<String, Object>>) res.get("authors");
//        List<String> authorNameList = new ArrayList<>();
//        for (Map author : authorList) {
//            String name;
//            try {
//                name = (String) author.get("name");
//            } catch (Exception e) {
//                continue;
//            }
//            if (name!=null && !name.equals("")) {
//                authorNameList.add(name);
//            }
//        }
        StringBuilder sb = new StringBuilder();
        for (String s : keywords) {
            if (!s.equals("")) {
                sb.append(s);
                sb.append(' ');
                break;
            }
        }
        if (sb.length()<=0) {
            return null;
        }
        sb.deleteCharAt(sb.length()-1);

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
        BoolQueryBuilder tmp = new BoolQueryBuilder();
        boolQueryBuilder.must(
                tmp.should(QueryBuilders.multiMatchQuery(sb.toString(), "title", "abstract"))
                        .should(QueryBuilders.nestedQuery("authors", QueryBuilders.matchQuery("authors.name", sb.toString()), ScoreMode.Avg))
                        .should(QueryBuilders.nestedQuery("venue", QueryBuilders.matchQuery("venue.raw", sb.toString()), ScoreMode.Avg))
        );
        searchSourceBuilder.query(boolQueryBuilder);
        SearchRequest request = new SearchRequest(new String[]{"sheep-paper"}, searchSourceBuilder);
        SearchResponse searchResponse = null;
        try {
            searchResponse = restHighLevelClient.search(request, EsConfig.COMMON_OPTIONS);
        } catch (IOException e) {
            e.printStackTrace();
        }
        SearchResult result = new SearchResult();
        SearchHits hits = searchResponse.getHits();
        //1. 封装查询到的论文信息
        if (hits.getHits()!=null&&hits.getHits().length>0) {
            List<PaperModel> paperModels = new ArrayList<>();
            for (SearchHit hit : hits) {
                String sourceAsString = hit.getSourceAsString();
                PaperModel paperModel = JSON.parseObject(sourceAsString, PaperModel.class);
                paperModels.add(paperModel);
            }

            result.setResults(paperModels);

            //封装分页信息
            //2.1 当前页码
            result.setPageNum(1);
            //2.2 总记录数
            long total = hits.getTotalHits().value;
            result.setTotal(total);
            //2.3 总页码
            Integer totalPages = (int) total % pagesize == 0 ?
                    (int) total / pagesize : (int) total / pagesize + 1;
            result.setTotalPages(totalPages);
        }
        return result;
    }
}
