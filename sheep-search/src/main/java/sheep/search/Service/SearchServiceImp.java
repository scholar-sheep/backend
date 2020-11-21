package sheep.search.Service;

import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import  com.carrotsearch.hppc.IntObjectHashMap;
import org.redisson.api.RScoredSortedSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import sheep.search.config.EsConfig;
import sheep.search.vo.PaperModel;
import sheep.search.vo.SearchParam;
import sheep.search.vo.SearchResult;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.util.StringUtils;
import java.io.IOException;
import java.util.*;
/*分布式锁*/
import org.redisson.api.RScoredSortedSet;
import org.redisson.api.RedissonClient;
@Slf4j
@Service
public class SearchServiceImp implements SearchService {

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    @Autowired
    private RedisTemplate redisTemplate;
    @Override
    public SearchResult getSearchResult (SearchParam searchParam)
    {
        addHotWord(searchParam);
        SearchResult searchResult=null;
        SearchRequest request = bulidSearchRequest(searchParam);
        SearchResponse searchResponse = null;
        try {
            searchResponse = restHighLevelClient.search(request, EsConfig.COMMON_OPTIONS);
        } catch (IOException e) {
            e.printStackTrace();
        }
        searchResult = bulidSearchResult(searchParam,searchResponse);
        return searchResult;
    }
    private SearchResult bulidSearchResult(SearchParam searchParam, SearchResponse searchResponse) {
        SearchResult result = new SearchResult();
        SearchHits hits = searchResponse.getHits();
        //1. 封装查询到的论文信息
        if (hits.getHits()!=null&&hits.getHits().length>0){
            List<PaperModel> paperModels = new ArrayList<>();
            for (SearchHit hit : hits) {
                String sourceAsString = hit.getSourceAsString();
                PaperModel paperModel = JSON.parseObject(sourceAsString, PaperModel.class);
                paperModels.add(paperModel);
            }
            result.setResults(paperModels);
        }
        return result;
    }
    private SearchRequest bulidSearchRequest(SearchParam searchParam) {

        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        //1. 构建bool query
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
        //1.1  fuzzy search
        if (!StringUtils.isEmpty(searchParam.getKeyword())) {
            boolQueryBuilder.should(QueryBuilders.fuzzyQuery("title", searchParam.getKeyword()))
                    .should(QueryBuilders.fuzzyQuery("keywords", searchParam.getKeyword()));
        }
        //bool query构建完成
        searchSourceBuilder.query(boolQueryBuilder);
        //sheep-paper是要查询的索引
        SearchRequest request = new SearchRequest(new String[]{"sheep-paper"}, searchSourceBuilder);
        return request;
    }
    //统计热搜词
    public void addHotWord(SearchParam searchParam) {
        String keyword=searchParam.getKeyword();
        if(!StringUtils.isEmpty(keyword)) {
            // 给被搜索的关键字加热度
            String hotword = "searchHotWord";
            // 如果关键字存在 热度+1
            Double x = 1.0;
            redisTemplate.opsForZSet().incrementScore(hotword, keyword, x);
        }
    }

    @Override
    public SearchResult hotSearchWord() {
        SearchResult result = new SearchResult();
        List<Map<Object, Object>> list = new ArrayList<>();
        //获取所有键
        Set<String> keys = redisTemplate.keys("searchHotWord");
        for (String key : keys) {

            HashMap<Object, Object> map = new HashMap<>();
            //倒排，获取前3条数据
            if (redisTemplate.type(key).code() == "zset") {
                Object value = redisTemplate.opsForZSet().reverseRange(key, 0, 2);
                map.put("key", key);
                map.put("value", value);
                list.add(map);
            }
        }
        result.setResults(list);
        return result;
    }
}
