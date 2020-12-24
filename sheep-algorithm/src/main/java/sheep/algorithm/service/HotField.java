package sheep.algorithm.service;

import java.util.*;

import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.search.*;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sheep.algorithm.config.Client;
import sheep.algorithm.config.RedisUtil;
import sheep.algorithm.pojo.FieldResult;
import sheep.algorithm.pojo.NetworkResult;
import com.alibaba.fastjson.JSON;
import sheep.algorithm.pojo.PaperModel;

import java.io.IOException;

@Slf4j
@Service
public class HotField{
    @Autowired
    RedisUtil redisUtil;
    public  static String paperIndex = "sheep-paper";

    public static void main(String[] args) throws IOException{
        for (String s : getHotField(10, 2019, 2020)) {
            System.out.println(s);
        }
    }

    public static ArrayList<String> getHotField(int n, int from, int to) throws IOException{
        HashMap<String, Integer> keywords = new HashMap<>();

        SearchRequest request = new SearchRequest(paperIndex);

        request.scroll(TimeValue.timeValueMinutes(1L));

        SearchSourceBuilder builder = new SearchSourceBuilder();
        builder.query(QueryBuilders.rangeQuery("year").lte(to).gte(from));

        int size = 10000;
        builder.size(size);
        
        request.source(builder);

        RestHighLevelClient client = Client.getClient();
        SearchResponse response = client.search(request, RequestOptions.DEFAULT);

        String scrollId = response.getScrollId();
        long total = response.getHits().getTotalHits().value;
        System.out.println(total);
        for (SearchHit hit : response.getHits().getHits()) {
            if(hit.getSourceAsMap().get("keywords")==null)continue;
            System.out.println(hit.getSourceAsMap().get("keywords"));
            for(String keyword : (List<String>)(hit.getSourceAsMap().get("keywords"))){

                if(!keywords.containsKey(keyword)){
                    keywords.put(keyword, 1);
                }
                else{
                    keywords.put(keyword,keywords.get(keyword)+1);
                }
//                System.out.println(keywords.get(keyword));
            }
        }
       while (true) {
           total -= size;
           System.out.println(total);
           SearchScrollRequest scrollRequest = new SearchScrollRequest(scrollId);

           //指定scrollId的生存时间
           scrollRequest.scroll(TimeValue.timeValueMinutes(1L));

           //执行查询获取返回结果
           SearchResponse scrollResp = client.scroll(scrollRequest, RequestOptions.DEFAULT);

           //判断是否查询到了数据
           SearchHit[] hits = scrollResp.getHits().getHits();
           if(hits != null && hits.length > 0) {
               for (SearchHit hit : hits) {
                   String sourceAsString = hit.getSourceAsString();
                   PaperModel paperModel = JSON.parseObject(sourceAsString, PaperModel.class);
                   /*
                   if(hit.getSourceAsMap().get("keywords")==null)continue;
                   System.out.println(hit.getSourceAsMap().get("keywords").toString());
                   for(String keyword : (List<String>)(hit.getSourceAsMap().get("keywords"))){*/
                   if(paperModel.getKeywords()==null)continue;
                       for(String keyword :paperModel.getKeywords()){
                           System.out.print(keyword);
                       if(!keywords.containsKey(keyword)){
                           keywords.put(keyword, 1);
                       }
                       else{
                           keywords.put(keyword,keywords.get(keyword)+1);
                       }
//                       System.out.println(keywords.get(keyword));
                   }
               }
           }else{
               break;
           }
       }

        //10. 创建CLearScrollRequest
        ClearScrollRequest clearScrollRequest = new ClearScrollRequest();

        //11. 指定ScrollId
        clearScrollRequest.addScrollId(scrollId);

        //12. 删除ScrollId
        ClearScrollResponse clearScrollResponse = client.clearScroll(clearScrollRequest, RequestOptions.DEFAULT);

        System.out.println(keywords.size());
        keywords = (HashMap<String, Integer>) sortDescend(keywords);
        System.out.println("end");
        ArrayList<String> hot_field = new ArrayList<>();
        for (String keyword : keywords.keySet()) {
            hot_field.add(keyword);
            if (hot_field.size()==n)break;
        }
        return hot_field;
    }

    public static <K, V extends Comparable<? super V>> Map<K, V> sortDescend(Map<K, V> map) {
        List<Map.Entry<K, V>> list = new ArrayList<>(map.entrySet());
        Collections.sort(list, new Comparator<Map.Entry<K, V>>() {
            @Override
            public int compare(Map.Entry<K, V> o1, Map.Entry<K, V> o2) {
                int compare = (o1.getValue()).compareTo(o2.getValue());
                return -compare;
            }
        });

        Map<K, V> returnMap = new LinkedHashMap<K, V>();
        for (Map.Entry<K, V> entry : list) {
            returnMap.put(entry.getKey(), entry.getValue());
        }
        return returnMap;
    }
    public FieldResult getHot(int n, int from, int to) throws IOException
    {
        if(!redisUtil.hasKey("hotfields")){
        RestHighLevelClient client = Client.getClient();
        FieldResult fieldResults =new FieldResult();
        List<FieldResult.Field> results=new ArrayList<>();
        //若redis中不存在则先存入
        if(!redisUtil.hasKey("hotfields"+from+to))
            redisUtil.set("hotfields"+from+to,this.getHotField(n,from,to),15);
        ArrayList<String> fields=(ArrayList<String>)redisUtil.get("hotfields"+from+to);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();

        for(String field:fields)
        {
            FieldResult.Field fieldResult=new FieldResult.Field();
            searchSourceBuilder.query(QueryBuilders.termQuery("keywords.raw",field)).sort("n_citation", SortOrder.DESC).size(5);
            SearchRequest request = new SearchRequest(new String[]{"sheep-paper"}, searchSourceBuilder);
            SearchResponse searchResponse = client.search(request, RequestOptions.DEFAULT);
            SearchHits hits = searchResponse.getHits();
            //1. 封装查询到的论文信息
            if (hits.getHits()!=null&&hits.getHits().length>0)
            {
                fieldResult.setField(field);
                List<PaperModel> paperModels = new ArrayList<>();
                for (SearchHit hit : hits) {
                    String sourceAsString = hit.getSourceAsString();
                    PaperModel paperModel = JSON.parseObject(sourceAsString, PaperModel.class);
                    paperModels.add(paperModel);
                }
                fieldResult.setPapers(paperModels);

            }
            results.add(fieldResult);
        }
        fieldResults.setFields(results);
        redisUtil.set("hotfields",fieldResults,15);}
        return (FieldResult) redisUtil.get("hotfields");
    }
}