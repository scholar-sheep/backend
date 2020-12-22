package sheep.search.Service;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import sheep.search.config.EsConfig;
import sheep.search.vo.ScholarModel;
import sheep.search.vo.ScholarParam;
import sheep.search.vo.SearchResult;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class SearchScholarImp implements SearchScholarService {
    @Autowired
    private RestHighLevelClient restHighLevelClient;
    @Autowired
    private RedisTemplate redisTemplate;

    private int pagesize = 1;

    @Override
    public SearchResult getSearchResult(ScholarParam searchParam) {
        SearchResult searchResult = null;
        SearchRequest request = bulidSearchRequest(searchParam);
        SearchResponse searchResponse = null;
        try {
            searchResponse = restHighLevelClient.search(request, EsConfig.COMMON_OPTIONS);
        } catch (IOException e) {
            e.printStackTrace();
        }
        searchResult = bulidSearchResult(searchParam, searchResponse);
        return searchResult;
    }

    private SearchRequest bulidSearchRequest(ScholarParam searchParam) {
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        //1. 构建bool query
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
        if (!StringUtils.isEmpty(searchParam.getName())) {
            boolQueryBuilder.must(QueryBuilders.matchQuery("name", searchParam.getName()));
        }
        if (!StringUtils.isEmpty(searchParam.getOrg())) {
            boolQueryBuilder.must(QueryBuilders.matchQuery("org", searchParam.getOrg()));
        }
        //bool query构建完成
        searchSourceBuilder.query(boolQueryBuilder);

        //2.分页
        searchSourceBuilder.from((searchParam.getPageNum() - 1) * pagesize);
        searchSourceBuilder.size(pagesize);

        //sheep-paper是要查询的索引
        SearchRequest request = new SearchRequest(new String[]{"sheep-scholar"}, searchSourceBuilder);
        return request;
    }

    private SearchResult bulidSearchResult(ScholarParam searchParam, SearchResponse searchResponse) {
        SearchResult result = new SearchResult();
        SearchHits hits = searchResponse.getHits();
        //1. 封装查询到的学者信息
        if (hits.getHits() != null && hits.getHits().length > 0) {
            List<ScholarModel> scholarModels = new ArrayList<>();
            for (SearchHit hit : hits) {
                String sourceAsString = hit.getSourceAsString();
                ScholarModel scholarModel = JSON.parseObject(sourceAsString, ScholarModel.class);
                scholarModels.add(scholarModel);
            }
            result.setResults(scholarModels);

        }
        //
        //封装分页信息
        //2.1 当前页码
        result.setPageNum(searchParam.getPageNum());
        //2.2 总记录数
        long total = hits.getTotalHits().value;
        result.setTotal(total);
        //2.3 总页码
        Integer totalPages = (int) total % pagesize == 0 ?
                (int) total / pagesize : (int) total / pagesize + 1;
        result.setTotalPages(totalPages);
        return result;
    }
}
