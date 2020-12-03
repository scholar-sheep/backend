package sheep.portal.service;

import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import sheep.portal.entity.EsPortal;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface EsPortalService {

    void save(EsPortal esPortal);

    void deleteByID(int id);

    int update(int id, EsPortal esPortal) throws IOException;

    EsPortal getInformation(int id) throws IOException;



}
