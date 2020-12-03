package sheep.portal.service;

import com.alibaba.fastjson.JSON;
import org.apache.http.HttpHost;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.search.fetch.subphase.FetchSourceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import sheep.portal.entity.EsPortal;
import sheep.portal.mapper.EsPortalMapper;
import org.elasticsearch.client.RestHighLevelClient;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

@Service
public class EsPortalServiceImp implements EsPortalService{

    @Qualifier("elasticsearchClient")
    @Autowired
    RestHighLevelClient highLevelClient;

    @Autowired
    EsPortalMapper esPortalMapper;

    @Override
    public void save(EsPortal esPortal){
        esPortalMapper.save(esPortal);
    }

    @Override
    public void deleteByID(int id){
        esPortalMapper.deleteById(id);
    }

    @Override
    //成功更新返回1
    //无更新返回2
    //更新失败返回3
    public int update(int id, EsPortal esPortal) throws IOException {
        //构建改的hashmap
        Map<String, Object> jsonMap = new HashMap<>();
        if(esPortal.getName() != null) jsonMap.put("name", esPortal.getName());
        if(esPortal.getOrg() != null) jsonMap.put("org", esPortal.getOrg());
        if(esPortal.getN_pubs() != 0) jsonMap.put("n_pubs", esPortal.getN_pubs());
        if(esPortal.getN_citation() != 0) jsonMap.put("n_citation", esPortal.getN_citation());
        if(esPortal.getTags_t() != null) jsonMap.put("tags_t", esPortal.getTags_t());
        if(esPortal.getTags_w() != 0) jsonMap.put("tags_w", esPortal.getTags_w());
        //构建updateRequest
        UpdateRequest updateRequest = new UpdateRequest("portal", "_doc", String.valueOf(id));
        updateRequest.doc(jsonMap);
        UpdateResponse updateResponse = highLevelClient.update(updateRequest,RequestOptions.DEFAULT);
        if (updateResponse.getResult() == DocWriteResponse.Result.UPDATED)
            return 1;
        else if (updateResponse.getResult() == DocWriteResponse.Result.NOOP)
            return 2;
        else return 0;
    }

    @Override
    public EsPortal getInformation(int id) throws IOException {
        GetRequest getRequest = new GetRequest("portal", String.valueOf(id));
        GetResponse response =  highLevelClient.get(getRequest, RequestOptions.DEFAULT);
        String sourceAsString = response.getSourceAsString();
        return JSON.parseObject(sourceAsString, EsPortal.class);
    }
}
