package sheep.algorithm.service;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.nested.Nested;
import org.elasticsearch.search.aggregations.bucket.nested.NestedAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sheep.algorithm.config.Client;
import sheep.algorithm.config.RedisUtil;
import sheep.algorithm.pojo.FieldResult;
import sheep.algorithm.pojo.PaperModel;

import javax.naming.directory.SearchResult;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class OrgRank {
    @Autowired
    RedisUtil redisUtil;
    public  String paperIndex = "sheep-paper";

    public List<String> getOrgRank()throws IOException {
        if(!redisUtil.hasKey("orgrank")) {
            List<String> result = new ArrayList<>();
            RestHighLevelClient client = Client.getClient();
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
            NestedAggregationBuilder nested = AggregationBuilders.nested("by_author", "authors");
            nested.subAggregation(AggregationBuilders.terms("by_org").field("authors.org.raw").size(5));
            searchSourceBuilder.aggregation(nested).size(0);
            SearchRequest request = new SearchRequest(new String[]{paperIndex}, searchSourceBuilder);
            SearchResponse searchResponse = client.search(request, RequestOptions.DEFAULT);
            Aggregations aggregations = searchResponse.getAggregations();
            Nested authors = aggregations.get("by_author");
            Terms names = authors.getAggregations().get("by_org");
            for (Terms.Bucket bucket : names.getBuckets()) {
                String name = bucket.getKeyAsString();
                result.add(name);
            }
            redisUtil.set("orgrank",result);
        }
        //若redis中不存在则先存入
        return (List<String>)redisUtil.get("orgrank");
    }




}
