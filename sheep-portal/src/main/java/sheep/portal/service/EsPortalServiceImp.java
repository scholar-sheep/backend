package sheep.portal.service;

import com.alibaba.fastjson.JSON;
import org.elasticsearch.action.DocWriteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import sheep.portal.pojo.PaperParam;
import sheep.portal.util.*;
import sheep.portal.entity.EsPortal;
import sheep.portal.mapper.EsPortalMapper;
import org.elasticsearch.client.RestHighLevelClient;
import sheep.portal.pojo.PaperList;
import sheep.portal.pojo.PaperModel;

import java.io.IOException;
import java.util.*;



@Service
public class EsPortalServiceImp implements EsPortalService{

    @Qualifier("elasticsearchClient")
    @Autowired
    RestHighLevelClient highLevelClient;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private  RedisUtil redisUtil;

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
        if(esPortal.getTags()!= null) jsonMap.put("tags_t", esPortal.getTags());
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
        GetRequest getRequest = new GetRequest("sheep-scholar", String.valueOf(id));
        GetResponse response =  highLevelClient.get(getRequest, RequestOptions.DEFAULT);
        String sourceAsString = response.getSourceAsString();
        EsPortal esPortal= JSON.parseObject(sourceAsString, EsPortal.class);
        return esPortal;
    }


    public PaperModel getPaperDetail(String id) throws IOException {
        GetRequest getRequest = new GetRequest("sheep-paper", id);
        GetResponse response =  highLevelClient.get(getRequest, RequestOptions.DEFAULT);
        String sourceAsString = response.getSourceAsString();
        PaperModel paperModel= JSON.parseObject(sourceAsString, PaperModel.class);
        //扁平化作者列表
        StringBuilder sb=new StringBuilder();
        List<PaperModel.Author> authors=paperModel.getAuthors();
        if(authors!=null) {
            for (PaperModel.Author author : authors) {
                sb.append(author.getName());
                sb.append(",");
            }
            sb.deleteCharAt(sb.length() - 1);
            paperModel.setAuthorNames(sb.toString());
        }
        return paperModel;
    }
    @Override
    public void setPaperList(String id) throws IOException {
        GetRequest getRequest = new GetRequest("sheep-scholar", id);
        GetResponse response =  highLevelClient.get(getRequest, RequestOptions.DEFAULT);
        String sourceAsString = response.getSourceAsString();
        EsPortal esPortal= JSON.parseObject(sourceAsString, EsPortal.class);
        List<EsPortal.Pub> pubs=esPortal.getPubs();
        //封装学者的论文列表
        for(EsPortal.Pub pub:pubs)
        {
            PaperModel paperModel=this.getPaperDetail(pub.getI());
            redisUtil.lSet(id,paperModel);
        }

    }
    @Override
    public PaperList getPaperList(String id,String sort,Integer page_num) throws IOException
    {
        if(page_num==null)page_num=1;
        Long offset=Long.valueOf(page_num-1);
        Long pageSize= Long.valueOf(1);
        //若redis中不存在则先存入
        if(!redisUtil.hasKey(id))
            this.setPaperList(id);
        PaperList paperList=new PaperList();
        List<PaperModel> list=(List)redisUtil.sort(id,id+"->",offset,pageSize,sort);
        paperList.setResults(list);
        return paperList;
    }
    @Override
    //失败添加返回0
    //成功添加返回1
    //重复添加返回2
    public int addPaper(String portal_id, String paper_id) throws IOException
    {
        PaperModel paperModel=this.getPaperDetail(paper_id);
        List<Object>paperModels=redisUtil.lGet(portal_id,0,-1);
        for(Object item:paperModels)
        {
            PaperModel paper=(PaperModel)item;
            if(paper.getId().equals(paper_id)){
                return 2;
            }
        }
        return redisUtil.lSet(portal_id,paperModel)==true?1:0;
    }
    @Override
    // 0删除论文失败 1删除论文成功
    public int deletePaper(String portal_id,String paper_id) throws  IOException
    {
        List<Object>paperModels=redisUtil.lGet(portal_id,0,-1);
        for(Object item:paperModels)
        {
            PaperModel paper=(PaperModel)item;
            if(paper.getId().equals(paper_id)){
                return redisUtil.lRemove(portal_id,0,item)>0?1:0;

            }
        }
        return 0;
    }
    @Override
    public int createPaper(String portal_id,PaperParam paperParam)
    {
        PaperModel newPaper= new PaperModel(paperParam);
        return redisUtil.lSet(portal_id,newPaper)==true?1:0;
    }
}
