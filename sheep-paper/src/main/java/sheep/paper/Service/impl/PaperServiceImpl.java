package sheep.paper.Service.impl;

import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import sheep.common.exception.ErrorType;
import sheep.common.utils.ResultDTO;
import sheep.paper.Entity.Author;
import sheep.paper.Entity.BriefPaperInfo;
import sheep.paper.Entity.Favorite;
import sheep.paper.Entity.Paper;
import sheep.paper.Repository.FavoriteRepository;
import sheep.paper.Repository.PaperRepository;
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
    PaperRepository paperRepository;
    @Autowired
    FavoriteRepository favoriteRepository;

    @Override
    public GetResponse getDetailInfoInES(GetRequest getRequest) throws IOException {
        return highLevelClient.get(getRequest, RequestOptions.DEFAULT);
    }

    @Override
    public Paper getMySqlInfoById(String paperIdStr) {
        return paperRepository.findPaperByPaperId(paperIdStr);
    }

    @Override
    public Map<String, Object> getEsInfoById(String paperIdStr) {
        GetRequest getRequest = new GetRequest("paper", paperIdStr);
        try {
            GetResponse response = getDetailInfoInES(getRequest);
            return response.getSourceAsMap();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public BriefPaperInfo makeUpBriefPaperInfoWithOutFavorInfo(Paper paper, Map<String, Object> responseMap) {
        if (paper == null && responseMap==null) {
            return null;
        }
        BriefPaperInfo paperInfo = new BriefPaperInfo();
        paperInfo.setPaperId(paper.getPaperId());
        paperInfo.setPaperTitle((String) responseMap.get("title"));
        paperInfo.setPaperAbstract((String) responseMap.get("abstract"));
        List<Author> authorList =  (List<Author>) responseMap.get("author");
        List<String> authorNameList = new ArrayList<>();
        for (Author author : authorList) {
            authorNameList.add(author.getName());
        }
        paperInfo.setAuthorNames(authorNameList);
        paperInfo.setDocType(paper.getDocType());
        paperInfo.setLang(paper.getLang());
        paperInfo.setPublisher(paper.getPublisher());
        paperInfo.setPdfLink(paper.getPdfLink());
        return paperInfo;
    }

    @Override
    public BriefPaperInfo getBriefPaperInfoById(String paperIdStr, int userId) {
        Paper paper = getMySqlInfoById(paperIdStr);
        Map<String, Object> responseMap = getEsInfoById(paperIdStr);
        if (paper == null && responseMap==null) {
            return null;
        }
        BriefPaperInfo paperInfo = makeUpBriefPaperInfoWithOutFavorInfo(paper, responseMap);
        Favorite favorite = favoriteRepository.findFavoriteByUseridAndPaperid(userId, paperIdStr);
        paperInfo.setFavored(favorite != null);
        return paperInfo;
    }

    @Override
    public Boolean favor(int userId, String paperIdStr) {
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
        Favorite favorite = favoriteRepository.findFavoriteByUseridAndPaperid(userId, paperIdStr);
        try {
            favoriteRepository.deleteFavoriteByFavoriteid(favorite.getFavoriteid());
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @Override
    public List<Favorite> getfavorites(int ID) {
        return favoriteRepository.findFavoritesByUserid(ID);
    }
}
