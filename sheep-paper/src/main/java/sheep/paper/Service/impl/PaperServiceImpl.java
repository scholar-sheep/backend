package sheep.paper.Service.impl;

import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import sheep.paper.Entity.Favorite;
import sheep.paper.Repository.FavoriteRepository;
import sheep.paper.Service.PaperService;

import java.io.IOException;
import java.util.List;

@Service
public class PaperServiceImpl implements PaperService {

    @Qualifier("elasticsearchClient")
    @Autowired
    RestHighLevelClient highLevelClient;
    @Autowired
    FavoriteRepository favoriteRepository;

    @Override
    public GetResponse getDetail(GetRequest getRequest) throws IOException {
        return highLevelClient.get(getRequest, RequestOptions.DEFAULT);
    }

    @Override
    public Favorite favor(int userId, String paperIdStr) {
        Favorite favorite = new Favorite();
        favorite.setPaperid(paperIdStr);
        favorite.setUserid(userId);
        return favoriteRepository.saveAndFlush(favorite);
    }

    @Override
    public List<Favorite> getfavorites(int ID) {
        return favoriteRepository.findFavoritesByUserid(ID);
    }
}
