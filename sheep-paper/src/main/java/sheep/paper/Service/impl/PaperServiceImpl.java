package sheep.paper.Service.impl;

import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import sheep.paper.Entity.BriefPaperInfo;
import sheep.paper.Entity.Favorite;
import sheep.paper.Repository.FavoriteRepository;
import sheep.paper.Service.PaperService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
