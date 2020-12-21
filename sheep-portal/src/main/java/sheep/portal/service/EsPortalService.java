package sheep.portal.service;

import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import sheep.portal.entity.EsPortal;
import sheep.portal.pojo.PaperList;
import sheep.portal.pojo.PaperModel;
import sheep.portal.pojo.PaperParam;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface EsPortalService {

    void save(EsPortal esPortal);

    int deleteByID(String id)throws IOException;

    int update(String id, EsPortal esPortal) throws IOException;

    EsPortal getInformation(String id) throws IOException;
    void setPaperList(String id) throws IOException;
    List<PaperModel> getPaperList(String id, String sort) throws IOException;
    int addPaper(String portal_id, String paper_id) throws IOException;
    int deletePaper(String portal_id,String paper_id) throws  IOException;
    int createPaper(String portal_id,PaperParam paperParam)  ;

}
