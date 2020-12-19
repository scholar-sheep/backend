package sheep.paper.Service;

import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import sheep.paper.Entity.Favorite;

import java.io.IOException;
import java.util.List;

public interface PaperService {
    GetResponse getDetail(GetRequest getRequest) throws IOException;

    Favorite favor(int userId, int paperId);
    List<Favorite> getfavorites(int ID);
}
