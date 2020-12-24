package sheep.paper.Service.impl;

import com.alibaba.fastjson.JSON;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import sheep.paper.Entity.BriefPaperInfo;
import sheep.paper.Entity.Favorite;
import sheep.paper.Entity.PaperData;
import sheep.paper.Entity.PaperModel;
import sheep.paper.Repository.FavoriteRepository;
import sheep.paper.Service.PaperService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class PaperServiceImpl implements PaperService {

    @Qualifier("elasticsearchClient")
    @Autowired
    RestHighLevelClient highLevelClient;
    @Autowired
    FavoriteRepository favoriteRepository;

    @Override
    public GetResponse getDetailInfoInES(GetRequest getRequest) throws IOException {
        return highLevelClient.get(getRequest, RequestOptions.DEFAULT);
    }

    @Override
    public String getEsInfoById(String paperIdStr) {
        GetRequest getRequest = new GetRequest("sheep-paper", paperIdStr);
        try {
            GetResponse response = getDetailInfoInES(getRequest);
            return response.getSourceAsString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }@Override
    public PaperData getPaperDetailById(String paperIdStr) {
        GetRequest getRequest = new GetRequest("sheep-paper", paperIdStr);
        SearchSourceBuilder searchSourceBuilder=new SearchSourceBuilder();
        try {
            GetResponse response = getDetailInfoInES(getRequest);
            String sourceAsString = response.getSourceAsString();
            PaperData paperData= JSON.parseObject(sourceAsString, PaperData.class);
            String title=paperData.getTitle();
            searchSourceBuilder.query(QueryBuilders.multiMatchQuery(title,"title")).sort("n_citation", SortOrder.DESC).size(5);
            SearchRequest request = new SearchRequest(new String[]{"sheep-paper"}, searchSourceBuilder);
            SearchResponse searchResponse = highLevelClient.search(request, RequestOptions.DEFAULT);
            SearchHits hits = searchResponse.getHits();
            // 封装查询到的论文信息
            if (hits.getHits()!=null&&hits.getHits().length>0)
            {
                List<PaperModel> paperModels = new ArrayList<>();
                for (SearchHit hit : hits)
                {
                    String paperString = hit.getSourceAsString();
                    PaperModel paperModel = JSON.parseObject(paperString, PaperModel.class);
                    paperModels.add(paperModel);
                }
                paperData.setPaperList(paperModels);
            }
            return paperData;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }



    @Override
    public BriefPaperInfo getBriefPaperInfoById(String paperIdStr, int userId) {
//        Map<String, Object> responseMap = getEsInfoById(paperIdStr);
//        if (responseMap==null) {
//            return null;
//        }
//        BriefPaperInfo paperInfo = makeUpBriefPaperInfoWithOutFavorInfo(responseMap, paperIdStr);
//        paperInfo.setFavored(checkFavor(userId, paperIdStr));
//        return paperInfo;
        return null;
    }

    @Override
    public Boolean favor(int userId, String paperIdStr) {
        if (checkFavor(userId, paperIdStr)) {
            return false;
        }
        Favorite favorite = new Favorite();
        favorite.setPaperid(paperIdStr);
        favorite.setUserid(userId);
        try {
            favoriteRepository.saveAndFlush(favorite);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @Override
    public Boolean unfavor(int userId, String paperIdStr) {
        if (!checkFavor(userId, paperIdStr)) {
            return false;
        }
        Favorite favorite = favoriteRepository.findFavoriteByUseridAndPaperid(userId, paperIdStr);
//        try {
            favoriteRepository.deleteByFavoriteid(favorite.getFavoriteid());
//        } catch (Exception e) {
//            return false;
//        }
        return true;
    }

    @Override
    public Boolean checkFavor(int userId, String paperIdStr) {
        return favoriteRepository.existsByPaperidAndUserid(paperIdStr, userId);
    }

    @Override
    public List<Favorite> getfavorites(int ID) {
        return favoriteRepository.findAllByUserid(ID);
    }
}
