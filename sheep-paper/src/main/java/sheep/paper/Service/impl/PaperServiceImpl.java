package sheep.paper.Service.impl;

import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import sheep.paper.Entity.Author;
import sheep.paper.Entity.BriefPaperInfo;
import sheep.paper.Entity.Favorite;
import sheep.paper.Entity.Paper;
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

    // TODO 这里是不是需要错误处理之类的（不合法数据
    @Override
    public Paper makeUpPaperInfoWithOutFavorInfo(Map<String, Object> responseMap, String paperIdStr) {
        Paper paper = new Paper();
        paper.setPaperId(paperIdStr);
        paper.setTitle((String) responseMap.get("title"));
        List<Map<String, Object>> authors = (List<Map<String, Object>>) responseMap.get("authors");
        List<Author> authorList = new ArrayList<>();
        for (Map map : authors) {
            Author author = new Author();
            author.setId((String) map.get("id"));
            author.setName((String) map.get("name"));
            author.setOrg((String) map.get("org"));
            authorList.add(author);
        }
        paper.setAuthors(authorList);
        paper.setVenue((String) responseMap.get("venue"));
        paper.setYear((Integer) responseMap.get("year"));
        List<String> keywords = (List<String>) responseMap.get("keywords");
        paper.setKeywords(keywords);
        paper.setnCitation((Integer) responseMap.get("n_citation"));
        paper.setPublisher((String) responseMap.get("publisher"));
        paper.setIsbn((String) responseMap.get("isbn"));
        paper.setIssn((String) responseMap.get("issn"));
        paper.setPdfLink((String) responseMap.get("pdf"));
        paper.setUrl((List<String>) responseMap.get("url"));
        paper.setPaperAbstract((String) responseMap.get("abstact"));
        return paper;
    }

    @Override
    public BriefPaperInfo makeUpBriefPaperInfoWithOutFavorInfo(Map<String, Object> responseMap, String paperIdStr) {
        if (responseMap==null) {
            return null;
        }
        BriefPaperInfo paperInfo = new BriefPaperInfo();
        paperInfo.setPaperId(paperIdStr);
        paperInfo.setPaperTitle((String) responseMap.get("title"));
        paperInfo.setPaperAbstract((String) responseMap.get("abstract"));
        List<Author> authorList =  (List<Author>) responseMap.get("author");
        List<String> authorNameList = new ArrayList<>();
        for (Author author : authorList) {
            authorNameList.add(author.getName());
        }
        paperInfo.setAuthorNames(authorNameList);
        paperInfo.setPublisher((String) responseMap.get("publisher"));
        paperInfo.setPdfLink((String) responseMap.get("pdf"));
        return paperInfo;
    }

    @Override
    public BriefPaperInfo getBriefPaperInfoById(String paperIdStr, int userId) {
        Map<String, Object> responseMap = getEsInfoById(paperIdStr);
        if (responseMap==null) {
            return null;
        }
        BriefPaperInfo paperInfo = makeUpBriefPaperInfoWithOutFavorInfo(responseMap, paperIdStr);
        paperInfo.setFavored(checkFavor(userId, paperIdStr));
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
    public Boolean checkFavor(int userId, String paperIdStr) {
        return favoriteRepository.existsByPaperidAndUserid(paperIdStr, userId);
    }

    @Override
    public List<Favorite> getfavorites(int ID) {
        return favoriteRepository.findFavoritesByUserid(ID);
    }
}
