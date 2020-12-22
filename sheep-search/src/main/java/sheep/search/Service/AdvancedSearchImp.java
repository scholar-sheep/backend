package sheep.search.Service;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.nested.Nested;
import org.elasticsearch.search.aggregations.bucket.nested.NestedAggregationBuilder;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import sheep.search.config.EsConfig;
import sheep.search.vo.PaperModel;
import sheep.search.vo.SearchParam;
import sheep.search.vo.SearchResult;

import java.io.IOException;
import java.util.*;

@Slf4j
@Service
public class AdvancedSearchImp implements SearchPaperService {

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    @Autowired
    private RedisTemplate redisTemplate;

    private int pagesize=10;
    @Override
    public SearchResult getSearchResult (SearchParam searchParam)
    {
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
                //扁平化作者列表
                StringBuilder sb=new StringBuilder();
                List<PaperModel.Author> authors=paperModel.getAuthors();
                if(authors!=null) {
                    for (PaperModel.Author author : authors) {
                        sb.append(author.getName());
                        sb.append(",");
                    }
                    if(sb.length()!=0)
                        sb.deleteCharAt(sb.length() - 1);
                    paperModel.setAuthorNames(sb.toString());
                }
                //设置高亮属性
                //获得经过高亮处理的字符
                HighlightField title = hit.getHighlightFields().get("title");
                if(title!=null){
                    String hiTitle = title.getFragments()[0].string();
                    //替换成经高亮处理的字符串
                    paperModel.setTitle(hiTitle);}

                HighlightField Abstract = hit.getHighlightFields().get("abstract");
                if(Abstract!=null){
                    String hiAbstract = Abstract.getFragments()[0].string();
                    paperModel.setAbstract(hiAbstract);}

                HighlightField venue = hit.getHighlightFields().get("venue.raw");
                if(venue!= null){
                    String hiVenue = venue.getFragments()[0].string();
                    System.out.println( venue.getFragments().length);

                    //paperModel.setVenue(hiVenue);
                }

                HighlightField authorName = hit.getHighlightFields().get("authors.name");
                if(authorName!= null){
                    String hiName = authorName.getFragments()[0].string();

                    }


                paperModels.add(paperModel);
            }
            result.setResults(paperModels);
        }
        //封装分页信息
        //2.1 当前页码
        result.setPageNum(searchParam.getPageNum());
        //2.2 总记录数
        long total = hits.getTotalHits().value;
        result.setTotal(total);
        //2.3 总页码
        Integer totalPages = (int)total % pagesize == 0 ?
                (int)total / pagesize : (int)total / pagesize + 1;
        result.setTotalPages(totalPages);
        //3.查询结果涉及的领域
        List <SearchResult.fieldVo> fieldVos=new ArrayList<>();
        Aggregations aggregations =searchResponse.getAggregations();
        Terms fos=aggregations.get("by_keywords");
        for(Terms.Bucket bucket:fos.getBuckets())
        {
                String field=bucket.getKeyAsString();
                Long num=bucket.getDocCount();
                SearchResult.fieldVo fieldVo=new SearchResult.fieldVo(field,num);
                fieldVos.add(fieldVo);
        }
        result.setFields(fieldVos);
        //4.查询结果涉及的作者
        List <String> authorNames=new ArrayList<>();
        Aggregations Authoraggregations =searchResponse.getAggregations();
        Nested authors=Authoraggregations.get("by_author");
        Terms names= authors.getAggregations().get("by_name");

        for(Terms.Bucket bucket:names.getBuckets())
        {
            String name=bucket.getKeyAsString();
            authorNames.add(name);
        }
        result.setAuthorNames(authorNames);
        return result;
    }
    private SearchRequest bulidSearchRequest(SearchParam searchParam) {
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        //1. 构建bool query
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();
        //关键词出现位置仅标题
        if(searchParam.getPos()==1)
        {
            //1.1  包含全部关键词
            if (!StringUtils.isEmpty(searchParam.getAll())) {
                BoolQueryBuilder tmp = new BoolQueryBuilder();
                boolQueryBuilder.must(
                        tmp.should(QueryBuilders.multiMatchQuery(searchParam.getAll(), "title").operator(Operator.AND))
                );
            }
            //1.2 包含完整关键词
            if (!StringUtils.isEmpty(searchParam.getAccurate())) {
                BoolQueryBuilder tmp = new BoolQueryBuilder();
                boolQueryBuilder.must(
                        tmp.should(QueryBuilders.matchPhraseQuery("title", searchParam.getAccurate()))
                );
            }
            //1.3包含任意关键词
            if (!StringUtils.isEmpty(searchParam.getAny())) {
                BoolQueryBuilder tmp = new BoolQueryBuilder();
                boolQueryBuilder.must(
                        tmp.should(QueryBuilders.multiMatchQuery(searchParam.getAny(), "title"))
                );
            }
            //1.4 不含以下任意字词
            if (!StringUtils.isEmpty(searchParam.getExclude())) {
                BoolQueryBuilder tmp = new BoolQueryBuilder();
                boolQueryBuilder.mustNot(
                        tmp.should(QueryBuilders.multiMatchQuery(searchParam.getExclude(), "title"))
                );
            }
        }
        //关键词在任何位置出现
        else {
            //1.1  包含全部关键词
            if (!StringUtils.isEmpty(searchParam.getAll())) {
                BoolQueryBuilder tmp = new BoolQueryBuilder();
                boolQueryBuilder.must(
                        tmp.should(QueryBuilders.multiMatchQuery(searchParam.getAll(), "title", "abstract").operator(Operator.AND))
                                .should(QueryBuilders.nestedQuery("authors", QueryBuilders.matchQuery("authors.name", searchParam.getAll()).operator(Operator.AND), ScoreMode.Avg))
                                .should(QueryBuilders.nestedQuery("venue", QueryBuilders.matchQuery("venue.raw", searchParam.getAll()).operator(Operator.AND), ScoreMode.Avg))
                                .minimumShouldMatch(1)
                );
            }
            //1.2 包含完整关键词
            if (!StringUtils.isEmpty(searchParam.getAccurate())) {
                BoolQueryBuilder tmp = new BoolQueryBuilder();

                boolQueryBuilder.must(
                        tmp.should(QueryBuilders.matchPhraseQuery("title", searchParam.getAccurate()))
                                .should(QueryBuilders.matchPhraseQuery("abstract", searchParam.getAccurate()))
                                .should(QueryBuilders.nestedQuery("authors", QueryBuilders.matchPhraseQuery("authors.name", searchParam.getAccurate()), ScoreMode.Avg))
                                .should(QueryBuilders.nestedQuery("venue", QueryBuilders.matchPhraseQuery("venue.raw", searchParam.getAccurate()), ScoreMode.Avg))
                                //.minimumShouldMatch(1)
                );
            }
            //1.3包含任意关键词
            if (!StringUtils.isEmpty(searchParam.getAny())) {
                BoolQueryBuilder tmp = new BoolQueryBuilder();
                boolQueryBuilder.must(
                        tmp.should(QueryBuilders.multiMatchQuery(searchParam.getAny(), "title", "abstract"))
                                .should(QueryBuilders.nestedQuery("authors", QueryBuilders.matchQuery("authors.name", searchParam.getAny()), ScoreMode.Avg))
                                .should(QueryBuilders.nestedQuery("venue", QueryBuilders.matchQuery("venue.raw", searchParam.getAny()), ScoreMode.Avg))
                );
            }
            //1.4 不含以下任意字词
            if (!StringUtils.isEmpty(searchParam.getExclude())) {
                BoolQueryBuilder tmp = new BoolQueryBuilder();
                boolQueryBuilder.mustNot(
                        tmp.should(QueryBuilders.multiMatchQuery(searchParam.getExclude(), "title", "abstract"))
                                .should(QueryBuilders.nestedQuery("authors", QueryBuilders.matchQuery("authors.name", searchParam.getExclude()), ScoreMode.Avg))
                                .should(QueryBuilders.nestedQuery("venue", QueryBuilders.matchQuery("venue.raw", searchParam.getExclude()), ScoreMode.Avg))
                );
            }
        }
        //1.5指定刊物
        if(!StringUtils.isEmpty(searchParam.getVenue()))
        {
            boolQueryBuilder.must(QueryBuilders.nestedQuery("venue",QueryBuilders.matchQuery("venue.raw",searchParam.getVenue()),ScoreMode.Avg));
        }
        //1.6指定作者姓名
        if(!StringUtils.isEmpty(searchParam.getAuthor()))
        {
            boolQueryBuilder.must(QueryBuilders.nestedQuery("authors",QueryBuilders.matchQuery("authors.name",searchParam.getAuthor()),ScoreMode.Avg));
        }

        //1.7 指定时间区间
        RangeQueryBuilder rangeQueryBuilder = QueryBuilders.rangeQuery("year");
        if (!StringUtils.isEmpty(searchParam.getYearRange())) {
            try{
            String[] years = searchParam.getYearRange().split("_");
            if (years.length == 1) {
                if (searchParam.getYearRange().startsWith("_")) {
                    rangeQueryBuilder.lte(Integer.parseInt(years[0]));
                }else {
                    rangeQueryBuilder.gte(Integer.parseInt(years[0]));
                }
            } else if (years.length == 2) {
                //_2020会截取成["","2020"]
                if (!years[0].isEmpty()) {
                    rangeQueryBuilder.gte(Integer.parseInt(years[0]));
                }
                rangeQueryBuilder.lte(Integer.parseInt(years[1]));
            }} catch (Exception e)
            {
                rangeQueryBuilder.lte(0);
            }
            }
            boolQueryBuilder.filter(rangeQueryBuilder);

        //1.8搜索结果按领域筛选
        if(!StringUtils.isEmpty(searchParam.getFos()))
        {
            String[] fos=searchParam.getFos().split(",");
            //boolQueryBuilder.filter(QueryBuilders.termsQuery("keywords.raw",fos));

            BoolQueryBuilder tmp = new BoolQueryBuilder();
            for(int i=0;i<fos.length;i++)
            {
                tmp.must(QueryBuilders.termsQuery("keywords.raw",fos[i]));
            }
            boolQueryBuilder.must(tmp);
        }
        //bool query构建完成
        searchSourceBuilder.query(boolQueryBuilder);
        //2.highlight
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.field("title");
        highlightBuilder.field("abstract");
        highlightBuilder.field("venue.raw");
        highlightBuilder.field("authors.name");

        highlightBuilder.preTags("<b style='color:red'>");
        highlightBuilder.postTags("</b>");
        searchSourceBuilder.highlighter(highlightBuilder);

        //3.分页
        searchSourceBuilder.from((searchParam.getPageNum() - 1) *pagesize);
        searchSourceBuilder.size(pagesize);
        //4.sort  sort=year/n_citation-desc/asc
        if (!StringUtils.isEmpty(searchParam.getSort())) {
            String[] sortSplit = searchParam.getSort().split("-");
            searchSourceBuilder.sort(sortSplit[0], sortSplit[1].equalsIgnoreCase("asc") ? SortOrder.ASC : SortOrder.DESC);
        }
        //5.聚合
        //5.1按照keywords 聚合 指定返回前2条记录
        TermsAggregationBuilder keywordsAgg = AggregationBuilders.terms("by_keywords").field("keywords.raw").size(5);
        searchSourceBuilder.aggregation(keywordsAgg);
        //5.2按照作者姓名聚合
        NestedAggregationBuilder nested = AggregationBuilders.nested("by_author", "authors");
        nested.subAggregation(AggregationBuilders.terms("by_name").field("authors.name.raw").size(5));
        searchSourceBuilder.aggregation(nested);

        //sheep-paper是要查询的索引
        SearchRequest request = new SearchRequest(new String[]{"sheep-paper-test"}, searchSourceBuilder);
        return request;

    }
    //统计热搜词
    public void addHotWord(SearchParam searchParam) {
        String keyword=searchParam.getAny();
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
