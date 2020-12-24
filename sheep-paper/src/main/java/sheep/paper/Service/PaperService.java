package sheep.paper.Service;

import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import sheep.paper.Entity.BriefPaperInfo;
import sheep.paper.Entity.Favorite;
import sheep.paper.Entity.PaperData;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface PaperService {

    GetResponse getDetailInfoInES(GetRequest getRequest) throws IOException;

    /*
     *
     * @Description 在 Elastic Search 中查询论文信息
     * @Param [paperIdStr]
     * @return java.util.Map<java.lang.String,java.lang.Object>
     **/
    String getEsInfoById(String paperIdStr);

    /*
     *
     * @Description 根据 id 获取 brief paper info
     * @Param [paperId, userId]
     * @return sheep.paper.Entity.BriefPaperInfo
     **/
    BriefPaperInfo getBriefPaperInfoById(String paperIdStr, int userId);

    /*
     *
     * @Description 收藏文献
     * @Param [userId, paperIdStr]
     * @return sheep.paper.Entity.Favorite
     **/
    Boolean favor(int userId, String paperIdStr);

    /*
     *
     * @Description 取消收藏文献
     * @Param [userId, paperIdStr]
     * @return java.lang.Boolean
     **/
    Boolean unfavor(int userId, String paperIdStr);

    /*
     *
     * @Description 检查用户是否收藏了文献
     * @Param [userId, paperIdStr]
     * @return java.lang.Boolean
     **/
    Boolean checkFavor(int userId, String paperIdStr);

    /*
     *
     * @Description 获取收藏的文献列表
     * @Param [ID]
     * @return java.util.List<sheep.paper.Entity.Favorite>
     **/
    List<Favorite> getfavorites(int ID);
    public PaperData getPaperDetailById(String paperIdStr);
}
