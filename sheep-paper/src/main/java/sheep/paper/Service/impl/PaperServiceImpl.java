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
        GetRequest getRequest = new GetRequest("sheep-paper", paperIdStr);
        try {
            GetResponse response = getDetailInfoInES(getRequest);
            return response.getSourceAsMap();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Paper makeUpPaperInfoWithOutFavorInfo(Map<String, Object> responseMap, String paperIdStr) {
        if (responseMap==null) {
            return null;
        }
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
        paper.setVenue((String) ((Map<String, Object>) responseMap.get("venue")).get("raw"));
        try {
            paper.setYear((Integer) responseMap.get("year"));
        } catch (Exception e) {
            ;
        }
        List<String> keywords;
        try {
            keywords = (List<String>) responseMap.get("keywords");
        } catch (Exception e) {
            keywords = new ArrayList<>();
            keywords.add((String) responseMap.get("keywords"));
        }
        paper.setKeywords(keywords);
        try {
            paper.setnCitation((Integer) responseMap.get("n_citation"));
        } catch (Exception e) {
            ;
        }
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
//        paperInfo.setPaperAbstract((String) responseMap.get("abstract"));

        List<Map<String, Object>> authorList =  (List<Map<String, Object>>) responseMap.get("authors");
        List<String> authorNameList = new ArrayList<>();
        for (Map author : authorList) {
            String name;
            try {
                name = (String) author.get("name");
            } catch (Exception e) {
                continue;
            }
            if (name!=null && !name.equals("")) {
                authorNameList.add(name);
            }
        }
        if (authorList.size()!=0) {
            paperInfo.setAuthorNames(authorNameList);
        }
//        paperInfo.setPublisher((String) responseMap.get("publisher"));
//        paperInfo.setPdfLink((String) responseMap.get("pdf"));
        paperInfo.setVenue((String) ((Map<String, Object>) responseMap.get("venue")).get("raw"));
        try {
            paperInfo.setnCitation((Integer) responseMap.get("n_citation"));
        } catch (Exception e) {
            ;
        }
        try {
            paperInfo.setYear((Integer) responseMap.get("year"));
        } catch (Exception e) {
            ;
        }
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
