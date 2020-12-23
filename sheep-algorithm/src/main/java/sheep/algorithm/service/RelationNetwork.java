package sheep.algorithm.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sheep.algorithm.config.RedisUtil;
import sheep.algorithm.pojo.Scholar;
import sheep.algorithm.config.Client;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class RelationNetwork {
    public  String scholarIndex = "sheep-scholar", paperIndex = "sheep-paper";
    @Autowired
    RedisUtil redisUtil;
    public  ArrayList<ArrayList> generateNetwork(String scholarId){
        RestHighLevelClient client = Client.getClient();

        ArrayList<RelationLine> relationLines = new ArrayList<>();
        ArrayList<RelationNode> relationNodes = new ArrayList<>();
        ArrayList<ArrayList> result = new ArrayList<>();
        int[] a = {2, 3};
        ArrayList<String> relationResult1 = new ArrayList<>(), relationResult2 = new ArrayList<>();
        relationResult1.add(scholarId);
        //n为网络层数
        int n = 2, i;
        for(i=0; i<n; i++){
            for(String id : relationResult1){
                ArrayList<String> singleNetwork=generateSingleNetwork(id, client, relationLines, relationNodes);
                if(singleNetwork!=null&&!singleNetwork.isEmpty())
                    relationResult2.addAll(singleNetwork);
            }
            relationResult1 = relationResult2;
            relationResult2 = new ArrayList<>();
        }

        try {
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        result.add(relationLines);
        result.add(relationNodes);
        return result;

    }

    public  ArrayList<String> generateSingleNetwork(String scholarId, RestHighLevelClient client,
                                                          ArrayList<RelationLine> relationLines, ArrayList<RelationNode> relationNodes){
        ArrayList<String> papers = new ArrayList<>();
        ArrayList<Scholar> scholars = new ArrayList<>();
        ArrayList<String>ids = new ArrayList<>();
        String tid ;
        Scholar sourceScholar,tscholar;
        JSONObject temp;
        Map classMap = new HashMap();
        classMap.put("pubs",Scholar.Pub.class);
        classMap.put("tags",Scholar.Tag.class);
        try {
            GetResponse response = client.get((new GetRequest(scholarIndex)).id(scholarId), RequestOptions.DEFAULT);
            JSONObject jsonObject = JSONObject.fromObject(response.getSource());
            if(jsonObject.isNullObject()){
                return null;
            }
            //sourceName = jsonObject.get("name").toString();
            //
            sourceScholar=(Scholar) JSONObject.toBean(jsonObject,Scholar.class,classMap);
            JSONArray jsonArray = JSONArray.fromObject(jsonObject.get("pubs"));
            for(int i=0; i<jsonArray.size(); i++){
                papers.add(jsonArray.getJSONObject(i).get("i").toString());
            }
            //jsonObject论文
            for(String s : papers){
                response = client.get((new GetRequest(paperIndex)).id(s), RequestOptions.DEFAULT);
                jsonObject = JSONObject.fromObject(response.getSource());
                if(jsonObject.isNullObject()){
                    continue;
                }
                jsonArray = JSONArray.fromObject(jsonObject.get("authors"));
                for(int i=0; i<jsonArray.size(); i++){
                    temp = jsonArray.getJSONObject(i);
                    if(temp.isNullObject()){
                        continue;
                    }
                    tid = temp.get("id").toString();
                    tscholar=(Scholar)JSONObject.toBean(temp,Scholar.class,classMap);
                    //tname = temp.get("name").toString();
                    if(!tid.isEmpty() && !tid.equals(scholarId) && !ids.contains(tid)){
                        ids.add(tid);
                        scholars.add(tscholar);
                    }
                }
                System.out.println(jsonObject);
            }
            RelationNode sourceNode=new RelationNode(sourceScholar);
            if(!relationNodes.contains(sourceNode))
                relationNodes.add(sourceNode);
            for(Scholar targetScholar : scholars){
                RelationNode tnode = new RelationNode(targetScholar);
                RelationLine tline = new RelationLine(sourceScholar, targetScholar);
                if(!relationNodes.contains(tnode)){
                    relationNodes.add(tnode);
                }
                if(!relationLines.contains(tline)){
                    relationLines.add(tline);
                }
            }

//            System.out.println(jsonObject);
            System.out.println(papers);
            System.out.println(ids);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ids;
    }

    public ArrayList<ArrayList> getNetwork(String id) throws IOException
    {
        //若redis中不存在则先存入
        if(!redisUtil.hasKey("network"+id))
            redisUtil.set("network"+id,this.generateNetwork(id),15);
       return (ArrayList<ArrayList>)redisUtil.get("network"+id);
    }
}


@Data
@AllArgsConstructor
@NoArgsConstructor
class RelationLine{
    Scholar source;
    Scholar target;

    @Override
    public boolean equals(Object obj){
        if(this == obj){
            return true;
        }
        if(obj == null){
            return false;
        }
        if(obj instanceof RelationLine){
            RelationLine other = (RelationLine) obj;
            return (this.source.getId().equals(other.source.getId()) && this.target.getId().equals(other.target.getId()))
                    || (this.source.getId().equals(other.target.getId()) && this.target.getId().equals(other.source.getId()));
        }
        return false;
    }

}
@Data
@AllArgsConstructor
@NoArgsConstructor
class RelationNode{
    Scholar scholar;

    @Override
    public boolean equals(Object obj){
        if(this == obj){
            return true;
        }
        if(obj == null){
            return false;
        }
        if(obj instanceof RelationNode){
            RelationNode other = (RelationNode) obj;
            return this.scholar.getId().equals(other.scholar.getId());
        }
        return false;
    }
}
